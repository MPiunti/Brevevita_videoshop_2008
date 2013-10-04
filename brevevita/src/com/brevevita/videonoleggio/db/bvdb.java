/*
 * bvdb.java
 *
 * Created on 5 gennaio 2008, 13.39
 */

package com.brevevita.videonoleggio.db;


import com.sun.demo.addressbook.*;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
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
public class bvdb {
    
    private Connection dbConnection;
    private Properties dbProperties;
    private boolean isConnected;
    private String dbName;
    // statement clienti
    private PreparedStatement stmtSaveNewCliente;
    private PreparedStatement stmtUpdateExistingCliente;
    private PreparedStatement stmtUpdateCreditoCliente;
    private PreparedStatement stmtGetListClienti;    
    private PreparedStatement stmtGetCliente;    
    private PreparedStatement stmtGetClientIdFromName;
    private PreparedStatement stmtDeleteCliente;
    // statement articoli
    private PreparedStatement stmtSaveNewArticolo;
    private PreparedStatement stmtUpdateExistingArticolo;
    private PreparedStatement stmtGetListArticoli;   
    private PreparedStatement stmtGetArticolo;
    private PreparedStatement stmtGetArticoloIdFromTitle;
    private PreparedStatement stmtDeleteArticolo;
    // statement Noleggi
    private PreparedStatement stmtNewNoleggio;
    private PreparedStatement stmtCompletaNoleggio;
    private PreparedStatement stmtGetIncompletiNoleggi; 
    private PreparedStatement stmtGetNoleggio;
    private PreparedStatement stmtGetNoleggi4Cl;
    private PreparedStatement stmtGetNoleggiCliente;
    private PreparedStatement stmtGetNoleggiArticolo;
    private PreparedStatement stmtGetNoleggi4MovCl;
    // statement Tariffe
    private PreparedStatement stmtUpdateTariffe;
    private PreparedStatement stmtSaveTariffe;
    private PreparedStatement stmtGetTariffe;
    
    /** Creates a new instance of BrevevitaDB */
    public bvdb() {
        this("BVDB");
    }
    
