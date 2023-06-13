package com.seadox.aquamanpro.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.util.ArrayList;

public class DrillList implements Parcelable {
    private final double CALORIES = 0.25;
    private ArrayList<Drill> warmup = new ArrayList<>();
    private ArrayList<Drill> main = new ArrayList<>();
    private ArrayList<Drill> warmdown = new ArrayList<>();
    private int distance;
    private int calories;
    private String time;
    private String UID;

    public DrillList() {
    }

    protected DrillList(Parcel in) {
        distance = in.readInt();
        calories = in.readInt();
        time = in.readString();
        UID = in.readString();
    }

    public static final Creator<DrillList> CREATOR = new Creator<DrillList>() {
        @Override
        public DrillList createFromParcel(Parcel in) {
            return new DrillList(in);
        }

        @Override
        public DrillList[] newArray(int size) {
            return new DrillList[size];
        }
    };

    public ArrayList<Drill> getWarmup() {
        return warmup;
    }

    public DrillList setWarmup(ArrayList<Drill> warmup) {
        this.warmup = warmup;
        return this;
    }

    public ArrayList<Drill> getMain() {
        return main;
    }

    public DrillList setMain(ArrayList<Drill> main) {
        this.main = main;
        return this;
    }

    public ArrayList<Drill> getWarmdown() {
        return warmdown;
    }

    public DrillList setWarmdown(ArrayList<Drill> warmdown) {
        this.warmdown = warmdown;
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public DrillList setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public int getCalories() {
        return calories;
    }

    public DrillList setCalories(int calories) {
        this.calories = calories;
        return this;
    }

    public String getTime() {
        return time;
    }

    public DrillList setTime(String time) {
        this.time = time;
        return this;
    }

    public String getUID() {
        return UID;
    }

    public DrillList setUID(String UID) {
        this.UID = UID;
        return this;
    }

    public void calcData() {
        int distance = 0, laps = 0;
        this.time = "00:00:00";

        for (Drill drill : getWarmup()) {
            distance = Integer.parseInt(drill.getDistance());
            laps = Integer.parseInt(drill.getRounds());

            this.time = calcTime(drill);
            this.distance += laps * distance;
        }

        for (Drill drill : getMain()) {
            distance = Integer.parseInt(drill.getDistance());
            laps = Integer.parseInt(drill.getRounds());

            this.time = calcTime(drill);
            this.distance += laps * distance;
        }

        for (Drill drill : getWarmdown()) {
            distance = Integer.parseInt(drill.getDistance());
            laps = Integer.parseInt(drill.getRounds());

            this.time = calcTime(drill);
            this.distance += laps * distance;
        }
        this.calories = (int) (CALORIES * this.distance);
    }

    private String calcTime(Drill drill) {
        int hour = 0, min = 0, sec = 0, prevHour = 0, prevMin = 0, prevSec = 0, laps = 0;
        min = Integer.parseInt(drill.getTime().split(":")[0]);
        sec = Integer.parseInt(drill.getTime().split(":")[1]);

        prevHour = Integer.parseInt(time.split(":")[0]);
        prevMin = Integer.parseInt(time.split(":")[1]);
        prevSec = Integer.parseInt(time.split(":")[2]);

        LocalTime prevTime = LocalTime.of(prevHour, prevMin, prevSec);

        if (sec / 60 > 0) {
            int additionalMinutes = sec / 60;
            min += additionalMinutes;
            sec %= 60;
        }

        if (min / 60 > 0) {
            int additionalHours = min / 60;
            hour += additionalHours;
            min %= 60;
        }

        laps = Integer.parseInt(drill.getRounds());

        LocalTime time = LocalTime.of(hour, min, sec);
        LocalTime sumTime = LocalTime.ofSecondOfDay(((long) laps * time.toSecondOfDay()) + prevTime.toSecondOfDay());

        return (sumTime.getHour() == 0 ? "00" : sumTime.getHour()) + ":" + (sumTime.getMinute() < 10 ? "0" : "") + sumTime.getMinute() + ":" + (sumTime.getSecond() < 10 ? "0" : "") + sumTime.getSecond();
    }

    @Override
    public String toString() {
        return "DrillList{" +
                "warmup=" + warmup +
                ", main=" + main +
                ", warmdown=" + warmdown +
                ", distance=" + distance +
                ", calories=" + calories +
                ", time='" + time + '\'' +
                ", UID='" + UID + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(distance);
        dest.writeInt(calories);
        dest.writeString(time);
        dest.writeString(UID);
    }
}
