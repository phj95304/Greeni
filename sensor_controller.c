#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>  // Used to check and report errors that occur during the execution of library functions. 0: Normal, other values: Abnormal
#include <wiringPi.h>  // C library for GPIO (General Purpose Input/Output) operations
#include <wiringPiSPI.h>  // Library for SPI communication, used for utilizing the SPI functionality of A/D Converters like MCP3008

#include "MQTTClient.h"

#define CS_MCP3008 10 //BCM_GPIO 8
#define SPI_CHANNEL 0
#define SPI_SPEED 1000000 // 1MHz
#define ADDRESS "113.198.84.26:1883"
#define CLIENTID "GREEN"
#define PAYLOAD "Hello World"
#define TOPIC "GREEN"

#define TOPIC1 "TEMPO"
#define TOPIC2 "HUMI"
#define TOPIC3 "LIGHT"

#define QOS 1
#define TIMEOUT 1000L

//bcm check
#define LED1 0 //BCM 17
#define LED2 2 //BCM 27
#define LED3 3 //BCM 22
#define LED4 21 //BCM 5
#define LED5 22 //BCM 6
#define LED6 23 //BCM 13
#define LED7 24 //BCM 19
#define LED8 25 //BCM 26

#define LED9 1 //BCM 18
#define LED10 4 //BCM 23
#define LED11 5 //BCM 24
#define LED12 6 //BCM 25
#define LED13 26//BCM 12
#define LED14 27//BCM 16
#define LED15 7 //BCM 4

//pump
#define IN1 28//BCM 20
#define IN2 29 //BCM 21

float tempo = 0;
float humi = 0;
float light = 0;

void sendMsg(MQTTClient *, char *, char *);

int read_mcp3008_adc(unsigned char adcChannel)
{
	unsigned char buff[3];
	int adcValue = 0;
	buff[0] = 0x06 | ((adcChannel & 0x07) >> 2);
	buff[1] = ((adcChannel & 0x07) << 6);
	buff[2] = 0x00;
	digitalWrite(CS_MCP3008, 0); // Low : CS Active
	wiringPiSPIDataRW(SPI_CHANNEL, buff, 3);
	buff[1] = 0x0F & buff[1];
	adcValue = (buff[1] << 8) | buff[2];
	digitalWrite(CS_MCP3008, 1); // High : CS Inactive
	return adcValue;
}


void mainLoop(MQTTClient *pClient) {
	char buf[10];

	int adcChannel = 0;
	int adcValue = 0;

	float tempoVolt = 0;
	float humiVolt = 0;
	float lightVolt = 0;



	while (1) {
		adcChannel = 2;//0: Water Level, 1: Humidity, 2: Temperature, 3: Light
		adcValue = read_mcp3008_adc(adcChannel);
		printf("MCP3008 IC Ch1 Value = %u\n", adcValue);
		tempoVolt = adcValue;
		tempo = (tempoVolt / 4078) + 23;//Since the sensor output ranges from 0V to 3.3V, converting the output to a digital value in the range of 0 to 4095 (12-bit). Adjusts the conversion accordingly.
		printf("TempVolt : %f", tempoVolt);
		printf("Temperature : %f\n\n ", tempo);
		printf("\n\n------------------------\n\n");

		sprintf(buf, "%.2f", tempo);
		sendMsg(pClient, TOPIC1, buf);

		delay(200);

		if ((tempo>19) || (humi > -15)) {
			digitalWrite(IN1, 1);
			digitalWrite(IN2, 0);
			delay(100);
		}
		else if (tempo <= 19 && humi <= 78) {
			digitalWrite(IN1, 0);
			digitalWrite(IN2, 0);
			delay(100);
		}
		delay(1000);


		adcChannel = 1;//0: Water Level, 1: Humidity, 2: Temperature, 3: Light
		adcValue = read_mcp3008_adc(adcChannel);

		humiVolt = (3.25 / 4095)*adcValue;
		humi = (humiVolt / (3.25 / 100)) - 15;
		printf("\nMCP3008 IC Ch2 Value = %d\n", adcValue);
		printf("HumiVolt : %f", humiVolt);
		printf("Humidity : %f \n", humi);
		printf("\n\n------------------------\n\n");

		sprintf(buf, "%.2f", humi);
		sendMsg(pClient, TOPIC2, buf);

		delay(200);


		adcChannel = 3;//0: Water Level, 1: Humidity, 2: Temperature, 3: Light
		adcValue = read_mcp3008_adc(adcChannel);

		lightVolt = (3.25 / 4095)*adcValue;
		light = (lightVolt / (3.25 / 100));
		printf("\nMCP3008 IC Ch3 Value = %d\n", adcValue);
		printf("LuxVolt : %f", lightVolt);
		printf("Lux   :%f ", light);
		printf("\n\n------------------------\n\n");

		sprintf(buf, "%.2f", light);
		sendMsg(pClient, TOPIC3, buf);

		if (light>5) {//
			printf("LED ON");
			digitalWrite(LED1, 1);
			digitalWrite(LED2, 1);
			digitalWrite(LED3, 1);
			digitalWrite(LED4, 1);
			digitalWrite(LED5, 1);
			digitalWrite(LED6, 1);
			digitalWrite(LED7, 1);
			digitalWrite(LED8, 1);
			digitalWrite(LED9, 1);
			digitalWrite(LED10, 1);
			digitalWrite(LED11, 1);
			digitalWrite(LED12, 1);
			digitalWrite(LED13, 1);
			digitalWrite(LED14, 1);
			digitalWrite(LED15, 1);

		}
		else if (light <= 5) {
			printf("LED OFF");
			digitalWrite(LED1, 0);
			digitalWrite(LED2, 0);
			digitalWrite(LED3, 0);
			digitalWrite(LED4, 0);
			digitalWrite(LED5, 0);
			digitalWrite(LED6, 0);
			digitalWrite(LED7, 0);
			digitalWrite(LED8, 0);
			digitalWrite(LED9, 0);
			digitalWrite(LED10, 0);
			digitalWrite(LED11, 0);
			digitalWrite(LED12, 0);
			digitalWrite(LED13, 0);
			digitalWrite(LED14, 0);
			digitalWrite(LED15, 0);
		}
		delay(2000);
	}
}

