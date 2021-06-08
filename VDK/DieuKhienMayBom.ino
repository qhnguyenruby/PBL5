#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>
#include "DHT.h"
#include <Wire.h>
#include <FirebaseArduino.h>
#include <ArduinoJson.h>
#include <RtcDS3231.h> 


RtcDS3231<TwoWire> rtcObject(Wire); 

const char* FIREBASE_HOST = "apptuoicay-default-rtdb.firebaseio.com";
const char* FIREBASE_AUTH = "5TLbkWaz8PDqfR9Y0LezksE2ApXHmHvfTAjFpc95";

const int DHTPIN = D4;
const int DAD = A0;
const int DHTTYPE = DHT11;
const int pump = D7;

DHT dht(DHTPIN, DHTTYPE);
int Time;
int doc_cbdoamdat, TBcbdoamdat,phantram_kho,phantram_am;
int doamdatthat;
float nhietdothat,doamthat;
const char* ssid     = "Happydays";
const char* password = "Mau@1964";
//const char* ssid     = "TungSpice";
//const char* password = "tung778899";
//const char* ssid     = "MYMSSV";
//const char* password = "102180123";
//const char* ssid     = "Huy-Tung";
//const char* password = "22041974";
float tbnhietdo, tbdoam ;
float nhietdo , doam;
const char* pushhistorylink = "http://192.168.11.65:81/NhietDoDoAm/pushhistory.php";
const char* serverName = "http://192.168.11.65:81/NhietDoDoAm/pushsensordata.php";
const char* recivelink = "http://192.168.11.65:81/NhietDoDoAm/send.php";
const char* tuoilink = "http://192.168.11.65:81/NhietDoDoAm/history.php";
//
//const char* pushhistorylink = "http://192.168.1.228/NhietDoDoAm/pushhistory.php";
//const char* serverName = "http://192.168.1.228/NhietDoDoAm/pushsensordata.php";
//const char* recivelink = "http://192.168.1.228/NhietDoDoAm/send.php";
//const char* tuoilink = "http://192.168.1.228/NhietDoDoAm/history.php";

//const char* pushhistorylink = "http://192.168.100.7:81/NhietDoDoAm/pushhistory.php";
//const char* serverName = "http://192.168.100.7:81/NhietDoDoAm/pushsensordata.php";
//const char* recivelink = "http://192.168.100.7:81/NhietDoDoAm/send.php";
//const char* tuoilink = "http://192.168.100.7:81/NhietDoDoAm/history.php";

String apiKeyValue = "tPmAT5Ab3j7F9";
String strluongnuoc ,strsolantuoi;
String stat, luongnuoc, iddat;

