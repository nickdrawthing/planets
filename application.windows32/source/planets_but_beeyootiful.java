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

public class planets_but_beeyootiful extends PApplet {

int scrnW = 200;
int scrnH = 100;
Boolean turnTime = false;
String textYearsPassed = "0";
String textShipSpeed = "1.3";
float yearsPassed = (float)Integer.parseInt(textYearsPassed);
float shipSpeed = Float.parseFloat(textShipSpeed);
float viewAngle = 3;
float drawMod = .4f;
float simSpeed = 0.002f;
float bounceTime = 0;
Planet originPlanet, destPlanet;
Planet mercury = new Planet("Mercury", 57.91f, .24f, 0, false);
Planet venus = new Planet("Venus", 108.2f, 0.7f, 0, false);
Planet earth = new Planet("Earth", 149.6f, 1, .5f, false);
Planet mars = new Planet("Mars", 227.9f, 1.89f, .625f, false);
Planet jupiter = new Planet("Jupiter", 778.5f, 12, .625f, false);
Planet saturn = new Planet("Saturn", 1429, 29, .75f, true);
Planet uranus = new Planet("Uranus", 2871, 84, .125f, false);
Planet neptune = new Planet("Neptune", 4498, 165, .875f, false);
Button[] orgBtns = new Button[8], destBtns = new Button[8];
Button currOrgBtn, currDestBtn;
TextBox shipSpd = new TextBox("Ship Speed (in Million Km / Day (Min 0.1)", textShipSpeed, 5, 5, 400, 40, true, true);
TextBox timePassed = new TextBox("Time Passed in Years", textYearsPassed, 5, 50, 400, 40, false, true);
TextBox timeToGet = new TextBox("Total Travel Time in Years", "", 5, 95, 400, 40, false, false);
TextBox currTextBox = shipSpd;
Star[][] stars = new Star[3][100];
int[] clrs = new int[3];
PImage bg;
String newline = System.getProperty("line.separator");

//int saveImgCount = 0;

public void setup() {
  
  //surface.setSize(scrnW, scrnH);
  background(15,0,40);
  bg = loadImage(createBG());
   
  clrs[0] = randColor();
  clrs[1] = randColor();
  clrs[2] = randColor();
  for (int i = 0; i < 3; i ++){
    for (int j = 0; j < 100; j++){
      stars[i][j] = new Star();
    }
  }

  for (int i = 0; i < orgBtns.length; i++) {
    orgBtns[i] = new Button(true, 10, (35*i)+170, 80, 30);
    destBtns[i] = new Button(false, 100, (35*i)+170, 80, 30);
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
  mercury.img = loadImage("Mercury.png");
  venus.img = loadImage("Venus.png");
  earth.img = loadImage("Earth.png");
  mars.img = loadImage("Mars.png");
  jupiter.img = loadImage("Jup.png");
  saturn.img = loadImage("Sat.png");
  uranus.img = loadImage("Uran.png");
  neptune.img = loadImage("Nept.png");
}

public void draw() {
  //background(15,0,40);
  image(bg,0,0);
  for (int i = 0; i < 3; i ++){
    noStroke();
    fill(clrs[i]);
    for (int j = 0; j < 100; j++){
      stars[i][j].place(); 
    }
  }
  bounceTime += 0.2f;
  textSize(14);
  fill(255);
  stroke(255);
  textAlign(LEFT, BASELINE);
  if (turnTime) {
    yearsPassed+=simSpeed;
    timePassed.affect = Float.toString(yearsPassed);
    //text(yearsPassed,10,20);
  } else {
    try {
      yearsPassed = Float.parseFloat(timePassed.affect);
    }
    catch(Exception e) {
    }
    //text(textYearsPassed, 10, 20);
  }
  try {
    timeToGet.affect = howLong(originPlanet, destPlanet)+ " Years (~"+ (int)(howLong(originPlanet, destPlanet)*364.25f) +" Days)";
  }
  catch(Exception e) {
  }
  try {
    shipSpeed = max(Float.parseFloat(shipSpd.affect), 0.1f);
  }
  catch(Exception e) {
  }
  stroke(255);
  fill(255,220,0);
  //ellipse(960, 540, 20, 20);
  mercury.place(yearsPassed);
  venus.place(yearsPassed);
  earth.place(yearsPassed);
  mars.place(yearsPassed);
  jupiter.place(yearsPassed);
  saturn.place(yearsPassed);
  uranus.place(yearsPassed);
  neptune.place(yearsPassed);
  fill(255);
  textAlign(CENTER, CENTER);
  text("Origin", 50, 150);
  text("Destination", 140, 150);
  for (int i = 0; i < orgBtns.length; i++) {
    orgBtns[i].place();
    destBtns[i].place();
  }
  shipSpd.place();
  timePassed.place();
  timeToGet.place();

  textAlign(LEFT, BASELINE);
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
    , 10, 500);
   //saveImgCount++;          // this stuff is just for saving a png sequence
   //if (saveImgCount < 240) save("imgs/"+saveImgCount + ".png");
}


public float howLong(Planet aP, Planet bP) {
  float totalTime=0, 
    //radius = totalTime * shipSpeed;
    radius = totalTime * shipSpeed * 364.25f;
  Vectr orig = aP.whereAt(yearsPassed), 
    dest = bP.whereAt(yearsPassed+totalTime);
  while (dist(orig, dest) > radius) {
    radius = totalTime * shipSpeed * 364.25f;
    totalTime += 0.01f;
    dest = bP.whereAt(yearsPassed+totalTime);
  };
  noFill();
  ellipse((width/2)+(dest.x*drawMod), (height/2)+(dest.y/viewAngle*drawMod), 5, 5);
  line((width/2)+(orig.x*drawMod), (height/2)+(orig.y/viewAngle*drawMod), (width/2)+(dest.x*drawMod), (height/2)+(dest.y/viewAngle*drawMod));
  fill(255);
  return totalTime;
}
public float dist(Vectr a, Vectr b) {
  float totDist = (float) Math.sqrt(Math.pow((a.x-b.x), 2)+Math.pow((a.y-b.y), 2));  
  return totDist;
}

public int randColor(){
  return color(randColVal(), randColVal(), randColVal(),randColVal()-50);
}

public int randColVal(){
  int retVal = (int)random(51)+205;
  return retVal;
}

public String createBG(){
  String bgName = "BGImage.png";
  float x,y,rad;
  float step = 150;
  float radMin = 100, radMax = 500;
  noStroke();
  beginShape();
  fill(color(14,0,40));
  vertex(0,0);
  vertex(width,0);
  fill(color(40,0,80));
  vertex(width,height);
  vertex(0,height);
  endShape();
  streakyColours(15,1000,20,20,50,125,150);
  streakyColours(50,100,150,100,500,0,175);
  //for (int j=0; j<50; j++){
  //  x = random(width);
  //  y = random(height);
  //  rad = random(radMin,radMax);
  //  fill(random(175),random(175),random(175),1);
  //  for (int i=0; i < 100;i++){
  //    x = min(width,(max(0,x+random(-step,step))));
  //    y = min(height,(max(0,y+random(-step,step))));
  //    rad = min(radMax,(max(radMin,rad+random(-10,10))));
  //    ellipse(x,y,rad,rad);
  //  }
  //}
  //for (int j=0; j<10; j++){
  //  x = random(width/4);
  //  y = random(height/4);
  //  rad = random(radMin,radMax);
  //  fill(random(175),random(175),random(175),1);
  //  for (int i=0; i < 100;i++){
  //    x = min(width,(max(0,x+random(-step,step))));
  //    y = min(height,(max(0,y+random(-step,step))));
  //    rad = min(radMax,(max(radMin,rad+random(-10,10))));
  //    ellipse(x,y,rad,rad);
  //  }
  //}
  save(bgName);
  return bgName;
}

public void streakyColours(int passes, int iterations, float step, float radMin, float radMax, int colMin, int colMax){ 
    for (int j=0; j<passes; j++){
    float x = random(width);
    float y = random(height);
    float rad = random(radMin,radMax);
    fill(random(colMin,colMax),random(colMin,colMax),random(colMin,colMax),1);
    for (int i=0; i < iterations;i++){
      x = min(width,(max(0,x+random(-step,step))));
      y = min(height,(max(0,y+random(-step,step))));
      rad = min(radMax,(max(radMin,rad+random(-10,10))));
      ellipse(x,y,rad,rad);
    }
  }
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
    if (keyCode == 46 ||(keyCode >= 48 && keyCode <= 57)||(keyCode >= 96 && keyCode <= 105 || keyCode == 110 || keyCode == 190) ) {
      currTextBox.affect = currTextBox.affect + key;
    }
  }
  if (keyCode == 45) {
    drawMod *= 0.99f;
  }
  if (keyCode == 61) {
    drawMod /= 0.99f;
  }

