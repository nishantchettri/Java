package com.sudoku.execute;

import java.util.List;
import java.util.Stack;

import com.sudoku.domain.Variable;
import com.sudoku.utilities.CheckBoard;
import com.sudoku.utilities.SortList;

public class NoHeuristic {

	static boolean result = false;

	/**
	 * RUN BACKTRACKING WITHOUT FC OR AC3
	 * 
	 * @param selectedVariable
	 * @param board
	 * @param unassignedVariableList
	 * @param assignedVariableList
	 * @return
	 */
	static boolean runBacktrackingWithNoHeuristic(Variable selectedVariable, Variable[][] board,
			List<Variable> unassignedVariableList, Stack<Variable> assignedVariableList) {

		boolean conflict = false;
		int assignValue = 0;

		// If new variable is being assigned a value
		if (selectedVariable.getAssignedValue() == 0) {

			// Loop variable's domain
			for (int i = 0; i < selectedVariable.getDomainList().size(); i++) {
				// Select domain value
				assignValue = selectedVariable.getDomainList().get(i);

				// Assign domain value
				selectedVariable.setAssignedValue(assignValue);
				//SudokuFrame.update_a_cell(board,selectedVariable);

				// Check for assigned values in neighbour
				CheckBoard check = new CheckBoard();
				conflict = check.checkAll(board, selectedVariable);

				// reset selected variable's assigned value
				selectedVariable.setAssignedValue(0);
				//SudokuFrame.update_a_cell(board,selectedVariable);

				// check if conflict found or not
				// if found and another domain value exists, continue loop
				// if conflict found but domain complete, select previous
				// variable
				// if no conflict, assign value, select next variable
				if (conflict && i < (selectedVariable.getDomainList().size() - 1)) {
					continue;
				} else if (conflict && i == (selectedVariable.getDomainList().size() - 1)) {
					// If root variable has no domain value left
					// end search
					if (!assignedVariableList.isEmpty()) {
						// if assigned variable list has value, remove last
						// entered variable and assign new value
						runBacktrackingWithNoHeuristic(assignedVariableList.pop(), board, unassignedVariableList,
								assignedVariableList);
						break;
					} else {
						System.out.println("No solution found");
						result = true;
						break;
					}
				} else if (!conflict) {
					// assign value
					selectedVariable.setAssignedValue(assignValue);
					// System.out.println("The variable: " +
					// selectedVariable.getVariableId() + " is assigned the
					// domain: "
					// + selectedVariable.getAssignedValue());
					// Confirm assignment
					// remove from unassigned list
					// add to assigned list
					unassignedVariableList.remove(selectedVariable);
					assignedVariableList.push(selectedVariable);
					break;
				}
			}
		} else {
			// loop variable domain
			for (int i = 0; i < selectedVariable.getDomainList().size(); i++) {
				// loop until currently assigned and variable domain are same
				if (selectedVariable.getAssignedValue() == selectedVariable.getDomainList().get(i)) {
					// if currently assigned value is not last value in domain,
					// assign next value
					// if currently assigned value is last value in domain,
					// select next variable from assigned list
					if (i < (selectedVariable.getDomainList().size() - 1)) {
						assignValue = selectedVariable.getDomainList().get(i + 1);
						selectedVariable.setAssignedValue(assignValue);
					} else if (i == (selectedVariable.getDomainList().size() - 1)) {
						selectedVariable.setAssignedValue(0);
						unassignedVariableList.add(selectedVariable);
						SortList.sortList(unassignedVariableList);
						// If root variable has no domain value left
						// end search
						if (!assignedVariableList.isEmpty()) {
							// if assigned variable list has value, remove last
							// entered variable and assign new value
							runBacktrackingWithNoHeuristic(assignedVariableList.pop(), board, unassignedVariableList,
									assignedVariableList);
							break;
						} else {
							System.out.println("No solution found");
							result = true;
							break;
						}
					}

					// Check for assigned values in neighbour
					CheckBoard check = new CheckBoard();
					conflict = check.checkAll(board, selectedVariable);

					// if conflict, select next value
					if (conflict && i < (selectedVariable.getDomainList().size() - 1)) {
						continue;
					} else if (!conflict) {
						selectedVariable.setAssignedValue(assignValue);
						// Display
						// System.out.println("The variable: " +
						// selectedVariable.getVariableId()
						// + " is assigned the domain: " +
						// selectedVariable.getAssignedValue());
						// Confirm assignment
						unassignedVariableList.remove(selectedVariable);
						assignedVariableList.push(selectedVariable);
						break;
					}
				}
			}
		}
		return result;
	}
}
