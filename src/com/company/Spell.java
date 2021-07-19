package com.company;

public class Spell {
    public String name;
    public String school;
    public String components;
    public int level;
    public int castingTime;
    public int range;
    public int duration;

    public Spell(String name, int level, String school, int castingTime, int range, String components, int duration) {
        this.name = name;
        this.level = level;
        this.school = school;
        this.castingTime = castingTime;
        this.range = range;
        this.components = components;
        this.duration = duration;
    }

    public String toString() {
        return name + ": level " + level + " " + school + " spell. Casting Time: " + castingTime + " minutes, range: " + range + " feet, Components: " + components
                + ", duration: " + duration + " minutes";
    }
}
