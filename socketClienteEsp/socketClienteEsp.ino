#include <WiFi.h>

int led = 23;

//Bloco de configuração WIFI
const char* ssid = "ESP32-REDE";
const char* password =  "123456789";
IPAddress gateway;

const uint16_t port = 9999;
const char* host = "192.168.4.2";

void setup() {
 Serial.begin(9600);
  pinMode(led, OUTPUT);
  //criando rede
  Serial.println("Criando rede...");
  WiFi.softAP(ssid, password);
  IPAddress IP = WiFi.softAPIP();
  Serial.println("AP IP address: ");
  Serial.println(IP);

}

void loop() {
  // put your main code here, to run repeatedly:
  Serial.println("ola");
  WiFiClient client;

  if(!client.connect(host,port)){
    Serial.println("conexão falhada com o servidor");
    delay(4000);
    return;
  }

  Serial.println("conexão com o servidor concluida");

  //client.stop;
  Serial.println("esperando mensagem chegar");
  while(true){
    delay(200);
    if(client.available()){
      char mensagem = client.read();
      Serial.println("Mensagem recebida do servidor = ");
      Serial.println(mensagem);
      digitalWrite(led,HIGH);
      delay(500);
      client.flush();
      continue;  
    }else{
      Serial.println("Sem dados a serem lidos");
      digitalWrite(led,LOW);
    }
  }
  Serial.println("conexão com o servidor fechada");
  
  delay(10000);
}
