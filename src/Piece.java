import javax.swing.*;
import java.awt.*;

public class Piece extends JPanel {
    private char colourID;
    private int pieceIndex;

    private int piecePosition;
    private String colour;
    private boolean isAtBase = true; //initially true
    private boolean isHome; //initially false
    private int positionX;
    private int positionY;

    Piece() {
    }

    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;

        switch (colourID) {
            case 'R' -> g2D.setPaint(Color.RED);
            case 'B' -> g2D.setPaint(Color.BLUE);
            case 'Y' -> g2D.setPaint(Color.YELLOW);
            case 'G' -> g2D.setPaint(Color.GREEN);
        }
        g2D.fillOval(5, 5, 20, 20);

        g2D.setPaint(Color.black);
        g2D.setStroke(new BasicStroke(3.0f));
        g2D.drawOval(5, 5, 20, 20);

    }

    Piece(char ID, int pieceNumber) {
        super(new GridLayout(1, 0));
        colourID = ID;
        isAtBase = true;
        isHome = false;
        pieceIndex = pieceNumber;
        switch (ID) {
            case 'R' -> colour = "Red";
            case 'B' -> colour = "Blue";
            case 'Y' -> colour = "Yellow";
            case 'G' -> colour = "Green";
        }
        setPosition(ID, pieceNumber);
        setBounds(positionX, positionY, 30, 30);
    }


    //setting piece positions
    private void setPosition(char x, int num) {
        positionX = BoardPositions.getPositionX(x, num);
        positionY = BoardPositions.getPositionY(x, num);

    }


    //moving a piece
    public void move() {
        if (piecePosition == 56) {
            positionX = BoardPositions.moveToFinalX(colourID, pieceIndex);
            positionY = BoardPositions.moveToFinalY(colourID, pieceIndex);
            isHome = true;
        } else{
            positionX = BoardPositions.moveToPositionX(colourID, piecePosition);
            positionY = BoardPositions.moveToPositionY(colourID, piecePosition);
        }

        setBounds(positionX + 5, positionY + 5, 30, 30);
        piecePosition++;
    }
    public boolean PieceIsAtBase() {
        return isAtBase;
    }

    public void setAtBase(boolean atBase) {
        isAtBase = atBase;
    }

    public int getPiecePosition() {
        return piecePosition;
    }
}
