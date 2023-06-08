package com.seadox.aquamanpro.Models;

public class Drill {
    private String time;
    private String stroke;
    private String distance;
    private String rounds;
    private int color;
    private boolean fins;
    private boolean pullBuoy;
    private boolean paddles;
    private boolean kikboard;

    public Drill() {
    }

    public String getTime() {
        return time;
    }

    public Drill setTime(String time) {
        this.time = time;
        return this;
    }

    public String getStroke() {
        return stroke;
    }

    public Drill setStroke(String stroke) {
        this.stroke = stroke;
        return this;
    }

    public String getDistance() {
        return distance;
    }

    public Drill setDistance(String distance) {
        this.distance = distance;
        return this;
    }

    public String getRounds() {
        return rounds;
    }

    public Drill setRounds(String rounds) {
        this.rounds = rounds;
        return this;
    }

    public int getColor() {
        return color;
    }

    public Drill setColor(int color) {
        this.color = color;
        return this;
    }

    public boolean isFins() {
        return fins;
    }

    public Drill setFins(boolean fins) {
        this.fins = fins;
        return this;
    }

    public boolean isPullBuoy() {
        return pullBuoy;
    }

    public Drill setPullBuoy(boolean pullBuoy) {
        this.pullBuoy = pullBuoy;
        return this;
    }

    public boolean isPaddles() {
        return paddles;
    }

    public Drill setPaddles(boolean paddles) {
        this.paddles = paddles;
        return this;
    }

    public boolean isKikboard() {
        return kikboard;
    }

    public Drill setKikboard(boolean kikboard) {
        this.kikboard = kikboard;
        return this;
    }

    @Override
    public String toString() {
        return "Drill{" +
                "time='" + time + '\'' +
                ", stroke='" + stroke + '\'' +
                ", distance='" + distance + '\'' +
                ", rounds='" + rounds + '\'' +
                ", color=" + color +
                ", fins=" + fins +
                ", pullBuoy=" + pullBuoy +
                ", paddles=" + paddles +
                ", kikboard=" + kikboard +
                '}';
    }
}
