package com.example.staratlas.dao;

import java.util.ArrayList;

public class Space {
    private final ArrayList<Star> stars;

    public Space() {
        stars = new ArrayList<>();
    }

    public void addStar(Star star) {
        stars.add(star);
    }

    public void removeStar(Star star) {
        stars.remove(star);
    }

    public ArrayList<Star> getStars() {
        return stars;
    }
}