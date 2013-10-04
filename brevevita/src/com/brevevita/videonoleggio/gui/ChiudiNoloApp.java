/*
 * ChiudiNoloApp.java
 *
 * Created on 29 marzo 2008, 22.02
 *
 *  aliCE (c) 2007-2008
 *  Michele Piunti
 */

package com.brevevita.videonoleggio.gui;

import com.sun.demo.addressbook.*;
import com.brevevita.videonoleggio.util.DateOperations;

import com.brevevita.videonoleggio.db.bvdb;
import java.awt.Image;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.sql.Date;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author michelepiunti
 */
public class ChiudiNoloApp extends JPanel 
    implements ActionListener {
                    
    private JPanel centralPane;
    private ChiudiNoloPanel clNoloPanel;
    private BrevevitaTable nol;
                
    
    private int selectedEntry = -1;
    private static bvdb db;
    
    /** Creates a new instance of ChiudiNoloApp */
    public ChiudiNoloApp(bvdb DB) {
        this.db = DB;
        initComponents();

        clNoloPanel.addActionListener(this);
        nol.addMouseListener(new MouseEv());
    }
    
    private void initComponents() {
        this.setLayout(new BorderLayout());
        //centralPane = new JPanel(new BorderLayout());

        clNoloPanel = new ChiudiNoloPanel();

        nol = new BrevevitaTable(db);
        nol.setOpaque(true); //content panes must be opaque
        nol.updateModelIncl();
        
        this.add(clNoloPanel, java.awt.BorderLayout.EAST);
        this.add(nol, java.awt.BorderLayout.CENTER);
   
        //this.add(centralPane, java.awt.BorderLayout.CENTER);
        
    }// 
    
    private void chiudiNoleggio() {
 
        Noleggio nolo = clNoloPanel.getNoleggio(); 
        Double mora = (clNoloPanel.getMora());
        if(mora.doubleValue()>0){
            //System.out.println("updating credito: -" + mora);
            db.updateCreditoCliente(nolo.getClId(), (-mora) );
        }

        //System.out.println("closing -- " + nolo);
        db.chiudiNoleggio(nolo); 

        nol.updateModelIncl();
//        System.out.println(" credito di "+idCl+" has been updated to: " +res);
    }
    
    protected void paintComponent(java.awt.Graphics g) {
        System.out.println("chiudi nolo RRREEEPPPAAAIIINNNTTT");
        nol.updateModelIncl();
    }
    
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("ActionEvent: " + actionCommand);
        if (actionCommand.equalsIgnoreCase("CHIUDI_NOLO")) {
            chiudiNoleggio();
        } else if (actionCommand.equalsIgnoreCase("MORA")) {
            clNoloPanel.editMora();
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
            int idNolo = nol.getNoloID();
//            System.out.println(" - query DB idNOLO: "+ idNolo);
            Noleggio n = db.getNoleggio(idNolo);            
       
            Date start = n.getD1();
            Calendar c = Calendar.getInstance();
            Date end = new Date(c.getTimeInMillis());
            n.setD2(end);            
            
            long giorni_extra = DateOperations.giorniFra(start, end)-1;
            double mora = giorni_extra * db.tariffa2();
            clNoloPanel.setNoleggio(n, giorni_extra, mora, nol.getCampi() ); 
           
        }
    }
    
}
