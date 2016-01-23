import java.awt.Color;
import java.awt.EventQueue;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class GUI {

	private JFrame frmTeamW;
	private Grid grid;
	private ArrayList<Square> squares = new ArrayList<Square>();
	private ArrayList<Square> blackSquares = new ArrayList<Square>();
	private ArrayList<Square> solutionSquares = new ArrayList<Square>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmTeamW.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmTeamW = new JFrame();
		frmTeamW.setTitle("Team W - COMP 354 Project Puzzle GCHQ");
		frmTeamW.setBounds(100, 100, 480, 374);
		frmTeamW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTeamW.getContentPane().setLayout(null);
		
		generateEmptyGrid();
		generateSolutionGrid();
		saveSolutionGrid();
		calculateHeaders();
		prepareQuestionGrid();
		
		JLabel lblDifficultyEasy = new JLabel("Difficulty: Easy");
		lblDifficultyEasy.setBounds(36, 52, 98, 14);
		frmTeamW.getContentPane().add(lblDifficultyEasy);
		
		JButton btnShowSolution = new JButton("Show Solution");
		btnShowSolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				for (int i = 0; i < squares.size(); i++)
				{
					squares.get(i).setFlag(solutionSquares.get(i).getFlag());
				}
			}
		});
		btnShowSolution.setBounds(301, 249, 120, 35);
		frmTeamW.getContentPane().add(btnShowSolution);
		
	}

	/**
	 * Creates a grid of Square objects in the appropriate size depending on the selected difficulty.
	 * Easy:	4x4
	 * Medium:	6x8
	 * Hard:	8x8
	 * The Square object's id is labeled from left to right, top to bottom.
	 */
	private void generateEmptyGrid()
	{
		// 4x4 example
		grid = new Grid(4, 4);
		int totalRows = grid.getRowSize();
		int totalCols = grid.getColumnSize();		
		
		// Solution Grid
		JPanel questionPanel = new JPanel();
		questionPanel.setBounds(113, 144, 140, 140);
		frmTeamW.getContentPane().add(questionPanel);
		questionPanel.setLayout(new GridLayout(totalRows, totalCols, 0, 0));
		
		// Generate square objects in the grid
		
		int id = 1;
		for (int rowCtr = 1; rowCtr <= totalRows; rowCtr++)
		{
			for (int colCtr = 1; colCtr <= totalCols; colCtr++)
			{
				
				JButton btnNewButton = new JButton();

				Square s = new Square(id, false, rowCtr, colCtr, btnNewButton);
				squares.add(s);
				
				questionPanel.add(btnNewButton);
				
				id++;
			}
		}
	}
	
	/**
	 *  Randomize the solution for the grid.
	 */
	private void generateSolutionGrid() {
				
		double fillPercentage = 0.50;		// 50%
		
		// the total number of squares to fill in
		int totalFillSquares = (int) Math.floor(squares.size() * fillPercentage);
		
		// counter to keep track of filled in squares
		int ctr = 0;
		
		while (ctr < totalFillSquares)
		{
			// Choose a random square
			int index = (int) Math.floor(Math.random() * squares.size());
		
			Square selectedSquare = squares.get(index);
		
			// Check if the selected square is white
			if (selectedSquare.getFlag() == false)
			{
				selectedSquare.setFlag(true);			// turn the square black
				ctr++;									// increase the counter
				
				blackSquares.add(selectedSquare);		// add the black square to the list of black squares	
			}
		}
	}
	
	private void saveSolutionGrid() {
		for (Square s : squares)
		{
			Square square = new Square(s.getID(), s.getFlag(), s.getRowPosition(), s.getColumnPosition(), new JButton());
			solutionSquares.add(square);
		}
	}
	
	/**
	 * Calculate the headers of the solution generated.
	 */
	private void calculateHeaders() {

		// Row Header
		
		JPanel headerRows = new JPanel();
		headerRows.setBounds(66, 144, 37, 140);
		frmTeamW.getContentPane().add(headerRows);
		headerRows.setLayout(new GridLayout(4, 1, 0, 0));
				
		int length;
		String str;

		for (int j = 0; j < grid.getRowSize(); j++)
		{
			length = 0;
			str = "";
			
			for (int i = 0; i < grid.getColumnSize(); i++)
			{
				int offset = grid.getRowSize() * j;
				
				if (squares.get(offset + i).getFlag())
				{
					length++;
				} else {
					if (length != 0)
					{
						str = str + length + "   ";
						length = 0;
					}
				}
			}
			
			if (length != 0)
				str = str + length;
			
			JLabel solutionR1 = new JLabel(str.trim());
			solutionR1.setHorizontalAlignment(SwingConstants.RIGHT);
			headerRows.add(solutionR1);

		}
		
		// Column Header
		
		JPanel headerCols = new JPanel();
		headerCols.setBounds(113, 98, 140, 35);
		frmTeamW.getContentPane().add(headerCols);
		headerCols.setLayout(new GridLayout(1, 4, 0, 0));

		for (int j = 0; j < grid.getColumnSize(); j++)
		{
			length = 0;
			str = "<html>";		// to be able to use the break-line <br> code
			
			for (int i = 0; i < grid.getRowSize(); i++)
			{
				int offset = grid.getColumnSize() * i;
				
				if (squares.get(offset + j).getFlag())
				{
					length++;
				} else {
					if (length != 0)
					{
						str = str + length + "<br> ";
						length = 0;
					}
				}
			}
			
			if (length != 0)
				str = str + length + "<html>";
			
			JLabel solutionC1 = new JLabel(str);
			solutionC1.setHorizontalAlignment(SwingConstants.CENTER);
			headerCols.add(solutionC1);

		}		
	}
	
	/**
	 * Prepare the question grid by randomly removing most of the black squares.
	 */
	private void prepareQuestionGrid() 
	{
		double removalPercentage = 0.75;		// 75%  example
		
		// the total number of squares to remove from the blackSquares list
		int numOfSquaresToRemove = (int) Math.floor(blackSquares.size() * removalPercentage);
		
		// counter to keep track of the number of squares removed
		int ctr = 0;
		
		while (ctr < numOfSquaresToRemove)
		{
			// Choose a random square from the list of blackSquares
			int index = (int) Math.floor(Math.random() * blackSquares.size());
		
			Square selectedSquare = blackSquares.get(index);
		
			if (selectedSquare.getFlag())
			{
				selectedSquare.setFlag(false);
				ctr++;
			}
		}
	}
}