  if (keyCode == 91) {
    viewAngle -= 0.1f;
  }
  if (keyCode == 93) {
    viewAngle += 0.1f;
  }
  
  viewAngle = max(1,viewAngle);

  if (keyCode == 32) {
    if (turnTime) {
      textYearsPassed = Float.toString(yearsPassed);
    }
    turnTime = !turnTime;
  }
  if (keyCode == ESC) {
    exit();
  }

  if (keyCode == 37) {
    yearsPassed--;
    if (!turnTime) {
      timePassed.affect = Float.toString(yearsPassed);
    }
  }
  if (keyCode == 39) {
    yearsPassed++;
    if (!turnTime) {
      timePassed.affect = Float.toString(yearsPassed);
    }
  }
  if (keyCode == 40) {
    yearsPassed-=0.083333f;
    if (!turnTime) {
      timePassed.affect = Float.toString(yearsPassed);
    }
  }
  if (keyCode == 38) {
    yearsPassed+=0.083333f;
    if (!turnTime) {
      timePassed.affect = Float.toString(yearsPassed);
    }
  }
  if (keyCode == 78) {
    simSpeed*=.8f;
  }
  if (keyCode == 77) {
    simSpeed/=.8f;
  }
}
//*/
public void mousePressed() {
  for (int i = 0; i < orgBtns.length; i++) {
    orgBtns[i].press();
    destBtns[i].press();
  }
  shipSpd.press();
  timePassed.press();
}

