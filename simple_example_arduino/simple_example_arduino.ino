/*
 * Copyright (C) 2014 InventIt Inc.
 * 
 * See https://github.com/inventit/moat-iot-get-started
 */

#include <DallasTemperature.h>
#include <OneWire.h>
#include <Boards.h>
#include <Firmata.h>
#include <LiquidCrystal.h>

bool cmdComplete = false;
int cmdLen = 0;

#define tempPin 2    // Where the temperature sensor's signal pin connect to
OneWire oneWire(tempPin);
DallasTemperature sensors(&oneWire);

#define BAUDRATE 19200

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);
int lcd_key         = 0;
int adc_key_in    = 0;
#define btnRIGHT    0
#define btnUP         1
#define btnDOWN     2
#define btnLEFT     3
#define btnSELECT 4
#define btnNONE     5

#define MAXBUFFLEN 147
#define MAXTEXTLEN 141
char buff[MAXBUFFLEN]; // save strings come from PC side
char text[MAXTEXTLEN]; // text to show
int textLen = 0; // length of text to show
int curShowPos = 0;
unsigned long lastLcdUpdateTime = 0;
int updateInterval = 3000; // update text for 3 seconds (about)
#define MINUPDATEINTERVAL 500
#define MAXUPDATEINTERVAL 8000
#define UPDATEINTERVALSTEP 500

bool clickValid = true; // avoid dupplicate click

#define VERSION "1.0.12"

void showSpeed() {
	// / ( MAXUPDATEINTERVAL - UPDATEINTERVALSTEP);
    unsigned long pos = updateInterval/500 - 1;
    pos = 15-pos; 
    pos = min(15, pos); 
    pos = max(0, pos);    

    lcd.setCursor(0,1);
    int i;
    for(i=0; i<pos; i++) {
        lcd.print('>');
    }
    lcd.print('|');
    for(i=pos+1; i<15; i++) {
        lcd.print('<');
    }
}

byte showWhat = 1;
void showBanner() {
    showWhat++;
    if( showWhat%2 == 0 ) {
        lcd.clear();
        lcd.print("MOAT App V"); 
        lcd.print(VERSION);
        lcd.setCursor(0,1);
        lcd.print("        Inventit");
    } 
    else {
        lcd.clear();
        lcd.print("Nothing to do...");
        lcd.setCursor(0,1);
        lcd.print("  --MOAT IoT App"); 
    }
}

void setup() {    
    lcd.begin(16, 2); // start the library 
    showBanner();

    Serial.begin(BAUDRATE);    
    sensors.begin();
}    

// read the buttons
int read_LCD_buttons() {
    adc_key_in = analogRead(0); // read the value from the sensor 
    // my buttons when read are centered at these valies:
	// 0, 144, 329, 504, 741
    // we add approx 50 to those values and check to see if we are close
	// for this example, only care of select button
    if (adc_key_in < 790 && adc_key_in > 555) return btnSELECT;
	// We make this the 1st option for speed reasons
	// since it will be the most likely result
    if (adc_key_in > 1000) return btnNONE;
    if (adc_key_in < 50)     return btnRIGHT;    
    if (adc_key_in < 195)    return btnUP; 
    if (adc_key_in < 380)    return btnDOWN; 
    if (adc_key_in < 555)    return btnLEFT; 
    if (adc_key_in < 790)    return btnSELECT;     
    return btnNONE;    // when all others fail, return this...
}

void notify(char *msg, bool toShowSpeed) {
    lcd.clear();
    lcd.print(msg);

    if( toShowSpeed ) {
        showSpeed();
    }

    delay(500);
    // refresh immediately
    lastLcdUpdateTime = millis() - updateInterval; 
}

// "SELECT" button really clicked, execute the expected task
void buttonClicked() {
    notify("Who clicked me?", false);

    Serial.flush();
    Serial.println("CLICKED:TRUE");
}

