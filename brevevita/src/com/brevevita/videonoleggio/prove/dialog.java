/*
 * dialog.java
 *
 * Created on 10 marzo 2008, 22.25
 *
 *  aliCE (c) 2007-2008
 *  Michele Piunti
 */

package com.brevevita.videonoleggio.prove;

import javax.swing.JOptionPane;
import javax.swing.JFrame;

/**
 *
 * @author michelepiunti
 */
public class dialog {
    
    /** Creates a new instance of dialog */
    public dialog() {
    }
    public static void main(String[] args) {
        
        int n = JOptionPane.showConfirmDialog(
            new JFrame(),
            "Would you like green eggs and ham?",
            "An Inane Question",
            JOptionPane.YES_NO_OPTION);

        if(n == JOptionPane.YES_OPTION)
          // procedura da eseguire in caso affermativo
            System.out.println("YES" + n);
          else if(n == JOptionPane.NO_OPTION)
            // procedura da eseguire in caso negativo
               System.out.println("NO OPTION" + n);
           // else;

    }

}
