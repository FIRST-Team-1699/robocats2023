package frc.team1699.utils.leds.colors;

public class HSVColor {
    private int hue, saturation;
    private final int value = 35;

    public HSVColor() {
        hue = 0;
        saturation = 0;
    }

    public HSVColor(int h, int s) {
        hue = h;
        saturation = s;
    }

    public int getHue() {
        return hue;
    }

    public void setHue(int h) {
        hue = h;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int s) {
        saturation = s;
    }
    
    public int getValue() {
        return value;
    }
}
