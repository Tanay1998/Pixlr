#include <FAB_LED.h>

/// @brief This parameter says how many LEDs will be lit up in your strip.
const uint8_t numPixels = 40;

/// @brief This says how bright LEDs will be (max is 255)
const uint8_t maxBrightness = 2;

sk6812<D,7>   LEDstrip;

rgbw pixels[numPixels] = {};

int inByte = 0;
static int frames = 2; //comment this out when using serial communication with moto mod
static const uint32_t index1[2][40] = { //use progmem. Arrays of large arrays are too large for comfort to store on dynamic memory
{
0x00000000, 0x00000000, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 
0x00000000, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 
0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 
0x00000000, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 
0x00000000, 0x00000000, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000
},
{
0x00000000, 0x00000000, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 
0x00000000, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 
0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 
0x00000000, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 
0x00000000, 0x00000000, 0xff00ffff, 0xff00ffff, 0xff00ffff, 0x00000000, 0x00000000, 0x00000000
}}; //pac-man

static const uint32_t index2[1][40] = {
{

0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 
0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 
0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 0xffffffff, 
0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 
0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300, 0xffff2300

}}; //patriotic

static const uint32_t index3[1][40] = {
{

0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 
0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 
0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 
0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 
0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a, 0xff109e1a

}}; //holiday

static const uint32_t index4[1][40] = {
{

0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 
0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 
0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 
0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 
0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5, 0xff0c64b5

}}; //sephia

static const uint32_t index5[1][40] = {
{

0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 
0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 
0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 
0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 
0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff, 0xff00deff

}}; //sunshine

static const uint32_t index6[1][40] = {
{

0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 
0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 
0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 
0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 
0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e, 0xffb7cf8e

}}; //ice


static const uint32_t index7[1][40] = {
{

0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 
0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 
0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 
0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 
0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc, 0xff0606bc

}}; //hell

static const uint32_t index8[1][40] = {
{

0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 
0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 
0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 
0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 
0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13, 0xff00ff13

}}; //green

static const uint32_t index9[1][40] = {
{

0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 
0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 
0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 
0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 
0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffe912ff

}}; //vogue

static const uint32_t index10[2][40] = {
{

0xffffffff, 0xffffffff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffda0517, 0xffda0517, 0xffe912ff, 
0xffffffff, 0xffffffff, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xffda0517, 0xffda0517, 0xffe912ff, 
0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4, 0xff12fff4, 
0xffffd912, 0xffffd912, 0xffffd912, 0xff12fff4, 0xffe912ff, 0xffe912ff, 0xffe912ff, 0xff48da0f, 
0xffffd912, 0xffffd912, 0xffffd912, 0xff12fff4, 0xffe912ff, 0xffe912ff, 0xff48da0f, 0xffe912ff

},
{

0xff48da0f, 0xff48da0f, 0xff12fff4, 0xffe912ff, 0xffe912ff, 0xffffd912, 0xffffd912, 0xffffd912, 
0xff48da0f, 0xff48da0f, 0xff12fff4, 0xffe912ff, 0xffe912ff, 0xff48da0f, 0xff48da0f, 0xffffffff, 
0xff12fff4, 0xffffd912, 0xff12fff4, 0xff12fff4, 0xffe912ff, 0xff48da0f, 0xff12fff4, 0xffffffff, 
0xffe912ff, 0xffda0517, 0xffda0517, 0xff12fff4, 0xffe912ff, 0xff48da0f, 0xff48da0f, 0xffffffff, 
0xffe912ff, 0xffda0517, 0xffda0517, 0xff12fff4, 0xffe912ff, 0xffffffff, 0xffffffff, 0xffda0517

}}; //disco

static const uint32_t index11[1][40] = {
{

0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14, 
0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14, 
0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14, 
0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14, 
0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14
}}; // red and blue split

static const uint32_t index12[1][40] = {
{
0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14, 
0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14, 
0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14, 
0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14, 
0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff0000ff, 0xff710b14, 0xff710b14, 0xff710b14, 0xff710b14
}}; //lightning

uint32_t loaded_pixel_data[3][40]= {};

