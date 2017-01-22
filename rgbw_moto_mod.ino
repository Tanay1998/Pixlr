#include <FAB_LED.h>

/// @brief This parameter says how many LEDs will be lit up in your strip.
const uint8_t numPixels = 40;

/// @brief This says how bright LEDs will be (max is 255)
const uint8_t maxBrightness = 255;

#define POV_OFFSET 16

sk6812<D,7>   LEDstrip;

rgbw pixels[numPixels] = {};

static const uint32_t rainbow[8] = {
 0x008300b5,
0x000013ff,
0x0000eaff,
0x0056ff00,
0x00e4ff00,
0x00ff8d00,
0x00ff4100,
0x00ff0000
};

static const uint8_t char_map[][5] = {
  //A
  {
    B11100000,
    B00011100,
    B00010011,
    B00011100,
    B11100000
  },
  {//B
    B00000000,
    B01110110,
    B10001001,
    B10001001,
    B11111111
  },
  {//C
    B00000000,
    B01000010,
    B10000001,
    B01000010,
    B00111100
  },
  { //D
    B00111100,
    B01000010,
    B10000001,
    B10000001,
    B11111111
  },
  { //E
    B10001001,
    B10001001,
    B10001001,
    B10001001,
    B11111111
  },
  { //F
    B00001001,
    B00001001,
    B00001001,
    B00001001,
    B11111111
  },
  { //G
    B01110010,
    B10010001,
    B10010001,
    B10000001,
    B01111110
  },
  { //H
    B11111111,
    B00001000,
    B00001000,
    B00001000,
    B11111111
  },
  { //I
    B10000001,
    B10000001,
    B11111111,
    B10000001,
    B10000001
  },
  { //J
    B00000001,
    B10000001,
    B01111111,
    B10000001,
    B01110001
  },
  { //K
    B10000001,
    B01000010,
    B00100100,
    B00011000,
    B11111111
  },
  { //L
    B10000000,
    B10000000,
    B10000000,
    B10000000,
    B11111111
  },
  { //M
    B11111111,
    B00000010,
    B11111100,
    B00000010,
    B11111111
  },
  { //N
    B11111111,
    B00110000,
    B00001100,
    B00000010,
    B11111111
  },
  { //O
    B01111110,
    B10000001,
    B10000001,
    B10000001,
    B01111110
  },
  { //P
    B00000110,
    B00001001,
    B00001001,
    B00001001,
    B11111111
  },
  { //Q
    B10000000,
    B01111111,
    B01100001,
    B01000001,
    B01111111
  },
  { //R
    B10000110,
    B01001001,
    B00101001,
    B00011001,
    B11111111
  },
  { //S
    B01100110,
    B10010001,
    B10010001,
    B10010001,
    B10001110
  },
  { //T
    B00000001,
    B00000001,
    B11111111,
    B00000001,
    B00000001
  },
  { //U
    B01111111,
    B10000000,
    B10000000,
    B10000000,
    B01111111
  },
  { //V
    B00111111,
    B01100000,
    B10000000,
    B01100000,
    B00111111
  },
  { //W
    B00111111,
    B11100000,
    B00110000,
    B11100000,
    B00111111
  },
  { //X
    B11000111,
    B00101000,
    B00010000,
    B00101000,
    B11000111
  },
  { //Y
    B00000111,
    B00001000,
    B11110000,
    B00001000,
    B00000111
  },
  { //Z
    B10000011,
    B10000101,
    B10001001,
    B10010001,
    B11100001
  },
  
};
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

uint32_t loaded_pixel_data[2][40]= {};
uint32_t frameTime = 0;
uint32_t frameDelta = 0;
char buf[30];
uint8_t visualizerData[8] = {};
uint8_t currentIndex = 0;
uint8_t endIndex = 0;

#define MODSERIAL Serial1 

