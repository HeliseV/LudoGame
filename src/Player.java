public class Player {
    private final char playerColourID; //colour should be represented by char for simplicity
    public String colour;//optional string for colour name with appropriate getter
    public Piece[] pieces = new Piece[4];//array of player pieces
    private int activePawn = 0; //which of the players piece is moving


    Player(char ID) //create a player
    {
        playerColourID = ID;
        setPlayerPieces();
        switch (ID) {
            case 'R' -> colour = "Red";
            case 'B' -> colour = "Blue";
            case 'Y' -> colour = "Yellow";
            case 'G' -> colour = "Green";
        }
    }

    // setter for pawns
    private void setPlayerPieces() {
        for (int i = 0; i < 4; i++) {
            pieces[i] = new Piece(playerColourID, i);
        }

    }

    //movement for active pawn
    public void movePiece(int moveNumber) {
        if (!pieces[activePawn].PieceIsAtBase())
        {
            if (moveNumber + pieces[activePawn].getPiecePosition() < 57) {
                for (int x = 0; x < moveNumber; x++) {
                    pieces[activePawn].move();
                }
            } else if (moveNumber + pieces[activePawn].getPiecePosition() == 57) {
                System.out.println(moveNumber + pieces[activePawn].getPiecePosition());
                for (int x = 0; x < moveNumber; x++) {
                    pieces[activePawn].move();

                }
                if (activePawn < 3) {
                    activePawn++;
                } else {
                    System.out.println("winner");
                }
            }
        } else if (moveNumber == 6) {
            pieces[activePawn].move();
            pieces[activePawn].setAtBase(false);
            System.out.println(moveNumber);
        }
    }
//    public abstract void hasWon();
}
