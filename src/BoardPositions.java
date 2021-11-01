public class BoardPositions {
    final int refX = 0;
    final int refY = 240;
    public static Location[] mainLocation = new Location[52];
    public static Location[] fullLocationR = new Location[57];
    public static Location[] fullLocationY = new Location[57];
    public static Location[] fullLocationG = new Location[57];
    public static Location[] fullLocationB = new Location[57];

    public static Location[] greenLocation = new Location[7];
    public static Location[] blueLocation = new Location[7];
    public static Location[] redLocation = new Location[7];
    public static Location[] yellowLocation = new Location[7];

    public static Location[] greenBase = new Location[4];
    public static Location[] blueBase = new Location[4];
    public static Location[] redBase = new Location[4];
    public static Location[] yellowBase = new Location[4];

    public static Location greenStart;
    public static Location blueStart;
    public static Location redStart;
    public static Location yellowStart;

    public static Location[] greenEnd = new Location[4];
    public static Location[] blueEnd = new Location[4];
    public static Location[] redEnd = new Location[4];
    public static Location[] yellowEnd = new Location[4];

    public BoardPositions() {
        createMainPath(); //this function creates the general white path pieces can be on
        createBases();  //creates the base for pieces not yet in the game
        createFullPath(); // defines the route each coloured piece must pass
        greenStart = fullLocationG[0];
        blueStart = fullLocationB[0];
        redStart = fullLocationR[0];
        yellowStart = fullLocationY[0];
        endGame();

    }

    private void endGame() {
        for (int x = 0; x < 4; x++) {
            int move = 20 * (x - 1) - 10;
            greenEnd[x] = new Location("G", "" + x, greenLocation[4].getx() - 30, greenLocation[4].gety() + move);
            blueEnd[x] = new Location("B", "" + x, blueLocation[4].getx() + 30, blueLocation[4].gety() + move);
            yellowEnd[x] = new Location("Y", "" + x, yellowLocation[4].getx() + move, yellowLocation[4].gety() - 30);
            redEnd[x] = new Location("R", "" + x, redLocation[4].getx() + move, redLocation[4].gety() + 30);
        }
    }

    //function to create the paths for the different coloured pieces
    private void createFullPath() {
        //blue
        System.arraycopy(mainLocation, 0, fullLocationB, 0, 51);
        System.arraycopy(blueLocation, 0, fullLocationB, 51, 6);
        //red
        System.arraycopy(mainLocation, 13, fullLocationR, 0, 39);
        System.arraycopy(mainLocation, 0, fullLocationR, 39, 12);
        System.arraycopy(redLocation, 0, fullLocationR, 51, 6);
        //green
        System.arraycopy(mainLocation, 26, fullLocationG, 0, 26);
        System.arraycopy(mainLocation, 0, fullLocationG, 26, 25);
        System.arraycopy(greenLocation, 0, fullLocationG, 51, 6);
        //yellow
        System.arraycopy(mainLocation, 39, fullLocationY, 0, 13);
        System.arraycopy(mainLocation, 0, fullLocationY, 13, 38);
        System.arraycopy(yellowLocation, 0, fullLocationY, 51, 6);
    }


    //to get piece x coordinate
    public static int getPositionX(char x, int num) {
        return switch (x) {
            case 'M' -> mainLocation[num].getx();
            case 'R' -> redBase[num].getx();
            case 'B' -> blueBase[num].getx();
            case 'Y' -> yellowBase[num].getx();
            case 'G' -> greenBase[num].getx();
            default -> mainLocation[0].getx();
        };
    }

    //to get piece y coordinate
    public static int getPositionY(char x, int num) {
        return switch (x) {
            case 'M' -> mainLocation[num].gety();
            case 'R' -> redBase[num].gety();
            case 'B' -> blueBase[num].gety();
            case 'Y' -> yellowBase[num].gety();
            case 'G' -> greenBase[num].gety();
            default -> mainLocation[0].gety();
        };
    }

    //gets the x coordinate for the next position
    public static int moveToPositionX(char x, int num) {
        return switch (x) {
            case 'R' -> fullLocationR[num].getx();
            case 'B' -> fullLocationB[num].getx();
            case 'Y' -> fullLocationY[num].getx();
            case 'G' -> fullLocationG[num].getx();
            default -> 0;
        };
    }

    //gets the x coordinate for the next position
    public static int moveToPositionY(char y, int num) {
        return switch (y) {
            case 'R' -> fullLocationR[num].gety();
            case 'B' -> fullLocationB[num].gety();
            case 'Y' -> fullLocationY[num].gety();
            case 'G' -> fullLocationG[num].gety();
            default -> 0;
        };
    }

    public static int moveToFinalX(char x, int num) {
        return switch (x) {
            case 'R' -> redEnd[num].getx();
            case 'B' -> blueEnd[num].getx();
            case 'Y' -> yellowEnd[num].getx();
            case 'G' -> greenEnd[num].getx();
            default -> 0;
        };
    }

    //gets the x coordinate for the next position
    public static int moveToFinalY(char y, int num) {
        return switch (y) {
            case 'R' -> redEnd[num].gety();
            case 'B' -> blueEnd[num].gety();
            case 'Y' -> yellowEnd[num].gety();
            case 'G' -> greenEnd[num].gety();
            default -> 0;
        };
    }

    //location for where the pieces are before they are allowed to move
    private void createBases() {
        for (int x = 0; x < 4; x++) {
            greenBase[x] = new Location();
            greenBase[x].setID("G", ("" + x));
            blueBase[x] = new Location();
            blueBase[x].setID("B", ("" + x));
            redBase[x] = new Location();
            redBase[x].setID("R", ("" + x));
            yellowBase[x] = new Location();
            yellowBase[x].setID("Y", ("" + x));
        }

        blueBase[0].setPosition(50 + refX, refY - 190);
        blueBase[1].setPosition(50 + refX, refY - 80);
        blueBase[2].setPosition(160 + refX, refY - 190);
        blueBase[3].setPosition(160 + refX, refY - 80);

        for (int x = 0; x < 4; x++) {
            greenBase[x].setPosition(blueBase[x].getx() + 360, blueBase[x].gety() + 360);
            redBase[x].setPosition(blueBase[x].getx() + 360, blueBase[x].gety());
            yellowBase[x].setPosition(blueBase[x].getx(), blueBase[x].gety() + 360);
        }
    }

    //to create the main movement part that all pieces use
    private void createMainPath() {
        int[] temp = {refX, refY};
        for (int x = 0; x < 52; x++) {
            if (temp[1] == refY && temp[0] >= refX && temp[0] < refX + 200) {
                temp[0] += 40;

                blueLocation[x] = new Location("B", "" + x, temp[0], temp[1] + 40);
            } else if (temp[0] == refX + 200 && temp[1] == refY) {
                temp[0] += 40;
                temp[1] -= 40;
            } else if (temp[0] == refX + 240 && temp[1] <= refY - 40 && temp[1] >= refY - 200) {
                temp[1] -= 40;
            } else if (temp[0] >= refX + 240 && temp[0] < refX + 320 && temp[1] == refY - 240) {
                temp[0] += 40;
            } else if (temp[0] == refX + 320 && temp[1] < refY - 40 && temp[1] >= refY - 240) {
                temp[1] += 40;
                redLocation[x - 13] = new Location("R", "" + (x - 13), temp[0] - 40, temp[1]);
            } else if (temp[0] == refX + 320 && temp[1] == refY - 40) {
                temp[0] += 40;
                temp[1] += 40;
            } else if (temp[1] == refY && temp[0] >= refX + 320 && temp[0] < refX + 560) {
                temp[0] += 40;
            } else if (temp[1] >= refY && temp[1] < refY + 80 && temp[0] == refX + 560) {
                temp[1] += 40;
            } else if (temp[1] == refY + 80 && temp[0] > refX + 360 && temp[0] <= refY + 560) {
                temp[0] -= 40;
                greenLocation[x - 26] = new Location("G", "" + (x - 26), temp[0], temp[1] - 40);
            } else if (temp[0] == refX + 360 && temp[1] == refY + 80) {
                temp[0] -= 40;
                temp[1] += 40;
            } else if (temp[0] == refX + 320 && temp[1] >= refY + 120 && temp[1] < refY + 320) {
                temp[1] += 40;
            } else if (temp[0] > refX + 240 && temp[0] <= refX + 320 && temp[1] == refY + 320) {
                temp[0] -= 40;
            } else if (temp[0] == refX + 240 && temp[1] > refY + 120 && temp[1] <= refY + 320) {
                temp[1] -= 40;
                yellowLocation[x - 39] = new Location("G", "" + (x - 39), temp[0] + 40, temp[1]);
            } else if (temp[0] == refX + 240 && temp[1] == refY + 120) {
                temp[0] -= 40;
                temp[1] -= 40;
            } else if (temp[1] == refY + 80 && temp[0] > refX && temp[0] <= refX + 240) {
                temp[0] -= 40;
            } else if (temp[1] > refY && temp[1] <= refY + 80 && temp[0] == refX) {
                temp[1] -= 40;
            }
            mainLocation[x] = new Location("M", "" + x, temp[0], temp[1]);
        }
    }
}
