/*
 * ChiudiNoloPanel.java
 *
 * Created on 29 marzo 2008, 21.38
 *
 *  aliCE (c) 2007-2008
 *  Michele Piunti
 */

package com.sun.demo.addressbook;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.sql.Date;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane; 
import javax.swing.event.EventListenerList;

/**
 *
 * @author michelepiunti
 */
public class ChiudiNoloPanel extends javax.swing.JPanel {
    
    int id;
    boolean isEditable;
    Noleggio noleggio;
    java.awt.GridBagConstraints gridBagConstraints;
    Calendar c;
    EventListenerList listeners;
    private JButton FineNoloButt,moraButt;
    private JTextArea NoloText;
    private JTextField moratxt;
    private JLabel dettagli,mora,gextra;
    private JScrollPane scrollP;
    
    /** Creates a new instance of ChiudiNoloPanel */
    public ChiudiNoloPanel() {
        c = Calendar.getInstance();
        initComponents();
        noleggio = new Noleggio();
        noleggio.setId(-1);
        listeners = new EventListenerList();
    }
    
    private void initComponents() {
       dettagli = new JLabel("Dettagli Noleggio:");
       mora = new JLabel("Mora (euro):");
       gextra = new JLabel("Giorni Extra:");
       NoloText = new JTextArea(8,23);
       scrollP = new JScrollPane(NoloText);
       moratxt = new JTextField(8);
       moratxt.setEditable(false);
       FineNoloButt = new JButton("Chiudi Questo Noleggio");
       FineNoloButt.setActionCommand("CHIUDI_NOLO");
       FineNoloButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireActionEvent(evt);
            }
       });
       
       moraButt = new JButton("Modifica Mora");
       moraButt.setActionCommand("MORA");
       moraButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireActionEvent(evt);
            }
       });
       
       setLayout(new java.awt.GridBagLayout());
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(dettagli, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        add(scrollP, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        add(mora, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        add(moratxt, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        add(gextra, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        add(moraButt, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        add(new JLabel("* *"), gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        add(FineNoloButt, gridBagConstraints);

   }
   
   public void setNoleggio(Noleggio n, long gg, double extra, String campi){
       noleggio=n;   
       NoloText.setText(campi );
       NoloText.append("\n\nData Inizio: " + n.getD1() );
       NoloText.append("\nData Fine(oggi): " + n.getD2());
       NoloText.append("\nNOTE:\n " + n.getNote());
       gextra.setText("Giorni Extra: " + gg); 
       moratxt.setText("" + extra);
   }
   
   public Noleggio getNoleggio(){       
       NoloText.setText("");
       
       return noleggio;
   }
   
   public Double getMora(){
       double M = 0;
       String moraS = moratxt.getText();
       if(!moraS.equals("")){
           String moraS2 = moraS.replace(',','.');
           try{
               M = new Double(moraS2);
           }catch(Exception e){
               e.printStackTrace();
           }           
       }
       moratxt.setText("");
       moratxt.setEditable(false);
       return M;
   }
   
   public void editMora(){
       moratxt.setEditable(true);
   }
   
   
   private void fireActionEvent(ActionEvent evt) {
       ActionListener[] listenerList = listeners.getListeners(ActionListener.class);

       for (int i = listenerList.length-1; i>=0; --i) {
           listenerList[i].actionPerformed(evt);
       }
        
   }
    
   public void addActionListener(ActionListener listener) {
       listeners.add(ActionListener.class, listener);
   }
    
}
