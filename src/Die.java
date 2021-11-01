//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.lang.Math;
//import javax.swing.*;
//
//public class Die extends JPanel {
//    JButton dieButton = new JButton("Roll Die");
//    private int currentRollValue = 0;   //Die current roll value set by roll die method, add getter??
//
//    public int getCurrentRollValue() {
//        return currentRollValue;
//    }
//
//    public char getPlayerToRoll() {
//        return playerToRoll;
//    }
//
//    private char playerToRoll = 0;  //Player eligible to roll
//
//
//    public Die() {
//        setBounds(600, 550, 100, 50);
//        add(this.dieButton);
//        theGambler gambit = new theGambler();
//        dieButton.addActionListener(gambit);
//    }
//
//    //random number generator for roll value
//    public int rollDie() {
//        int rollValue = (int) ((Math.random() * 6) + 1);
//        currentRollValue = rollValue;
//        return rollValue;
//    } //Allows Player to Roll if eligible, conditions?? also return roll value
//
//    public boolean isSix() {
//        return currentRollValue == 6;
//    }
//
//    ;
//    //A die roll of 6 is affects the game, this could simplify code
//
//
//    private class theGambler implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            System.out.println(rollDie());
//
//        }
//    }
////    public abstract void setPlayerToRoll(); //Based on the game's logic the player who can roll the die should be set
////    public abstract void getPlayerToRoll(); //Provide access to the players turn
//}