void setup()
{
  Serial.begin(115200);
  Serial1.begin(115200);
  /*
  while (!Serial) { //for serial communication -- SWITCH STATEMENT
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
#define INDEX 0
#define LENGTH 1
#define PAYLOAD 2

#define BUILTIN 1
#define FRAME 2
#define STRING 3
#define AUDIO 4
#define BLANK 0
unsigned int state = 0;
unsigned int command = 0;
unsigned int index = 0;
unsigned int lengthOfInput = 0;
unsigned int t_frames = 0;

void loop()
{


  
  if (MODSERIAL.available() > 0) {
    state = MODSERIAL.read();
    Serial.println(state);
  }
  switch(state) {
    case BLANK:
      command = BLANK;
      Builtin(0);
    break;
    case BUILTIN:
      command = BUILTIN;
      while(!MODSERIAL.available()){}
      index = MODSERIAL.read();
      Serial.println("builtin");
      Builtin(index);
    break;
    case FRAME:
      command = FRAME;
      // Read Size
      while(!MODSERIAL.available()){}
      lengthOfInput = MODSERIAL.read();
      
      // Read all incoming bytes
      t_frames = lengthOfInput / (40 * 4);
      for (int f = 0; f < t_frames; f++)
      {
        for (int x = 0; x < 40; x++)
        {
          int temp = 0;
          for (int t = 0; t < 4; t++)
          {
            while(!MODSERIAL.available()){}
            temp = MODSERIAL.read();
            if (t != 3) temp <<= 8;    
          }
          if (f < 3) loaded_pixel_data[f][x] = temp;
        }
      }
      // Display the Loaded Pixel Data
      DrawFrames(loaded_pixel_data, t_frames, t_frames);
    break;
    case STRING:
      command = STRING;
      // Read Size
      // while(!MODSERIAL.available()){}
      lengthOfInput = 8;//= MODSERIAL.read();
      
      // Read all incoming bytes 
      for (int i = 0; i < lengthOfInput; i++)
      {
        while (!MODSERIAL.available()) {}
        buf[i] = MODSERIAL.read();
      }

      //Execute
      POV();
    break;
    case AUDIO:
      command = AUDIO;
      // Read size = 8
      // while(!MODSERIAL.available()){}
      lengthOfInput = 8;//= MODSERIAL.read();
      Serial.println("start audio");
      // Get next eight bytes which specify the heights of the 8 columns from left to right in values 0-5
      for (int i = 0; i < lengthOfInput; i++)
      {
        while (!MODSERIAL.available()) {}
        visualizerData[i] = MODSERIAL.read();
      }
      Serial.println("Stop audio");
      // Output this array to the board
      DisplayVisualizer();  
      state = 0;    
    break;
    default:
    break;
  
  }
  
}

void DisplayVisualizer()
{
  // 1 5 0 4 3 2 5 2 in visualizerData
  //visualizerData is 0-5
  //Serial.println("visualizer");
  uint8_t bomb = 0;
  for (int i = 0; i < 8; i++) {
    if(visualizerData[i] >= 54) {
      bomb++;
    }
  }
  for (pos = 0; pos < numPixels; pos++) {
    //pixels[pos].h = 0xFF; // hgrb has h field
    pixels[pos].g = 0;
    pixels[pos].b = 0;
    pixels[pos].r = 0;
    pixels[pos].w = 0; // grbw has w field
  }

  for (int col = 0; col < 8; col++)
  {
    if(bomb > 1) {
      for (int i = 0; i < 5; i++) {
        pos = col + 8 * i;
        pixels[pos].g = 255;
        pixels[pos].b = 255;
        pixels[pos].r = 255;
        pixels[pos].w = 255;
      }
    } else {
    for (int row = 4; row >= 5 - (visualizerData[col] - 50) && row >= 0; row--)
    {
       pos = col + 8 * row;
       pixels[pos].g = (uint8_t)((rainbow[7 - col] >> 16) & 0xFF);
       pixels[pos].r = (uint8_t)((rainbow[7 - col] >> 8) & 0xFF);
       pixels[pos].b = (uint8_t)(rainbow[7 - col] & 0xFF);
       pixels[pos].w = (uint8_t)((rainbow[7 - col] >> 24) & 0xFF);
    }
    }
    /*
    for (int row = 4; row >= 5 - visualizerData[col + 8] && row >= 0; row--)
    {
       pos = col + 8 * row;
       pixels[pos].r = 16;
       //pixels[pos].g = 0;
       pixels[pos].b = 0;
       pixels[pos].w = 0;
    }
    */
  }

  LEDstrip.sendPixels(numPixels, pixels);
  LEDstrip.refresh();
}

