package frc.team1699.utils.leds;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import frc.team1699.utils.leds.colors.*;

public class LEDController {
    private int rainbowFirstPixelHue = 50;
    private int rainbowTimesIterated = 0;

    private int flashBangIterations = 0;

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
        rainbowTimesIterated++;
        /*
         * “Redundancy is the single most important
         * engineering tool for designing, implementing,
         * and – importantly – proving reliability in all
         * complex, safety-critical technologies.”
         * - Mr. John Downer, May 2009
         */
        if (rainbowTimesIterated % 1 == 0) {
            rainbowFirstPixelHue += 1;
        }
        rainbowFirstPixelHue %= 180;
        leds.setData(ledBuffer);
    }

    public void solidColor(HSVColor color) {
        for (int i = 0; i < ledLength; i++) {
            System.out.println("changing color of " + i);
            ledBuffer.setHSV(i, color.getHue(), color.getSaturation(), color.getValue());
        }
        leds.setData(ledBuffer);
        currentColor = color;
    }

    public void alternateColors(HSVColor colorOne, HSVColor colorTwo) {
        for (int i = 0; i < ledLength; i++) {
            if (i % 2 == 0) {
                ledBuffer.setHSV(i, colorOne.getHue(), colorOne.getSaturation(), colorOne.getValue());
            } else {
                ledBuffer.setHSV(i, colorTwo.getHue(), colorTwo.getSaturation(), colorTwo.getValue());
            }
        }
        leds.setData(ledBuffer);
    }

    public void flashBang(HSVColor color) {
        for (int i = 0; i < ledLength; i++) {
            if (flashBangIterations % 2 == 0) {
                ledBuffer.setHSV(i, color.getHue(), color.getSaturation(), color.getValue());
            } else {
                ledBuffer.setHSV(i, 0, 0, 0);
            }
        }
        leds.setData(ledBuffer);
        flashBangIterations++;
    }

    // untested
    public void bus(HSVColor color, int busLength, int busStartPixel) {
        int end = busStartPixel + busLength;
        for (int i = busStartPixel; i <= end; i++) {
            int index = i % ledLength;
            ledBuffer.setHSV(index, color.getHue(), color.getSaturation(), color.getValue());
        }
        int i = (((busStartPixel - 1) % ledLength) + ledLength) % ledLength;
        HSVColor white = new White();
        ledBuffer.setHSV(i, white.getHue(), white.getSaturation(), white.getValue());
        leds.setData(ledBuffer);
        currentColor = color;
        busStartPixel++;
    }

    public HSVColor getColor() {
        return currentColor;
    }
}