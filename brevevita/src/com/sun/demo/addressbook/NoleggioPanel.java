/*
 * NoleggioPanel.java
 *
 * Created on 24 febbraio 2008, 17.53
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
import javax.swing.event.EventListenerList;


/**
 *
 * @author michelepiunti
 */
public class NoleggioPanel extends javax.swing.JPanel {
    
    int id;
    boolean isEditable;
    Noleggio noleggio;
    java.awt.GridBagConstraints gridBagConstraints;
    Cliente cl;
    Articolo art;
    Calendar c;
    EventListenerList listeners;
    
    /** Creates a new instance of NoleggioPanel */
    public NoleggioPanel() {
        c = Calendar.getInstance();
        initComponents();
        noleggio = new Noleggio();
        noleggio.setId(-1);
        listeners = new EventListenerList();
    }
    
    private void initComponents() {
        cercaCl = new JLabel("Cerca Cliente:");
        cercaArt = new JLabel("Cerca Articolo:");
        cliente = new JLabel("*");
        articolo = new JLabel("*");
        note = new JLabel("Note:");
        dataInizio = new JLabel(""+new Date(c.getTimeInMillis()));
        cercaClButt = new JButton("cerca");
        cercaClButt.setActionCommand("FIND_CLIENTE");
        cercaClButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireActionEvent(evt);
            }
        });
        cercaArtButt = new JButton("cerca");
        cercaArtButt.setActionCommand("FIND_ARTICOLO");
        cercaArtButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireActionEvent(evt);
            }
        });
        checkButt = new JButton("Controlla se Nolegiato");
        checkButt.setActionCommand("CHECK");
        checkButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireActionEvent(evt);
            }
        });
        
        
        NoloButt = new JButton("Noleggia!");
        NoloButt.setActionCommand("NEW_NOLO");
        NoloButt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fireActionEvent(evt);
            }
        });
        
        
        cercaCltxt = new JTextField(15);
        cercaArtxt = new JTextField(15);
        notetxt = new JTextField(23);
        
        setLayout(new java.awt.GridBagLayout());
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(cercaCl, gridBagConstraints);
        
        //gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(cercaCltxt, gridBagConstraints);
        
        //gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(cercaClButt, gridBagConstraints);
        
        //gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        add(cliente, gridBagConstraints);
        
        //gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 1;
        add(cercaArt, gridBagConstraints);
        
        //gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        add(cercaArtxt, gridBagConstraints);
        
        //gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 1;
        add(cercaArtButt, gridBagConstraints);
        
        //gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        add(articolo, gridBagConstraints);
        
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        add(note, gridBagConstraints);
        
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        add(notetxt, gridBagConstraints);
        
        //gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 1;
        add(dataInizio, gridBagConstraints);
        
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        add(checkButt, gridBagConstraints);
        
        //gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        add(NoloButt, gridBagConstraints);
    }
    
    
    void setData1() {  
        Date start = new Date(c.getTimeInMillis());
        System.out.println("startdate " + start);
        noleggio.setD1(start);
    }
    
    void setData2() {    
        Date end = new Date(c.getTimeInMillis());
        System.out.println("enddate " + end);
        noleggio.setD2(end);
    }
    
    public void setArticolo(Articolo a){
        this.art = a;
        noleggio.setArId(a.getId());
        noleggio.setId(-1);
//        this.invalidate();
//        this.repaint();
//        articolo.invalidate();
        articolo.setText(art.getString());
       
//        this.repaint();
    }
    
    public void setCliente(Cliente c){
        this.cl = c;
        noleggio.setClId(c.getId());
        noleggio.setId(-1);
//        this.invalidate();
//        this.repaint();
//        cliente.invalidate();
        cliente.setText(cl.getString());
//        
//        this.repaint();
    }
    
    private void setNote(){
        noleggio.setNote(notetxt.getText());
    }
    
    private void setInfo1(){
        noleggio.setInfo1("");
    }
    
    private void setInfo2(){
        noleggio.setInfo2("");
    }
    
    public Noleggio getNoleggio() {
        setData1();
        setNote();
        setInfo1();
        setInfo2();
//        setId(-1);   
        notetxt.setText("");

        return noleggio;
    }
    
    public String getClstr(){
        return cercaCltxt.getText();
    }
        
    public String getArstr(){
        return cercaArtxt.getText();
    }
    

    
//    protected void paintComponent(java.awt.Graphics g) {
        
//        if(cl!=null)
//            cliente.setText(cl.getString());
//        if(art!=null)
//            articolo.setText(art.getString());
        
//    }
    
    private void fireActionEvent(ActionEvent evt) {
        ActionListener[] listenerList = listeners.getListeners(ActionListener.class);

        for (int i = listenerList.length-1; i>=0; --i) {
            listenerList[i].actionPerformed(evt);
        }
        
    }
    
    public void addActionListener(ActionListener listener) {
        listeners.add(ActionListener.class, listener);
    }

    
    private JLabel cercaCl,cercaArt,cliente,articolo,dataInizio,note;
    private JButton cercaClButt,cercaArtButt, NoloButt, checkButt;
    private JTextField cercaCltxt,cercaArtxt,notetxt;
     
    
}