void sendMsg(MQTTClient *pClient, char *pTopic, char* msg) {// Publish 
    MQTTClient_message pubmsg = MQTTClient_message_initializer;
    MQTTClient_deliveryToken token; // Token generated when the client is created, used to confirm if the message has been successfully delivered before unblocking the client.
    pubmsg.payload = msg; // Content inside the buffer
    pubmsg.payloadlen = strlen(msg); // Length of the buffer
    pubmsg.qos = QOS; // Message assurance. The messages sent are determined by the QOS (Quality of Service) level.
                        // QOS = 0: Message may or may not be delivered to the server (response not guaranteed)
                        // QOS = 1: Server ensures the message has arrived (push-back message). If no response is received, the client sends the message again with the DUP header.
                        // Server, upon receiving a duplicate message, publishes the message to the subscriber and sends a different push-back message to the client.
                        // QOS = 2: Highest level of message assurance. In case of problems, the protocol is re-executed starting from the last unacknowledged message.
                        // Message is sent and received only once (no duplication).
    
    pubmsg.retained = 0;
    MQTTClient_publishMessage(*pClient, pTopic, &pubmsg, &token);
}



int initSpi() {
	if (wiringPiSetup() == -1)
	{
		fprintf(stdout, "Unable to start wiringPi: %s\n", strerror(errno));
		return 1;
	}
	if (wiringPiSPISetup(SPI_CHANNEL, SPI_SPEED) == -1)
	{
		fprintf(stdout, "wiringPiSPISetup Failed: %s\n", strerror(errno));
		return 1;
	}
	pinMode(CS_MCP3008, OUTPUT);
}

int main(int argc, char * argv[])
{
	if (wiringPiSetup() == -1) return 1;//error check

	//pump output setting
	pinMode(IN1, OUTPUT);
	pinMode(IN2, OUTPUT);

	pinMode(IN1, OUTPUT);
	pinMode(IN2, OUTPUT);

	//led output setting
	pinMode(LED1, OUTPUT);
	pinMode(LED2, OUTPUT);
	pinMode(LED3, OUTPUT);
	pinMode(LED4, OUTPUT);
	pinMode(LED5, OUTPUT);
	pinMode(LED6, OUTPUT);
	pinMode(LED7, OUTPUT);
	pinMode(LED8, OUTPUT);
	pinMode(LED9, OUTPUT);
	pinMode(LED10, OUTPUT);
	pinMode(LED11, OUTPUT);
	pinMode(LED12, OUTPUT);
	pinMode(LED13, OUTPUT);
	pinMode(LED14, OUTPUT);
	pinMode(LED15, OUTPUT);

	MQTTClient client;
	MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer;
	int rc;
	char *serverAddress = NULL;

	if (argc != 2) {
		serverAddress = ADDRESS;//If the number of input values is not 2, use the default address: 113.198.84.26:1883
	}
	else {
		serverAddress = argv[1];//If there are 2 input values, use the second one (argv[1]) as the address
	}

	MQTTClient_create(&client, serverAddress, CLIENTID,//ClientID => GREEN
		MQTTCLIENT_PERSISTENCE_NONE, NULL);//mqtt client creation
	conn_opts.keepAliveInterval = 20;
	conn_opts.cleansession = 1;

	if ((rc = MQTTClient_connect(client, &conn_opts)) != MQTTCLIENT_SUCCESS)
	{
		printf("Failed to connect, return code %d\n", rc);
		exit(EXIT_FAILURE);
	}

	initSpi();
	mainLoop(&client);

	MQTTClient_disconnect(client, 10000);
	MQTTClient_destroy(&client);//exit
	return 0;
}
