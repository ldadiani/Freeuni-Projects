
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.table.JTableHeader;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;


public class SudokuFrame extends JFrame implements ActionListener, DocumentListener  {

	private JButton button;
	private JCheckBox checkBox;
	private JTextArea first;
	private JTextArea second;

	public SudokuFrame() {
		super("Sudoku Solver");

		// YOUR CODE HERE

		// Could do this:
		// setLocationByPlatform(true);


		JPanel panela=new JPanel( new BorderLayout(4,4));

		//these are two textBoxes
		first= new JTextArea(15,20);
		first.setBorder(new TitledBorder("puzzle"));
		panela.add(first,BorderLayout.CENTER);
		Document doc=first.getDocument();
		doc.addDocumentListener((DocumentListener) this);

		second=new JTextArea(15,20);
		second.setBorder(new TitledBorder("Solution"));
		panela.add(second,BorderLayout.EAST);

		JPanel panel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		button= new JButton();
		button.setText("check");
		button.addActionListener((ActionListener) this);
		panel.add(button);

		checkBox=new JCheckBox();
		checkBox.setText("Auto check");
		checkBox.setSelected(false);
		panel.add(checkBox);
		panela.add(panel,BorderLayout.SOUTH);
		this.add(panela);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);

	}


	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		SudokuFrame frame = new SudokuFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==button){
			check();
		}

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		methodForText();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		methodForText();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		methodForText();
	}

	private void methodForText() {
		if(checkBox.isSelected()){
			check();
		}
	}

	private void check(){
		try {
			Sudoku sudoku= new Sudoku(first.getText());
			int solved=sudoku.solve();
			String answer=sudoku.getSolutionText();
			if(solved!=0) {
				second.setText(answer);
				second.append("\n");
				second.append("solutions: " + solved + "\n");
				second.append("elapsed: " + sudoku.getElapsed() + "\n");
			} else {
				second.setText("This sudoku doesn't have a solution");
			}
		} catch (Exception error) {
			second.setText("Parsing problem");
		}
	}
}
