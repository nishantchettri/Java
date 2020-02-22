package com.sudoku.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sudoku.domain.Variable;
import com.sudoku.execute.Sudoku;

public class SudokuFrame{
	

	//size of the board
	static int BOARD_SIZE = 9;
	
	//JFrame that encompasses the entire UI
	static JFrame sudokuJFrame = new JFrame("Sudoku Solver");
	//JPanel for the area on the UI that shows the file reader
	static JPanel fileReader = new JPanel();
	//JPanel for the area on the UI that shows the puzzle board
	static PuzzleBoardJPanel puzzle_board ;
	//JPanel for the area on the UI that shows the settings
	static JPanel settingsArea = new JPanel();
	//JPanel for the area on the UI that shows the execution time
	static JPanel time_area = new JPanel();
	//JButton for upload
	static JButton upload_button  = null;
	//Toolkit to obtain screen info etc
	static Toolkit tk;
	//Variable that holds dimensions of screen
	static Dimension dim;
	//Variable to hold xPos of the JFrame
	static int xPos;
	//Variable to hold YPos of the JFrame
	static int yPos;
	//input filenmae with path
	static String filename = null;
	private static Sudoku sudoku;
	//boolean to determine is the puzzle is already drawn on the board
	static boolean puzzle_present = false;
	

	// Main method
	public static void main(String[] args) {
		//Create instances of SudokuFrame and sudoku
		SudokuFrame sudokuFrame = new SudokuFrame();
		sudoku = new Sudoku();
		
		//set size of the frame
		sudokuJFrame.setSize(800,800);
		Container contentPane = sudokuJFrame.getContentPane();
		sudokuJFrame.setLayout(new BoxLayout(contentPane,BoxLayout.PAGE_AXIS));
		tk = Toolkit.getDefaultToolkit();
			
		 dim = tk.getScreenSize();
		 xPos = (dim.width / 2) - (sudokuJFrame.getWidth() / 2);
		 yPos = (dim.height / 2) - (sudokuJFrame.getHeight() / 2);
		
			// set location based on the screen size obtained from toolkit
		 sudokuJFrame.setLocation(xPos, yPos);
		 sudokuJFrame.setResizable(false);
		 sudokuJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 sudokuJFrame.setTitle("Sudoku Puzzle solver");
		 
		 JLabel file_location = new JLabel("File Location : nil");
		 
		 //create button for upload
		 upload_button = new JButton("Upload a puzzle");
		 upload_button.setText("Upload a puzzle");
		 // Action listener - Function that gets called when upload button is clicked
		 upload_button.addActionListener(new ActionListener() {
			
			@SuppressWarnings("static-access")
			@Override
			public void actionPerformed(ActionEvent e) {
				// check if the clicked button was the upload button
				if(e.getSource() == sudokuFrame.upload_button)
				{
					filename = open_file_chooser();
					//invoke method is sudoku to read the input file and update Model
					sudoku.set_board(filename);
					
					//draw initial board
					set_initial_board(sudoku.getBoard());
				}
				
			}
			
			// Method to open file chooser
			public String open_file_chooser()
		     {
				// Open file chooser in a new JFrame
				 JFrame f = new JFrame();
				 String filename = null;
		   	     JFileChooser chooser = new JFileChooser();
		         FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		         chooser.setFileFilter(filter);
		         int returnVal = chooser.showOpenDialog(f);
		         //Check if 'OK' button was clicked on the upload window
		         if(returnVal == JFileChooser.APPROVE_OPTION) {
		        	 //read the selected filename
		            filename = chooser.getSelectedFile().getPath();
		            file_location.setText("File Location :" + filename);
		            // repaint the UI to show the selected filename with path
			         file_location.repaint();
			         // Display the settings area Jpanel and time area JPanel
			         sudokuJFrame.add(settingsArea);
			         sudokuJFrame.add(time_area);}
		         
		         return filename;
		     }
		});
		 
		 
		 // Add fileReader Jpanel's contents to it
		 fileReader.setLayout(new FlowLayout());
		 fileReader.add(file_location);
		 fileReader.add(upload_button);
		 //Add fileReader Jpanel to the main Frame 
		 sudokuJFrame.setContentPane(fileReader);
		 
		 // Add settingsArea Jpanel's contents to it
		 JLabel settings_label = new JLabel("  Select Run Settings", JLabel.LEFT);
		  settingsArea.setLayout(new BoxLayout(settingsArea, BoxLayout.X_AXIS));
		 settingsArea.add(settings_label);
		 
		 
		// Add time_area Jpanel's contents to it
		 JLabel time_to_solve = new JLabel ();
		 time_area.setLayout(new BoxLayout(time_area, BoxLayout.X_AXIS));
		 time_area.add(time_to_solve);
			
		 
		 // create checkboxes for Forward check and AC3
		  JCheckBox forwardCheck = new JCheckBox("Forward Check");
	      JCheckBox ac3 = new JCheckBox("AC3");
	     
	      
	      forwardCheck.setMnemonic(KeyEvent.VK_C);
	      ac3.setMnemonic(KeyEvent.VK_M);
	
          // Add actionlisterner. The function the should get called when the forward check checkbox is checked/unchecked
	      forwardCheck.addItemListener(new ItemListener() {
			
	    	  //if the checkbox was checked the boolean performForwardChecking in Sudoku.java is set to true else false
			@Override
			public void itemStateChanged(ItemEvent e) {
				sudoku.setPerformForwardChecking(e.getStateChange()==1);
				
			}
		});
	      
	      
	   // Add actionlistener. The function the should get called when the AC3 checkbox is checked/unchecked
	      ac3.addItemListener(new ItemListener() {
	    	//if the checkbox was checked the boolean performAC3 in Sudoku.java is set to true else false
	         public void itemStateChanged(ItemEvent e) {   
	        	 sudoku.setPerformAC3(e.getStateChange()==1);
	        	 
	         }     
	         
	      });

	      // Add the two checkboxes to settings area
	      settingsArea.add(forwardCheck);
	      settingsArea.add(ac3);
	      
	      //JButton to initiate solution
	      JButton solve_puzzle = new JButton("Solve it !!");
	      // action listener 
	      solve_puzzle.addActionListener(new ActionListener() {
			
			@Override
			//This function gets called on click of solve it button
			public void actionPerformed(ActionEvent arg0) {
				//invoke start solving method in sudoku
				long time = sudoku.startSolving();
				//update UI with time taken to solve puzzle
				time_to_solve.setText("Time to solve: " + time + " milliseconds");
				time_to_solve.repaint();
				
			}
		});
	      
	      //Add solve it puzzle to settings area
	      settingsArea.add(solve_puzzle);
	      
	      //Reset button
	      JButton reset_puzzle = new JButton("Reset");
	      reset_puzzle.addActionListener(new ActionListener() {
			
			@Override
			//resets the board to initial configuration
			public void actionPerformed(ActionEvent arg0) {
				
				sudoku.reset_puzzle();
				sudoku.set_board(filename);
				forwardCheck.setSelected(false);
				ac3.setSelected(false);
				sudokuJFrame.remove(puzzle_board);
				set_initial_board(sudoku.getBoard());
				
				
				
			}
		});
	      
	      // Add rest button to the JPanel
	      settingsArea.add(reset_puzzle);
	      sudokuJFrame.setVisible(true);
	}
	
