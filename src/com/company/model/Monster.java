package com.company.model;

import com.company.services.DiceService;

import java.util.ArrayList;

public class Monster {
    public int hitPoints;
    public int attackBonus;
    public int armorClass;
    public int experiencePoints;
    public boolean isDead;
    public String name;
    String description;
    public Weapon weapon;
    public ArrayList<Item> treasure = new ArrayList<>();

    public Monster(String name, String description, int hitPoints, int armorClass, int attackBonus, Weapon attack,
                   int experiencePoints) {
        this.name = name;
        this.description = description;
        this.hitPoints = hitPoints;
        this.attackBonus = attackBonus;
        this.armorClass = armorClass;
        this.weapon = attack;
        this.experiencePoints = experiencePoints;
    }

    public void addTreasure(Item treasure) {
        this.treasure.add(treasure);
        System.out.println(treasure + " has been added.");
    }


    public void checkStatus() {
        isDead = hitPoints <= 0;
        if (isDead) {
            System.out.println(name + " has been defeated!");
        }
    }


    //Some minor tweaks to reduce overhead
    public void attack(Weapon attack, PlayerCharacter target) {
        int flatRoll = DiceService.roll_20(); //We get the value of the dice rolled first.
        int attackRoll = flatRoll + attackBonus; //We modify the output of the first roll and store result here
        if (attackRoll >= target.armorClass && flatRoll != 1 || flatRoll == 20) {
            System.out.println(name + " hits " + target.name + " with " + attack.name + "!");
            int damageRoll = weapon.getDamage(); //Moved this logic into the WeaponClass
            target.hitPoints -= damageRoll;
            System.out.println(target.name + " takes " + damageRoll + " points of damage!");
            target.checkStatus();
        } else if (flatRoll == 1) {
            System.out.println(name + " missed! Critical Fail!");
        } else {
            System.out.println(attack.name + " missed!");
        }
    }

}
