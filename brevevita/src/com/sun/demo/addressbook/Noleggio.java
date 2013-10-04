/*
 * Noleggio.java
 *
 * Created on 24 febbraio 2008, 17.58
 *
 *  aliCE (c) 2007-2008
 *  Michele Piunti
 */

package com.sun.demo.addressbook;

import java.sql.Date;

/**
 *
 * @author michelepiunti
 */
public class Noleggio {
    
    /** Creates a new instance of Noleggio */
    public Noleggio() {
    }
    
    public Noleggio(int idcl, int idart, Date d1, Date d2,
            String not, String eu, String in1, String in2, int id) {
        this.idCL = idcl;
        this.idART = idart;
        this.Data1 = d1;
        this.Data2 = d2;
        this.prezzo = eu;
        this.note = not;
        this.info1 = in1;
        this.info2 = in2;

        this.id = id;
    }
    
    public void setClId(int id) {
        this.idCL = id;
    }
    
    public int getClId() {
        return idCL;
    }
    
    public void setNote(String s){
        note=s;
    }
    
    public String getNote(){
        return note;
    }
    
    public void  setPrezzo(String s){
        prezzo=s;
    }
    public String getPrezzo(){
        return prezzo;
    }
    
    public void    setInfo1(String s){
        info1=s;
    }
    
    public String getInfo1(){
        return info1;
    }
    
    public void  setInfo2(String s){
        info2=s;
    }
    
    public String getInfo2(){
        return info2;
    }
    
    public void setArId(int id) {
        this.idART = id;
    }
    
    public int getArId() {
        return idART;
    }
    
    public void setD1(Date data) {
        this.Data1 = data;
    }
    
    public Date getD1() {
        return Data1;
    }
    
    public void setD2(Date data) {
        this.Data2 = data;
    }
    
    public Date getD2() {
        return Data2;
    }
    
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public String toString(){
        String ret = "ID cliente: " + idCL +
                "\nID articolo: " + idART +
                "\n\nDATA INIZIO: " + Data1 +
                "\nDATA RICONSEGNA: " + Data2 +
                "\n   note:\n" + note + "\n" + info1 + "\n" +info2;
        return ret;
    }
    
    private int idCL;
    private int idART;
    private Date Data1;
    private Date Data2;
    private String prezzo;
    private String note;
    private String info1;
    private String info2;
    private int id;
    
    private static final int PRIMENO = 37;
    
}
