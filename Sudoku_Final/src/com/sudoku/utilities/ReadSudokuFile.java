package com.sudoku.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.sudoku.domain.Variable;

public class ReadSudokuFile {

	/**
	 * Read given sudoku file
	 * 
	 * @param filename
	 */
	public Variable[][] readSudokuPuzzle(String filename, Variable[][] board) {
		try {
			// Read file
			File variableListFile = new File(filename);
			FileInputStream fis = new FileInputStream(variableListFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));

			// To store each line
			String line = null;
			// To store split values in each line
			String[] cell;
			// To set id for each variable
			int id = 0;
			// Row Counter
			int rowIndex = 0;
			
			Variable variable;
			List<Integer> domainList;
			List<Integer> initDomainList;

			// Loop each line
			while ((line = br.readLine()) != null) {
				// Split all values of line into string array
				cell = line.split("\t");
				// Loop each string and assign to variable
				for (int i = 0; i < cell.length; i++) {
					// To set variable information
					variable = new Variable(rowIndex,i);
					variable.setVariableId(id);
					variable.setAssignedValue(Integer.parseInt(cell[i]));
					// Set Domain List
					domainList = new ArrayList<Integer>();
					initDomainList = new ArrayList<Integer>();
					if (variable.getAssignedValue() != 0) {
						variable.setUnchangeable(true);
						domainList.add(variable.getAssignedValue());
						initDomainList.add(variable.getAssignedValue());
					} else {
						// Domain List, 1-9
						for (int j = 1; j < 10; j++) {
							domainList.add(j);
							initDomainList.add(j);
						}
					}
					variable.setDomainList(domainList);
					variable.setInitialDomList(initDomainList);
					// Set variable to board
					board[rowIndex][i] = variable;
					id++;
				}
				rowIndex++;
			}
			// Close buffered reader
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return board;
	}
}
