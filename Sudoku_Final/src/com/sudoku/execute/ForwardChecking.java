package com.sudoku.execute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.sudoku.domain.Variable;
import com.sudoku.utilities.CheckBoard;
import com.sudoku.utilities.SortList;

public class ForwardChecking {

	// Loop until assignment returns success or failure
	static boolean result = false;

	/**
	 * RUN BACKTRACKING WITH FORWARD CHECKING
	 * 
	 * @param selectedVariable
	 * @param board
	 * @param unassignedVariableList
	 * @param assignedVariableList
	 * @return
	 */
	public static boolean runBacktrackingWithFC(Variable selectedVariable, Variable[][] board,
			List<Variable> unassignedVariableList, Stack<Variable> assignedVariableList) {

		boolean invalid = false;
		int assignValue = 0;
		CheckBoard check = new CheckBoard();

		// If new variable is being assigned a value
		if (selectedVariable.getAssignedValue() == 0) {
			// System.out.println("\nThe variable: " +
			// selectedVariable.getVariableId() + " has "
			// + selectedVariable.getDomainList().toString() + " \n ");
			// Loop variable's domain
			for (int i = 0; i < selectedVariable.getDomainList().size(); i++) {
				// Select domain value
				assignValue = selectedVariable.getDomainList().get(i);
				// Assign domain value
				selectedVariable.setAssignedValue(assignValue);
				
				//SudokuFrame.update_a_cell(board,selectedVariable);
				invalid = check.checkAll(board, selectedVariable);
				if (!invalid) {
					invalid = forwardChecking(selectedVariable);
				} else if (invalid && i < (selectedVariable.getDomainList().size() - 1)) {
					continue;
				} else if (invalid && i == (selectedVariable.getDomainList().size() - 1)) {
					selectedVariable.setAssignedValue(0);
					//SudokuFrame.update_a_cell(board,selectedVariable);
					Variable popVariable = assignedVariableList.pop();
					if (!popVariable.isUnchangeable()) {
						reassignDomainValues(popVariable, popVariable.getAssignedValue());
						// System.out.println("The variable: " +
						// popVariable.getVariableId() + " will be assigned");
						runBacktrackingWithFC(popVariable, board, unassignedVariableList, assignedVariableList);
					} else {
						result = true;
						System.out.println("No Solution Found");
					}
					break;
				}
				// Check for empty set in next move
				// reset selected variable's assigned value
				selectedVariable.setAssignedValue(0);
				// check if conflict found or not
				// if found and another domain value exists, continue loop
				// if conflict found but domain complete, select previous
				// variable
				// if no conflict, assign value, select next variable
				if (invalid && i < selectedVariable.getDomainList().size() - 1) {
					reassignDomainValues(selectedVariable, assignValue);
					continue;
				} else if (invalid && i == selectedVariable.getDomainList().size() - 1) {
					reassignDomainValues(selectedVariable, assignValue);
					// If root variable has no domain value left
					// end search
					// Remove previously assigned variable and assign new value
					Variable popVariable = assignedVariableList.pop();
					if (!popVariable.isUnchangeable()) {
						reassignDomainValues(popVariable, popVariable.getAssignedValue());
						// System.out.println("The variable: " +
						// popVariable.getVariableId() + " will be assigned");
						runBacktrackingWithFC(popVariable, board, unassignedVariableList, assignedVariableList);
					} else {
						result = true;
						System.out.println("No Solution Found");
					}
					break;
				} else if (!invalid) {
					// assign value
					selectedVariable.setAssignedValue(assignValue);
					// System.out.println("-----------------------------------------");
					// System.out.println("The variable: " +
					// selectedVariable.getVariableId() + " is assigned the
					// domain: "
					// + selectedVariable.getAssignedValue());
					// System.out.println("-----------------------------------------");
					// Confirm assignment
					// remove from unassigned list
					// add to assigned list
					unassignedVariableList.remove(selectedVariable);
					assignedVariableList.push(selectedVariable);
					break;
				}
			}
		} else {
			if (selectedVariable.isUnchangeable()) {
				result = true;
			} else {
				// loop variable domain
				for (int i = 0; i < selectedVariable.getDomainList().size(); i++) {
					// loop until currently assigned and variable domain are
					// same
					if (selectedVariable.getAssignedValue() == selectedVariable.getDomainList().get(i)) {
						// if currently assigned value is not last value in
						// domain,
						// assign next value
						// if currently assigned value is last value in domain,
						// select next variable from assigned list
						if (i < selectedVariable.getDomainList().size() - 1) {
							assignValue = selectedVariable.getDomainList().get(i + 1);
							selectedVariable.setAssignedValue(assignValue);
						} else if (i == selectedVariable.getDomainList().size() - 1) {
							// reassignDomainValues(selectedVariable,
							// selectedVariable.getAssignedValue());
							// System.out.println(
							// "----------------------------------------------------------------------------");
							// System.out.println("The variable: " +
							// selectedVariable.getVariableId()
							// + " returned an empty set so previous value will
							// be reassigned");
							// System.out.println(
							// "----------------------------------------------------------------------------");
							selectedVariable.setAssignedValue(0);
							unassignedVariableList.add(selectedVariable);
							SortList.sortListByDomain(unassignedVariableList);
							// If root variable has no domain value left
							// end search
							// Remove last entered variable and assign new value
							Variable popVariable = assignedVariableList.pop();
							if (!popVariable.isUnchangeable()) {
								reassignDomainValues(popVariable, popVariable.getAssignedValue());
								runBacktrackingWithFC(popVariable, board, unassignedVariableList, assignedVariableList);
							} else {
								System.out.println("No solution found");
								result = true;
							}
							break;
						}

						// Check for assigned values in neighbour
						invalid = check.checkAll(board, selectedVariable);
						if (!invalid) {
							invalid = forwardChecking(selectedVariable);
						} else if (invalid && i < selectedVariable.getDomainList().size() - 1) {
							continue;
						} else if (invalid && i == selectedVariable.getDomainList().size() - 1) {
							selectedVariable.setAssignedValue(0);
							Variable popVariable = assignedVariableList.pop();
							if (!popVariable.isUnchangeable()) {
								reassignDomainValues(popVariable, popVariable.getAssignedValue());
								runBacktrackingWithFC(popVariable, board, unassignedVariableList, assignedVariableList);
							} else {
								result = true;
								System.out.println("No Solution Found");
							}
							break;
						}

						// if conflict, select next value
						if (invalid && i < selectedVariable.getDomainList().size() - 1) {
							reassignDomainValues(selectedVariable, assignValue);
							continue;
						} else if (!invalid) {
							selectedVariable.setAssignedValue(assignValue);
							// Confirm assignment
							unassignedVariableList.remove(selectedVariable);
							assignedVariableList.push(selectedVariable);
							break;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Check if adjacent variables are assigned with same value
	 * 
	 * @param selectedVariable
	 * @return assigned
	 */
	private static boolean forwardChecking(Variable selectedVariable) {
		// System.out.println("---------------------------------------------------");
		// System.out.println("The variable: #" +
		// selectedVariable.getVariableId() + " is assigned the value of: "
		// + selectedVariable.getAssignedValue());
		// System.out.println("---------------------------------------------------");
		boolean invalid = false;
		List<Variable> adjacentVariableList = new ArrayList<Variable>();
		adjacentVariableList = selectedVariable.getDegreeList();
		// Loop the adjacent variable list (degree)
		for (int i = 0; i < adjacentVariableList.size(); i++) {
			Variable adjVariable = null;
			adjVariable = adjacentVariableList.get(i);
			if (!adjVariable.isUnchangeable()
					&& adjVariable.getDomainList().contains(selectedVariable.getAssignedValue())) {
				for (int j = 0; j < adjVariable.getDomainList().size(); j++) {
					if (adjVariable.getDomainList().get(j) == selectedVariable.getAssignedValue()) {
						adjVariable.getDomainList().remove(j);
					}
				}
			}
			if (adjVariable.getDomainList().isEmpty()) {
				invalid = true;
				break;
			}
		}

		return invalid;

	}

	/**
	 * Add value to adjacent variable's domain if forward checking unsuccessful
	 * 
	 * @param selectedVariable
	 * @param assignValue
	 */
	private static void reassignDomainValues(Variable selectedVariable, int assignValue) {
		// System.out.println("Variable: " + selectedVariable.getVariableId()
		// + " reassigns its adjcent variables with value: " + assignValue);
		// Get List of Adjacent Variables from Variable's Degree List
		List<Variable> adjacentVariableList = new ArrayList<Variable>();
		adjacentVariableList = selectedVariable.getDegreeList();
		Variable adjacentVariable = null;
		// Search through the adjacent variable list
		for (int i = 0; i < adjacentVariableList.size(); i++) {
			// Retrieve a variable
			adjacentVariable = adjacentVariableList.get(i);
			// Check if variable is changeable and if the selected variable is
			// allowed to add value
			if (!adjacentVariable.isUnchangeable()) {
				if (!adjacentVariable.getDomainList().contains(assignValue)
						&& adjacentVariable.getInitialDomList().contains(assignValue)) {
					adjacentVariable.getDomainList().add(assignValue);
					// Sort the domain list by ascending order
					Collections.sort(adjacentVariable.getDomainList());
					// System.out.println(
					// "Variable: " + adjacentVariable.getVariableId() + " has
					// domain value " + assignValue
					// + " added back to its domain: " +
					// adjacentVariable.getDomainList().toString());

				}
			}
		}
	}
}
