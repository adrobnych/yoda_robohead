
#include <AFMotor.h>

AF_DCMotor motorA(1);
AF_DCMotor motorB(2);


char blueToothVal;           //value sent over via bluetooth
char lastValue;              //stores last state of device (on/off)
 
void setup()
{
 Serial.begin(9600); 
 //pinMode(13,OUTPUT);  // led
 
  Serial.println("Motor test!");

  // turn on motor
  motorA.setSpeed(920);
  motorB.setSpeed(1000);
}

 
void loop()
{
  if(Serial.available())
  {//if there is data being recieved
    blueToothVal=Serial.read(); //read it
  }
  if (blueToothVal=='n')
  {//if value from bluetooth serial is n
    //digitalWrite(13,HIGH);            //switch on LED
    goForward();
    //if (lastValue!='n')
    //  Serial.println(F("LED is on")); //print LED is on
    lastValue=blueToothVal;
    blueToothVal = 'f';
  }
}

void goForward(){
  Serial.print("tick");
  //motor.setSpeed(1000);
  //motor.run(FORWARD);
  motorA.run(BACKWARD);
  motorB.run(BACKWARD);
  delay(2000);
  //Serial.print("tech");
  motorA.run(RELEASE);
  motorB.run(RELEASE);
}


