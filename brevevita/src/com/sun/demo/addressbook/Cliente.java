/*
 * Cliente.java
 *
 * Created on 5 gennaio 2008, 14.27
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.sun.demo.addressbook;

/**
 *
 * @author Michele Piunti
 */
public class Cliente {
    
    /**
     * Creates a new instance of Cliente
     */
    public Cliente() {
        this.id=-1;
        this.credito = 0.0;
    }
    
    public Cliente(String codicecl, String lastName, String firstName, String middleName) {
        this(codicecl, lastName, firstName, middleName, 0.0, null, null, null, null, 
                null, null, null);
    }
    
    public Cliente(String codicecl, String lastName, String firstName, String middleName,
            Double credito, String phone, String email, String address1, String address2,
            String city, String state, String postalCode, int id) {
        
        this(codicecl, lastName, firstName, middleName, credito, phone, email, address1, address2,
                city, state, postalCode, "ITA", id);
    }
        
    public Cliente(String codicecl, String lastName, String firstName, String middleName,
            Double credito, String phone, String email, String address1, String address2,
            String city, String state, String postalCode) {
        
        this(codicecl, lastName, firstName, middleName, credito, phone, email, address1, address2,
                city, state, postalCode, "ITA", -1);
    }
    
    public Cliente(String cod, String lastName, String firstName, String middleName,
            Double credito, String phone, String email, String address1, String address2,
            String city, String state, String postalCode, String country, int id) {
        this.codicecl = cod;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.credito = credito;
        this.phone = phone;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.id = id;
    }
    
    public void setCodiceCl(String codcl) {
        this.codicecl = codcl;
    }
    
    public String getCodiceCl() {
        return codicecl;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public String getMiddleName() {
        return middleName;
    }
    
    public void setCredito(Double ricarica) {
        this.credito = ricarica;
    }
    
    public void setCredito(double nc) {
        this.credito = nc;
    }
    
    public Double getCredito() {
        return credito;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setEmail(String email) {
        this.email = email;
        
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    /**
     * Getter for property address1.
     * @return Value of property address1.
     */
    public String getAddress1() {
        return this.address1;
    }

    /**
     * Setter for property address1.
     * @param address1 New value of property address1.
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * Getter for property address2.
     * @return Value of property address2.
     */
    public String getAddress2() {
        return this.address2;
    }

    /**
     * Setter for property address2.
     * @param address2 New value of property address2.
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * Getter for property city.
     * @return Value of property city.
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Setter for property city.
     * @param city New value of property city.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter for property state.
     * @return Value of property state.
     */
    public String getState() {
        return this.state;
    }

    /**
     * Setter for property state.
     * @param state New value of property state.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter for property postalCode.
     * @return Value of property postalCode.
     */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * Setter for property postalCode.
     * @param postalCode New value of property postalCode.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Getter for property country.
     * @return Value of property country.
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * Setter for property country.
     * @param country New value of property country.
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getString() {
     String ret ="";
     ret += firstName+" "+lastName+", "+city;
     return ret;
    }
    
    public int hashCode() {
        int value = 1;
        value = value*PRIMENO + (lastName == null ? 0 : lastName.hashCode());
        value = value*PRIMENO + (middleName == null ? 0 : middleName.hashCode());
        value = value*PRIMENO + (firstName == null ? 0 : firstName.hashCode());
        value = value*PRIMENO + (phone == null ? 0 : phone.hashCode());
        value = value*PRIMENO + (email == null ? 0 : email.hashCode());
        value = value*PRIMENO + (address1 == null ? 0 : address1.hashCode());
        value = value*PRIMENO + (address2 == null ? 0 : address2.hashCode());
        value = value*PRIMENO + (city == null ? 0 : city.hashCode());
        value = value*PRIMENO + (state == null ? 0 : state.hashCode());
        value = value*PRIMENO + (postalCode == null ? 0 : postalCode.hashCode());
        value = value*PRIMENO + (country == null ? 0 : country.hashCode());
        
        // don't use the id since this is generated by db
        
        return value;
    }
    
    
    public boolean equals(Object other) {
        boolean bEqual = false;
        if (this == other) {
            bEqual = true;
        } else if (other instanceof Cliente) {
            Cliente someome = (Cliente) other;
            if ((lastName == null ? someome.lastName == null : lastName.equalsIgnoreCase(someome.lastName)) &&
                    (firstName == null ? someome.firstName == null : firstName.equalsIgnoreCase(someome.firstName)) && 
                    (middleName == null ? someome.middleName == null : middleName.equalsIgnoreCase(someome.middleName)) &&
                    (phone == null ? someome.phone == null : phone.equalsIgnoreCase(someome.phone)) &&
                    (email == null ? someome.email == null : email.equalsIgnoreCase(someome.email)) &&
                    (address1 == null ? someome.address1 == null : address1.equalsIgnoreCase(someome.address1)) &&
                    (address2 == null ? someome.address2 == null : address2.equalsIgnoreCase(someome.address2)) &&
                    (city == null ? someome.city == null : city.equalsIgnoreCase(someome.city)) &&
                    (state == null ? someome.state == null : state.equalsIgnoreCase(someome.state)) &&
                    (postalCode == null ? someome.postalCode == null : postalCode.equalsIgnoreCase(someome.postalCode)) &&
                    (country == null ? someome.country == null : country.equalsIgnoreCase(someome.country))) {
                // don't use id in determining equality
                
                bEqual = true;
            }
        }
        
        return bEqual;
    }
   
    
    private String codicecl;
    private String lastName;
    private String firstName;
    private String middleName;
    private Double credito;
    private String phone;
    private String email;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private int id;
    
    private static final int PRIMENO = 37;
    
}