class TextBox { 
  String flavour;
  String affect; 
  float x, y, w, h;
  boolean pressed=false, selectable = true;
  TextBox() {
    x=0;
    y=0;
    w=100;
    h=40;
  }
  TextBox(String flav, String aff, float xx, float yy, float ww, float hh, boolean pr, boolean slct) {
    flavour = flav; 
    affect = aff; 
    x=xx; 
    y=yy; 
    w=ww; 
    h=hh; 
    pressed = pr; 
    selectable = slct;
  }
  public void place() {
    if (pressed) {    
      noFill(); 
      stroke(255);
      rect(x, y, w, h, 4);
    } else if (!selectable) {
      noStroke();
      fill(50,0,100,50);
      rect(x, y, w, h, 4);
    }
    fill(255);
    textAlign(LEFT, BASELINE);
    text(flavour, x+10, y+(h*.35f));
    text(affect, x+10, y+(h*.85f));
  }
  public void press() {
    if (selectable) {
      if (mouseX > x && mouseX < x+w && mouseY > y && mouseY < y+h) {
        try {
          currTextBox.pressed = false;
        }
        catch(Exception e) {
        }
        pressed = true;
        currTextBox = this;
      }
    }
  }
}

class Button {
  Planet plnt;
  float x, y, w, h;
  boolean pressed=false, isOrigin;
  Button() {
    isOrigin=true;
    x=0;
    y=0;
    w=100;
    h=40;
  }
  Button(boolean og, float xx, float yy, float ww, float hh) {
    isOrigin=og;
    x=xx;
    y=yy;
    w=ww;
    h=hh;
  }
  public void place() {
    fill(200,170,100);
    if (pressed) {
      stroke(255);
    } else {
      noStroke();
    }
    rect(x, y, w, h, 4);
    rect(x+1, y+1, w-2, h-2, 4);
    fill(0);
    textAlign(CENTER, CENTER);
    text(plnt.name, x+(w/2), y+(h/2));
  }
  public void press() {
    if (mouseX > x && mouseX < x+w && mouseY > y && mouseY < y+h) {
      pressed = true;
      if (isOrigin) {
        originPlanet=plnt;
        try {
          if (currOrgBtn != this)currOrgBtn.pressed = false;
        }
        catch(Exception e) {
        }
        currOrgBtn = this;
      } else {
        destPlanet=plnt;
        try {
          if (currDestBtn != this)currDestBtn.pressed = false;
        }
        catch(Exception e) {
        }
        currDestBtn = this;
      }
    }
  }
}
class Planet {
  PImage img;
  String name;
  float radius, orbit, x, y, startPos, bounceOffset = random(1);
  boolean ring;
  Planet() {
    name = "planet"; 
    radius = 10; 
    orbit = 10; 
    startPos = 10; 
    ring = false;
  };
  Planet(String nm, float rad, float orb, float stP, boolean ringg) {
    name = nm; 
    radius = rad; 
    orbit = orb; 
    startPos = stP*orb; 
    ring = ringg;
  };
  public void place(float currTime) {
    noFill();
    ellipse(width/2, height/2, radius*2*drawMod, (radius*2*drawMod)/viewAngle);
    x = (cos((currTime + startPos)*((2*PI)/orbit))*radius);
    y = (-sin((currTime + startPos)*((2*PI)/orbit))*(radius/viewAngle));
    /*
    if (ring) ellipse((x*drawMod)+960, (y*drawMod)+540, 10, 10/viewAngle);
    fill(255);
    ellipse((x*drawMod)+960, (y*drawMod)+540, 5, 5);
    */
    image(img,(x*drawMod)+(width/2)-(img.width/2), (cos(bounceTime+bounceOffset)*2)+(y*drawMod)+(height/2)-(img.height/2));
  }
  public Vectr whereAt(float time) {
    Vectr retVal = new Vectr();
    retVal.x = cos((time+startPos)*((2*PI)/orbit))*radius;
    retVal.y = -sin((time+startPos)*((2*PI)/orbit))*radius;
    return retVal;
  }
}
class Vectr {
  float x, y;
  Vectr() {
    x=0;
    y=0;
  }
  Vectr(float xx, float yy) {
    x=xx;
    y=yy;
  }
}

class Star{
  float x, y,radius;
  Star(){
    x=random((float)width);
    y=random((float)height);
    radius=pow(random(2),1.5f);
  }
  public void place(){
    x-=0.5f;
    if (x<0){x=width;y=random(height);}
    ellipse(x,y,radius,radius);
  }
}
  public void settings() {  size(1920, 1080,P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "planets_but_beeyootiful" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
