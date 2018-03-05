import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class planets extends PApplet {

Boolean turnTime = false;
String textYearsPassed = "0";
String textShipSpeed = "1.3";
float yearsPassed = (float)Integer.parseInt(textYearsPassed);
float shipSpeed = Float.parseFloat(textShipSpeed);
float viewAngle = 3;
float drawMod = .4f;
float simSpeed = 0.005f;
Planet originPlanet,destPlanet;
Planet mercury = new Planet("Mercury",57.91f,.24f,0,false);
Planet venus = new Planet("Venus",108.2f,0.7f,0,false);
Planet earth = new Planet("Earth",149.6f,1,.5f,false);
Planet mars = new Planet("Mars",227.9f,1.89f,.625f,false);
Planet jupiter = new Planet("Jupiter",778.5f,12,.625f,false);
Planet saturn = new Planet("Saturn",1429,29,.75f,true);
Planet uranus = new Planet("Uranus",2871,84,.125f,false);
Planet neptune = new Planet("Neptune",4498,165,.875f,false);
Button[] orgBtns = new Button[8],destBtns = new Button[8];
Button currOrgBtn,currDestBtn;
TextBox shipSpd = new TextBox("Ship Speed (in Million Km / Day (Min 0.1)",textShipSpeed,5,5,400,40,true,true);
TextBox timePassed = new TextBox("Time Passed in Years",textYearsPassed,5,50,400,40,false,true);
TextBox timeToGet = new TextBox("Total Travel Time in Years","",5,95,400,40,false,false);
TextBox currTextBox = shipSpd;
String newline = System.getProperty("line.separator");
 
public void setup() {
  
  background(0);
  
  for (int i = 0; i < orgBtns.length; i++){
    orgBtns[i] = new Button(true,10,(35*i)+170,80,30);
    destBtns[i] = new Button(false,100,(35*i)+170,80,30);  
  }
  orgBtns[0].plnt = mercury;
  orgBtns[1].plnt = venus;
  orgBtns[2].plnt = earth;
  orgBtns[3].plnt = mars;
  orgBtns[4].plnt = jupiter;
  orgBtns[5].plnt = saturn;
  orgBtns[6].plnt = uranus;
  orgBtns[7].plnt = neptune;
  destBtns[0].plnt = mercury;
  destBtns[1].plnt = venus;
  destBtns[2].plnt = earth;
  destBtns[3].plnt = mars;
  destBtns[4].plnt = jupiter;
  destBtns[5].plnt = saturn;
  destBtns[6].plnt = uranus;
  destBtns[7].plnt = neptune;
}
 
public void draw() {
  background(0);
  textSize(14);
  fill(255);
  stroke(255);
  textAlign(LEFT,BASELINE);
  if (turnTime){
    yearsPassed+=simSpeed;
    timePassed.affect = Float.toString(yearsPassed);
    //text(yearsPassed,10,20);
  }else{
    try {yearsPassed = Float.parseFloat(timePassed.affect);}catch(Exception e){}
    //text(textYearsPassed, 10, 20);
  }
  try{timeToGet.affect = howLong(originPlanet,destPlanet)+ " Years (~"+ (int)(howLong(originPlanet,destPlanet)*364.25f) +" Days)";}catch(Exception e){}
  try{shipSpeed = max(Float.parseFloat(shipSpd.affect),0.1f);}catch(Exception e){}
  ellipse(960,540,10,10);
  mercury.place(yearsPassed);
  venus.place(yearsPassed);
  earth.place(yearsPassed);
  mars.place(yearsPassed);
  jupiter.place(yearsPassed);
  saturn.place(yearsPassed);
  uranus.place(yearsPassed);
  neptune.place(yearsPassed);
  textAlign(CENTER,CENTER);
  text("Origin",50,150);
  text("Destination",140,150);
  for(int i = 0; i < orgBtns.length; i++){
    orgBtns[i].place();
    destBtns[i].place();
  }
  shipSpd.place();
  timePassed.place();
  timeToGet.place();
  
  textAlign(LEFT,BASELINE);
  text("INSTRUCTIONS" + newline + 
  "Click text fields to edit (numbers only)"  + newline +
  "Click buttons to specify origin and destination" + newline +
  "- and = control camera zoom" + newline +
  "[ and ] control camera angle" + newline +
  "SPACEBAR toggles simulation" + newline +
  "N and M control simulation speed" + newline +
  "Left and Right step forward or back one year" + newline +
  "Up and Down step forward or back one month"  + newline + newline + newline +
  "A janky-ass tool by nickdrawthing (Nick Hendriks)"
  ,10,500);
}
public float howLong(Planet aP, Planet bP){
  float totalTime=0,
  //radius = totalTime * shipSpeed;
  radius = totalTime * shipSpeed * 364.25f;
  Vectr orig = aP.whereAt(yearsPassed),
  dest = bP.whereAt(yearsPassed+totalTime);
  while (dist(orig,dest) > radius){
    radius = totalTime * shipSpeed * 364.25f;
    totalTime += 0.01f;
    dest = bP.whereAt(yearsPassed+totalTime);    
  };
  noFill();
  ellipse(960+(dest.x*drawMod),540+(dest.y/viewAngle*drawMod),5,5);
  line(960+(orig.x*drawMod),540+(orig.y/viewAngle*drawMod),960+(dest.x*drawMod),540+(dest.y/viewAngle*drawMod));
  fill(255);
  return totalTime;
}
public float dist(Vectr a, Vectr b){
  float totDist = (float) Math.sqrt(Math.pow((a.x-b.x),2)+Math.pow((a.y-b.y),2));  
  return totDist;
}
//*/
public void keyPressed() {
  System.out.print(keyCode);
  if (keyCode == BACKSPACE) {
    if (currTextBox.affect.length() > 0) {
      currTextBox.affect = currTextBox.affect.substring(0, currTextBox.affect.length()-1);
    }
  } else if (keyCode == DELETE) {
    currTextBox.affect = "";
  } else if (keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT) {
    if(keyCode == 46 ||(keyCode >= 48 && keyCode <= 57)||(keyCode >= 96 && keyCode <= 105 || keyCode == 110 || keyCode == 190) ){
      currTextBox.affect = currTextBox.affect + key;
    }
  }
  if (keyCode == 45) {drawMod *= 0.99f;}
  if (keyCode == 61) {drawMod /= 0.99f;}
  
  if (keyCode == 91) {viewAngle -= 0.1f;}
  if (keyCode == 93) {viewAngle += 0.1f;}
  
  if (keyCode == 32) {
    if (turnTime){
      textYearsPassed = Float.toString(yearsPassed);
    }
    turnTime = !turnTime;
  }
  if (keyCode == ESC) {exit();}
  
  if (keyCode == 37){
    yearsPassed--;
    if(!turnTime){timePassed.affect = Float.toString(yearsPassed);}
  }
  if (keyCode == 39){
    yearsPassed++;
    if(!turnTime){timePassed.affect = Float.toString(yearsPassed);}
  }
  if (keyCode == 40){
    yearsPassed-=0.083333f;
    if(!turnTime){timePassed.affect = Float.toString(yearsPassed);}
  }
  if (keyCode == 38){
    yearsPassed+=0.083333f;
    if(!turnTime){timePassed.affect = Float.toString(yearsPassed);}
  }
  if (keyCode == 78){simSpeed*=.8f;}
  if (keyCode == 77){simSpeed/=.8f;}
}
//*/
public void mousePressed(){
  for (int i = 0; i < orgBtns.length; i++){
    orgBtns[i].press();
    destBtns[i].press();
  }
  shipSpd.press();
  timePassed.press();
}

class TextBox{ 
  String flavour;
  String affect; 
  float x, y, w, h;
  boolean pressed=false, selectable = true;
  TextBox(){
    x=0;y=0;w=100;h=40;
  }
  TextBox(String flav, String aff, float xx, float yy, float ww, float hh, boolean pr, boolean slct){
    flavour = flav; affect = aff; x=xx; y=yy; w=ww; h=hh; pressed = pr; selectable = slct;
  }
  public void place(){
    if(pressed){    
      noFill(); 
      stroke(255);
      rect(x,y,w,h);
    }else if (!selectable){
      noStroke();
      fill(90);
      rect(x,y,w,h);
    }
    fill(255);
    textAlign(LEFT,BASELINE);
    text(flavour,x+10,y+(h*.35f));
    text(affect,x+10,y+(h*.85f));
  }
  public void press(){
    if(selectable){
      if(mouseX > x && mouseX < x+w && mouseY > y && mouseY < y+h){
        try{currTextBox.pressed = false;}catch(Exception e){}
        pressed = true;
        currTextBox = this;
      }
    }
  }
}

class Button{
  Planet plnt;
  float x, y, w, h;
  boolean pressed=false,isOrigin;
  Button(){
    isOrigin=true;x=0;y=0;w=100;h=40;
  }
  Button(boolean og, float xx, float yy, float ww, float hh){
    isOrigin=og;x=xx;y=yy;w=ww;h=hh;  
  }
  public void place(){
    fill(200);
    if (pressed){stroke(255);}else{noStroke();}
    rect(x,y,w,h);
    fill(0);
    textAlign(CENTER,CENTER);
    text(plnt.name,x+(w/2),y+(h/2));
  }
  public void press(){
    if(mouseX > x && mouseX < x+w && mouseY > y && mouseY < y+h){
      pressed = true;
      if(isOrigin){
        originPlanet=plnt;
        try{if(currOrgBtn != this)currOrgBtn.pressed = false;}catch(Exception e){}
        currOrgBtn = this;
      }else{
        destPlanet=plnt;
        try{if(currDestBtn != this)currDestBtn.pressed = false;}catch(Exception e){}
        currDestBtn = this;
      }
    }
  }
}
class Planet{
  String name;
  float radius, orbit, x, y, startPos;
  boolean ring;
  Planet(){
    name = "planet"; radius = 10; orbit = 10; startPos = 10; ring = false;
  };
  Planet(String nm, float rad, float orb, float stP, boolean ringg){
    name = nm; radius = rad; orbit = orb; startPos = stP*orb; ring = ringg;  
  };
  public void place(float currTime){
    noFill();
    ellipse(960,540,radius*2*drawMod,(radius*2*drawMod)/viewAngle);
    x = cos((currTime + startPos)*((2*PI)/orbit))*radius;
    y = -sin((currTime + startPos)*((2*PI)/orbit))*(radius/viewAngle);
    if (ring) ellipse((x*drawMod)+960,(y*drawMod)+540,10,10/viewAngle);
    fill(255);
    ellipse((x*drawMod)+960,(y*drawMod)+540,5,5);
  }
  public Vectr whereAt(float time){
    Vectr retVal = new Vectr();
    retVal.x = cos((time+startPos)*((2*PI)/orbit))*radius;
    retVal.y = -sin((time+startPos)*((2*PI)/orbit))*radius;
    return retVal;
  }
}
class Vectr {
  float x,y;
  Vectr(){
    x=0;y=0;  
  }
  Vectr(float xx, float yy){
    x=xx;y=yy;  
  }
}
  public void settings() {  size(1920, 1080); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#C61A1A", "planets" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