void Builtin(uint8_t inByte) {
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
        //delay(300);
        //Serial.println("Read zero");
      break;
      case 1:
        for (uint8_t pos = 0; pos < numPixels; pos++) {
          //pixels[pos].h = 0xFF; // hgrb has h field
          pixels[pos].g = 0;
          pixels[pos].b = 0;
          pixels[pos].r = 0;
          pixels[pos].w = 255; // grbw has w field
        }
        LEDstrip.sendPixels(numPixels,pixels); //after this, the second frame shows up in same block of code above
        LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
        //delay(300);
        //Serial.println("Read one");
      break;
      
      case 2:
        DrawFrames(index1, 2, 2); //pac-man
      break;
      case 3:
        DrawFrames(index2, 1, 1); //patriotic
      break;
      /*
      case 4:
        DrawFrames(index3, 1, 1); //holiday
      break;
      case 5:
        DrawFrames(index4, 1, 1); //sephia
      break;
      case 6:
        DrawFrames(index5, 1, 1); //sunshine
      break;
      case 7:
        DrawFrames(index6, 1, 1); //ice
      break;
      case 8:
        //Serial.println("Hell");
        DrawFrames(index7, 1, 1); //hell
      break;
      case 9:
        DrawFrames(index8, 1, 1); //green
      break;
      
      case 10:
        DrawFrames(index9, 1, 1); //vogue
      break;
      
      
     case 11:
        DrawFrames(index10, 2, 2);
      break;
      
      
      case 12:
        DrawFrames(index11, 1, 1); //red and blue split
      break;
      
      case 13:
        DrawFrames(index12, 1, 1); //lightning
        break;
        */
      case 100:
      for (uint8_t pos = 0; pos < numPixels; pos++) {
          //pixels[pos].h = 0xFF; // hgrb has h field
          pixels[pos].g = 0;
          pixels[pos].b = 0;
          pixels[pos].r = 0;
          pixels[pos].w = 128; // grbw has w field
        }// All white
        LEDstrip.sendPixels(numPixels,pixels); //after this, the second frame shows up in same block of code above
        LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
       break;
       /*
       case 'F':
       POV();
       break;
       case 'G':
       Scroll();
       break;
       */
      default:
         
      break;
    }
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
int c_pos = 0;
int col = 0;
char testString[27] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
void Scroll() {
  for(uint8_t j = 0; j < 5; j++) {
   for(uint8_t i = 0; i < 8; i++) {
    if((char_map[(uint8_t)testString[c_pos] - 65][j] >> i) & B1) {
      pixels[i + (8 * j)].g = 255;
    } else {
      pixels[i + (8 * j)].g = 0;
    }
    pixels[i + (8 * j)].r = 0;
    pixels[i + (8 * j)].b = 0;
    pixels[i + (8 * j)].w = 0;   
  }
  }
  LEDstrip.sendPixels(numPixels,pixels);
  c_pos++;
  if(!testString[c_pos]) {
    c_pos = 0;
  }
  delay(300);
 }

void POV() {
  for(uint8_t i = 0; i < 8; i++) {
    if((char_map[(uint8_t)testString[c_pos] - 65][col] >> i) & B1) {
      pixels[POV_OFFSET + i].g = 255;
    } else {
      pixels[POV_OFFSET + i].g = 0;
    }
    pixels[POV_OFFSET + i].r = 0;
    pixels[POV_OFFSET + i].b = 0;
    pixels[POV_OFFSET + i].w = 0;   
  }
  LEDstrip.sendPixels(numPixels,pixels); //after this, the second frame shows up in same block of code above
   // LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
  //delay(1);
  for(uint8_t pos = POV_OFFSET; pos < (POV_OFFSET + 8); pos++) {
    pixels[pos].g = 0;
    pixels[pos].r = 0;
    pixels[pos].b = 0;
    pixels[pos].w = 0;
    
  }
  LEDstrip.sendPixels(numPixels,pixels); //after this, the second frame shows up in same block of code above
    //LEDstrip.refresh(); // Hack: needed for apa102 to display last pixels
  //delay(1);
  if(col == 5) {
    col = 0;
    if(c_pos == 3) {
      c_pos = 0;
    } else {
      c_pos++;
    }
  } else {
    col++;
  }
  //Serial.println(c_pos);
}

