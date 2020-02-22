package com.sudoku.domain;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Variable extends JPanel {
	private static final long serialVersionUID = 1L;
	int variableId;
	int assignedValue;
	List<Integer> domainList;
	List<Variable> degreeList = new ArrayList<Variable>();
	List<Integer> initialDomList = new ArrayList<Integer>();
	boolean unchangeable = false;
	public int x, y; // the x,y position on the grid
	private Dimension dim = new Dimension();

	public Variable(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.setLocation(x, y);
		/** create a black border */
		setBorder(BorderFactory.createLineBorder(Color.black));

		/** set size to 50x50 pixel for one square */
		dim.setSize(50, 50);
		setPreferredSize(dim);
	}

	// GETTERS AND SETTERS
	public int getVariableId() {
		return variableId;
	}

	public void setVariableId(int variableId) {
		this.variableId = variableId;
	}

	public int getAssignedValue() {
		return assignedValue;
	}

	public void setAssignedValue(int assignedValue) {
		this.assignedValue = assignedValue;
	}

	public List<Integer> getDomainList() {
		return domainList;
	}

	public void setDomainList(List<Integer> domainList) {
		this.domainList = domainList;
	}

	public List<Variable> getDegreeList() {
		return degreeList;
	}

	public void setDegreeList(List<Variable> degreeList) {
		this.degreeList = degreeList;
	}

	public boolean isUnchangeable() {
		return unchangeable;
	}

	public void setUnchangeable(boolean unchangeable) {
		this.unchangeable = unchangeable;
	}

	public List<Integer> getInitialDomList() {
		return initialDomList;
	}

	public void setInitialDomList(List<Integer> initialDomList) {
		this.initialDomList = initialDomList;
	}
}
