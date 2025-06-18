import junit.framework.TestCase;
import  org.junit.jupiter.api.Test;


import static org.junit.Assert.assertEquals;

public class SudokuTest extends TestCase {


    @Test
    public void testToCallMain(){
        String[] args = new String[0];
        Sudoku.main(args);
    }
    @Test
    public void testSudoku(){
        Sudoku mySudoku=new Sudoku(Sudoku.easyGrid);
        assertEquals(1,mySudoku.solve());

        mySudoku=new Sudoku(Sudoku.mediumGrid);
        assertEquals(1,mySudoku.solve());

        mySudoku=new Sudoku(Sudoku.hardGrid);
        assertEquals(1,mySudoku.solve());

        assertEquals(false,mySudoku.getSolutionText().isEmpty());
        assertEquals(true, mySudoku.getElapsed()< 100);
        assertEquals(true, mySudoku.getElapsed() >= 0);

    }

    @Test
    public  void  testSudoku2(){
        String toSolve="123";
        for(int i=0; i< 78; i++){
            toSolve+="0";
        }
        Sudoku sudoku=new Sudoku(toSolve);
        assertEquals(true,sudoku.solve() >= 10);

    }


    @Test
    public  void testSudoku3(){
        try{
            String exception="hehe";
            Sudoku sudoku=new Sudoku(exception);

        } catch (Exception e){};
    }





}
