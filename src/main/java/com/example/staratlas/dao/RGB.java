package com.example.staratlas.dao;

import com.example.staratlas.repo.StarService;

import java.awt.*;

public class RGB {
    public RGB() {

    }
    public Color calc_color(StarService starService, int i) {
        float t = (float) (4600 * ((1 / ((0.92 * starService.getAllStars().get(i).getColor1()) + 1.7)) + (1 / ((0.92 * starService.getAllStars().get(i).getColor1()) + 0.62))));

        float x = 0, y = 0;
        if (t >= 1667 && t <= 4000) {
            x = (float) (((-0.2661239 * Math.pow(10, 9)) / Math.pow(t, 3)) + ((-0.2343580 * Math.pow(10, 6)) / Math.pow(t, 2))
                    + ((0.8776956 * Math.pow(10, 3)) / t) + 0.179910);
        } else if (t > 4000 && t <= 25000) {
            x = (float) (((-3.0258469 * Math.pow(10, 9)) / Math.pow(t, 3)) + ((2.1070379 * Math.pow(10, 6)) / Math.pow(t, 2))
                    + ((0.2226347 * Math.pow(10, 3)) / t) + 0.240390);
        }
        if (t >= 1667 && t <= 2222) {
            y = (float) (-1.1063814 * Math.pow(x, 3) - 1.34811020 * Math.pow(x, 2) + 2.18555832 * x - 0.20219683);
        } else if (t > 2222 && t <= 4000) {
            y = (float) (-0.9549476 * Math.pow(x, 3) - 1.37418593 * Math.pow(x, 2) + 2.09137015 * x - 0.16748867);
        } else if (t > 4000 && t <= 25000) {
            y = (float) (3.0817580 * Math.pow(x, 3) - 5.87338670 * Math.pow(x, 2) + 3.75112997 * x - 0.37001483);
        }

        float Y = (y == 0) ? 0 : 1;
        float X = (y == 0) ? 0 : (x * Y) / y;
        float Z = (y == 0) ? 0 : ((1 - x - y) * Y) / y;
        float r = (float) (0.41847 * X - 0.15866 * Y - 0.082835 * Z);
        float g = (float) (-0.091169 * X + 0.25243 * Y + 0.015708 * Z);
        float b = (float) (0.00092090 * X - 0.0025498 * Y + 0.17860 * Z);

        r = (r * 1000);
        if (r > 255)
            r = 255;
        g = (g * 1000);
        if (g > 255)
            g = 255;
        b = (b * 1000);
        if (b > 255)
            b = 255;

        return new Color((int) r,(int) g,(int) b);
    }
}
