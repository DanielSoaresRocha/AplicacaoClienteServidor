#include <WiFi.h>

int led = 23;

//Bloco de configuração WIFI
const char* ssid = "ESP32-REDE";
const char* password =  "123456789";
IPAddress gateway;

WiFiClient client;
int number = 2;
char* host ="";

const uint16_t port = 9999;
//const char* host = "192.168.4.2";//192.168.4.2

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
  if(tentaConexao()== false){
    number++;
    if(number>5)
      number = 2;
    return;
  }else{

  Serial.println("conexão com o servidor concluida");
  Serial.println("vai enviar mensagem");
  client.println("hello");
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
      digitalWrite(led,HIGH);
    }else{
      //Serial.println("Sem dados a serem lidos");
      digitalWrite(led,LOW);
    }
  }
  Serial.println("conexão com o servidor fechada");
  }
  delay(1000);
  
}

bool tentaConexao(){
    mudaHost();
    if(!client.connect(host,port)){
    Serial.println("conexão falhada com o servidor");
    Serial.println("______________________________");
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
