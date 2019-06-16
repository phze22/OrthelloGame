import java.util.*;

/**
 * A more advanced OthelloAI-implementation. It implements minimax-algorithm with a pruning operation.
 * The method to decide the next move takes favourable positions into account and responds in reasonable
 * time when choosing out of all legal positions in a current GameState.
 * @author  Philine Zeinert, Jowita Julia Podolak, Kim Ida Schild
 * @version 14.03.2019
 */
public class OthelloAI30 implements IOthelloAI {

    //maxDepth sets the maximal depth until which states in the game tree will be expanded
    private static final int maxDepth = 4;


    /**
     * Evaluates all legal positions at the current GameState and chooses the next possible
     * position with the highest chance of winning for the AI according to the called helper methods.
     * @param s The current GameState in which it is the AI's turn.
     * @return An action (position), which serves as the AI's next move in the game.
     */
    public Position decideMove(GameState s) {


        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        ArrayList<Position> legalPositions = s.legalMoves();
        if (legalPositions.isEmpty()) return new Position(-1, -1);

        int max = 0;
        int i = 0;
        Position maxPos = null;
        for (Position position : legalPositions) {

            i++;

            int depth = 0;
            // copy to maintain actual game state
            GameState tmpState = new GameState(s.getBoard(), s.getPlayerInTurn());
            tmpState.insertToken(position);//
            int minValue = MinValue(tmpState, alpha, beta, depth);
            if (minValue >= max) {
                max = minValue;
                maxPos = position;
            }
        }

        return maxPos;


    }

    /**
     * Iterates through every legal position in given GameState. Calls a MinValue for each position and
     * chooses the highest possible value along the search tree. Updates alpha with a more favourable value
     * at current GameState for Max, if there is such.
     * @param s The current GameState in which it is the AI's turn.
     * @param alpha current most favourable utility value for Max (AI)
     * @param beta current most favourable utility value for opponent
     * @param depth Depth in search tree when recursive call is supposed to stop to improve response time.
     * @return The highest value for Max out of all legal moves at the given GameState.
     */
    private int MaxValue(GameState s, int alpha, int beta, int depth) {

        if (depth >= maxDepth || s.isFinished()) return Evaluation(s);

        depth++;
        ArrayList<Position> legalPositions = s.legalMoves();

        int max = 0;

        for (Position position : legalPositions) {

            GameState tmpState = new GameState(s.getBoard(), s.getPlayerInTurn());
            tmpState.insertToken(position);
            int minValue = MinValue(tmpState, alpha, beta, depth);
            if (minValue > max) max = minValue;
            if (max >= beta) return max;
            alpha = Math.max(alpha, max);
        }

        return max;
    }

    /**
     * Iterates through every legal position in given GameState. Calls a MaxValue for each position and
     * chooses the lowest possible value. Updates beta with a more favourable value
     * for Min at current GameState, if there is such.
     * @param s The current GameState in which it is the opponent's turn.
     * @param alpha current most favourable utility value for AI
     * @param beta current most favourable utility value for opponent
     * @param depth Depth in search tree when recursive call is supposed to stop to improve response time.
     * @return The lowest value for Max (AI) out of all legal moves at the given GameState.
     */
    private int MinValue(GameState s, int alpha, int beta, int depth) {

        if (depth >= maxDepth || s.isFinished()) return Evaluation(s);
        depth++;

        ArrayList<Position> legalPositions = s.legalMoves();
        int min = Integer.MAX_VALUE;
        for (Position position : legalPositions) {
            GameState tmpState = new GameState(s.getBoard(), s.getPlayerInTurn());
            tmpState.insertToken(position);
            int maxValue = MaxValue(tmpState, alpha, beta, depth);
            if (maxValue < min) min = maxValue;
            if (min <= alpha) return min;
            beta = Math.min(beta, min);
        }

        return min;
    }


    /**
     * Assigns values to each position on the board.
     * Evaluates the value of all tokens of player 2 according to an assigned value for each positions.
     * @param s The current GameState.
     * @return Utility value of all tokens for player 2 in given GameState.
     */
    private int Evaluation(GameState s) {
        int[][] currentBoard = s.getBoard();

        int finalValue = 0;

        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j]==2) {

                    //corners
                    if (i == 0 && j == 0) finalValue = finalValue + 5;
                    else if (i == 0 && j == currentBoard.length - 1) finalValue = finalValue + 5;
                    else if (i == currentBoard.length - 1 && j == 0) finalValue = finalValue + 5;
                    else if (i == currentBoard.length - 1 && j == currentBoard.length - 1) finalValue = finalValue + 5;
                    //xs
                    if (currentBoard.length != 4) {
                        //left-upper corner
                        if (i == 0 && j == 1) finalValue = finalValue + 1;
                        else if (i == 1 && j == 1) finalValue = finalValue + 1;
                        else if (i == 1 && j == 0) finalValue = finalValue + 1;
                            //left-bottom corner
                        else if (i == currentBoard.length - 2 && j == 0) finalValue = finalValue + 1;
                        else if (i == currentBoard.length - 2 && j == 1) finalValue = finalValue + 1;
                        else if (i == currentBoard.length - 1 && j == 1) finalValue = finalValue + 1;
                            //right-bottom corner
                        else if (i == currentBoard.length - 1 && j == currentBoard.length - 2)
                            finalValue = finalValue + 1;
                        else if (i == currentBoard.length - 2 && j == currentBoard.length - 2)
                            finalValue = finalValue + 1;
                        else if (i == currentBoard.length - 2 && j == currentBoard.length - 1)
                            finalValue = finalValue + 1;
                            //right-upper corner
                        else if (i == 0 && j == currentBoard.length - 2) finalValue = finalValue + 1;
                        else if (i == 1 && j == currentBoard.length - 2) finalValue = finalValue + 1;
                        else if (i == 1 && j == currentBoard.length - 1) finalValue = finalValue + 1;

                            //sides

                        else if (i > 1 && i < currentBoard.length - 2 && j == 0) finalValue = finalValue + 4;
                        else if (i > 1 && i < currentBoard.length - 2 && j == currentBoard.length - 1)
                            finalValue = finalValue + 4;
                        else if (i == 0 && j > 1 && j < currentBoard.length - 2) finalValue = finalValue + 4;
                        else if (i == currentBoard.length - 1 && j > 1 && j < currentBoard.length - 2)
                            finalValue = finalValue + 4;

                    } else finalValue = finalValue + 2;

                }

            }

        }
        return finalValue;
    }
}
