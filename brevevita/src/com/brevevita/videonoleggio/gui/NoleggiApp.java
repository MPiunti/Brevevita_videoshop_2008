/*
 * NoleggiApp.java
 *
 * Created on 9 febbraio 2008, 19.49
 */

package com.brevevita.videonoleggio.gui;

import com.sun.demo.addressbook.*;

import com.brevevita.videonoleggio.db.bvdb;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Michele
 */
public class NoleggiApp extends JPanel 
        implements ActionListener {
                    
    private JPanel centralPane;
    private AddressListPanel clientiListPanel, articoliListPanel;
    private NoleggioPanel noleggioPanel;
    private BrevevitaTable nol;
                
    
    private int selectedEntry = -1;
    private static bvdb db;
    
    /** Creates a new instance of NoleggiApp */
    public NoleggiApp(bvdb DB) {
        this.db = DB;
        initComponents();

        noleggioPanel.addActionListener(this);
        updateLists();
    }
    
    private void initComponents() {
        this.setLayout(new BorderLayout());
        centralPane = new JPanel(new BorderLayout());

        noleggioPanel = new com.sun.demo.addressbook.NoleggioPanel();
        clientiListPanel = new com.sun.demo.addressbook.AddressListPanel();
        articoliListPanel = new com.sun.demo.addressbook.AddressListPanel();
        clientiListPanel.addListSelectionListener(new clientiListener());
        articoliListPanel.addListSelectionListener(new articoliListener());
        nol = new BrevevitaTable(db);
        nol.setOpaque(true); //content panes must be opaque
        nol.updateModelIncl();
        
        centralPane.add(noleggioPanel, java.awt.BorderLayout.CENTER);
        centralPane.add(nol, java.awt.BorderLayout.SOUTH);
   
        this.add(centralPane, java.awt.BorderLayout.CENTER);
        this.add(clientiListPanel, java.awt.BorderLayout.WEST);
        this.add(articoliListPanel, java.awt.BorderLayout.EAST);
        
    }//  
    
    
    private void saveNoleggio() {
        
//        if (tPanel.isEditable()) {
        Noleggio nolo = noleggioPanel.getNoleggio();
        int id = nolo.getId();

        int idCl = nolo.getClId();
        int idAr = nolo.getArId();
        String date1 = db.getNoleggioMovCl(idCl,idAr);
        System.out.println("***" + date1 + "*** note: "+nolo.getNote());
        if(date1!=null) {
            int n = JOptionPane.showConfirmDialog(
                new JFrame(),
                "Video giˆ noleggiato il: "+date1+
                    "\n vuoi procedere?",
                "Warning!",
                JOptionPane.YES_NO_OPTION);

            //if(n == JOptionPane.YES_OPTION)
              // procedura da eseguire in caso affermativo
                //System.out.println("YES" + n);
             // else 
            if(n == JOptionPane.NO_OPTION){
                return;
            }

        }
        //System.out.println("saving-- nolo  id: " + id);

        if (id == -1) {  
            id = db.saveNoleggio(nolo);
            nolo.setId(id);
            //System.out.println("id nuovo nolo: " +id);

            nol.updateModelIncl();
            double spesa = (-db.tariffa1());
            Double res = db.updateCreditoCliente(idCl, spesa);
//            System.out.println(" credito di "+idCl+" has been decreased of *"+
//                    spesa+"* and updated to: " +res);
        }
    }
    
    private void checkClAr(){
        
//        if (tPanel.isEditable()) {
        Noleggio nolo = noleggioPanel.getNoleggio();

        int idCl = nolo.getClId();
        int idAr = nolo.getArId();
        String date1 = db.getNoleggioMovCl(idCl,idAr);
        //System.out.println("***" + date1 + "***");
        if(date1!=null) {
            JOptionPane.showMessageDialog(new JFrame(),
                "Video giˆ noleggiato il " + date1,
                "Attenzione",
                JOptionPane.WARNING_MESSAGE);    
        }else {
            JOptionPane.showMessageDialog(new JFrame(),
                "Video MAI noleggiato " ,
                "OK",
                JOptionPane.INFORMATION_MESSAGE);       
        }
        
    }
    
    public void updateLists(){
        List<ListEntry> clienti = db.getListClienti();
        List<ListEntry> articoli = db.getListArticoli();
        clientiListPanel.clear();
        clientiListPanel.addListEntries(clienti);
        articoliListPanel.clear();
        articoliListPanel.addListEntries(articoli);
    }

    
    private void findCliente() {
        String cl = noleggioPanel.getClstr().toUpperCase();
        int id = db.getClientIDFromName(cl);
        System.out.println("find " + id);
        if(id!=0)
          clientiListPanel.setSelectionWithID(id);
    }
    
    private void findArticolo() {
        String ar = noleggioPanel.getArstr().toUpperCase();
        int id = db.getArticoloIDFromTitle(ar);
        System.out.println("find " + id);
        if(id!=0)
          articoliListPanel.setSelectionWithID(id);
    }
    
    protected void paintComponent(java.awt.Graphics g) {
        updateLists();
    }
    
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("ActionEvent: " + actionCommand);
        if (actionCommand.equalsIgnoreCase("NEW_NOLO")) {
            saveNoleggio();
        } else if (actionCommand.equalsIgnoreCase("FIND_ARTICOLO")) {
            findArticolo();
        } else if (actionCommand.equalsIgnoreCase("FIND_CLIENTE")) {
            findCliente();
        } else if (actionCommand.equalsIgnoreCase("CHECK")) {
            checkClAr();
        } else if (actionCommand.equalsIgnoreCase("oho")) {
            //saveArticolo();
        }
    }



    
        
 class clientiListener implements ListSelectionListener  {
     
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        JList entryList = (JList) e.getSource();
        selectedEntry = entryList.getSelectedIndex();
        ListEntry entry = (ListEntry)entryList.getSelectedValue();
        if (entry != null) {
            int id = entry.getId();
            Cliente cliente = db.getCliente(id);
            System.out.println("selected cliente "+cliente.getFirstName() + "  " + cliente.getLastName());
            noleggioPanel.setCliente(cliente);
        } else {
            //BrevevitaPanel.clear();
        }
    }
     
 }
 
 class articoliListener implements ListSelectionListener  {
     
   public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        JList entryList = (JList) e.getSource();
        selectedEntry = entryList.getSelectedIndex();
        ListEntry entry = (ListEntry)entryList.getSelectedValue();
        if (entry != null) {
            int id = entry.getId();
            Articolo art = db.getArticolo(id);
            System.out.println("selected articolo "+art.getTitolo());
            noleggioPanel.setArticolo(art);
        } else {
            //BrevevitaPanel.clear();
        }
    }
     
     
 }
    
}
