/*
 * JTableTest.java
 *
 * Created on 9 marzo 2008, 16.24
 *
 *  aliCE (c) 2007-2008
 *  Michele Piunti
 */

package com.brevevita.videonoleggio.prove;

import java.awt.*;
import java.awt.event.*;

import java.util.Vector;

import javax.swing.*;
import javax.swing.table.*;

public class JTableTest extends JFrame {

    private JTable table = null;
    private JPanel southPanel = null;
    private JButton button = null;
    private JButton button1 = null;
    private Vector data = null;
    private Object[][] cellData = null;

    private DefaultTableModel model = null;

    public JTableTest() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 200);
        this.getContentPane().setLayout(new BorderLayout());

        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("Prima");
        model.addColumn("Seconda");
        model.addColumn("Terza");

        model.addRow(new Object[] {"v11", "v12", "v13"});
        model.addRow(new Object[] {"v21", "v22", "v23"});
        model.addRow(new Object[] {"v31", "v32", "v33"});

        button = new JButton("Aggiungi riga");
        button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        addRow();
        }
        });
        button1 = new JButton("Aggiorna valore 12");
        button1.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        updateValues();
        }
        });
        southPanel = new JPanel();
        southPanel.add(button);
        southPanel.add(button1);

        this.getContentPane().add(table, BorderLayout.CENTER);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);
    }

    private void addRow() {
        int count = model.getRowCount();
        count++;
        model.addRow(new Object[] {"v" + count + "1", "v" + count + "2", "v" + count + "3"});
    }

    private void updateValues() {
        model.setValueAt("Aggiornato", 0, 1);
    }

    public static void main(String[] args) {
        JTableTest jtable = new JTableTest();
        jtable.setVisible(true);
    }
}