// check if "SELECT" button be clicked, if clicked, call buttonClicked()
int clickIdleCycle = 0;
void checkClick() {
    if( !clickValid ) {
        if( ++clickIdleCycle > 10 ) {
            clickValid = true;
        }
    } 
    else {
        clickIdleCycle = 0;
    } 

    int key = read_LCD_buttons();

    if( key == btnSELECT ) {
        if( clickValid ) {
            clickValid = false;
            clickIdleCycle = 0;
            buttonClicked();
        }
        return;
    } 

    clickValid = true;

    switch(key) {
    case btnUP:
        lcd.display();
        notify("Open LCD", false);
        break;

    case btnDOWN:
        notify("Close LCD", false);
        lcd.noDisplay();
        break;    

    case btnLEFT:
        updateInterval += UPDATEINTERVALSTEP;
        updateInterval = min(updateInterval, MAXUPDATEINTERVAL);
        updateInterval = max(updateInterval, MINUPDATEINTERVAL);
        notify("Speed down", true);
        break;

    case btnRIGHT:
        updateInterval -= UPDATEINTERVALSTEP;
        updateInterval = min(updateInterval, MAXUPDATEINTERVAL);
        updateInterval = max(updateInterval, MINUPDATEINTERVAL);
        notify("Speed up", true);
        break;
    }

}

// Content currently showed in LCD
char line1[17], line2[17];
void drawLCD() {
    lastLcdUpdateTime = millis();

    if( textLen == 0 ) { // nothing to show
        showBanner();
        checkClick();
        return;
    }

    // roll back to start of the text
    if( curShowPos > textLen ) {
        curShowPos = 0;
    }

    checkClick();

    int i;
    if( textLen > curShowPos ) {
        for(i=0; i<16 && i<textLen-curShowPos; i++ ) {
            line1[i] = text[curShowPos+i];
        }
        line1[i] = 0;
        lcd.clear();
        lcd.print(line1);
    }

    checkClick();
    curShowPos += 16;

    if( textLen > curShowPos ) {
        for(i=0; i<16 && i<textLen-curShowPos; i++ ) {
            line2[i] = text[curShowPos+i];
        }
        line2[i] = 0;
        lcd.setCursor(0,1);
        lcd.print(line2);
    } 
}


void loop() {    
    checkClick();

    if( cmdComplete ) {
		// Got a whole command (a sentence end with \n
        buff[cmdLen] = 0;
        String cmd = buff;

        if( cmd.startsWith("LCD:") ) {
            if(cmdLen > MAXTEXTLEN+4 ) {
                Serial.println("LCD:TOOMUCHDATA");
                cmdComplete = false; // wait for next command
                cmdLen = 0;
                return;
            }

            textLen = cmdLen-4; // not include the first "LCD:"
            cmd.substring(4).getBytes((unsigned char *)text, MAXTEXTLEN-1);
            text[textLen] = 0;
            curShowPos = 0;

            checkClick();

            Serial.println("LCD:OK");
        } 
        else {
            if( cmd.startsWith("TEMP?") ) {
				// Send the command to get temperatures
                sensors.requestTemperatures();
                checkClick();
                float temp = sensors.getTempCByIndex(0);
                checkClick();

                Serial.print("TEMP:"); 
                Serial.println(temp);
            } 
            else {
                Serial.print("UNKNOWN:"); 
                Serial.println(cmd);
            }
        }

        cmdComplete = false;
        cmdLen = 0;
    } // if cmdComplete

    checkClick();

    if( millis() - lastLcdUpdateTime >= updateInterval
			|| millis() < lastLcdUpdateTime) {
		// time to update LCD (overflow simple handled
		// because it occure one time per 50 days
        drawLCD();
    }

    checkClick();
}

void serialEvent() {
    int num;
    while (num = Serial.available()) {
        // get the new byte:
        char inChar = (char)Serial.read(); 
        // add it to the inputString:
        buff[cmdLen++] = inChar;
        // if the incoming character is a newline, set a flag
        // so the main loop can do something about it:
        if (inChar == '\n' || cmdLen >= MAXBUFFLEN) {
            cmdLen--;
            cmdComplete = true;
        } 
    } 
}



