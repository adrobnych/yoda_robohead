char blueToothVal;           //value sent over via bluetooth
char lastValue;              //stores last state of device (on/off)
 
void setup()
{
 Serial.begin(9600); 
 pinMode(13,OUTPUT);
 
 //for stepper...
 pinMode(8, OUTPUT);
 pinMode(9, OUTPUT);
 pinMode(10, OUTPUT);
 pinMode(11, OUTPUT);
}
 
void turnOnStepper(){
 int i=0;
 //revolve one revolution clockwise
 for(i=0;i<512;i++){
 clockwiserotate();}
} 
 
void loop()
{
  if(Serial.available())
  {//if there is data being recieved
    blueToothVal=Serial.read(); //read it
  }
  if (blueToothVal=='n')
  {//if value from bluetooth serial is n
    digitalWrite(13,HIGH);            //switch on LED
    turnOnStepper();
    if (lastValue!='n')
      Serial.println(F("LED is on")); //print LED is on
    lastValue=blueToothVal;
  }
  else if (blueToothVal=='f')
  {//if value from bluetooth serial is n
    digitalWrite(13,LOW);             //turn off LED
    if (lastValue!='f')
      Serial.println(F("LED is off")); //print LED is on
    lastValue=blueToothVal;
  }
  delay(1000);
}

void clockwiserotate() { //revolve clockwise
step1();
step2();
step3();
step4();
step5();
step6();
step7();
step8();
}
void counterclockwiserotate() { //revolve counterclockwise
step1();
step7();
step6();
step5();
step4();
step3();
step2();
step1();
}
void step1(){
digitalWrite(8, HIGH);
digitalWrite(9, LOW);
digitalWrite(10, LOW);
digitalWrite(11, LOW);
delay(2);
}
void step2(){
digitalWrite(8, HIGH);
digitalWrite(9, HIGH);
digitalWrite(10, LOW);
digitalWrite(11, LOW);
delay(2);
}
void step3(){
digitalWrite(8, LOW);
digitalWrite(9, HIGH);
digitalWrite(10, LOW);
digitalWrite(11, LOW);
delay(2);
}
void step4(){
digitalWrite(8, LOW);
digitalWrite(9, HIGH);
digitalWrite(10, HIGH);
digitalWrite(11, LOW);
delay(2);
}
void step5(){
digitalWrite(8, LOW);
digitalWrite(9, LOW);
digitalWrite(10, HIGH);
digitalWrite(11, LOW);
delay(2);
}
void step6(){
digitalWrite(8, LOW);
digitalWrite(9, LOW);
digitalWrite(10, HIGH);
digitalWrite(11, HIGH);
delay(2);
}
void step7(){
digitalWrite(8, LOW);
digitalWrite(9, LOW);
digitalWrite(10, LOW);
digitalWrite(11, HIGH);
delay(2);
}
void step8(){
digitalWrite(8, HIGH);
digitalWrite(9, LOW);
digitalWrite(10, LOW);
digitalWrite(11, HIGH);
delay(2);
}