	// Method to remove the puzzle board from the UI
	public static void remove_board()
	{
		sudokuJFrame.remove(puzzle_board);
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param Variable[][] board
	 * Funtion draw the read the state of board from the varaible board and update the UI with the puzzle state
	 */
	public static void set_initial_board(Variable[][] board)
	{
		
		if(puzzle_present)
		{
			//If board is already present on the UI, remove it
			sudokuJFrame.remove(puzzle_board);
		}
		
		// create new puzzle JPanel and add it to the Frame
		puzzle_board = new PuzzleBoardJPanel(BOARD_SIZE , BOARD_SIZE);
		sudokuJFrame.add(puzzle_board);
		puzzle_present = true;
		
		
		// set Font
		 Font font = new Font("Calibri", Font.BOLD, 14);
		 JLabel assign_lbl = new JLabel();
		 assign_lbl.setFont(font);
		
		Component[] components = puzzle_board.getComponents(); 
		Component component = null; 
		Variable v;
		int x,y;
		for (int i = 0; i < components.length; i++) 
		{ 
			component = components[i]; 
		
			if (component instanceof Variable) 
			{ 
				//if its a puzzle cell 
				v = (Variable)component;
				x = v.y;
				y = v.x;
				// clear component of its previous values
				v.removeAll();
				//determine which variable to assign the cell to
				if(board[x][y].getVariableId() == v.getVariableId())
				System.out.println("x :: " + x + "y:: " + y);
				{   if(board[x][y].getAssignedValue() != 0)
					{
						v.removeAll();
						assign_lbl = new JLabel();
						assign_lbl.setFont(font);
						assign_lbl.setText(board[x][y].getAssignedValue()+"");
						
						//assign_lbl.setText(board[x][y].getAssignedValue()+"" + x + "" + y);
						//This is initial puzzle value that is not editable. set gray background to it
						if(board[x][y].isUnchangeable())
						{
							v.setBackground(Color.LIGHT_GRAY);
						}
						v.add(assign_lbl);
					}
					
				}
				
				
			} 
		} 
		
		//repaint board
		puzzle_board.doLayout();
		//display frame
		sudokuJFrame.setVisible(true);
	}
	

	
}