void setup() {
  Serial.begin(9600);
  pinMode(pump,OUTPUT);
  //dht
  dht.begin();
  // real time clock
  rtcObject.Begin();
  RtcDateTime currentTime = RtcDateTime(21,6,05,16,28,50);
  rtcObject.SetDateTime(currentTime);  
  //wifi
  WiFi.begin(ssid, password);
  Serial.println("Connecting");
  
  while(WiFi.status() != WL_CONNECTED) { 
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
  delay(5000);
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop() {
  dieukhienmaybom();
}

void pushtodatabase()
{
  readfromsensor();
  doamdatthat = doc_cbdoamdat; //Tính giá trị trung bình
  nhietdothat = nhietdo;
  doamthat = doam;
  phantram_kho = map(doamdatthat, 0, 1023, 0, 100);    //Chuyển giá trị Analog thành giá trị %
  phantram_am = 100 - phantram_kho;  //Tính giá trị phần trăm ẩm
  if(WiFi.status()== WL_CONNECTED){
    HTTPClient http;
    
    http.begin(serverName);
    
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    
    String httpRequestData = "api_key=" + apiKeyValue + "&nhietdo=" + String(nhietdothat)
                           + "&doam=" + String(doamthat) + "&doamdat=" + String(phantram_am)+"";
    Serial.print("httpRequestData: ");
    Serial.println("Truyen du lieu tu cam bien len Database Nhiet do:" + String(nhietdothat)+ " do am:" 
                    +String(doamthat) + " do am dat:"+String(phantram_am) + "");
    
    int httpResponseCode = http.POST(httpRequestData);
    
    if (httpResponseCode>0) {
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
    }
    else {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
    }
    http.end();
  }
  else {
    Serial.println("WiFi Disconnected");
  }  
}

void readfromsensor()
{
//  for (int i = 1; i< 3599;i++)
//  {
  nhietdo = dht.readTemperature();
  doam = dht.readHumidity();
  doc_cbdoamdat = analogRead(DAD);
  //} 
}

void pushhistorytodatabase()
{
  if(WiFi.status()== WL_CONNECTED){
    HTTPClient http;
    
    http.begin(pushhistorylink);
    
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    
    String httpRequestData = "api_key=" + apiKeyValue + "&luongnuoc=" + strluongnuoc + "&iddat=" +iddat;
                           
    Serial.print("httpRequestData: ");
    Serial.println("Truyen len lich su tuoi luong nuoc: "+ strluongnuoc);
    
    int httpResponseCode = http.POST(httpRequestData);
    
    if (httpResponseCode>0) {
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
    }
    else {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
    }
    http.end();
  }
  else {
    Serial.println("WiFi Disconnected");
  }  
}

void pullfromdatabase()
{
  if(WiFi.status() == WL_CONNECTED)
  {    
    HTTPClient http;   
    http.begin(recivelink);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    String httpRequestData = "api_key=" + apiKeyValue;
    int httpcode = http.POST(httpRequestData);
    HTTPClient httptuoi;   
    httptuoi.begin(tuoilink);
    httptuoi.addHeader("Content-Type", "application/x-www-form-urlencoded");
    String httpRequestDatatuoi = "api_key=" + apiKeyValue;
    int httpcodetuoi = httptuoi.POST(httpRequestDatatuoi);
    if(httpcode > 0)
    {
      //Serial.print("HTTP Response Code:");  
      //Serial.println(httpcode);
      strluongnuoc = http.getString();
      strsolantuoi = httptuoi.getString();
      //Serial.println("Luong nuoc tuoi tu dong: " + strluongnuoc);
      Serial.println("So lan da tuoi: "+ strsolantuoi);
    }
    else
    {
      //Serial.printf("Error Code:");
      //Serial.println(httpcode);
    }       
  }
}

void pullfromfirebase()
{
  if(WiFi.status()== WL_CONNECTED)
  {
    stat = Firebase.getString("lenh"); 
    luongnuoc = Firebase.getString("luongnuoc");
    iddat = Firebase.getString("iddat");
    Serial.println("Trang thai tuoi cay: " + stat + " ");
    //Serial.println("Luong nuoc tuoi thu cong: " +  luongnuoc);
  }
}


void dieukhienmaybom()
{
  RtcDateTime currentTime = rtcObject.GetDateTime();    //get the time from the RTC

  int RealHour = currentTime.Hour();
  int RealMinute = currentTime.Minute();
  int RealSecond = currentTime.Second();
  
  if(RealSecond == 01)
  {
    //if(RealMinute == 0 || RealMinute == 10 || RealMinute == 20 || RealMinute == 30 || RealMinute == 40 || RealMinute == 50)
    if(RealMinute == 10)
    {
      //Serial.println(String(RealMinute) + ":" + String(RealSecond));
      pushtodatabase(); 
    }
  }
  
  pullfromfirebase();
  pullfromdatabase();
  
  int timetuoi = luongnuoc.toInt()/50*1000;
  int timetuoiauto = strluongnuoc.toInt()/50*1000;
  if(stat == "0")
  {
    digitalWrite(pump,HIGH);
    Serial.println("Dang Tuoi Thu Cong");
    Serial.println("Luong Nuoc Tuoi La: " + luongnuoc + " ml");
    Serial.println("Thoi Gian Tuoi La: " + String(timetuoi/1000) + " Giay");
    delay(timetuoi);
    digitalWrite(pump,LOW);
  }
  if(stat == "1" && strsolantuoi == "0" && RealHour == 16 && RealMinute == 30)
  {
    digitalWrite(pump,HIGH);
    Serial.println("Dang Tuoi Tu Dong");
    Serial.println("Luong Nuoc Tuoi La: " + strluongnuoc + " ml");
    Serial.println("Thoi Gian Tuoi La: " + String(timetuoiauto/1000) + " Giay"); 
    delay(timetuoiauto);
    digitalWrite(pump,LOW);
    pushhistorytodatabase();
   }
   
}
