/*
 * TariffeApp.java
 *
 * Created on 2 aprile 2008, 21.11
 *
 *  aliCE (c) 2007-2008
 *  Michele Piunti
 */

package com.brevevita.videonoleggio.gui;

import com.brevevita.videonoleggio.db.bvdb;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

/**
 *
 * @author michelepiunti
 */
public class TariffeApp extends JPanel 
    implements ActionListener {
                    
    private JPanel centralPane;
    private JButton modButt,tarButt;
    private JTextField d1,d2,d3;
    private JLabel td1, td2, td3;

    private static bvdb db;
    
    /** Creates a new instance of ChiudiNoloApp */
    public TariffeApp(bvdb DB) {
        this.db = DB;
        initComponents();

    }
    
    private void initComponents() {
        td1 = new JLabel("Tariffa Primo Giorno");
        d1 = new JTextField(8);
        td2 = new JLabel("Tariffa Giorni Successivi");
        d2 = new JTextField(8);
        //td3 = new JLabel("Tariffa Giorni Successivi");
        //d3 = new JTextField(8);
        modButt = new JButton("Modifica");
        modButt.setActionCommand("MODIFICA");
        modButt.addActionListener(this);        
        tarButt = new JButton("Salva Tariffe");
        tarButt.setActionCommand("SALVA");
        tarButt.addActionListener(this);
        
        
        setLayout(new java.awt.GridBagLayout());
        
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(td1, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(d1, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(td2, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(d2, gridBagConstraints);
        
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 7;
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
//        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
//        add(td3, gridBagConstraints);
//        
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 8;
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
//        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
//        add(d3, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(modButt, gridBagConstraints);
        
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(tarButt, gridBagConstraints);        

        double[] tar = db.getTariffe();
        d1.setText(""+tar[0]);
        d2.setText(""+tar[1]);
        //d3.setText(""+tar[2]);
        d1.setEditable(false);
        d2.setEditable(false);
        //d3.setEditable(false);        
    }// 
    
    public void saveTariffe(){
      try{
        String d1str = d1.getText();
        String d1s = d1str.replace(',','.');
        double D1 = new Double(d1s).doubleValue();
        
        String d2str = d2.getText();
        String d2s = d2str.replace(',','.');
        double D2 = new Double(d2s).doubleValue();
        
//        String d3str = d3.getText();
//        d3str.replace(',','.');
//        double D3 = new Double(d3str).doubleValue();
        db.updateTariffe(D1,D2,D2);
        
        d1.setEditable(false);
        d2.setEditable(false);
        //d3.setEditable(false); 
     }catch(Exception e){
              //custom title, error icon
            JOptionPane.showMessageDialog(new JFrame(),
                "Controlla il formato Tariffe: deve essere numerico es: 3.50",
                "Dati Errati",
                JOptionPane.ERROR_MESSAGE);
     }

    }
    
    public void modificaTariffe(){
        d1.setEditable(true);
        d2.setEditable(true);
        //d3.setEditable(true);
    }
 
    
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("ActionEvent: " + actionCommand);
        if (actionCommand.equalsIgnoreCase("SALVA")) {
            saveTariffe();
        } else if (actionCommand.equalsIgnoreCase("MODIFICA")) {
            modificaTariffe();
        } else if (actionCommand.equalsIgnoreCase("yhhy")) {
            //findCliente();
        } else if (actionCommand.equalsIgnoreCase("jtttwpi")) {
            //editArticolo();
        } else if (actionCommand.equalsIgnoreCase("ohrggro")) {
            //saveArticolo();
        }
    }
    
    
    class MouseEv extends java.awt.event.MouseAdapter{
        public void mouseClicked(MouseEvent e) {


        }
    }
    
}
