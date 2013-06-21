//**************************************************************//
//  Name    : shiftIn Example 2.1                               //
//  Author  : Carlyn Maw                                        //
//  Date    : 25 Jan, 2007                                      //
//  Version : 1.0                                               //
//  Notes   : Code for using a CD4021B Shift Register    	//
//          :                                                   //
//****************************************************************

//define where your pins are
int latchPin = 9;
int dataPin = 6;
int clockPin = 7;

//Define variables to hold the data 
//for each shift register.
//starting with non-zero numbers can help
//troubleshoot
byte switchVar1 = 0;  //01001000
byte switchVar2 = 0; //10011111
byte switchVar3 = 0; //10011111
byte switchVar4 = 0; //10011111

int headerByte = 0;

void setup() {
  //start serial
  Serial.begin(9600);

  //define pin modes
  pinMode(latchPin, OUTPUT);
  pinMode(clockPin, OUTPUT); 
  pinMode(dataPin, INPUT);
}

void loop() {
  // magic header: 255
  if (Serial.available()  > 0) {
     headerByte = Serial.read();
  }
  
  //Pulse the latch pin:
  //set it to 1 to collect parallel data
  digitalWrite(latchPin,1);
  //set it to 1 to collect parallel data, wait
  delayMicroseconds(20);
  //set it to 0 to transmit data serially  
  digitalWrite(latchPin,0);

  //while the shift register is in serial mode
  //collect each shift register into a byte
  //the register attached to the chip comes in first 
  switchVar1 = shiftIn(dataPin, clockPin);
  switchVar2 = shiftIn(dataPin, clockPin);
  switchVar3 = shiftIn(dataPin, clockPin);
  switchVar4 = shiftIn(dataPin, clockPin);

  //Print out the results.
  //leading 0's at the top of the byte 
  //(7, 6, 5, etc) will be dropped before 
  //the first pin that has a high input
  //reading  
//  Serial.println(switchVar1, BIN);
//  Serial.println(switchVar2, BIN);
//  Serial.println(switchVar3, BIN);
//  Serial.println(switchVar4, BIN);
  // send sensor data
  Serial.write(switchVar1);
  Serial.write(switchVar2);
  Serial.write(switchVar3);
  Serial.write(switchVar4);
  // send again to confirm
  Serial.write(switchVar1);
  Serial.write(switchVar2);
  Serial.write(switchVar3);
  Serial.write(switchVar4);
  //white space
  //delay so all these print satements can keep up. 
 // delay(1000);
}

//------------------------------------------------end main loop

////// ----------------------------------------shiftIn function
///// just needs the location of the data pin and the clock pin
///// it returns a byte with each bit in the byte corresponding
///// to a pin on the shift register. leftBit 7 = Pin 7 / Bit 0= Pin 0

byte shiftIn(int myDataPin, int myClockPin) { 
  int i;
  int temp = 0;
  int pinState;
  byte myDataIn = 0;

  pinMode(myClockPin, OUTPUT);
  pinMode(myDataPin, INPUT);

//we will be holding the clock pin high 8 times (0,..,7) at the
//end of each time through the for loop

//at the begining of each loop when we set the clock low, it will
//be doing the necessary low to high drop to cause the shift
//register's DataPin to change state based on the value
//of the next bit in its serial information flow.
//The register transmits the information about the pins from pin 7 to pin 0
//so that is why our function counts down
  for (i=7; i>=0; i--)
  {
    digitalWrite(myClockPin, 0);
    delayMicroseconds(2);
    temp = digitalRead(myDataPin);
    if (temp) {
      pinState = 1;
      //set the bit to 0 no matter what
      myDataIn = myDataIn | (1 << i);
    }
    else {
      //turn it off -- only necessary for debuging
     //print statement since myDataIn starts as 0
      pinState = 0;
    }

    //Debuging print statements
    //Serial.print(pinState);
    //Serial.print("     ");
    //Serial.println (dataIn, BIN);

    digitalWrite(myClockPin, 1);

  }
  //debuging print statements whitespace
  //Serial.println();
  //Serial.println(myDataIn, BIN);
  return myDataIn;
}
