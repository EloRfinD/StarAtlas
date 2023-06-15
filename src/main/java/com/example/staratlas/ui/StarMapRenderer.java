package com.example.staratlas.ui;


import com.example.staratlas.Main;
import com.example.staratlas.dao.Star;
import com.example.staratlas.repo.StarService;

import java.awt.*;
import javax.swing.*;

public class StarMapRenderer extends JPanel {
    private final StarRenderer starRenderer;
    private Star[] stars;
    public Main main;

    public StarMapRenderer(Camera camera, Star[] stars, Main main) {
        this.starRenderer = new StarRenderer(camera);
        this.stars = stars;
        this.main = main;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Star focus = stars[0];
        int xFocus = 0;
        int yFocus = 0;
        int mouseX = main.lastX;
        int mouseY = main.lastY;
        double dist = Math.hypot(getWidth(), getHeight());
        double screenDim = Math.max(getHeight(), getWidth());
        double radius = 2.0 * screenDim / 800.0 / main.camera.getFieldOfView() * 90.0;
        int r = (int)Math.round(radius);
        for (Star star : stars) {
            if (starRenderer.shouldItEvenBeOnScreen(star)) {
                Point screenPosition = starRenderer.calculateScreenPosition(star, getWidth(), getHeight());
                int x = screenPosition.x;
                int y = screenPosition.y;
                double dst = Math.hypot(x-mouseX, y-mouseY);
                if (dst < dist) {
                    focus = star;
                    xFocus = x;
                    yFocus = y;
                    dist = dst;
                }
                if (star.getName().equals("МКС")) {
                    Color color = Color.GRAY;
                    g.setColor(color);
                    g.fillRect(x - r, y - r, 2 * r + 1, 2 * r + 1);
                    g.drawString("МКС", x + 2*r + 1, y - 2*r - 1);
                }
                else {
                    Color color = star.getColor();

                    g.setColor(color);

                    g.fillOval(x - r, y - r, 2 * r + 1, 2 * r + 1);

                }
            }
        }

        if (!main.reading) {
            main.focus = focus;
            main.focused = dist < 35;
        }
        g.setColor(Color.WHITE);
        if (main.reading) {
            Point coords = starRenderer.calculateScreenPosition(main.focus, getWidth(), getHeight());
            g.drawOval(coords.x-r-3,coords.y-r-3,2*r+7,2*r+7);
            g.drawString(main.focus.getName(),coords.x+2*r+1, coords.y-2*r-1);
        }
        else if (main.focused) {
            g.drawOval(xFocus-r-3,yFocus-r-3,2*r+7,2*r+7);
            g.drawString(focus.getName(),xFocus+2*r+1, yFocus-2*r-1);
        }
    }
}
