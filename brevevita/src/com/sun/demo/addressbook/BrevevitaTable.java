/*
 * BrevevitaTable.java
 *
 * Created on 8 marzo 2008, 11.56
 *
 *  aliCE (c) 2007-2008
 *  Michele Piunti
 */

package com.sun.demo.addressbook;

/*
 * BrevevitaTable.java requires no other files.
 */
import com.brevevita.videonoleggio.db.bvdb;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.*;
import java.text.*;

public class BrevevitaTable extends JPanel { 
    private boolean DEBUG = true;
    private static bvdb db;
    private BrevevitaTableModel model;
    private JTable table;

    public BrevevitaTable(bvdb DB) {
        
        super(new GridLayout(1,0));
        db=DB;
        //model = db.getListNoleggiParziali();


        table = new JTable();
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));

        //table.setFillsViewportHeight(true);

        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }
    
    public void updateModelIncl(){
        table.setModel(db.getListNoleggiParziali());       
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    public void setModelClienti(){
        table.setModel(db.getModelClienti());
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    public void setModelArticoli(){
        table.setModel(db.getModelArticoli());
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    public void updateModelCl(int idcl){
        table.setModel(db.getNoleggiCliente(idcl));        
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    public void updateModelAr(int idar){
        table.setModel(db.getNoleggiArticolo(idar));       
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    public int getNoloID(){
        javax.swing.table.TableModel model = table.getModel();
        int selRow = table.getSelectedRow();
        //System.out.println(" - selected row: "+ selRow);
        String idstr = ""+model.getValueAt(selRow, 0);
        //System.out.println(" - selected idNOLO: "+ idstr);
        return new Integer(idstr).intValue();
    }
    
    public String getCampi(){
        javax.swing.table.TableModel model = table.getModel();
        int selRow = table.getSelectedRow();
        //System.out.println(" - selected row: "+ selRow);
        String str = "titolo: "+model.getValueAt(selRow, 1) + "\ncliente: " + model.getValueAt(selRow, 2) ;
        return str;
    }
    
    public String getValue(int i, int j){ 
        return ""+table.getModel().getValueAt(i,j);
    }
    
    public void addMouseListener(MouseAdapter m){
        table.addMouseListener(m);
    }
    
    public void print(){
      try {
          MessageFormat headerFormat = new MessageFormat("Page {0}");
          MessageFormat footerFormat = new MessageFormat("- {0} -");
          table.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
        } catch (PrinterException pe) {
          System.err.println("Error printing: " + pe.getMessage());
        } 
    }
    
    
    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

//        System.out.println("Value of data: ");
//        for (int i=0; i < numRows; i++) {
//            System.out.print("    row " + i + ":");
//            for (int j=0; j < numCols; j++) {
//                System.out.print("  " + model.getValueAt(i, j));
//            }
//            System.out.println();
//        }
//        System.out.println("--------------------------");
    }
    


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SimpleTableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
//        BrevevitaTable newContentPane = new BrevevitaTable();
//        newContentPane.setOpaque(true); //content panes must be opaque
//        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    public void setDEBUG(boolean b) {
        DEBUG = b;
    }
}
