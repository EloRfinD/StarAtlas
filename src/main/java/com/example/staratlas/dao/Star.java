package com.example.staratlas.dao;

import java.awt.*;

public class Star {
    private final String id;
    private double rightAscension;
    private double declination;
    private Color color;

    private float color1;
    public String name;
    public String desc;

    public Star(String id, double rightAscension, double declination, float color1, String name) {
        this.id = id;
        this.rightAscension = -rightAscension;
        this.declination = declination;
        this.color1 = color1;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public float getColor1() {
        return color1;
    }

    public void setColor1(float color1) {
        this.color1 = color1;
    }

    public double getRightAscension() {
        return rightAscension;
    }

    public void setRightAscension(double rightAscension) {
        this.rightAscension = rightAscension;
    }

    public double getDeclination() {
        return declination;
    }

    public void setDeclination(double declination) {
        this.declination = declination;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + ";" + name +";" + rightAscension + ";" + declination + ";" + color1;
    }
}