/*
 * MainFrame.java
 *
 * Created on 9 febbraio 2008, 15.27
 */

package com.brevevita.videonoleggio.gui;


import com.brevevita.videonoleggio.db.bvdb;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javax.swing.JTabbedPane;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author  Michele Piunti
 */
public class MainFrame extends javax.swing.JFrame  {
    
    JTabbedPane tabbedPane;
    ClientiApp appCl;
    ArticoliApp appAr;
    NoleggiApp appNol;
    ChiudiNoloApp appClNol;
    TariffeApp appTariffe;
    
    /** Creates new form AddressFrame */
    public MainFrame() {
        db = new bvdb();
        db.connect();
 
        initComponents();
        loadFrameIcon();
        windowAdapter = new WindowCloser();
        this.addWindowListener(windowAdapter);
        
    }
    
   
    /**
     * Load our own "address book" icon into our frame window.
     */
    private void loadFrameIcon() {
        URL imgUrl = null;
        ImageIcon imgIcon = null;
        
        imgUrl = MainFrame.class.getResource("./resources/brevevita.gif");
        imgIcon = new ImageIcon(imgUrl);
        Image img = imgIcon.getImage();
        this.setIconImage(img);
        
    }    
    
                        
    private void initComponents() {
        appCl = new ClientiApp(db);
        appAr = new ArticoliApp(db); 
        appNol = new NoleggiApp(db); 
        appClNol = new ChiudiNoloApp(db);
        appTariffe = new TariffeApp(db);
        this.setTitle("Brevevita Videonoleggio");
        setSize(1000,600);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("CLIENTI", null, appCl, "Anagrafica e Credito Clienti " );
        tabbedPane.addTab("VIDEO", null, appAr, "Archivio Video" );
        tabbedPane.addTab("NUOVO NOLEGGIO", null, appNol, "Inizio Noleggio");
        tabbedPane.addTab("CHIUDI NOLEGGIO", null, appClNol, "Chiudi Noleggio");
        tabbedPane.addTab("TARIFFE", null, appTariffe, "Modifica Tariffe Noleggi");

        windowAdapter = new WindowCloser();
        this.addWindowListener(windowAdapter);        
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE); 
        this.setVisible(true);
        this.add(tabbedPane);

        //pack();
    }//                         

    

    public static void main(String[] args) {
        instance = new MainFrame();
        instance.setVisible(true);
    }
    


    public static MainFrame getInstance(){
        return instance;
    }
    
    private static MainFrame instance;
   
    private bvdb db;
    private WindowAdapter windowAdapter;
    
    class WindowCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            db.disconnect();
            System.out.println("DB disconnected, hallo!! \n\n Brevevita.com \n\n");
        }
    
    }
}