void setup()
{
  Serial.begin(115200);
  Serial1.begin(115200);
  /*while (!Serial) { //for serial communication -- SWITCH STATEMENT
    ; // wait for serial port to connect. Needed for native USB port only
  }*/
  // RGB initializes to zero, but for APA102, h is set to full brightness.
  // Depending on which class you use for your pixels, comment the h or w field
  // accordingly.
  
  for (uint8_t pos = 0; pos < numPixels; pos++) {
    //pixels[pos].h = 0xFF; // hgrb has h field
    pixels[pos].g = 0;
    pixels[pos].b = 0;
    pixels[pos].r = 0;
    pixels[pos].w = 0; // grbw has w field
  }

  LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels

  // Clear display
  LEDstrip.sendPixels(numPixels,pixels);
  LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
}

////////////////////////////////////////////////////////////////////////////////
/// @brief This method is automatically called repeatedly after setup() has run.
/// It is the main loop.
////////////////////////////////////////////////////////////////////////////////
int pos = 0;
void loop()
{
  if (Serial1.available() > 0) {
    inByte = Serial1.read();
    Serial.println(inByte);
  }
    switch (inByte) {
      case 0:
        for (uint8_t pos = 0; pos < numPixels; pos++) {
          //pixels[pos].h = 0xFF; // hgrb has h field
          pixels[pos].g = 0;
          pixels[pos].b = 0;
          pixels[pos].r = 0;
          pixels[pos].w = 0; // grbw has w field
        }
        LEDstrip.sendPixels(numPixels,pixels); //after this, the second frame shows up in same block of code above
        LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
        delay(300);
        Serial.println("Read zero");
      break;
      case 1:
        for (uint8_t pos = 0; pos < numPixels; pos++) {
          //pixels[pos].h = 0xFF; // hgrb has h field
          pixels[pos].g = 0;
          pixels[pos].b = 0;
          pixels[pos].r = 0;
          pixels[pos].w = maxBrightness; // grbw has w field
        }
        LEDstrip.sendPixels(numPixels,pixels); //after this, the second frame shows up in same block of code above
        LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
        delay(300);
        Serial.println("Read one");
      break;
      case 2:
        DrawFrames(index1, 2, 2); //pac-man
      break;
      case '3':
        DrawFrames(index2, 1, 1); //patriotic
      break;
      case '4':
        DrawFrames(index3, 1, 1); //holiday
      break;
      case '5':
        DrawFrames(index4, 1, 1); //sephia
      break;
      case '6':
        DrawFrames(index5, 1, 1); //sunshine
      break;
      case '7':
        DrawFrames(index6, 1, 1); //ice
      break;
      case '8':
        Serial.println("Hell");
        DrawFrames(index7, 1, 1); //hell
      break;
      case '9':
        DrawFrames(index8, 1, 1); //green
      break;
//      case '10':
//        DrawFrames(index9, 1, 1); //vogue
//      break;
//      case '11':
//        DrawFrames(index10, 2, 2);
//      break;
//      case '12':
//        DrawFrames(index11, 1, 1); //red and blue split
//      break;
//      case '13':
//        DrawFrames(index12, 1, 1); //lightning
      default:
         
      break;
    }
  
 

   /*
   if(pos - 1 >= 0) {
   pixels[pos - 1].g = 0;
   pixels[pos - 1].b = 0;
   pixels[pos - 1].r = 0;
   pixels[pos - 1].w = 0;
   LEDstrip.sendPixels(numPixels, pixels);
    LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
   }

   pixels[pos].g = 0;
   pixels[pos].b = 0;
   pixels[pos].r = 0;
   pixels[pos].w = 16;
   LEDstrip.sendPixels(numPixels, pixels);
    LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
    delay(100);
    if(pos < 40) {
      pos++;
    } else {
      pos = 0;
    }
    */
 /*
  LEDstrip.sendPixels(numPixels, pixels);
  LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
  */
}

void DrawFrames(uint32_t frameArray[][40], int numFrames, int fps) {
  if (numFrames < 4) {
    for (int frame = 0; frame < numFrames; frame++) {
      for (uint8_t pos = 0; pos < numPixels; pos++) {
        pixels[pos].g = frameArray[frame][pos] & 0xFF; //green is switched with red. ¯\_(ツ)_/¯
        pixels[pos].r = (frameArray[frame][pos] >> 8) & 0xFF;
        pixels[pos].b = (frameArray[frame][pos] >> 16) & 0xFF;
        pixels[pos].w = 0; //(pixel_data[frame][pos] >> 24) & 0xFF; 
        //Serial.print(frameArray[frame][pos]); Serial.print(" ");
      }
      LEDstrip.sendPixels(numPixels,pixels); //after this, the second frame shows up in same block of code above
      LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
      delay(1000 / fps);
    }
  }
  else {
    Serial.println("Too many frames in GIF");
  }
}

