package com.sudoku.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.sudoku.domain.Variable;

public class PuzzleBoardJPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//JPanel the correspond a grid on the UI
	PuzzleBoardJPanel(int w, int h) {
        super(new GridBagLayout());
        Variable v ;
        int id =0;
        GridBagConstraints c = new GridBagConstraints();
        /** construct the grid */
        for (int i=0; i<w; i++) {
            for (int j=0; j<h; j++) {
                c.weightx = 1.0;
                c.weighty = 1.0;
                c.fill = GridBagConstraints.BOTH;
                c.gridx = i;
                c.gridy = j;
                //create a variable for each cell
                v = new Variable(i, j);
                // set its location indexes and Id
                v.setLocation(new Point(i, j));
                v.setVariableId(id);
                add(v, c);
                id++;
                
            }
        }

        /** create a black border */ 
        setBorder(BorderFactory.createLineBorder(Color.black)); 
    }



}
