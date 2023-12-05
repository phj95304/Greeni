#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "MQTTClient.h"

#define ADDRESS     "113.198.79.118:1883"
#define CLIENTID    "TakingPicture"
#define TOPIC       "IMG"
#define PAYLOAD     "Hello World!"
#define QOS         1
#define TIMEOUT     10000L

volatile MQTTClient_deliveryToken deliveredtoken;

void delivered(void *context, MQTTClient_deliveryToken dt) {
    printf("Message with token value %d delivery confirmed\n", dt);
    deliveredtoken = dt;
}

int msgarrvd(void *context, char *topicName, int topicLen, MQTTClient_message *message) {
    MQTTClient_freeMessage(&message);
    MQTTClient_free(topicName);

    // When a message arrives, take a picture and save it in the specified folder.
    pid_t fk = fork();
    if (!fk) {
        execl("/usr/bin/raspistill", "/usr/bin/raspistill", "-o", "image.jpg", "-t", "10", "-w", "600", "$");
    }
    return 1;
}

void connlost(void *context, char *cause) {
    printf("\nConnection lost\n");
    printf("Cause: %s\n", cause);
}

// The following section from volatile to connlost implements three callback methods (delivered, msgarrvd, and connlost)
// that the client will use for asynchronous operations.

int main(int argc, char *argv[]) {
    MQTTClient client; // Local variable used in the program
    MQTTClient_connectOptions conn_opts = MQTTClient_connectOptions_initializer; // Local variable used in the program
    int rc;
    int ch;

    MQTTClient_create(&client, ADDRESS, CLIENTID, MQTTCLIENT_PERSISTENCE_NONE, NULL);
    // &client: pointer to the handle of the newly created client, an error code that can be tested to verify successful completion in production code
    // ADDRESS: URI of the MQTT port monitoring client connection requests
    // CLIENTID: Name used to identify the client
    // MQTTCLIENT_PERSISTENCE_NONE: Specifies that the client state is in memory and will be lost in case of a system failure
    // No messages are processed until the MQTTClient_connect function is called

    conn_opts.keepAliveInterval = 20;
    conn_opts.cleansession = 1;
    conn_opts.keepAliveInterval = 20;
    conn_opts.cleansession = 1;

    MQTTClient_setCallbacks(client, NULL, connlost, msgarrvd, delivered); // Called while the server disconnects the client

    // Connecting the client
    if ((rc = MQTTClient_connect(client, &conn_opts)) != MQTTCLIENT_SUCCESS) {
        printf("Failed to connect, return code %d\n", rc);
        exit(EXIT_FAILURE);
    }
    printf("Subscribing to topic %s\nfor client %s using QoS%d\n\n"
           "Press Q<Enter> to quit\n\n", TOPIC, CLIENTID, QOS);

    MQTTClient_subscribe(client, TOPIC, QOS);
    do {
        ch = getchar();
    } while (ch != 'Q' && ch != 'q');
    MQTTClient_disconnect(client, 10000);
    MQTTClient_destroy(&client);
    return rc;
}
