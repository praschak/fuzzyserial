const int
  PING_PIN = 7,
  TOUCH_PIN = A0;

void setup() {
  Serial.begin(115200);
}

void loop() {
  long duration, cm;

  pinMode(PING_PIN, OUTPUT);
  digitalWrite(PING_PIN, LOW);
  delayMicroseconds(2);
  digitalWrite(PING_PIN, HIGH);
  delayMicroseconds(5);
  digitalWrite(PING_PIN, LOW);

  pinMode(PING_PIN, INPUT);
  duration = pulseIn(PING_PIN, HIGH);

  // convert the time into a distance
  cm = microsecondsToCentimeters(duration);
  
  int touchValue = analogRead(TOUCH_PIN);
  
  Serial.print(cm, DEC);
  Serial.print(",");
  Serial.print(touchValue, DEC);
  Serial.println();
  
  delay(10);
}

long microsecondsToCentimeters(long microseconds)
{
  // The speed of sound is 340 m/s or 29 microseconds per centimeter.
  // The ping travels out and back, so to find the distance of the
  // object we take half of the distance travelled.
  return microseconds / 29 / 2;
}
