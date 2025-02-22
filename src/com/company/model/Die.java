package com.company.model;

import java.util.ArrayList;
import java.util.Random;

public class Die {
    public int numberOfSides;
    public int maxValue;
    public int faceUpValue;

    public Die(int numberOfSides, int maxValue) {
        this.numberOfSides = numberOfSides;
        this.maxValue = maxValue;
    }

    public int roll() {
        Random random = new Random();

        faceUpValue = random.nextInt(maxValue) + 1;
        return showResult();
    }

    public int roll(int numberOfDice) {
        Random random = new Random();

        faceUpValue = numberOfDice * (random.nextInt(maxValue) + 1);
        return showResult();
    }

    public int roll(int numberOfDice, byte modifier) {
        Random random = new Random();

        faceUpValue = (numberOfDice * (random.nextInt(maxValue) + 1)) + modifier;
        return showResult();
    }

    public int rollPlusModifier(byte modifier) {
        Random random = new Random();

        faceUpValue = (random.nextInt(maxValue) + 1) + modifier;
        return showResult();
    }

    public int showResult() {
        return faceUpValue;
    }


    /*
    I have deprecated this method, as it doesn't make use of the existing roll logic in this class
    I am moving this logic to the PlayerCharacter.java class
     */
    @Deprecated
    public static int rollStats() {
        ArrayList<Integer> statArray = new ArrayList<>();

        while (statArray.size() <= 3) {
            Random random = new Random();

            statArray.add(random.nextInt(6) + 1);
        }


        int min = statArray.get(0);

        for (int i = 0; i < statArray.size(); i++) {
            if (statArray.get(i) <= min) {
                min = statArray.get(i);
            }

        }

        statArray.remove(statArray.indexOf(min));
        return statArray.get(0) + statArray.get(1) + statArray.get(2);
    }

    public String toString() {
        return "d" + numberOfSides;
    }


}