    public bvdb(String DBName) {
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
        String systemDir = /*userHomeDir + */"./brevevitaDB";
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
        dbPropInputStream = bvdb.class.getResourceAsStream("Configuration.properties");
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
            statement.execute(strCreateNOLEGGITable);
            statement.execute(strCreateTARIFFETable);
            connect();
              saveTariffe(3.0, 1.5, 1.5);
              double[] tariffe = getTariffe();
              System.out.println("** tariffe are: " +  
                      tariffe[0] + "," +  tariffe[1] + "," +  tariffe[2] + " **");
            disconnect();
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
            stmtUpdateCreditoCliente = dbConnection.prepareStatement(strUpdateCreditoCliente);        
            stmtGetCliente = dbConnection.prepareStatement(strGetCliente);
            stmtGetClientIdFromName = dbConnection.prepareStatement(strGetClienteFromName);
            stmtDeleteCliente = dbConnection.prepareStatement(strDeleteCliente);
            
            stmtSaveNewArticolo = dbConnection.prepareStatement(strSaveArticolo, Statement.RETURN_GENERATED_KEYS);
            stmtUpdateExistingArticolo = dbConnection.prepareStatement(strUpdateArticolo);            
            stmtGetArticolo = dbConnection.prepareStatement(strGetArticolo);
            stmtGetArticoloIdFromTitle = dbConnection.prepareStatement(strGetArticoloFromTitle);
            stmtDeleteArticolo = dbConnection.prepareStatement(strDeleteArticolo);
            
            stmtNewNoleggio = dbConnection.prepareStatement(strSaveNoleggio, Statement.RETURN_GENERATED_KEYS);
            stmtCompletaNoleggio = dbConnection.prepareStatement(strUpdateDataEndNoleggio);
            stmtGetIncompletiNoleggi = dbConnection.prepareStatement(strGetNoleggiIncompleti);
            stmtGetNoleggiCliente = dbConnection.prepareStatement(strGetNoleggiCliente);
            stmtGetNoleggiArticolo = dbConnection.prepareStatement(strGetNoleggiArticolo);
            stmtGetNoleggio = dbConnection.prepareStatement(strGetNoleggio);
            stmtGetNoleggi4MovCl = dbConnection.prepareStatement(strGetNoleggi4MovCl);
            
            stmtUpdateTariffe = dbConnection.prepareStatement(strUpdateTariffe);
            stmtSaveTariffe = dbConnection.prepareStatement(strSaveTariffe, Statement.RETURN_GENERATED_KEYS);
            stmtGetTariffe = dbConnection.prepareStatement(strGetTariffe);
            
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
            stmtSaveNewCliente.setDouble(5, record.getCredito());
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
    
    public int saveNoleggio(Noleggio record) {
        int id = -1;
        try {
            stmtNewNoleggio.clearParameters();

            stmtNewNoleggio.setInt(1, record.getClId());
            stmtNewNoleggio.setInt(2, record.getArId());
            stmtNewNoleggio.setDate(3, record.getD1());
            stmtNewNoleggio.setDate(4, record.getD2());
            stmtNewNoleggio.setString(5, record.getNote());
            stmtNewNoleggio.setString(6, record.getPrezzo());
            stmtNewNoleggio.setString(7, record.getInfo1());
            stmtNewNoleggio.setString(8, record.getInfo2());

            int rowCount = stmtNewNoleggio.executeUpdate();
            ResultSet results = stmtNewNoleggio.getGeneratedKeys();
            if (results.next()) {
                // results ha una sola colonna, ID!!
                id = results.getInt(1);
                //System.out.println("new noleggio saved:\n " + results );
            }

        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return id;
    }
    
    public int saveTariffe(double d1, double d2, double d3) {
        int id = -1;
        try {
            stmtSaveTariffe.clearParameters();

            stmtSaveTariffe.setDouble(1, d1);
            stmtSaveTariffe.setDouble(2, d2);
            stmtSaveTariffe.setDouble(3, d3);
            
            int rowCount = stmtSaveTariffe.executeUpdate();
            ResultSet results = stmtSaveTariffe.getGeneratedKeys();
            if (results.next()) {
                // results ha una sola colonna, ID!!
                id = results.getInt(1);
                //System.out.println("new tariffe saved:\n " + results );
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
            stmtUpdateExistingCliente.setDouble(5, record.getCredito());
            
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
    
    public Double updateCreditoCliente(int id, double diff) {
        Double updated = -1.0;
        try {
            Cliente cl = getCliente(id);
            double creditod = (cl.getCredito()).doubleValue();
            updated = new Double(creditod+diff);
            stmtUpdateCreditoCliente.clearParameters();
            
            stmtUpdateCreditoCliente.setDouble(1, updated);
            stmtUpdateCreditoCliente.setInt(2, id);
            stmtUpdateCreditoCliente.executeUpdate();
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return updated;
    }
    
    public boolean chiudiNoleggio(Noleggio n) {
        boolean ret=false;
        try {            
            stmtCompletaNoleggio.clearParameters();            
            stmtCompletaNoleggio.setDate(1, n.getD2());
            stmtCompletaNoleggio.setInt(2, n.getId());
            stmtCompletaNoleggio.executeUpdate();
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        return ret;
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
            
            stmtUpdateExistingArticolo.setInt(11, record.getId()); 
            stmtUpdateExistingArticolo.executeUpdate();
            bEdited = true;
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        return bEdited;
        
    }
    
    public boolean updateTariffe(double d1, double d2, double d3) {
        boolean bEdited = false;
        try {
            stmtSaveTariffe.clearParameters();

            stmtUpdateTariffe.setDouble(1, d1);
            stmtUpdateTariffe.setDouble(2, d2);
            stmtUpdateTariffe.setDouble(3, d3);   
            
            stmtUpdateTariffe.setInt(4, 1);  
            int rowCount = stmtUpdateTariffe.executeUpdate();
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
                String mName = "";
                
                ListEntry entry = new ListEntry(lName, fName, "", id);
                listEntries.add(entry);
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            
        }
        
        return listEntries;
    }
    
    public BrevevitaTableModel getModelClienti() {
       BrevevitaTableModel model = new BrevevitaTableModel();
//ID ,CODICECL,LASTNAME,FIRSTNAME,MIDDLENAME,CREDITO,PHONE,EMAIL,ADDRESS1,ADDRESS2,CITY,STATE,POSTALCODE,COUNTRY  
       String strGetClienti =
            "SELECT CODICECL,LASTNAME, FIRSTNAME,CREDITO,ADDRESS1 FROM APP.CLIENTI "  +
            "ORDER BY LASTNAME ASC";
       String cod,ln,fn,mn,cr,add;
       
       model.addColumn("CODICE");
       model.addColumn("COGNOME");
       model.addColumn("NOME");
//       model.addColumn("CODICE");
       model.addColumn("CREDITO");
       model.addColumn("INDIRIZZO");
       Statement queryStatement = null;
       ResultSet results = null;        
        try {
            queryStatement = dbConnection.createStatement();
            results = queryStatement.executeQuery(strGetClienti);
            while(results.next()) {
                cod = results.getString(1);
                ln = results.getString(2);
                fn = results.getString(3);                                
                cr = results.getString(4); 
                add = results.getString(5);
                model.addRow(new Object[] {cod, ln, fn, cr, add});
            }            
        } catch (SQLException sqle) {
            sqle.printStackTrace();            
        }
       return model;
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
                
                ListEntry entry = new ListEntry(titolo, anno, "", id);
                listArticoli.add(entry);
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            
        }
        
        return listArticoli;
    }
    
   public BrevevitaTableModel getModelArticoli() {
       BrevevitaTableModel model = new BrevevitaTableModel();
//ID  , CODICE ,TITOLO , REGISTA ,ATTORI,ANNO ,  DESCRIZIONE ,PREZZO  ,SUPPORTO ,INFO1, INFO2
       String strGetArticoli =
            "SELECT CODICE, TITOLO, REGISTA, ANNO, SUPPORTO, INFO1 FROM APP.ARTICOLI "  +
            "ORDER BY TITOLO ASC";
       String cod,titolo,anno,reg,supp,naz;
       
       model.addColumn("CODICE");
       model.addColumn("TITOLO");
       model.addColumn("REGISTA");
       model.addColumn("ANNO");
       model.addColumn("NAZIONE");
       model.addColumn("SUPPORTO");
       Statement queryStatement = null;
       ResultSet results = null;        
        try {
            queryStatement = dbConnection.createStatement();
            results = queryStatement.executeQuery(strGetArticoli);
            while(results.next()) {
                cod = results.getString(1);
                titolo = results.getString(2);
                reg = results.getString(3); 
                anno = results.getString(4);                
                supp = results.getString(5); 
                naz = results.getString(6);
                
                model.addRow(new Object[] {cod, titolo, reg, anno, naz, supp});
            }            
        } catch (SQLException sqle) {
            sqle.printStackTrace();            
        }
       return model;
   }
        
    
    public List<ListEntry> getListNoleggi() {
        List<ListEntry> listArticoli = new ArrayList<ListEntry>();
        Statement queryStatement = null;
        ResultSet results = null;     
        try {
            queryStatement = dbConnection.createStatement();
            results = queryStatement.executeQuery(strGetListNoleggi);
            while(results.next()) {
                int id = results.getInt(1);
                int idCl = results.getInt(2);
                int idAr = results.getInt(3);                
                Date start = results.getDate(4);   
                Date end = results.getDate(5);

                System.err.println("\n"+getNomeClienteFromID(idCl)+" - "+getTitoloArticoloFromID(idAr)+ " - " +start+ " - "+end);
      
//                ListEntry entry = new ListEntry(""+idCl, ""+idAr, ""+start, ""+end);
//                listArticoli.add(entry);
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();          
        }
        
        return listArticoli;
    }
    
    public BrevevitaTableModel getListNoleggiParziali() {
        BrevevitaTableModel model = new BrevevitaTableModel();
        model.addColumn("ID");
        model.addColumn("Titolo");
        model.addColumn("Cliente");
        model.addColumn("Data Affitto");
        
        Statement queryStatement = null;
        ResultSet results = null;     
        try {
            queryStatement = dbConnection.createStatement();
            results = queryStatement.executeQuery(strGetNoleggiIncompleti);
            while(results.next()) {
                int id = results.getInt(1);
                int idCl = results.getInt(2);
                String nomeCL = getNomeClienteFromID(idCl);
                int idAr = results.getInt(3);       
                String  titoloAr = getTitoloArticoloFromID(idAr);
                Date start = results.getDate(4);   
                
                model.addRow(new Object[] {id, titoloAr, nomeCL, start});

//                System.err.println("\n"+getNomeClienteFromID(idCl)+
//                        " - "+getTitoloArticoloFromID(idAr)+ " - " +start+ " - "+end);

            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();          
        }
        
        return model;
    }
    
    private String getNomeClienteFromID(int id){
        String ret="";
        ResultSet cliente = null;
        
        try{
           stmtGetCliente.setInt(1, id);
           cliente = stmtGetCliente.executeQuery();
           if(cliente.next()  ){
                  ret = cliente.getString(4) + " " + cliente.getString(3);
                }       
        } catch (SQLException sqle) {
            sqle.printStackTrace();          
        }
        return ret;        
    }
    
    private String getTitoloArticoloFromID(int id){
        String ret="";
        ResultSet articolo = null;
        
        try{
           stmtGetArticolo.setInt(1, id);
           articolo = stmtGetArticolo.executeQuery();
           if(articolo.next() ){
               ret = articolo.getString(3);
           }
        } catch (SQLException sqle) {
            sqle.printStackTrace();          
        }
        return ret;
    }
    
    public int getClientIDFromName(String name){
        int id = 0;
        ResultSet res = null;
        try{
          stmtGetClientIdFromName.setString(1,name);
          res = stmtGetClientIdFromName.executeQuery();
          if(res.next())
              id=res.getInt(1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();          
        } 
        return id;   
    }
    
    public int getArticoloIDFromTitle(String title){
        int id = 0;
        ResultSet res = null;
        try{
          stmtGetArticoloIdFromTitle.setString(1,title);
          res = stmtGetArticoloIdFromTitle.executeQuery();
          if(res.next())
              id=res.getInt(1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();          
        } 
        return id;
    }
    
    public String getNoleggioMovCl(int idCl, int idAr) {
        String date1 = null;
        ResultSet res = null;
        try{
          stmtGetNoleggi4MovCl.setInt(1,idCl);
          stmtGetNoleggi4MovCl.setInt(2,idAr);
          res = stmtGetNoleggi4MovCl.executeQuery();
          if(res.next())
              date1=res.getString(4);
        } catch (SQLException sqle) {
            sqle.printStackTrace();          
        } 
        return date1;
    }
    
    
    public BrevevitaTableModel getNoleggiCliente(int idcl){
        int n,idAr;
        Date start,end;
        BrevevitaTableModel model = new BrevevitaTableModel();
        model.addColumn("n¡ ");
        model.addColumn("Titolo");        
        model.addColumn("Data Affitto");
        model.addColumn("Data Riconsegna");  
        
        ResultSet res = null;
        try{
          stmtGetNoleggiCliente.setInt(1,idcl);
          res = stmtGetNoleggiCliente.executeQuery();
          
          for(int i=1;res.next();i++){
              // ID ,IDCLIENTE,IDARTICOLO,DATASTART,DATAEND,NOTE,PREZZO,INFO1,INFO2
              n=i;
              idAr = res.getInt(3);       
              String  titoloAr = getTitoloArticoloFromID(idAr);
              start = res.getDate(4);  
              end = res.getDate(5);
              
              model.addRow(new Object[] {n, titoloAr, start, end});
          }              
        } catch (SQLException sqle) {
            sqle.printStackTrace();          
        } 
        return model;
    }
    
    public BrevevitaTableModel getNoleggiArticolo(int idar){
        int n,idCl;
        Date start,end;
        BrevevitaTableModel model = new BrevevitaTableModel();
        model.addColumn("n¡");
        model.addColumn("Cliente");        
        model.addColumn("Data Affitto");
        model.addColumn("Data Riconsegna");  
        
        ResultSet res = null;
        try{
          stmtGetNoleggiArticolo.setInt(1,idar);
          res = stmtGetNoleggiArticolo.executeQuery();
          for(int i = 1;res.next();i++){
              // ID ,IDCLIENTE,IDARTICOLO,DATASTART,DATAEND,NOTE,PREZZO,INFO1,INFO2
              n=i;
              idCl = res.getInt(2);       
              String  nomeCl = getNomeClienteFromID(idCl);
              start = res.getDate(4);  
              end = res.getDate(5);
              
              model.addRow(new Object[] {n, nomeCl, start, end});
          }              
        } catch (SQLException sqle) {
            sqle.printStackTrace();          
        } 
        return model;
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
                Double credito = result.getDouble("CREDITO");
                String phone = result.getString("PHONE");
                String email = result.getString("EMAIL");
                String add1 = result.getString("ADDRESS1");
                String add2 = result.getString("ADDRESS2");
                String city = result.getString("CITY");
                String state = result.getString("STATE");
                String postalCode = result.getString("POSTALCODE");
                String country = result.getString("COUNTRY");
                int id = result.getInt("ID");
//            Cliente(String codicecl, String lastName, String firstName, String middleName,
//            Double credito, String phone, String email, String address1, String address2,
//            String city, String state, String postalCode, String country, int id)
                
                cl = new Cliente(codicecl, lastName, firstName, middleName, credito, phone,
                        email, add1, add2, city, state, postalCode, country, id);
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
    
    public Noleggio getNoleggio(int index) {
        Noleggio nol = null;
        try {
            stmtGetNoleggio.clearParameters();
            stmtGetNoleggio.setInt(1, index);
            ResultSet result = stmtGetNoleggio.executeQuery();

            if (result.next()) {
                
                int id = result.getInt("ID");
                int idcl = result.getInt("IDCLIENTE");
                int idar = result.getInt("IDARTICOLO");
                Date data1 = result.getDate("DATASTART");
                Date data2 = result.getDate("DATAEND");
                String note = result.getString("NOTE");
                String prezzo = result.getString("PREZZO");
                String info1 = result.getString("INFO1");
                String info2 = result.getString("INFO2");

                nol = new Noleggio(idcl, idar, data1, data2, note, prezzo, info1, info2, id );
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return nol;
    }
    
    public double[] getTariffe() {
        double[] ret = new double[3];
        double d0,d1,d2;

        try {
            stmtGetTariffe.clearParameters();
            stmtGetTariffe.setInt(1, 1);
            ResultSet result = stmtGetTariffe.executeQuery();

            if (result.next()) {                
                d0 = result.getDouble("GIORNO1");
                d1 = result.getDouble("GIORNO2");
                d2 = result.getDouble("GIORNO3");
                ret[0] = d0;
                ret[1] = d1;
                ret[2] = d2;
            }         
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }
        
        return ret;
    }
    
    public double tariffa1(){
        double[] ret = getTariffe();
        return ret[0];
    }
    
    public double tariffa2(){
        double[] ret = getTariffe();
        return ret[1];
    }
    
    public double tariffa3(){
        double[] ret = getTariffe();
        return ret[2];
    }
    
    public static void main(String[] args) {
        bvdb db = new bvdb();
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
            "    CREDITO     DOUBLE, " +
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
    
    private static final String strCreateNOLEGGITable =
            "create table APP.NOLEGGI (" +
            "    ID          INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "    IDCLIENTE   INTEGER, " +
            "    IDARTICOLO  INTEGER, " +
            "    DATASTART   VARCHAR(50), " +
            "    DATAEND     VARCHAR(50), " +
            "    NOTE        VARCHAR(200), " +
            "    PREZZO      VARCHAR(30), " +
            "    INFO1       VARCHAR(30), " +
            "    INFO2       VARCHAR(30) " +
            ")";
    
    private static final String strCreateTARIFFETable =
            "create table APP.TARIFFE (" +
            "    ID          INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
            "    GIORNO1     DOUBLE, " +
            "    GIORNO2     DOUBLE, " +
            "    GIORNO3     DOUBLE " +
            ")"; 
    
    //
    
    private static final String strGetCliente =
            "SELECT * FROM APP.CLIENTI " +
            "WHERE ID = ?";
    
    private static final String strGetClienteFromName =
            "SELECT ID FROM APP.CLIENTI " +
            "WHERE LASTNAME = ?";
    
    private static final String strGetArticolo =
            "SELECT * FROM APP.ARTICOLI " +
            "WHERE ID = ?";
    
    private static final String strGetNoleggio =
            "SELECT * FROM APP.NOLEGGI " +
            "WHERE ID = ?";
    
    private static final String strGetArticoloFromTitle =
            "SELECT ID FROM APP.ARTICOLI " +
            "WHERE TITOLO = ?";
    
    private static final String strGetNoleggiIncompleti =
            "SELECT * FROM APP.NOLEGGI " +
            "WHERE DATAEND IS NULL " +
            "ORDER BY ID DESC"; 
    // ID ,IDCLIENTE,IDARTICOLO,DATASTART,DATAEND,NOTE,PREZZO,INFO1,INFO2
    
    private static final String strGetNoleggiCliente =
            "SELECT * FROM APP.NOLEGGI " +
            "WHERE IDCLIENTE = ? " +
            "ORDER BY ID DESC"; 
    
   private static final String strGetNoleggiArticolo =
            "SELECT * FROM APP.NOLEGGI " +
            "WHERE IDARTICOLO = ? " +
            "ORDER BY ID DESC";
    
    private static final String strGetNoleggi4Cl =
            "SELECT * FROM APP.NOLEGGI " +
            "WHERE IDCLIENTE = ?";  
    
    private static final String strGetNoleggi4MovCl =
            "SELECT * FROM APP.NOLEGGI " +
            "WHERE IDCLIENTE = ? AND IDARTICOLO = ?";  
    
    private static final String strGetTariffe =
            "SELECT * FROM APP.TARIFFE " +
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
    
    private static final String strSaveNoleggio =
            "INSERT INTO APP.NOLEGGI " +
            "   (IDCLIENTE, IDARTICOLO, DATASTART, DATAEND, NOTE, PREZZO, INFO1, INFO2 ) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String strSaveTariffe =
            "INSERT INTO APP.TARIFFE " +
            "   (GIORNO1, GIORNO2, GIORNO3 ) " +
            "VALUES (?, ?, ?)";
    
    //
    
    private static final String strUpdateCreditoCliente =
            "UPDATE APP.CLIENTI "+
            "SET CREDITO = ? " +
            "WHERE ID = ?";
    
    private static final String strUpdateDataEndNoleggio =
            "UPDATE APP.NOLEGGI "+
            "SET DATAEND = ? " +
            "WHERE ID = ?";
    
    private static final String strUpdateTariffe =
            "UPDATE APP.TARIFFE "+
            "SET  GIORNO1 = ?, " +
            "     GIORNO2 = ?, " +
            "     GIORNO3 = ? " +
            "WHERE ID = ?";    
    //
    
    private static final String strGetListClienti =
            "SELECT ID, LASTNAME, FIRSTNAME, MIDDLENAME FROM APP.CLIENTI "  +
            "ORDER BY LASTNAME ASC";
    
    private static final String strGetListArticoli =
            "SELECT ID, TITOLO, ANNO FROM APP.ARTICOLI "  +
            "ORDER BY TITOLO ASC";
    
    private static final String strGetListNoleggi =
            "SELECT * FROM APP.NOLEGGI "  +
            "ORDER BY ID DESC";

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
    
    private static final String strUpdateNoleggio =
            "UPDATE APP.NOLEGGI " +
            "SET IDCLIENTE = ?," +
            "    IDARTICOLO = ?, " +
            "    DATASTART = ?, " +
            "    DATAEND = ?, " +
            "    NOTE = ?," +
            "    PREZZO = ?, " +
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
