package frc.team1699.utils.leds;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.team1699.utils.leds.colors.HSVColor;

public class LEDController {
    private int rainbowFirstPixelHue = 50;

    private AddressableLED leds;
    private AddressableLEDBuffer ledBuffer;
    private int ledLength;

    private HSVColor currentColor;

    public LEDController(int ledLength, int port) {
        this.ledLength = ledLength;
        leds = new AddressableLED(port);
        ledBuffer = new AddressableLEDBuffer(ledLength);
        leds.setLength(ledLength);
    }

    public void start() {
        leds.start();
    }

    public void stop() {
        leds.stop();
    }

    
    public void rainbow() {
        for (int i = 0; i < ledLength; i++) {
            final int hue = (rainbowFirstPixelHue + (i * 180 / ledLength)) % 180;
            ledBuffer.setHSV(i, hue, 255, 50);
        }
        rainbowFirstPixelHue += 3;
        rainbowFirstPixelHue %= 180;
        leds.setData(ledBuffer);
    }

    public void solidColor(HSVColor color) {
        for(int i = 0; i < ledLength; i++) {
            System.out.println("changing color of " + i);
            ledBuffer.setHSV(i, color.getHue(), color.getSaturation(), color.getValue());
        }
        leds.setData(ledBuffer);
        currentColor = color;
    }

    public void alternateColors(HSVColor colorOne, HSVColor colorTwo) {
        for(int i = 0; i < ledLength; i++) {
            if(i%2 == 0) {
                ledBuffer.setHSV(i, colorOne.getHue(), colorOne.getSaturation(), colorOne.getValue());
            } else {
                ledBuffer.setHSV(i, colorTwo.getHue(), colorTwo.getSaturation(), colorTwo.getValue());
            }
        }
        leds.setData(ledBuffer);
    }

    public HSVColor getColor() {
        return currentColor;
    }
}