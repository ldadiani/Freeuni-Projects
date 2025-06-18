
import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris{
    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    private Brain myBrain;
    private JCheckBox brainMode;
    private  boolean brainYes;
    private JSlider mySlider;
    private JPanel little;
    private JLabel status;
    private static  final int RANGE_MAX=98;

    JBrainTetris(int pixels) {
        super(pixels);
        myBrain=new DefaultBrain();
        brainYes=false;
    }

    @Override
    public JComponent   createControlPanel(){
        JComponent newPanel=super.createControlPanel();
        newPanel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        newPanel.add(brainMode);
        little = new JPanel();
        little.add(new JLabel("Adversary:"));
        mySlider = new JSlider(0, 100, 0);
        mySlider.setPreferredSize(new Dimension(100,15));
        little.add(mySlider);
        newPanel.add(little);
        status=new JLabel("ok");
        newPanel.add(status);
        return newPanel;
    }

    @Override
    public Piece pickNextPiece() {

        int sliderInt=mySlider.getValue();
        int rand=(int) (RANGE_MAX* random.nextDouble()) +1;

        boolean isMore=false;
        if(rand >=sliderInt ) isMore=true;
        if(sliderInt==0 || isMore){
            status.setText("ok");
            return super.pickNextPiece();
        }
        status.setText("*ok*");
        double currScore=0;
        Piece currentOne=pieces[0];
        for(int i=0; i< pieces.length; i++){
            Brain.Move currBest= new Brain.Move();
            currBest  = myBrain.bestMove(board,pieces[i],HEIGHT,currBest);
            if(currBest != null && currBest.score > currScore) {
                currScore=currBest.score;
                currentOne=currBest.piece;
            }

        }
        return  currentOne;
    }

    @Override
    public void tick(int verb) {
        brainYes=brainMode.isSelected();
        if(brainYes && verb==DOWN){
            super.board.undo();
            Brain.Move bestOne=new Brain.Move();
            bestOne= myBrain.bestMove(board,currentPiece, HEIGHT,bestOne);
            if(bestOne==null){
                super.tick(verb);
                return;
            }
            if(!bestOne.piece.equals(currentPiece)){
                super.tick(ROTATE);
            }
            if (bestOne.x > currentX){
                super.tick(RIGHT);
            } else if(bestOne.x < currentX) super.tick(LEFT);

            super.tick(DOWN);
        } else{
            super.tick(verb);
        }

    }



    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JTetris tetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }

}
