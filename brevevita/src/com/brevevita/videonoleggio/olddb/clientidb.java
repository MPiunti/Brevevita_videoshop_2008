/*
 * clientidb.java
 *
 * Created on 5 gennaio 2008, 13.39
 */

package com.brevevita.videonoleggio.olddb;


import com.sun.demo.addressbook.*;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @date Jan 2008
 * @author Michele Piunti
 */
public class clientidb {
    
    private Connection dbConnection;
    private Properties dbProperties;
    private boolean isConnected;
    private String dbName;
    // statement clienti
    private PreparedStatement stmtSaveNewCliente;
    private PreparedStatement stmtUpdateExistingCliente;
    private PreparedStatement stmtGetListClienti;    
    private PreparedStatement stmtGetCliente;
    private PreparedStatement stmtDeleteCliente;
    // statement articoli
    private PreparedStatement stmtSaveNewArticolo;
    private PreparedStatement stmtUpdateExistingArticolo;
    private PreparedStatement stmtGetListArticoli;   
    private PreparedStatement stmtGetArticolo;
    private PreparedStatement stmtDeleteArticolo;
    
    /** Creates a new instance of BrevevitaDB */
    public clientidb() {
        this("provaBrevevitaDB");
    }
    
    public clientidb(String DBName) {
        this.dbName = DBName;
        
        setDBSystemDir();
        dbProperties = loadDBProperties();
        String driverName = dbProperties.getProperty("derby.driver"); 
        loadDatabaseDriver(driverName);
        if(!dbExists()) {
            createDatabase();
        }
        
    }
    
    private boolean dbExists() {
        boolean bExists = false;
        String dbLocation = getDatabaseLocation();
        File dbFileDir = new File(dbLocation);
        if (dbFileDir.exists()) {
            bExists = true;
        }
        return bExists;
    }
    
    private void setDBSystemDir() {
        // decide on the db system directory
        //String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = /*userHomeDir + */"./.brevevita";
        System.setProperty("derby.system.home", systemDir);
        
        // create the db system directory
        File fileSystemDir = new File(systemDir);
        fileSystemDir.mkdir();
    }
    
    private void loadDatabaseDriver(String driverName) {
        // load Derby driver
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
    }
    
