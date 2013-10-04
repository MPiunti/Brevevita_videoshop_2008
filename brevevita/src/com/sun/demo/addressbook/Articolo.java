/*
 * Articolo.java
 *
 * Created on 12 gennaio 2008, 18.32
 *
 */

package com.sun.demo.addressbook;

/**
 *
 * @author Michele
 */
public class Articolo {
    
    private int id; 
    private String codice,titolo,regista,attori,anno,descrizione,prezzo,supporto,info1,info2;       
    
    public Articolo() {
        
    }
    
    public Articolo(String cod, String tit, String reg, String att, String ann, 
            String descr, String prez, String supp) {
       this( cod,  tit,  reg,  att,  ann, descr,  prez,  supp, "", "", -1);
    }
    
   public Articolo(String cod, String tit, String reg, String att, String ann, 
            String descr, String prez, String supp, String info1, String info2) {
       this( cod,  tit,  reg,  att,  ann, descr,  prez,  supp, info1, info2, -1);
    }
    
    /** Creates a new instance of Articolo */
    public Articolo(String cod, String tit, String reg, String att, String ann, 
            String descr, String prez, String supp, String in1,String in2, int Id) {
        this.codice = cod;
        this.titolo = tit;
        this.regista = reg;
        this.attori = att;
        this.anno = ann;
        this.descrizione = descr;
        this.prezzo = prez;
        this.supporto = supp;
        this.info1= in1;
        this.info2= in2;
        this.id = Id;        
    
    }
    
    public int getId(){
        return id;
    }
    
    public void setId(int n){
        id=n;
    }
    
    public String getCodice() {
        return codice;
    }
    
    public void setCodice(String sss) {
        this.codice = sss;
    }
    
    public String getTitolo() {
        return titolo;
    }
    
    public void setTitolo(String sss) {
        this.titolo = sss;
    }
    
    public String getRegista() {
        return regista;
    }
    
    public void setRegista(String sss) {
        this.regista = sss;
    }
    
    public String getAttori() {
        return attori;
    }
    
    public void setAttori(String sss) {
        this.attori = sss;
    }
    
    public String getAnno() {
        return anno;
    }
    
    public void setAnno(String sss) {
        this.anno = sss;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String sss) {
        this.descrizione = sss;
    }
    
    public String getPrezzo() {
        return prezzo;
    }
    
    public void setPrezzo(String sss) {
        this.prezzo = sss;
    }
    
    public String getSupporto() {
        return supporto;
    }
    
    public void setSupporto(String sss) {
        this.supporto = sss;
    }
     
    public String getInfo1() {
        return info1;
    }
    
    public void setInfo1(String sss) {
        this.info1 = sss;
    }
         
    public String getInfo2() {
        return info2;
    }
    
    public void setInfo2(String sss) {
        this.info2 = sss;
    }
    
    public String getString() {
     String ret ="";
     ret+= titolo + "("+anno+")"+" , "+supporto;
     return ret;
    }
    
}
