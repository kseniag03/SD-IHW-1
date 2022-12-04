/**
 * The functionality of the Reversi game
 */
public interface Reversi {
    /** Print possible moves to go */
    void printPossibleMoves();

    /** Determine what point is better to go (count max cell's value) */
    void move();

    /** Move to point */
    void move(Point point);

    /** Cancel last move */
    void cancelMove();

    /** Check if black goes */
    boolean getTurn();

    /** Check if there is no moves to go */
    boolean hasGameEnded();
}
