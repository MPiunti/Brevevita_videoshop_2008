/*
 * Main.java
 *
 * Created on 12 gennaio 2008, 22.47
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.sun.demo.addressbook;

import java.awt.Container;
import javax.swing.JPanel;
import com.brevevita.videonoleggio.db.bvdb;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Michele Piunti
 */
public class Main extends javax.swing.JFrame  {
    
    /** Creates a new instance of Main */
    public Main() {
    }
    
   public static void main(String[] args) {
        ArticoliFrame art = new ArticoliFrame();
        art.setVisible(false);
        ClientiFrame cl = new ClientiFrame(); 
        cl.setVisible(false);
        Container artC = art.getContentPane(); 
        Container clC = cl.getContentPane(); 
    }
    
}
