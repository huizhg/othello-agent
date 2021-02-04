import java.util.concurrent.TimeoutException;
import java.util.LinkedList;

public class AlphaBetaPruning implements OthelloAlgorithm {
    public static final int DEFAULT_DEPTH = 7;
    public static final long DEFAULT_TIME_LIMIT = Long.MAX_VALUE;
    private OthelloEvaluator evaluator;
    private int depth;
    private long timeLimit;

    public AlphaBetaPruning() {
        this(DEFAULT_DEPTH, DEFAULT_TIME_LIMIT);
    }

    public AlphaBetaPruning(int depth, long timeLimit) {
        this(depth, timeLimit, new StaticHeuristic());
    }

    public AlphaBetaPruning(int depth, long timeLimit, OthelloEvaluator evaluator) {
        this.depth = depth;
        this.timeLimit = timeLimit;
        this.evaluator = evaluator;
    }

    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Override
    public void setEvaluator(OthelloEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public void setSearchDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth(){
        return this.depth;
    }

    @Override
    public OthelloAction evaluate(OthelloPosition position) throws IllegalMoveException, TimeoutException {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        if(position.maxPlayer){
            return this.maxValue(position, alpha, beta, this.getDepth());
        }else{
            return this.minValue(position, alpha, beta, this.getDepth());
        }
    }

    /**
     * Search the best move for min node (Black player).
     * @param position OthelloPosition
     * @param alpha The lower bound value for a node
     * @param beta The upper bound value for a node
     * @param depth Search depth
     * @return Return the best move for a min node
     * @throws TimeoutException
     * @throws IllegalMoveException
     */
    private OthelloAction minValue(OthelloPosition position, int alpha, int beta, int depth) throws TimeoutException, IllegalMoveException {

        this.checkTime();
        LinkedList<OthelloAction> moves = position.getMoves();


        if (depth == 0) {
            OthelloAction leafMove = new OthelloAction(0, 0);
            leafMove.setValue(this.evaluator.evaluate(position));
            if(moves.size() == 0){
                leafMove.setPassMove(true);
            }
            return leafMove;
        }
        if (moves.size() == 0) {
            OthelloAction passMove = new OthelloAction(0, 0, true);
            passMove.setValue(this.evaluator.evaluate(position));
            return passMove;
        }

        OthelloAction bestMove = new OthelloAction(0, 0);
        bestMove.setValue(Integer.MAX_VALUE);
        for (OthelloAction move : moves) {
            OthelloPosition newPosition = position.makeMove(move);
            OthelloAction childMove = this.maxValue(newPosition, alpha, beta, depth-1);

            if(bestMove.getValue() > childMove.getValue()){
                bestMove = move;
                bestMove.setValue(childMove.getValue() );

            }
            int value = Integer.min(bestMove.getValue(), childMove.getValue());
            beta = Integer.min(beta, value);
            if(alpha > beta){
                break;
            }

        }
        return bestMove;


    }

    /**
     * Search the best move for max node (White player)
     * @param position OthelloPosition
     * @param alpha The lower bound value for a node
     * @param beta The upper bound value for a node
     * @param depth Search depth
     * @return Return the best move for a max node
     * @throws TimeoutException
     * @throws IllegalMoveException
     */
    private OthelloAction maxValue(OthelloPosition position, int alpha, int beta, int depth) throws TimeoutException, IllegalMoveException {
        this.checkTime();
        LinkedList<OthelloAction> moves = position.getMoves();


        if (depth == 0) {
            OthelloAction leafMove = new OthelloAction(0, 0);
            leafMove.setValue(this.evaluator.evaluate(position));
            return leafMove;
        }
        if (moves.size() == 0) {
            OthelloAction passMove = new OthelloAction(0, 0, true);
            passMove.setValue(this.evaluator.evaluate(position));
            return passMove;
        }

        OthelloAction bestMove = new OthelloAction(0, 0);
        bestMove.setValue(Integer.MIN_VALUE);
        for (OthelloAction move : moves) {
            OthelloPosition newPosition = position.makeMove(move);
            OthelloAction childMove = this.minValue(newPosition, alpha, beta, depth-1);

            if(bestMove.getValue() < childMove.getValue()){
                bestMove = move;
                bestMove.setValue(childMove.getValue() );

            }
            int value = Integer.max(bestMove.getValue(), childMove.getValue());
            alpha = Integer.max(alpha, value);
            if(alpha > beta){
                break;
            }

        }
        return bestMove;

    }


    public void checkTime() throws TimeoutException {
        if (System.currentTimeMillis() > this.timeLimit) {
            throw new TimeoutException();
        }
    }
}