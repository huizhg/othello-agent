public class StaticHeuristic implements OthelloEvaluator{
    public static final int[][] BOARD_WEIGHT = {
            {20, -3, 11,  8,  8, 11, -3, 20},
            {-3, -7, -4,  1,  1, -4, -7, -3},
            {11, -4,  2,  2,  2,  2, -4, 11},
            { 8,  1,  2,  1,  1,  2,  1,  8},
            { 8,  1,  2,  1,  1,  2,  1,  8},
            {11, -4,  2,  2,  2,  2, -4, 11},
            {-3, -7, -4,  1,  1, -4, -7, -3},
            {20, -3, 11,  8,  8, 11, -3, 20}};

    @Override
    /**
     * Returns an integer, representing a heuristic evaluation of the position.
     * @param position
     * @return
     */
    public int evaluate(OthelloPosition position){
        return evaluateBoard(position) + evaluateNumOfMoves(position);
    }

    /**
     * Return the board score of the position.
     * @param position
     * @return
     */
    private int evaluateBoard(OthelloPosition position) {
        int value = 0;
        for(int i = 1; i <= OthelloPosition.BOARD_SIZE; i++){
            for(int j = 1; j <= OthelloPosition.BOARD_SIZE; j++){
                if(position.board[i][j] == 'W'){
                    value += StaticHeuristic.BOARD_WEIGHT[i - 1][j - 1];
                }else if(position.board[i][j] == 'B'){
                    value += StaticHeuristic.BOARD_WEIGHT[i - 1][j - 1]*(-1);
                }
            }
        }
        return value;
    }

    /**
     * This method is used to calculate how many moves does the current player
     * and it's opponent player have. Use the number of moves to evaluate
     * the position. The more moves the max player has, the higher the score.
     * @param position
     * @return
     */
    private int evaluateNumOfMoves(OthelloPosition position){
        int myPlayerScore = position.getMoves().size();

        OthelloPosition opponentPlayer = position.clone();
        opponentPlayer.maxPlayer = !opponentPlayer.maxPlayer;
        int opponentScore = opponentPlayer.getMoves().size();

        if(position.maxPlayer){
            return myPlayerScore - opponentScore;
        }else{
            return opponentScore - myPlayerScore;
        }

    }
}
