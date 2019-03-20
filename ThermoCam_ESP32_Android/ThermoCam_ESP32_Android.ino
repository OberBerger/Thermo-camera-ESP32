//ThermalCAM and Server


#include <Wire.h>
#include <Adafruit_AMG88xx.h>
#include "BluetoothSerial.h"
#include "esp_bt_device.h"


BluetoothSerial SerialBT;
uint8_t BTpixels[68];

Adafruit_AMG88xx amg;

int minT = 15;
int maxT = 30;

float pixels[AMG88xx_PIXEL_ARRAY_SIZE];

void setup() {

  Serial.begin(115200);


  SerialBT.begin("Thermo Camera"); //Bluetooth device name
  printDeviceAddress();

  Serial.println(F("AMG8833 thermal camera!"));

  bool status;

  // default settings
  status = amg.begin();
  if (!status) {
    Serial.println("Could not find a valid AMG88xx sensor, check wiring!");
    while (1);
  }
}


void loop() {

  int i = 0;
  int colorIndex = 0;


  //read all the pixels
  amg.readPixels(pixels);

  
  if (Serial.available()) {
    SerialBT.write(Serial.read());
  }
  if (SerialBT.available()) {
    byte iByte = SerialBT.read();
    //Serial.println(iByte);  
    if (iByte==0x61){ //"a"
      //SerialBT.flush();  
      for (int i=0; i < AMG88xx_PIXEL_ARRAY_SIZE ; i++) {
        int colorTemp;
        if (pixels[i] >= maxT) colorTemp = maxT;
        else if (pixels[i] <= minT) colorTemp = minT;
        else colorTemp = pixels[i];
        uint8_t colorIndex = map(colorTemp, minT, maxT, 0, 255);
        colorIndex = constrain(colorIndex, 0, 255); 
        BTpixels[i]=colorIndex;
      }
      BTpixels[AMG88xx_PIXEL_ARRAY_SIZE]=minT;
      BTpixels[AMG88xx_PIXEL_ARRAY_SIZE+1]=maxT;
      uint8_t z=(30+pixels[28]);
      BTpixels[AMG88xx_PIXEL_ARRAY_SIZE+2]=z;
      BTpixels[AMG88xx_PIXEL_ARRAY_SIZE+3]=(uint8_t) 0;
      SerialBT.write(BTpixels,68);
    }
    if (iByte==0x62 && minT>0){ //"b"
      minT = minT - 1;
    }
    if (iByte==0x63 && minT<maxT-2){ //"c"
      minT = minT + 1;
    }
    if (iByte==0x64 && maxT>minT+2){ //"d"
      maxT = maxT - 1;
    }
    if (iByte==0x65 && maxT<80){ //"e"
      maxT = maxT + 1;
    }
    
  }

}


void printDeviceAddress() {
 
  const uint8_t* point = esp_bt_dev_get_address();
 
  for (int i = 0; i < 6; i++) {
 
    char str[3];
 
    sprintf(str, "%02X", (int)point[i]);
    Serial.print(str);
 
    if (i < 5){
      Serial.print(":");
    }
 
  }
  Serial.println();
}
