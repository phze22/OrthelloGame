import java.util.ArrayList;
import java.util.Random;

/**
 * A simple OthelloAI-implementation. The method to decide the next move just
 * returns a random legal move.
 * @author Mai Ajspur
 * @version 9.2.2018
 */
public class RandomAI implements IOthelloAI {

    /**
     * Returns random legal move or an illegal move, if no legal moves are available.
     */
    public Position decideMove(GameState s) {
        Random random = new Random();
        ArrayList<Position> moves = s.legalMoves();
        if (!moves.isEmpty())
            return moves.get(random.nextInt(moves.size()));
        else
            return new Position(-1, -1);
    }
}
