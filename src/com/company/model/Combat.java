package com.company.model;

import com.company.services.DiceService;

import java.util.Locale;
import java.util.Scanner;

public class Combat {
// TODO add cast spell and use item options to combat flow, possibly integrate multi-monster combat
    public static boolean isFled = false;

    public static void round(PlayerCharacter player , Monster encounter) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(encounter.name + " stands in your path! What do you want to do?\n(l)ook\n(a)ttack\n(c)" +
                "ast spell\n(r)un\n(w)eapon change");

        String choice = scanner.nextLine();

        switch (choice.toLowerCase(Locale.ROOT)) {
            case "l" :
                System.out.println(encounter.name + " stands before you.\n" + encounter.description);
                round(player,encounter);
                break;

            case "a" :
                int playerInitiative = DiceService.roll_20() + player.abilityCheck("dex");
                int encounterInitiative = DiceService.roll_20();

                if (playerInitiative >= encounterInitiative) {
                    player.attack(player.readiedWeapon, encounter);
                    if (!encounter.isDead) {
                        encounter.attack(encounter.weapon, player);
                    }
                } else {
                    encounter.attack(encounter.weapon, player);
                    if (!player.isDead) {
                        player.attack(player.readiedWeapon, encounter);
                    } else {
                        break;
                    }
                }

                if (!player.isDead && !encounter.isDead) {
                    round(player, encounter);
                }
                break;

            case "r" :
                System.out.println(player.name + " attempts to flee!");

                int attackOfOpportunity = DiceService.roll_20();

                if (attackOfOpportunity >= player.armorClass) {
                    System.out.println("The attempt failed!");

                    int damageRoll = ( DiceService.rollType(encounter.weapon.damageCode) * encounter.weapon.numberOfDice);

                    player.hitPoints -= damageRoll;

                    System.out.println(player.name + " takes " + damageRoll + " points of damage");

                    player.checkStatus();
                    if (player.isDead) {
                        break;
                    } else {
                        System.out.println(player.name + " has " + player.hitPoints + " hit points remaining.");
                    }
                    round(player,encounter);
                    break;
                } else {
                    System.out.println(player.name + " has escaped the encounter!");
                    isFled = true;
                    break;
                }

            case "c" :
                playerInitiative = DiceService.roll_20() + player.abilityCheck("dex");
                encounterInitiative = DiceService.roll_20();

                if (playerInitiative >= encounterInitiative) {

                    if (player.spells.size() > 0 && player.isArcane || player.isDivine) {
                        System.out.println("Which Spell will " + player.name + " cast?\nEnter number next to Spell name");
                        for (int i = 0; i < player.spells.size(); i++) {
                            System.out.println((i + 1) + ") " + player.spells.get(i));
                        }
                        int input = scanner.nextInt();

                        player.castSpell(player.spells.get(input - 1), encounter);

                        encounter.checkStatus();

                        if (!encounter.isDead) {
                            encounter.attack(encounter.weapon, player);
                            round(player, encounter);
                            break;
                        }

                        break;
                    } else {
                        System.out.println(player.name + " has no spells to cast!");

                        round(player, encounter);
                        break;
                    }
                } else {
                    encounter.attack(encounter.weapon, player);
                    if (!player.isDead) {
                        if (player.spells.size() > 0 && player.isArcane || player.isDivine) {
                            System.out.println("Which Spell will " + player.name + " cast?\nEnter number next to Spell name");
                            for (int i = 0; i < player.spells.size(); i++) {
                                System.out.println((i + 1) + ") " + player.spells.get(i));
                            }
                            int input = scanner.nextInt();

                            player.castSpell(player.spells.get(input - 1), encounter);

                            round(player, encounter);
                            break;
                        }
                    } else {
                        System.out.println(player.name + " has no spells to cast!");

                        round(player, encounter);
                        break;
                    }
                }

            case "w":
                playerInitiative = DiceService.roll_20() + player.abilityCheck("dex");
                encounterInitiative = DiceService.roll_20();

                if (playerInitiative >= encounterInitiative) {

                    player.changeActiveWeapon();

                    encounter.attack(encounter.weapon, player);

                    if (!player.isDead && !encounter.isDead) {
                        round(player, encounter);
                    }
                    break;

                } else {
                    encounter.attack(encounter.weapon, player);
                    if (!player.isDead) {

                        player.changeActiveWeapon();

                        if (!player.isDead && !encounter.isDead) {
                            round(player, encounter);
                        }
                        break;
                    }
                }
            default :
                System.out.println("Invalid entry");
                round(player,encounter);
        }
    }
}
