package com.sudoku.utilities;

import com.sudoku.domain.Variable;

public class CheckBoard {

	boolean duplicate = false;
	
	/*int row = 0;
	int column = 0;
	
	static int[] value = new int[3];
	Variable variable = new Variable();
	Set<Integer> variableSet = new HashSet<Integer>();*/

	/**
	 * CHECK ALL 9 SUB BOARDS AS WELL AS OVERALL BOARD
	 * 
	 * @param board
	 * @return duplicate
	 */
	public boolean checkAll(Variable[][] board, Variable variable) {
		int row = 0;
		int col = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j].getVariableId() == variable.getVariableId()) {
					row = i;
					col = j;
				}
			}
		}
		duplicate = checkSubBoard(board, row, col);
		if (!duplicate) {
			duplicate = checkRowAndCol(board, row, col);
		}
		return duplicate;
	}

	/**
	 * CHECK ALL 9 SUB BOARDS
	 * 
	 * @param board
	 * @return duplicate
	 */
	private boolean checkSubBoard(Variable[][] board, int row, int col) {
		duplicate = false;

		// Obtain primary index (top-left corner) of sub-board
		int primaryRow = (row / 3) * 3;
		int primaryCol = (col / 3) * 3;

		// Check sub board values
		duplicate = compareSubBoardValues(board, row, col, primaryRow, primaryCol);

		// Return TRUE or FALSE
		return duplicate;
	}

	/**
	 * CHECK IF EACH SUB BOARD HAS UNIQUE INTEGER VALUES
	 * 
	 * @param row
	 * @param column
	 * @param board
	 * @return TRUE or FALSE
	 */
	private boolean compareSubBoardValues(Variable[][] board, int row, int col, int primaryRow, int primaryCol) {
		duplicate = false;
		// Loop rows
		for (int i = 0; i < 3; i++) {
			// Loop columns
			for (int j = 0; j < 3; j++) {
				if ((board[row][col].getVariableId() != board[primaryRow + i][primaryCol + j].getVariableId())
						&& (board[row][col].getAssignedValue() == board[primaryRow + i][primaryCol + j]
								.getAssignedValue())) {
					duplicate = true;
				}
			}
		}
		return duplicate;
	}

	/**
	 * CHECK 9x9 BOARD FOR HORIZONTAL or VERTICAL MATCH
	 * 
	 * @param board
	 * @return duplicate
	 */
	private boolean checkRowAndCol(Variable[][] board, int row, int col) {
		duplicate = checkRow(board, row, col);
		if (!duplicate) {
			duplicate = checkCol(board, row, col);
		}
		return duplicate;
	}

	/**
	 * CHECK HORIZONTAL MATCH
	 * 
	 * @param board
	 * @return TRUE or FALSE
	 */
	private boolean checkRow(Variable[][] board, int row, int col) {
		duplicate = false;
		int assignedValue = board[row][col].getAssignedValue();
		for (int i = 0; i < board.length; i++) {
			int nextAssignedValue = board[row][i].getAssignedValue();
			if (i != col && assignedValue == nextAssignedValue) {
				duplicate = true;
				break;
			}
			if (duplicate) {
				break;
			}
		}
		return duplicate;

	}

	/**
	 * CHECK VERTICAL MATCH
	 * 
	 * @param board
	 * @return TRUE or FALSE
	 */
	private boolean checkCol(Variable[][] board, int row, int col) {
		duplicate = false;
		int assignedValue = board[row][col].getAssignedValue();
		for (int i = 0; i < board.length; i++) {
			int nextAssignedValue = board[i][col].getAssignedValue();
			if (i != row && assignedValue == nextAssignedValue) {
				duplicate = true;
				break;
			}
			if (duplicate) {
				break;
			}
		}
		return duplicate;
	}

}
