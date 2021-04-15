#include <WiFi.h>
#include <ESP_FlexyStepper.h>
#include <ESP32Servo.h>

int ledVermelho = 22;
int ledVerde = 23;

//Bloco de configuração motor de passo
// IO pin assigments
const int MOTOR_STEP_PIN = 33;
const int MOTOR_DIRECTION_PIN = 25;

// Speed settings
const int DISTANCE_TO_TRAVEL_IN_STEPS = 2000; // DISTÂNCIA PARA VIAJAR EM PASSOS
const int SPEED_IN_STEPS_PER_SECOND = 300; // 300Hz (300 pulsos/passos por segundo)
const int ACCELERATION_IN_STEPS_PER_SECOND = 800; // ACELERAÇÃO EM PASSOS POR SEGUNDO
const int DECELERATION_IN_STEPS_PER_SECOND = 800; // DESACELERAÇÃO EM PASSOS POR SEGUNDO

// create the stepper motor object
ESP_FlexyStepper stepper;

// END Bloco de configuração motor de passo

//Bloco de configuração WIFI
const char* ssid = "ESP32-REDE";
const char* password =  "123456789";
IPAddress gateway;

WiFiClient client;
int number = 2;
char* host ="";

const uint16_t port = 9999;

static const int servoPin = 5;
Servo servo;

void setup() {
  Serial.begin(115200);
  servo.attach(servoPin);
  pinMode(ledVermelho, OUTPUT);
  pinMode(ledVerde, OUTPUT);
//  criando rede
  Serial.println("Criando rede...");
  WiFi.softAP(ssid, password);
  IPAddress IP = WiFi.softAPIP();
  Serial.println("AP IP address: ");
  Serial.println(IP);

  // motor de passo
  stepper.connectToPins(MOTOR_STEP_PIN, MOTOR_DIRECTION_PIN);
  stepper.setSpeedInStepsPerSecond(SPEED_IN_STEPS_PER_SECOND);
  stepper.setAccelerationInStepsPerSecondPerSecond(ACCELERATION_IN_STEPS_PER_SECOND);
  stepper.setDecelerationInStepsPerSecondPerSecond(DECELERATION_IN_STEPS_PER_SECOND);

  stepper.startAsService();
  // end motor de passo

  servo.write(0);
}

void loop() {
  digitalWrite(ledVermelho,HIGH);
  digitalWrite(ledVerde,LOW);
  // put your main code here, to run repeatedly:
  if(tentaConexao()== false){
    number++;
    if(number>5)
      number = 2;
    return;
  }else{
    digitalWrite(ledVermelho,LOW);
    digitalWrite(ledVerde,HIGH);
    Serial.println("conexão com o servidor concluida");
    Serial.println("vai enviar mensagem");
    client.println("esp32");
    client.flush();
    Serial.println("enviou a mensagem");
    //client.stop;
    Serial.println("esperando mensagem chegar");
    while(true){
      if(!client.connected()){//verifica se o esp ainda está conectado
        break;
      }
      delay(200);
      if(client.available()){
        char mensagem = client.read();
        Serial.println("Mensagem recebida do servidor = ");
        Serial.println(mensagem);
        moverMotor(mensagem);
        moverMotorPasso(mensagem);
      }
    }
    Serial.println("conexão com o servidor fechada");
 }
 delay(1000);
  
}

void moverMotor(char mensagem){
 if(mensagem == '1'){
    servo.write(178);
 }else if(mensagem == '0'){
   servo.write(0);
 }
}

void moverMotorPasso(char mensagem){
  int previousDirection = (mensagem == '1') ?  1 : -1;
  
  delay(500);
  long relativeTargetPosition = DISTANCE_TO_TRAVEL_IN_STEPS * previousDirection;
  Serial.printf("Moving stepper by %ld steps\n", relativeTargetPosition);
  stepper.setTargetPositionRelativeInSteps(relativeTargetPosition);
}

bool tentaConexao(){
    mudaHost();
    client.connect(host,port);
    if(!client.connected()){
    Serial.println("conexão falhada com o servidor");
    Serial.println("__________");
    //delay(4000);
    return false;
  }
  return true;
}

void mudaHost(){
  if(number == 2){
    host = "192.168.4.2";
  }else if(number == 3){
    host = "192.168.4.3";
  }else if(number == 4){
    host = "192.168.4.4";
  }else if(number == 5){
    host = "192.168.4.5";
  }
  
  Serial.print("tentando conexão com: ");
  Serial.println(host);
}
