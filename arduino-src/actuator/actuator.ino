#include <Servo.h>

const int
  OFF = 0,
  PURR = 1,
  HB_LOW = 2,
  HB_HIGH = 3;

const int
  SERVO_PIN_1 = 9,
  SERVO_PIN_2 = 10,
  VIBRATION_PIN_1 = 6;

int vibrationMode = OFF;

Servo servo1, servo2;

char servoInput[4];
char vibrateInput[2];
     
void setup() {
  Serial.begin(115200);
  Serial.flush();
  servo1.attach(SERVO_PIN_1);
  servo2.attach(SERVO_PIN_2);
  pinMode(VIBRATION_PIN_1, OUTPUT);
}
     
void loop() {
  if (Serial.available() >= 4) {
    
    servoInput[0] = Serial.read();
    servoInput[1] = Serial.read();
    servoInput[2] = Serial.read();
    servoInput[3] = 0;
    
    vibrateInput[0] = Serial.read();
    vibrateInput[1] = 0;
    
    int servoValue = atoi(servoInput);
    vibrationMode = atoi(vibrateInput);
    
    Serial.print("Received: ");
    Serial.print(servoValue, DEC);
    Serial.print(", ");
    Serial.println(vibrationMode, DEC);
    
    // scale it to use it with the actuator
    int scaledServoValue = map(servoValue, 0, 100, 40, 50);
    
    // sets the servo position according to the scaledServoValue servoValue
    servo1.write(scaledServoValue);
    servo2.write(scaledServoValue);
  }
  
  unsigned long counter = millis();
  
  switch (vibrationMode) {
    case OFF:
      analogWrite(VIBRATION_PIN_1, 0);
      break;
    
    case PURR:
      {
        int vibrationStep = counter % 1760;
        if (0 <= vibrationStep && vibrationStep < 800) {
          // vibrate(800, 200);
          analogWrite(VIBRATION_PIN_1, 200);
        } else if (800 <= vibrationStep && vibrationStep < 860) {
          // vibrateBreak(60);
          analogWrite(VIBRATION_PIN_1, 0);
        } else if (860 <= vibrationStep && vibrationStep < 1360) {
          // vibrate(500, 180);
          analogWrite(VIBRATION_PIN_1, 180);
        } else if (1360 <= vibrationStep && vibrationStep < 1760) {
          // vibrateBreak(400);
          analogWrite(VIBRATION_PIN_1, 0);
        }
      }
      break;
    
    case HB_LOW:
      {
        int vibrationStep = counter % 1000;
        if (0 <= vibrationStep && vibrationStep < 150) {
          // vibrate(150, 255);
          analogWrite(VIBRATION_PIN_1, 255);
        } else if (150 <= vibrationStep && vibrationStep < 300) {
          // vibrateBreak(150);
          analogWrite(VIBRATION_PIN_1, 0);
        } else if (300 <= vibrationStep && vibrationStep < 400) {
          // vibrate(100, 180);
          analogWrite(VIBRATION_PIN_1, 180);
        } else if (400 <= vibrationStep && vibrationStep < 1000) {
          // vibrateBreak(600);
          analogWrite(VIBRATION_PIN_1, 0);
        }
      }
      break; 
    
    case HB_HIGH:
      {
        int vibrationStep = counter % 400;
        if (0 <= vibrationStep && vibrationStep < 140) {
          // vibrate(140, 255);
          analogWrite(VIBRATION_PIN_1, 255);
        } else if (140 <= vibrationStep && vibrationStep < 220) {
          // vibrateBreak(80);
          analogWrite(VIBRATION_PIN_1, 0);
        } else if (220 <= vibrationStep && vibrationStep < 300) {
          // vibrate(80, 180);
          analogWrite(VIBRATION_PIN_1, 180);
        } else if (300 <= vibrationStep && vibrationStep < 400) {
          // vibrateBreak(100);
          analogWrite(VIBRATION_PIN_1, 0);
        }
      }
      break;
    default:
      analogWrite(VIBRATION_PIN_1, 0);
      break;    
  }
}

//void vibrate(int length, int level) {
//  length = length + length * (random(0,3) / 10.0);
//  level = level - level * (random(0,2) / 10.0);
//  analogWrite(VIBRATION_PIN_1, level);
//  delay(length);
//  analogWrite(VIBRATION_PIN_1, 0);
//}
//
//void vibrateBreak(int pb) {
//  pb = pb + pb * (random(0,3) / 10.0);
//  delay(pb);
//}
