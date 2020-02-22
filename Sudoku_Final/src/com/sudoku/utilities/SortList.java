package com.sudoku.utilities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sudoku.domain.Variable;

public class SortList {

	/**
	 * Sort List by variable ID
	 * 
	 * @param varList
	 */
	public static void sortList(List<Variable> varList) {
		Collections.sort(varList, new Comparator<Variable>() {
			@Override
			public int compare(Variable var1, Variable var2) {
				return var1.getVariableId() - var2.getVariableId();
			}
		});
	}

	/**
	 * Sort List by Degree
	 * 
	 * @param varList
	 */
	public static void sortListByDomain(List<Variable> varList) {
		Collections.sort(varList, new Comparator<Variable>() {
			@Override
			public int compare(Variable var1, Variable var2) {
				return var1.getDomainList().size() - var2.getDomainList().size();
			}
		});
	}
}
