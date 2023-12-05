# Smart Plant Pot Implementation

## Introduction
This paper details the implementation of a smart plant pot capable of autonomously providing light and water based on real-time measurements of temperature, humidity, and illumination. Additionally, the system captures and delivers real-time images of the plant using a camera embedded in the pot. The plant pot container is designed with five layers, each 3D-printed and assembled to house sensors, a pump, and a camera.

## System Components
- **Container Design:** The plant pot container is divided into five layers, each created using a 3D printer.
- **Internal Components:** Inside the container, sensors, a pump, and a camera are installed.
- **Android Application:** An Android application is developed to enable user control of the plant pot and monitor its state.
- **Communication Protocol:** The communication between the Android application and the Raspberry Pi utilizes the MQTT protocol.

## Implementation Details
- **Sensor Integration:** Sensors are employed to detect the current temperature, humidity, and illumination levels.
- **Autonomous Control:** The system autonomously provides light and water based on the detection results.
- **Camera Functionality:** Real-time images of the plant are captured by the embedded camera.
- **Container Assembly:** The container is assembled after printing each layer with a 3D printer.

## User Interface
- **Android Application:** A dedicated Android application is developed for user interaction.
- **Remote Monitoring:** Users can monitor the plant pot's state and control its functions through the application.

## Paper
- **An Implementation of Smart Gardening using Raspberry Pi and MQTT**
  - Kitae Hwang, **Park, Heyjin**, Jisu Kim, Lee Tae-Youn, and Jung In Hwan
  - In *The Institute of Internet, Broadcasting and Communication (IIBC)*, 2018, vol.18, no.1, pp.151-157
  - DOI: [https://doi.org/10.7236/JIIBC.2018.18.1.151](https://doi.org/10.7236/JIIBC.2018.18.1.151)

