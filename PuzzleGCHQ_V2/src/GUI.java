import java.awt.Color;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

//Testing something again
public class GUI {

	private JFrame frmTeamW;
	private Grid grid;
	private ArrayList<Square> squares = new ArrayList<Square>();
	private ArrayList<Square> blackSquares = new ArrayList<Square>();
	private ArrayList<Square> solutionSquares = new ArrayList<Square>();
	private JPanel questionPanel = new JPanel();
	private JPanel headerRows = new JPanel();
	private JPanel headerCols = new JPanel();
	private Square s;

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
		
		
		generateEmptyGrid(4,4,"easy");
		generateSolutionGrid();
		saveSolutionGrid();
		calculateHeaders();
		prepareQuestionGrid();
		
		JLabel lblDifficulty = new JLabel("Difficulty: " + String.valueOf(grid.getDifficulty()));
		lblDifficulty.setBounds(36, 32, 98, 14);
		lblDifficulty.setSize(150,40);
		frmTeamW.getContentPane().add(lblDifficulty);
		
		
		// Creating easy difficulty button
		JButton btnEasy = new JButton("Easy");
		btnEasy.setBounds(301, 89, 120, 35);
		btnEasy.setActionCommand("easy");
		frmTeamW.getContentPane().add(btnEasy);
		
		// Creating medium difficulty button
		JButton btnMedium = new JButton("Medium");
		btnMedium.setBounds(301, 139, 120, 35);
		btnMedium.setActionCommand("medium");
		frmTeamW.getContentPane().add(btnMedium);
		
		// Creating hard difficulty button
		JButton btnHard = new JButton("Hard");
		btnHard.setBounds(301, 189, 120, 35);
		btnHard.setActionCommand("hard");
		frmTeamW.getContentPane().add(btnHard);
		
		//  Action handler to reset everything whenever the buttons are clicked 
		ActionListener difficulty = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				String command = e.getActionCommand();
				
				reset();   //  Function to reset all panels in order to start new game
				
				//  If the easy button is clicked then make a 4x4 grid
				if(command.equals("easy")){
					generateEmptyGrid(4,4,"easy");
					lblDifficulty.setText("Difficulty: " + String.valueOf(grid.getDifficulty()));
				}
				
				//  If the medium button is clicked then make a 6x8 grid
				if(command.equals("medium")){
					generateEmptyGrid(6,8,"medium");
					lblDifficulty.setText("Difficulty: " + String.valueOf(grid.getDifficulty()));
				}
				
				//  If the hard button is clicked then make a 8x8 grid
				if(command.equals("hard")){
					generateEmptyGrid(8,8,"hard");
					lblDifficulty.setText("Difficulty: " + String.valueOf(grid.getDifficulty()));
				}
				
				//  Remake the game
				generateSolutionGrid();
				saveSolutionGrid();
				calculateHeaders();
				prepareQuestionGrid();
				
				frmTeamW.getContentPane().add(questionPanel);
				frmTeamW.getContentPane().add(headerRows);
				frmTeamW.getContentPane().add(headerCols);
				frmTeamW.revalidate();
				frmTeamW.repaint();
				
			}
		};
		
		btnEasy.addActionListener(difficulty);
		btnMedium.addActionListener(difficulty);
		btnHard.addActionListener(difficulty);
		
		
		JButton btnShowSolution = new JButton("Show Solution");
		btnShowSolution.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int errors = 0;
				boolean check;
				
				for (int i = 0; i < squares.size(); i++)
				{
					check = squares.get(i).getFlag();
					squares.get(i).setFlag(solutionSquares.get(i).getFlag());
					
					if(check != squares.get(i).getFlag())  // If the player made a mistake then it increments the error count
					{
						errors++;
					}
				}
				
				JOptionPane.showMessageDialog(null, "You made " + errors + " errors.");
				
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
	private void generateEmptyGrid(int row, int cols, String diff)
	{
		
		grid = new Grid(row, cols, diff);
		int totalRows = grid.getRowSize();
		int totalCols = grid.getColumnSize();		
		
		// Solution Grid
		questionPanel = new JPanel();
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
		
	    headerRows = new JPanel();
		headerRows.setBounds(36, 144, 37, 140);
		headerRows.setSize(60,140);
		frmTeamW.getContentPane().add(headerRows);
		headerRows.setLayout(new GridLayout(grid.getRowSize(), 10, 0, 0));
				
		int length;
		String str;

		for (int j = 0; j < grid.getRowSize(); j++)
		{
			length = 0;
			str = "";
			
			for (int i = 0; i < grid.getColumnSize(); i++)
			{
				int offset = grid.getColumnSize() * j;
				
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
		
		headerCols = new JPanel();
		headerCols.setBounds(113, 80, 140, 35);
		headerCols.setSize(140,60);
		frmTeamW.getContentPane().add(headerCols);
		headerCols.setLayout(new GridLayout(1, grid.getColumnSize(), 0, 0));

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
	
	
	/**
	 * Reset the values of all objects, array alike whenever a new puzzle is setup
	 */
	private void reset(){
		
		//  Clear all panels, objects and arrays
		frmTeamW.remove(questionPanel);
		frmTeamW.remove(headerRows);
		frmTeamW.remove(headerCols);
		grid = null;
		s = null;
		squares.clear();
		blackSquares.clear();
		solutionSquares.clear();
		
	}
	
}
