/*
 * ClientiFrame.java
 *
 * Created on 5 gennaio 2008, 15.27
 *  @author MPiunti
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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author  Michele Piunti
 */
public class ClientiApp 
        extends javax.swing.JPanel 
        implements ActionListener, ListSelectionListener {
    
    /** Creates new form AddressFrame */
    public ClientiApp(bvdb DB) {
        this.db = DB;
        initComponents();

        addressActionPanel.addActionListener(this);
        BrevevitaPanel.addActionListener(this);
        BrevevitaPanel.setEditable(false);
        List<ListEntry> entries = db.getListClienti();
        addressListPanel.addListEntries(entries);
        addressListPanel.addListSelectionListener(this);
    }
    
                            
    private void initComponents() {
        this.setLayout(new BorderLayout());
        addressActionPanel = new com.sun.demo.addressbook.AddressActionPanel();
        BrevevitaPanel = new com.sun.demo.addressbook.ClientePanel(); 
        addressListPanel = new com.sun.demo.addressbook.AddressListPanel();
        
        centralPane = new JPanel(new BorderLayout());
        
        nol = new BrevevitaTable(db);
        nol.setOpaque(true); //content panes must be opaque
        
        centralPane.add(BrevevitaPanel, java.awt.BorderLayout.CENTER);
        centralPane.add(nol, java.awt.BorderLayout.SOUTH);

        this.add(addressActionPanel, java.awt.BorderLayout.SOUTH);

        this.add(centralPane, java.awt.BorderLayout.CENTER);

        this.add(addressListPanel, java.awt.BorderLayout.WEST);
    
    }//       
    
    private void viewClienti(){
        System.out.println("View Clienti");
        //nol.updateModelCl(BrevevitaPanel.getCliente().getId());
        JFrame frame = new JFrame("Lista Clienti Brevevita");
        frame.setSize(800,600);
        final BrevevitaTable tab = new BrevevitaTable(db);
        //Create and set up the content pane.
        tab.setModelClienti();
        tab.setOpaque(true); //content panes must be opaque
        JButton but = new JButton("Stampa");
        but.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {    
                
                System.out.println("printing tab clienti");
                tab.print();
            }
        });
        frame.getContentPane().add(tab, BorderLayout.CENTER);
        frame.getContentPane().add(but, BorderLayout.SOUTH);

        //Display the window.
        //frame.pack();
        frame.setVisible(true);
        
    }

    
//    private void cancelCliente() {
//        BrevevitaPanel.clear();
//        BrevevitaPanel.setEditable(false);
//        ListEntry entry = addressListPanel.getSelectedListEntry();
//        if (entry != null) {
//            int id = entry.getId();
//            Cliente cliente = db.getCliente(id);
//            BrevevitaPanel.setCliente(cliente);
//        }
//        
//        
//    }
    
    private void newCliente(){
        BrevevitaPanel.clear();
        BrevevitaPanel.setEditable(true);
        
    }
    
    private void deleteCliente() {
        Cliente cl = BrevevitaPanel.getCliente();
        int n = JOptionPane.showConfirmDialog(
                new JFrame(),
                "Vuoi davvero CANCELLARE il Cliente:\n "+
                    cl.getFirstName() + " " + cl.getLastName() + " ?" +
                "\n\nse SI l'operazione non e' reversibile..",
                "Warning!",
                JOptionPane.YES_NO_OPTION);

        if(n == JOptionPane.YES_OPTION){

            int id = BrevevitaPanel.getId();
            if (id != -1) {
                db.deleteCliente(id);
                int selectedIndex = addressListPanel.deleteSelectedEntry();
                if (selectedIndex >= 0) {
                    selectedIndex = addressListPanel.setSelectedIndex(selectedIndex);
                    ListEntry entry = addressListPanel.getSelectedListEntry();
                    if (entry != null) {
                        id = entry.getId();
                        Cliente cliente = db.getCliente(id);
                        BrevevitaPanel.setCliente(cliente);
                    } else {
                        BrevevitaPanel.clear();
                    }
                }
            } else {
                BrevevitaPanel.clear();
            }
            BrevevitaPanel.setEditable(false);
        }
    }
    
    private void editCliente() {
        int selected = addressListPanel.getSelectedIndex();
        if (selected >=0) {
            BrevevitaPanel.setEditable(true);
        }
        
    }
    
   private void updateCredito(double cr) {
        int selected = BrevevitaPanel.getCliente().getId();
        if (selected >=0) {
            
            double updated = db.updateCreditoCliente(selected, cr);
            BrevevitaPanel.updateCredito(updated);            
        }       
    }
    
    private void saveCliente() {
        if (BrevevitaPanel.isEditable()) {
            Cliente cliente = BrevevitaPanel.getCliente();
            if(cliente!=null){
                int id = cliente.getId();
                    String lname = cliente.getLastName();
                    String fname = cliente.getFirstName();
                    String mname = cliente.getMiddleName();
                if (id == -1) {
                    id = db.saveCliente(cliente);
                    cliente.setId(id);

                } else {
                    db.editCliente(cliente);
                }
                updateList();
                BrevevitaPanel.setCliente(cliente);
                addressListPanel.setSelectionWithID(cliente.getId());
                BrevevitaPanel.setEditable(false);
             }//cliente != null   
        }
    }
    
    protected void paintComponent(java.awt.Graphics g) {
        System.out.println("clientiApp RRREEEPPPAAAIIINNNTTT");

        try{
            int id = BrevevitaPanel.getCliente().getId();
            if((id>=0))
               BrevevitaPanel.setCliente(db.getCliente(id));
        }catch(Exception e) {
            
        }
        
    }
    
    public void updateList(){
        List<ListEntry> clienti = db.getListClienti();
        addressListPanel.clear();
        addressListPanel.addListEntries(clienti);
    }
    
    
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("ActionEvent: " + actionCommand);
        if (actionCommand.equalsIgnoreCase("GET_LIST")) {
            viewClienti();
        } else if (actionCommand.equalsIgnoreCase("NEW_CLIENTE")) {
            newCliente();
        } else if (actionCommand.equalsIgnoreCase("CANCEL_CLIENTE")) {
            //deleteCliente();
        } else if (actionCommand.equalsIgnoreCase("EDIT_CLIENTE")) {
            editCliente();
        } else if (actionCommand.equalsIgnoreCase("SAVE_CLIENTE")) {
            saveCliente();
        } else if (actionCommand.equalsIgnoreCase("1_CREDITO")) {
            updateCredito(1.0);
        } else if (actionCommand.equalsIgnoreCase("5_CREDITO")) {
            updateCredito(5.0);
        }
    }


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
            BrevevitaPanel.setCliente(cliente);
            nol.updateModelCl(id);
        } else {
            BrevevitaPanel.clear();
        }
    }
    
    
    // Variables declaration - do not modify                     
    private AddressActionPanel addressActionPanel;
    private AddressListPanel addressListPanel;
    private ClientePanel BrevevitaPanel;
    private BrevevitaTable nol;
    private JPanel centralPane;

    // End of variables declaration                   
    
    private int selectedEntry = -1;
    private static bvdb db;

}
