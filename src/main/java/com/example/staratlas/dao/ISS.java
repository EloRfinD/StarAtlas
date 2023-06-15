package com.example.staratlas.dao;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ISS extends Star{
    private double rightAscension;
    private double declination;

    public static double GMST() {
        // Get current UTC time
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // Calculate the interval in UT1 days since 2000 January 1, 12h UT
        double julianDate = now.toEpochSecond(ZoneOffset.UTC) / 86400.0 + 2440587.5;
        double interval = julianDate - 2451545.0;

        // Calculate GMST using the formula
        double gmst = 18.697374558 + 24.06570982441908 * interval;

        // Reduce gmst to a value in the range 0-24 hours
        gmst %= 24;
        if (gmst < 0) {
            gmst += 24;
        }
        System.out.println(gmst);
        return gmst * 15;
    }

    public ISS(double rightAscension, double declination) {
        super("data\\move\\ISS\\ISS.txt", rightAscension, declination, 1.0f, "МКС");
        this.rightAscension = rightAscension;
        this.declination = declination;
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
}


