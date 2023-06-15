package com.example.staratlas.ui;

import com.example.staratlas.dao.Star;

import java.awt.*;

public class StarRenderer {
    private final Camera camera;

    public StarRenderer(Camera camera) {
        this.camera = camera;
    }

    public boolean shouldItEvenBeOnScreen(Star star) {
        double ra = star.getRightAscension() + camera.getYaw();
        double dec = star.getDeclination();
        double cameraYaw = 0;
        double cameraPitch = Math.toRadians(camera.getPitch());

        // Convert right ascension and declination to Cartesian coordinates
        double cosDec = Math.cos(Math.toRadians(dec));
        double x = Math.cos(Math.toRadians(ra)) * cosDec;
        double y = Math.sin(Math.toRadians(ra)) * cosDec;
        double z = Math.sin(Math.toRadians(dec));

        // Apply camera pitch and yaw to Cartesian coordinates
        double cosPitch = Math.cos(cameraPitch);
        double sinPitch = Math.sin(cameraPitch);
        double newX = cosPitch * x - sinPitch * z;
        double newZ = -sinPitch * x - cosPitch * z;
        if (newX < 0) {
            return false;
        }
        double FOV = 1/Math.tan(Math.toRadians(camera.getFieldOfView()/2));
        double chX = y/newX * FOV;
        double chY = newZ/newX * FOV;
        return !(chX < -1.01) && !(chY < -1.01) && !(chX > 1.01) && !(chY > 1.01);
    }

    public Point calculateScreenPosition(Star star, int screenWidth, int screenHeight) {
        double ra = star.getRightAscension() + camera.getYaw();
        double dec = star.getDeclination();
        double cameraPitch = Math.toRadians(camera.getPitch());

        // Convert right ascension and declination to Cartesian coordinates
        double cosDec = Math.cos(Math.toRadians(dec));
        double x = Math.cos(Math.toRadians(ra)) * cosDec;
        double y = Math.sin(Math.toRadians(ra)) * cosDec;
        double z = Math.sin(Math.toRadians(dec));

        // Apply camera pitch and yaw to Cartesian coordinates
        double cosPitch = Math.cos(cameraPitch);
        double sinPitch = Math.sin(cameraPitch);
        double newX = cosPitch * x - sinPitch * z;
        double newZ = -sinPitch * x - cosPitch * z;

        // Convert Cartesian coordinates back to right ascension and declination
        double newRa = Math.toDegrees(Math.atan2(y, newX));
        double FOV = 1/Math.tan(Math.toRadians(camera.getFieldOfView()/2));
        int screenDim = Math.max(screenHeight, screenWidth);
        int scY = (int) (screenHeight * 0.5 + (0.5 * screenDim * newZ/newX * FOV));
        int scX = (int) (screenWidth * 0.5 + (0.5 * screenDim * y/newX * FOV));
        if (newRa < 0) {
            newRa += 360;
        }

        // Calculate position on screen based on new right ascension and declination
        return new Point(scX, scY);
    }
}