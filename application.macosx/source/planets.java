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
float yearsPassed = (float)Integer.parseInt(textYearsPassed);
float shipSpeed = 1.3f;
float viewAngle = 3;
float drawMod = .4f;
Planet originPlanet,destPlanet;
Planet mercury = new Planet("Mercury",57.91f,.24f,0,false);
Planet venus = new Planet("Venus",108.2f,0.7f,0,false);
Planet earth = new Planet("Earth",149.6f,1,0,false);
Planet mars = new Planet("Mars",227.9f,1.89f,0,false);
Planet jupiter = new Planet("Jupiter",778.5f,12,0,false);
Planet saturn = new Planet("Saturn",1429,29,0,true);
Planet uranus = new Planet("Uranus",2871,84,0,false);
Planet neptune = new Planet("Neptune",4498,165,0,false);
Button[] orgBtns = new Button[8],destBtns = new Button[8];
Button currOrgBtn,currDestBtn;
 
public void setup() {
  
  background(0);
  
  for (int i = 0; i < orgBtns.length; i++){
    orgBtns[i] = new Button(true,10,(50*i)+100,80,30);
    destBtns[i] = new Button(false,100,(50*i)+100,80,30);  
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
    yearsPassed+=.001f;
    textYearsPassed = Float.toString(yearsPassed);
    text(yearsPassed,10,20);
  }else{
    try {yearsPassed = ((float)Integer.parseInt(textYearsPassed)/364.25f);}catch(Exception e){yearsPassed = 0;}
    text(textYearsPassed, 10, 20);
  }
  try{text(howLong(originPlanet,destPlanet),10,50);}catch(Exception e){}
  ellipse(500,250,10,10);
  mercury.place(yearsPassed);
  venus.place(yearsPassed);
  earth.place(yearsPassed);
  mars.place(yearsPassed);
  jupiter.place(yearsPassed);
  saturn.place(yearsPassed);
  uranus.place(yearsPassed);
  neptune.place(yearsPassed);
  for(int i = 0; i < orgBtns.length; i++){
    orgBtns[i].place();
    destBtns[i].place();
  }
}
public float howLong(Planet aP, Planet bP){
  float totalTime=0,
  radius = totalTime * shipSpeed;
  Vectr orig = aP.whereAt(yearsPassed),
  dest = bP.whereAt(yearsPassed+totalTime);
  while (dist(orig,dest) > radius){
    totalTime += 0.01f;
    radius = totalTime * shipSpeed;
    orig = aP.whereAt(yearsPassed);
    dest = bP.whereAt(yearsPassed+totalTime);    
  };
  noFill();
  ellipse(500+(dest.x*drawMod),250+(dest.y/viewAngle*drawMod),5,5);
  line(500+(orig.x*drawMod),250+(orig.y/viewAngle*drawMod),500+(dest.x*drawMod),250+(dest.y/viewAngle*drawMod));
  fill(255);
  return totalTime;
}
public float dist(Vectr a, Vectr b){
  float totDist = (float) Math.sqrt(Math.pow((a.x-b.x),2)+Math.pow((a.y-b.y),2));  
  return totDist;
}
//*/
public void keyPressed() {
  if (keyCode == BACKSPACE) {
    if (textYearsPassed.length() > 0) {
      textYearsPassed = textYearsPassed.substring(0, textYearsPassed.length()-1);
    }
  } else if (keyCode == DELETE) {
    textYearsPassed = "";
  } else if (keyCode != SHIFT && keyCode != CONTROL && keyCode != ALT) {
    if((keyCode >= 48 && keyCode <= 57)||(keyCode >= 96 && keyCode <= 105 || keyCode == 110 || keyCode == 190) ){
      textYearsPassed = textYearsPassed + key;
    }
  }
  if (keyCode == 32) {turnTime = !turnTime;}
  if (keyCode == ESC) {exit();}
}
//*/
public void mousePressed(){
  for (int i = 0; i < orgBtns.length; i++){
    orgBtns[i].press();
    destBtns[i].press();
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
    name = nm; radius = rad; orbit = orb; startPos = stP; ring = ringg;  
  };
  public void place(float currTime){
    noFill();
    ellipse(500,250,radius*2*drawMod,(radius*2*drawMod)/viewAngle);
    x = cos((currTime + startPos)*((2*PI)/orbit))*radius;
    y = -sin((currTime + startPos)*((2*PI)/orbit))*(radius/viewAngle);
    if (ring) ellipse((x*drawMod)+500,(y*drawMod)+250,10,10/viewAngle);
    fill(255);
    ellipse((x*drawMod)+500,(y*drawMod)+250,5,5);
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
  public void settings() {  size(1000, 500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#C61A1A", "planets" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