    private Properties loadDBProperties() {
        InputStream dbPropInputStream = null;
        dbPropInputStream = clientidb.class.getResourceAsStream("Configuration.properties");
        dbProperties = new Properties();
        try {
            dbProperties.load(dbPropInputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return dbProperties;
    }
    
    
    private boolean createTables(Connection dbConnection) {
        boolean bCreatedTables = false;
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.execute(strCreateCLIENTITable);
            statement.execute(strCreateARTICOLITable);
            //statement.execute(strCreateNOLEGGITable);
            bCreatedTables = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return bCreatedTables;
    }
    
    private boolean createDatabase() {
        boolean bCreated = false;
        Connection dbConnection = null;
        
        String dbUrl = getDatabaseUrl();
        dbProperties.put("create", "true");
        
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
            bCreated = createTables(dbConnection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        dbProperties.remove("create");
        return bCreated;
    }    
    
    public boolean connect() {
        String dbUrl = getDatabaseUrl();
        try {
            dbConnection = DriverManager.getConnection(dbUrl, dbProperties);
            
            stmtSaveNewCliente = dbConnection.prepareStatement(strSaveCliente, Statement.RETURN_GENERATED_KEYS);
            stmtUpdateExistingCliente = dbConnection.prepareStatement(strUpdateCliente);            
            stmtGetCliente = dbConnection.prepareStatement(strGetCliente);
            stmtDeleteCliente = dbConnection.prepareStatement(strDeleteCliente);
            
            stmtSaveNewArticolo = dbConnection.prepareStatement(strSaveArticolo, Statement.RETURN_GENERATED_KEYS);
            stmtUpdateExistingArticolo = dbConnection.prepareStatement(strUpdateArticolo);            
            stmtGetArticolo = dbConnection.prepareStatement(strGetArticolo);
            stmtDeleteArticolo = dbConnection.prepareStatement(strDeleteArticolo);
            
            isConnected = dbConnection != null;
        } catch (SQLException ex) {
            System.out.println("GRAVE connect()!!! Errore di connessione al db!!!!");
            ex.printStackTrace();
            isConnected = false;
        }
        return isConnected;
    }
    
    private String getHomeDir() {
        return System.getProperty(".");
    }
    
    public void disconnect() {
        if(isConnected) {
            String dbUrl = getDatabaseUrl();
            dbProperties.put("shutdown", "true");
            try {
                DriverManager.getConnection(dbUrl, dbProperties);
            } catch (SQLException ex) {
            }
            isConnected = false;
        }
    }
    
    public String getDatabaseLocation() {
        String dbLocation = System.getProperty("derby.system.home") + "/" + dbName;
        return dbLocation;
    }
    
    public String getDatabaseUrl() {
        String dbUrl = dbProperties.getProperty("derby.url") + dbName;
        return dbUrl;
    }
    
    
    // save new item    
    
    public int saveCliente(Cliente record) {
        int id = -1;
        try {
            stmtSaveNewCliente.clearParameters();
            
            stmtSaveNewCliente.setString(1, record.getCodiceCl());
            stmtSaveNewCliente.setString(2, record.getLastName());
            stmtSaveNewCliente.setString(3, record.getFirstName());
            stmtSaveNewCliente.setString(4, record.getMiddleName());
            stmtSaveNewCliente.setString(5, record.getCredito().toString());
            stmtSaveNewCliente.setString(6, record.getPhone());
            stmtSaveNewCliente.setString(7, record.getEmail());
            stmtSaveNewCliente.setString(8, record.getAddress1());
            stmtSaveNewCliente.setString(9, record.getAddress2());
            stmtSaveNewCliente.setString(10, record.getCity());
            stmtSaveNewCliente.setString(11, record.getState());
            stmtSaveNewCliente.setString(12, record.getPostalCode());
            stmtSaveNewCliente.setString(13, record.getCountry());
            int rowCount = stmtSaveNewCliente.executeUpdate();
            ResultSet results = stmtSaveNewCliente.getGeneratedKeys();
            if (results.next()) {
                id = results.getInt(1);
            }
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return id;
    }
    
    public int saveArticolo(Articolo record) {
        int id = -1;
        try {
            stmtSaveNewArticolo.clearParameters();
            
            stmtSaveNewArticolo.setString(1, record.getCodice());
            stmtSaveNewArticolo.setString(2, record.getTitolo());
            stmtSaveNewArticolo.setString(3, record.getRegista());
            stmtSaveNewArticolo.setString(4, record.getAttori());
            stmtSaveNewArticolo.setString(5, record.getAnno());
            stmtSaveNewArticolo.setString(6, record.getDescrizione());
            stmtSaveNewArticolo.setString(7, record.getPrezzo());
            stmtSaveNewArticolo.setString(8, record.getSupporto());
            stmtSaveNewArticolo.setString(9, record.getInfo1());
            stmtSaveNewArticolo.setString(10, record.getInfo2());

            int rowCount = stmtSaveNewArticolo.executeUpdate();
            ResultSet results = stmtSaveNewArticolo.getGeneratedKeys();
            if (results.next()) {
                id = results.getInt(1);
            }
            
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return id;
    }
    
    //  edit update item
    
    public boolean editCliente(Cliente record) {
        boolean bEdited = false;
        try {
            stmtUpdateExistingCliente.clearParameters();
            
            stmtUpdateExistingCliente.setString(1, record.getCodiceCl());
            stmtUpdateExistingCliente.setString(2, record.getLastName());
            stmtUpdateExistingCliente.setString(3, record.getFirstName());
            stmtUpdateExistingCliente.setString(4, record.getMiddleName());
            stmtUpdateExistingCliente.setString(5, record.getCredito().toString());
            
            stmtUpdateExistingCliente.setString(6, record.getPhone());
            stmtUpdateExistingCliente.setString(7, record.getEmail());
            stmtUpdateExistingCliente.setString(8, record.getAddress1());
            stmtUpdateExistingCliente.setString(9, record.getAddress2());
            stmtUpdateExistingCliente.setString(10, record.getCity());
            stmtUpdateExistingCliente.setString(11, record.getState());
            stmtUpdateExistingCliente.setString(12, record.getPostalCode());
            stmtUpdateExistingCliente.setString(13, record.getCountry());
            stmtUpdateExistingCliente.setInt(14, record.getId());
            stmtUpdateExistingCliente.executeUpdate();
            bEdited = true;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return bEdited;
        
    }
    
    public boolean editArticolo(Articolo record) {
        boolean bEdited = false;
        try {
            stmtUpdateExistingArticolo.clearParameters();
            
            stmtUpdateExistingArticolo.setString(1, record.getCodice());
            stmtUpdateExistingArticolo.setString(2, record.getTitolo());
            stmtUpdateExistingArticolo.setString(3, record.getRegista());
            stmtUpdateExistingArticolo.setString(4, record.getAttori());
            stmtUpdateExistingArticolo.setString(5, record.getAnno());
            stmtUpdateExistingArticolo.setString(6, record.getDescrizione());
            stmtUpdateExistingArticolo.setString(7, record.getPrezzo());
            stmtUpdateExistingArticolo.setString(8, record.getSupporto());
            stmtUpdateExistingArticolo.setString(9, record.getInfo1());
            stmtUpdateExistingArticolo.setString(10, record.getInfo2());
            
            stmtUpdateExistingCliente.setInt(11, record.getId()); 
            stmtUpdateExistingArticolo.executeUpdate();
            bEdited = true;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return bEdited;
        
    }
    
    // delete items
    
    public boolean deleteCliente(int id) {
        boolean bDeleted = false;
        try {
            stmtDeleteCliente.clearParameters();
            stmtDeleteCliente.setInt(1, id);
            stmtDeleteCliente.executeUpdate();
            bDeleted = true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return bDeleted;
    }
    
    public boolean deleteCliente(Cliente record) {
        int id = record.getId();
        return deleteCliente(id);
    }    
    
    public boolean deleteArticolo(int id) {
        boolean bDeleted = false;
        System.out.println("...deleting articolo with id: " + id);
        try {
            stmtDeleteArticolo.clearParameters();
            stmtDeleteArticolo.setInt(1, id);
            stmtDeleteArticolo.executeUpdate();
            bDeleted = true;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return bDeleted;
    }
    
    public boolean deleteArticolo(Articolo record) {
        int id = record.getId();
        return deleteArticolo(id);
    }  
    
    // lists
    
    public List<ListEntry> getListClienti() {
        List<ListEntry> listEntries = new ArrayList<ListEntry>();
        Statement queryStatement = null;
        ResultSet results = null;
        
        try {
            queryStatement = dbConnection.createStatement();
            results = queryStatement.executeQuery(strGetListClienti);
            while(results.next()) {
                int id = results.getInt(1);
                String lName = results.getString(2);
                String fName = results.getString(3);
                String mName = results.getString(4);
                
                ListEntry entry = new ListEntry(lName, fName, mName, id);
                listEntries.add(entry);
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            
        }
        
        return listEntries;
    }
    
        public List<ListEntry> getListArticoli() {
        List<ListEntry> listArticoli = new ArrayList<ListEntry>();
        Statement queryStatement = null;
        ResultSet results = null;
        
        try {
            queryStatement = dbConnection.createStatement();
            results = queryStatement.executeQuery(strGetListArticoli);
            while(results.next()) {
                int id = results.getInt(1);
                String titolo = results.getString(2);
                String anno = results.getString(3);                
                
                ListEntry entry = new ListEntry(titolo, anno, null, id);
                listArticoli.add(entry);
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            
        }
        
        return listArticoli;
    }
    
    // get item
    
    public Cliente getCliente(int index) {
        Cliente cl = null;
        try {
            stmtGetCliente.clearParameters();
            stmtGetCliente.setInt(1, index);
            ResultSet result = stmtGetCliente.executeQuery();
            if (result.next()) {
                String codicecl = result.getString("CODICECL");
                String lastName = result.getString("LASTNAME");
                String firstName = result.getString("FIRSTNAME");
                String middleName = result.getString("MIDDLENAME");
                Double credito = new Double(result.getString("CREDITO"));
                String phone = result.getString("PHONE");
                String email = result.getString("EMAIL");
                String add1 = result.getString("ADDRESS1");
                String add2 = result.getString("ADDRESS2");
                String city = result.getString("CITY");
                String state = result.getString("STATE");
                String postalCode = result.getString("POSTALCODE");
                String country = result.getString("COUNTRY");
                int id = result.getInt("ID");
                cl = new Cliente(codicecl, lastName, firstName, middleName, credito, phone,
                        email, add1, add2, city, state, postalCode,
                        country, id);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return cl;
    }
    
    
    public Articolo getArticolo(int index) {
        Articolo art = null;
        try {
            stmtGetArticolo.clearParameters();
            stmtGetArticolo.setInt(1, index);
            ResultSet result = stmtGetArticolo.executeQuery();

            if (result.next()) {
                String codice = result.getString("CODICE");
                String titolo = result.getString("TITOLO");
                String regista = result.getString("REGISTA");
                String attori = result.getString("ATTORI");
                String anno = result.getString("ANNO");
                String descrizione = result.getString("DESCRIZIONE");
                String prezzo = result.getString("PREZZO");
                String supporto = result.getString("SUPPORTO");
                String info1 = result.getString("INFO1");
                String info2 = result.getString("INFO2");

                int id = result.getInt("ID");
                art = new Articolo(codice,titolo,regista,attori,anno,descrizione,prezzo,supporto,info1,info2, id);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return art;
    }
    
    public static void main(String[] args) {
        clientidb db = new clientidb();
        System.out.println(db.getDatabaseLocation());
        System.out.println(db.getDatabaseUrl());
        db.connect();
        db.disconnect();
    }
    
    

    
    private static final String strCreateCLIENTITable =
            "create table APP.CLIENTI (" +
            "    ID          INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "    CODICECL    VARCHAR(30), " +
            "    LASTNAME    VARCHAR(30), " +
            "    FIRSTNAME   VARCHAR(30), " +
            "    MIDDLENAME  VARCHAR(30), " +
            "    CREDITO     VARCHAR(30), " +
            "    PHONE       VARCHAR(20), " +
            "    EMAIL       VARCHAR(30), " +
            "    ADDRESS1    VARCHAR(30), " +
            "    ADDRESS2    VARCHAR(30), " +
            "    CITY        VARCHAR(30), " +
            "    STATE       VARCHAR(30), " +
            "    POSTALCODE  VARCHAR(20), " +
            "    COUNTRY     VARCHAR(60) " +   // documento 
            ")";
    
    private static final String strCreateARTICOLITable =
            "create table APP.ARTICOLI (" +
            "    ID          INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "    CODICE      VARCHAR(30), " +
            "    TITOLO      VARCHAR(60), " +
            "    REGISTA     VARCHAR(50), " +
            "    ATTORI      VARCHAR(150), " +
            "    ANNO        VARCHAR(5), " +
            "    DESCRIZIONE VARCHAR(200), " +
            "    PREZZO      VARCHAR(30), " +
            "    SUPPORTO    VARCHAR(10), " +
            "    INFO1       VARCHAR(30), " +
            "    INFO2       VARCHAR(30) " +
            ")";
    
    //
    
    private static final String strGetCliente =
            "SELECT * FROM APP.CLIENTI " +
            "WHERE ID = ?";
    
    private static final String strGetArticolo =
            "SELECT * FROM APP.ARTICOLI " +
            "WHERE ID = ?";
    
    //
    
    private static final String strSaveCliente =
            "INSERT INTO APP.CLIENTI " +
            "   (CODICECL, LASTNAME, FIRSTNAME, MIDDLENAME, CREDITO, PHONE, EMAIL, ADDRESS1, ADDRESS2, " +
            "    CITY, STATE, POSTALCODE, COUNTRY) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String strSaveArticolo =
            "INSERT INTO APP.ARTICOLI " +
            "   (CODICE, TITOLO, REGISTA, ATTORI, ANNO, DESCRIZIONE, PREZZO, SUPPORTO, " +
            "   INFO1, INFO2 ) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    //
    
    private static final String strUpdateCreditoCliente =
            "UPDATE APP.CLIENTI "+
            "SET CREDITO = ? " +
            "WHERE ID = ?";
    
    //
    
    private static final String strGetListClienti =
            "SELECT ID, LASTNAME, FIRSTNAME, MIDDLENAME FROM APP.CLIENTI "  +
            "ORDER BY LASTNAME ASC";
    
    private static final String strGetListArticoli =
            "SELECT ID, TITOLO, ANNO FROM APP.ARTICOLI "  +
            "ORDER BY TITOLO ASC";

    //
                
    private static final String strUpdateCliente =
            "UPDATE APP.CLIENTI " +
            "SET CODICECL = ?," +
            "    LASTNAME = ?, " +
            "    FIRSTNAME = ?, " +
            "    MIDDLENAME = ?, " +
            "    CREDITO = ?," +
            "    PHONE = ?, " +
            "    EMAIL = ?, " +
            "    ADDRESS1 = ?, " +
            "    ADDRESS2 = ?, " +
            "    CITY = ?, " +
            "    STATE = ?, " +
            "    POSTALCODE = ?, " +
            "    COUNTRY = ? " +
            "WHERE ID = ?";
    
    private static final String strUpdateArticolo =
            "UPDATE APP.ARTICOLI " +
            "SET CODICE = ?," +
            "    TITOLO = ?, " +
            "    REGISTA = ?, " +
            "    ATTORI = ?, " +
            "    ANNO = ?," +
            "    DESCRIZIONE = ?, " +
            "    PREZZO = ?, " +
            "    SUPPORTO = ?, " +
            "    INFO1 = ?, " +
            "    INFO2 = ? " +
            "WHERE ID = ?";
    
    //
    
    private static final String strDeleteCliente =
            "DELETE FROM APP.CLIENTI " +
            "WHERE ID = ?";
    
    private static final String strDeleteArticolo =
            "DELETE FROM APP.ARTICOLI " +
            "WHERE ID = ?";
    
}
