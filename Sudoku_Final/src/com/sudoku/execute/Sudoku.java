package com.sudoku.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.sudoku.domain.Variable;
import com.sudoku.ui.SudokuFrame;
import com.sudoku.utilities.CheckBoard;
import com.sudoku.utilities.ReadSudokuFile;
import com.sudoku.utilities.SortList;

public class Sudoku {
	// Board info
	static int BOARD_SIZE = 9;
	private static Variable[][] board = new Variable[BOARD_SIZE][BOARD_SIZE];
	
	// Sudoku Puzzle Filename
	static int difficulty = 1;
	String filename = "";
	
	// Variable Lists
	static List<Variable> mrvList = new ArrayList<Variable>();
	static List<Variable> unassignedVariableList = new ArrayList<Variable>();
	static Stack<Variable> assignedVariableList = new Stack<Variable>();
	// Loop until assignment returns success or failure
	static boolean result = false;
	

	private static boolean performAC3 = false;

	// Check performance variables
	private static boolean performForwardChecking = false;
	/**
	 * @return the performForwardChecking
	 */
	public static boolean isPerformForwardChecking() {
		return performForwardChecking;
	}

	/**
	 * @param performForwardChecking the performForwardChecking to set
	 */
	public void setPerformForwardChecking(boolean performForwardChecking) {
		Sudoku.performForwardChecking = performForwardChecking;
	}

	/**
	 * @return the performAC3
	 */
	public static boolean isPerformAC3() {
		return performAC3;
	}

	/**
	 * @param performAC3 the performAC3 to set
	 */
	public void setPerformAC3(boolean performAC3) {
		Sudoku.performAC3 = performAC3;
	}

	
	/**
	 * @return the board
	 */
	public Variable[][] getBoard() {
		return board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(Variable[][] board) {
		Sudoku.board = board;
	}

	
	
	
	public void set_board(String filename)
	{
		ReadSudokuFile rsf = new ReadSudokuFile();
		board = rsf.readSudokuPuzzle(filename, board);
	}

	/**
	 * MAIN METHOD
	 * 
	 * @param args
	 */
	public long startSolving() {
		// Start calculating operation time
		long startTime = System.currentTimeMillis();

		// Assign Degree and Domain List
		assignDegree();

		// AC3 is performed to reduce domain values
		if (performAC3) {
			board = AC3.runBacktrackingWithAC3(board, unassignedVariableList, assignedVariableList);
		}

		// Loop until assignment is complete
		while (!result) {
			// if all MRV are equals
			// check secondary heuristic, i.e. degree
			if (!unassignedVariableList.isEmpty()) {
				// Check minimum remaining values
				SortList.sortListByDomain(unassignedVariableList);
				Variable selectedVariable = unassignedVariableList.get(0);
				// Run backtracking search with the selected variable
				// Run either forward checking or checking with no heuristic
				// depending on UI selection
				if (performForwardChecking) {
					result = ForwardChecking.runBacktrackingWithFC(selectedVariable, board, unassignedVariableList,
							assignedVariableList);
				} else {
					result = NoHeuristic.runBacktrackingWithNoHeuristic(selectedVariable, board, unassignedVariableList,
							assignedVariableList);
				}
			} else {
				result = true;
			}
		}

		// FINAL OUTPUT
		SortList.sortList(assignedVariableList);
		for (int i = 0; i < assignedVariableList.size(); i++) {
			System.out.println("The variable: " + assignedVariableList.get(i).getVariableId() + " is assigned value: "
					+ assignedVariableList.get(i).getAssignedValue());
		}

		// Check For Final Output Conflict
		CheckBoard check = new CheckBoard();
		boolean match = false;
		Variable variable = null;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				variable = board[i][j];
				match = check.checkAll(board, variable);
				if (match) {
					break;
				}
			}
			if (match) {
				System.out.println("DUPLICATE");
				break;
			}
		}
		if (!match) {
			SudokuFrame.remove_board();
			SudokuFrame.set_initial_board(board);
			System.out.println("SOLVED");
		}

		// Calculate operation end and search time
		long endTime = System.currentTimeMillis();
		long searchTime = endTime - startTime;

		// Display total execution time
		System.out.println("CPU Execution time: " + (searchTime) + " milliseconds");
		return searchTime;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * ASSIGN DEGREE AND FILTER DOMAINS
	 */
	private static void assignDegree() {
		Variable variable = null;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				variable = board[i][j];
				variable.setDegreeList(assignDegreeList(board, i, j));
				if (board[i][j].getAssignedValue() == 0) {
					unassignedVariableList.add(board[i][j]);
				} else {
					assignedVariableList.push(board[i][j]);
				}
			}
		}
	}

	/**
	 * Get Degree List for each variable
	 * 
	 * @param board
	 * @param row
	 * @param col
	 * @return
	 */
	private static List<Variable> assignDegreeList(Variable[][] board, int row, int col) {
		List<Variable> degreeList = new ArrayList<Variable>();
		Variable variable = null;

		// Get Sub-Board Degree
		// Obtain primary index (top-left corner) of sub-board
		int primaryRow = (row / 3) * 3;
		int primaryCol = (col / 3) * 3;
		// Loop sub-board but skip current cell
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (((primaryRow + i) != row || (primaryCol + j) != col)) {
					variable = board[primaryRow + i][primaryCol + j];
					degreeList.add(variable);
				}
			}
		}

		// Get Row Degree
		for (int i = 0; i < board.length; i++) {
			if (i != col) {
				variable = board[row][i];
				if (!degreeList.contains(variable)) {
					degreeList.add(variable);
				}
			}
		}

		// Get Column Degree
		for (int i = 0; i < board.length; i++) {
			if (i != row) {
				variable = board[i][col];
				if (!degreeList.contains(variable)) {
					degreeList.add(variable);
				}
			}
		}
		return degreeList;
	}
	
	public  void reset_puzzle()
	{
		System.out.println("Resetting Puzzle");
		board = new Variable[BOARD_SIZE][BOARD_SIZE];
		mrvList = new ArrayList<Variable>();
		unassignedVariableList = new ArrayList<Variable>();
		assignedVariableList = new Stack<Variable>();
		result = false;
		performForwardChecking = false;
		performAC3 = false;
		
	}

}
