import java.util.concurrent.TimeoutException;
public class Othello {
    public static void main(String[] args){
        if(args.length!=2){
            throw new IllegalArgumentException("There should be two arguments, a position string and a time limit in second");
        }
        if(args[0].length() > 65){
            throw new IllegalArgumentException("The position string is too long, it should be 65 long.");
        }
        if(args[0].length() < 65){
            throw new IllegalArgumentException("The position string is too short, it should be 65 long.");
        }

        OthelloPosition position = new OthelloPosition(args[0]);
        Long timeLimit = System.currentTimeMillis() + (long)Integer.parseInt(args[1])*1000;  //

        AlphaBetaPruning alphaBetaPruning = new AlphaBetaPruning();
        alphaBetaPruning.setTimeLimit(timeLimit);
        OthelloAction bestMove = new OthelloAction(0,0);
        try {
            for (int i = AlphaBetaPruning.DEFAULT_DEPTH; i < 1000; i++) {
                alphaBetaPruning.setSearchDepth(i);
                try {
                     bestMove = alphaBetaPruning.evaluate(position);
                } catch (TimeoutException e) {
                    break;
                }
            }
        }catch (IllegalMoveException e){
            System.out.println(e.getMessage());
        }
        if((bestMove.getRow()==0 || bestMove.getColumn()==0)){
            bestMove.setPassMove(true);
        }
        bestMove.print();

    }
}
