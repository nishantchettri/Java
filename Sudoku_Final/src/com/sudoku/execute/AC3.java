package com.sudoku.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.sudoku.domain.Variable;

public class AC3 {
	/**
	 * Matrix to the path between the nodes and their costs
	 */
	private static Variable[][] board;
	// list of unassigned variables
	static List<Variable> unassignedVariableList = new ArrayList<Variable>();
	// list of assigned variables
	static Stack<Variable> assignedVariableList = new Stack<Variable>();
	// all arcs in the csp
	static Stack<String> arcs = new Stack<String>();
	// flag to indicate if forward checking has to be done
	static boolean perform_forward_checking = true;
	// flag to indicate if AC3 checking has to be done
	static boolean perform_ac3 = false;
	// domain = 3 or 4
	static int domain_count = 0;

	/**
	 * Run Backtracking with AC3
	 * 
	 * @param selectedVariable
	 * @param board
	 * @param unassignedVariableList
	 * @param assignedVariableList
	 * @return
	 */
	static Variable[][] runBacktrackingWithAC3(Variable[][] board, List<Variable> unassignedVariableList,
			Stack<Variable> assignedVariableList) {
		AC3.board = board;
		AC3.unassignedVariableList = unassignedVariableList;
		AC3.assignedVariableList = assignedVariableList;
		perform_ac3 = true;
		if (perform_ac3) {
			addArcs();
			runAC3();
		}
		return board;

	}

	/**
	 * RUN THE AC3 FUNCTION
	 */
	private static void runAC3() {
		String arc;
		String[] vars;
		// boolean to indicate if a domain was removed from a variable
		boolean removed = false;

		while (!arcs.isEmpty()) {
			arc = arcs.pop();
			vars = arc.split(",");
			// Remove any inconsistent values from variables' domain
			removed = removeInconsistentValues(Integer.parseInt(vars[0].trim()), Integer.parseInt(vars[1].trim()));
			if (removed) {
				// If domain was removed from a variable, add its neighboring
				// arcs to the stack
				addBackNeighbourArcs(Integer.parseInt(vars[0]));
			}
		}
	}

	/**
	 * Add Arcs to the Queue
	 */
	private static void addArcs() {
		Variable var = null;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				var = board[i][j];
				// Add arc to the queue
				// Add unchangeable variables to the tail
				for (Variable deg : var.getDegreeList()) {
					if (var.getInitialDomList().size() != 1) {
						arcs.push(var.getVariableId() + "," + deg.getVariableId());
					} else {
						arcs.push(deg.getVariableId() + "," + var.getVariableId());
					}
				}

			}
		}
	}

	/**
	 * Function invoked if a domain was removed from variable with id x, by AC3.
	 * If so, all the neighbours of that variable are added back to the arc
	 * 
	 * @param variableId
	 */
	private static void addBackNeighbourArcs(int variableId) {
		Variable variable = null;
		variable = getVariableWithID(variableId);
		for (int i = 0; i < variable.getDegreeList().size(); i++) {
			// If variable is unchangeable, place it on the tail of the arc
			if (variable.getDegreeList().get(i).isUnchangeable()) {
				arcs.push(variable.getVariableId() + "," + variable.getDegreeList().get(i).getVariableId());
			} else {
				arcs.push(variable.getDegreeList().get(i).getVariableId() + "," + variable.getVariableId());
			}
		}
	}

	/**
	 * Returns a variable with a given id from the assigned/unassigned list
	 * 
	 * @param id
	 * @return
	 */
	private static Variable getVariableWithID(int id) {
		Variable var = null;

		boolean presentInAssignedList = false;
		for (int i = 0; i < assignedVariableList.size(); i++) {
			var = assignedVariableList.get(i);
			if (id == var.getVariableId()) {

				presentInAssignedList = true;
				break;
			}
		}
		if (!presentInAssignedList) {
			for (int i = 0; i < unassignedVariableList.size(); i++) {
				var = unassignedVariableList.get(i);
				if (id == var.getVariableId()) {
					break;
				}
			}
		}
		return var;
	}

	/**
	 * Invoked by ac3() to remove from tail the domains that are inconsistent
	 * for assignments in variable with id head.
	 * 
	 * @param head
	 *            Variable id
	 * @param tail
	 *            Variable id
	 * @return removed boolean indicating if any domain value was removed
	 */
	private static boolean removeInconsistentValues(int head, int tail) {
		boolean removed = false;
		// Primary Variable
		Variable variableX = getVariableWithID(head);
		List<Integer> domainOfX = variableX.getDomainList();

		// Secondary Variable
		Variable variableY = getVariableWithID(tail);
		List<Integer> domainOfY = variableY.getDomainList();

		// Loop Y's domain
		if (variableY.getAssignedValue() != 0) {
			// If variable Y has a previously assigned value
			for (int i = 0; i < domainOfX.size(); i++) {
				if (domainOfX.get(i) == variableY.getAssignedValue()) {
					domainOfX.remove(i);
				}
			}
		} else {
			// If variable Y does not have an assigned value
			for (int i = 0; i < domainOfX.size(); i++) {
				for (int j = 0; j < domainOfY.size(); j++) {
					if (domainOfY.size() == 1 && domainOfY.get(j) == domainOfX.get(i)) {
						domainOfX.remove(i);
						removed = true;
						break;
					}
				}
				if (removed) {
					break;
				}
			}
		}
		return removed;
	}

}
