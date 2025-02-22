package com.company.model;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Dungeon {
    public int numberOfRooms;
    public boolean isGameRunning;
    public String name;
    public Room currentRoom;
    public PlayerCharacter activeCharacter;
    private final Scanner scanner = new Scanner(System.in);
    public ArrayList<Room> rooms = new ArrayList<>();

    public Dungeon(String name, int numberOfRooms) {
        this.name = name;
        this.numberOfRooms = numberOfRooms;
        isGameRunning = false;
    }

    public void addRoom(Room room) {
        if (rooms.size() < numberOfRooms) {
            room.dungeon = this;
            rooms.add(room);
            System.out.println(room.name + " has been added.");
        }
        else {
            System.out.println(name + " already has " + numberOfRooms + " rooms");
        }
    }

    public void addCharacter(PlayerCharacter character) {
        this.activeCharacter = character;
        System.out.println(character.name + " has been placed at the start of the Dungeon.");
    }

    //TODO get this working with moving rooms
    public void startDungeon() {
        isGameRunning = true;
        currentRoom = rooms.get(0);
        activeCharacter.currentRoom = currentRoom;
        System.out.println("Opening Crawl: To be added into EntryWay type room.");
        roomLogic();
    }

    private String getRoomChoice() {
        String choice = "";

        System.out.println("What will " + activeCharacter.name + " do?\n(c)heck menu\n(l)ook\n(s)earch\n(o)pen door");
        if (currentRoom.roomMonsters.size() > 0) {
            System.out.println("(f)ight monster");
        }
        choice = scanner.nextLine();
        return choice;
    }

    private int getCombatantChoice() {
        int monsterSelection;

        do {
            System.out.println("There are monsters in this room! Which will you fight?");
            for (int i = 0; i < currentRoom.roomMonsters.size(); i++) {
                System.out.println((i + 1) + ") " + currentRoom.roomMonsters.get(i).name);
            }
            System.out.println("Enter the number of the Monster to fight:");

            monsterSelection = scanner.nextInt();

            if (monsterSelection < (currentRoom.roomMonsters.size()) -1) {
                System.out.println("Invalid entry, please try again");
            }

        } while (monsterSelection < (currentRoom.roomMonsters.size()) -1);

        return monsterSelection;
    }

    private void roomLogic() {

        while (isGameRunning) {

            String choice = "";

            choice = getRoomChoice();

                switch (choice.toLowerCase(Locale.ROOT)) {
                    case "c" :
                        activeCharacter.characterMenu();
                        break;

                    case "l":
                        System.out.println(currentRoom.description);
                        break;

                    case "s":
                        System.out.println(activeCharacter.name + " is searching...");
                        if (currentRoom.activeItem && !currentRoom.hasBeenSearched) {

                            int perceptionCheck = activeCharacter.abilityCheck("wis");

                            if (perceptionCheck >= currentRoom.searchTargetNumber) {

                                searchLogic();

                                break;

                            } else {
                                System.out.println(activeCharacter.name + " finds nothing.");
                                break;
                            }


                        } else if (currentRoom.activeItem) {
                            System.out.println(activeCharacter.name + " has searched this room before.");

                            searchLogic();

                        } else {
                            System.out.println(activeCharacter.name + " finds nothing.");
                            break;
                        }

                        break;

                    case "o":
                        System.out.println("Which door will " + activeCharacter.name + " attempt to open?");
                        int doorCount = 1;
                        for (Door door : currentRoom.doors) {
                            System.out.println(doorCount + ") " + door.type + " door, " + door.locationInRoom);
                            doorCount++;
                        }

                        int doorSelection = scanner.nextInt() - 1;

                        if (currentRoom.doors.get(doorSelection).isEntrance) {
                            currentRoom.doors.get(doorSelection).exitDungeon();
                        } else {
                            currentRoom.doors.get(doorSelection).open();
                        }

                        break;

                    case "f" :
                        if (currentRoom.roomMonsters.size() > 0) {

                            int monsterSelection = getCombatantChoice();
                            while (!currentRoom.roomMonsters.get(monsterSelection-1).isDead) {
                                Combat.round(activeCharacter, currentRoom.roomMonsters.get(monsterSelection - 1));

                                if (Combat.isFled) {
                                    break;
                                }
                            }

                            if (currentRoom.roomMonsters.get(monsterSelection-1).isDead) {
                                currentRoom.roomMonsters.remove(monsterSelection-1);
                            }

                        }
                        break;

                    default :
                        System.out.println("Invalid choice, try again.");
                        roomLogic();
                }


        }
    }

    public void searchLogic() {
        currentRoom.hasBeenSearched = true;
        System.out.println(activeCharacter.name + " succeeds in finding items!");
        System.out.println("Enter the number next to the item to pick it up");

        for (int i = 0; i < currentRoom.roomItems.size(); i++) {
            System.out.println((i + 1) + ") " + currentRoom.roomItems.get(i));
        }
        System.out.println("0) Take All");
        int selectedItem = scanner.nextInt() - 1;

        if (selectedItem != -1) {
            if (currentRoom.roomItems.get(selectedItem) instanceof Chest) {
                ((Chest) currentRoom.roomItems.get(selectedItem)).openChest();

                if (currentRoom.roomItems.size() <= 0) {
                    currentRoom.activeItem = false;
                }
            } else {
                System.out.println(activeCharacter.name + " takes " + currentRoom.roomItems.get(selectedItem));
                activeCharacter.addEquipment(currentRoom.roomItems.get(selectedItem));
                currentRoom.roomItems.remove(selectedItem);
            }

        } else {
            for (Item item : currentRoom.roomItems) {

                if (item instanceof Chest) {
                    System.out.println("Chest cannot be taken");
                } else {
                    activeCharacter.addEquipment(item);
                    System.out.println(activeCharacter.name + " takes " + currentRoom.roomItems.get(currentRoom.roomItems.indexOf(item)));

                }
            }
            for (int i = 0; i < currentRoom.roomItems.size(); i++) {
                if (currentRoom.roomItems.get(i) instanceof Chest) {
                    i++;
                } else {
                    currentRoom.roomItems.remove(i);
                    i--;
                }
            }


        }



}

    public void changeCurrentRoom(Room room) {
        currentRoom = room;
        activeCharacter.currentRoom = room;
    }

    public void endGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(activeCharacter.name + ", Score: " + activeCharacter.experiencePoints);
        System.out.println("Do you want to play again?\n(y)es or (n)o");
        String choice = scanner.nextLine();
        switch (choice.toLowerCase(Locale.ROOT)) {
            case "y" :
                startDungeon();
                break;
            case "n" :
                System.out.println("Thank you for playing!");
                isGameRunning = false;
                System.exit(0);
                break;
            default:
                System.out.println("Invalid selection");
                endGame();
        }
    }

}
