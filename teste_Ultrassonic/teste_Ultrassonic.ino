#include <Ultrasonic.h>

#define INTERVALO_LEITURA 250 //(ms)

#define PIN_TRIGGER 13
#define PIN_ECHO 12

Ultrasonic ultrasonic(PIN_TRIGGER, PIN_ECHO);

unsigned int distancia = 0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:

  Serial.println(getDistance());
  
  delay(INTERVALO_LEITURA);

}

/*
  FAZ A LEITURA DA DISTANCIA ATUAL CALCULADA PELO SENSOR
*/
int getDistance()
{
    //faz a leitura das informacoes do sensor (em cm)
    int distanciaCM;
    long microsec = ultrasonic.timing();
    // pode ser um float ex: 20,42 cm se declarar a var float 
    distanciaCM = ultrasonic.convert(microsec, Ultrasonic::CM);
  
    return distanciaCM;
}
