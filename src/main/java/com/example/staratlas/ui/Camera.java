package com.example.staratlas.ui;
import com.example.staratlas.Config;
public class Camera {
    private double pitch;
    private double yaw;
    private double fieldOfView;

    public Camera() {
        pitch = Config.CAMERA_PITCH;
        yaw = Config.CAMERA_YAW;
        fieldOfView = Config.CAMERA_FOV;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        if (pitch > 90) {
            pitch = 90;
        }
        if (pitch < -90) {
            pitch = -90;
        }
        this.pitch = pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public double getFieldOfView() {
        return fieldOfView;
    }

    public void setFieldOfView(double FOV) {
        if (FOV < 30) {
            FOV = 30;
        }
        if (FOV > 120) {
            FOV = 120;
        }
        this.fieldOfView = FOV;
    }

    public void rotate(double dx, double dy, double divisor) {
        setYaw(getYaw() + dx * fieldOfView / divisor);
        setPitch(getPitch() - dy * fieldOfView / divisor);
    }
}