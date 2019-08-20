/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * Creates Connection to the database object.
 * @author Small-Dell
 */
public class DBConnector {
    private static boolean connectionEstablished = false;
    private static DBConnector dbConnector;
    private static Connection conn = null;
    private static Statement stmt = null;
    private static String systemCountry = null;
    private static String systemLanguage = null;
    private static boolean offline = false;
    
    private DBConnector(){        
        String driver = "com.mysql.jdbc.Driver";
        String db = "U05Kcz";
        String url = "jdbc:mysql://52.206.157.109/" + db;
        String user = "U05Kcz";
        String pass = "53688525821";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pass);
            connectionEstablished = true;
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());                   
            switch(DatabaseObjects.getSystemLanguage()){
                case "en":
                    JOptionPane.showMessageDialog(null,"The SQL Gods have NOT granted you access to database: " + db + " because of Database Communication errors!-->>"+ex.getMessage(),"Denied!",JOptionPane.ERROR_MESSAGE);
                    break;
                case "fr":
                    JOptionPane.showMessageDialog(null,"Les dieux SQL ne vous ont pas accordé l'accès à la base de données: " + db + " à cause des erreurs de communication de base de données!","nié!",JOptionPane.ERROR_MESSAGE);
                    break;
                case "es":
                    JOptionPane.showMessageDialog(null,"Los dioses de SQL NO le han otorgado acceso a la base de datos: " + db + " Debido a errores de comunicación de base de datos!","¡Negado!",JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "The SQL Gods say do not know your language!","Doh!!",JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(null,"The SQL Gods have NOT granted you access to database : " + db + " because of SQLException: "+ex.getMessage(),"Denied!",JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }
    public static DBConnector getConnection(){
        if(!connectionEstablished){
            dbConnector = new DBConnector();            
        }
        return dbConnector;
    }
    
    public static Statement getStaticStatement(){
        if(stmt == null){
            try{
              stmt = conn.createStatement();
            } catch(SQLException e){
                printSQLException(e);
                return stmt;
            }
        }
        return stmt;
    }

    public static boolean isConnectionEstablished() {
        return connectionEstablished;
    }

    public static DBConnector getDbConnector() {
        return dbConnector;
    }

    public static Connection getConn() {
        return conn;
    }

    public static Statement getStmt() {
        return stmt;
    }

    public static String getSystemCountry() {
        return systemCountry;
    }

    public static void setSystemCountry(String systemCountry) {
        DBConnector.systemCountry = systemCountry;
    }

    public static String getSystemLanguage() {
        return systemLanguage;
    }

    public static void setSystemLanguage(String systemLanguage) {
        DBConnector.systemLanguage = systemLanguage;
    }

    public static boolean isOffline() {
        return offline;
    }

    public static void setOffline(boolean offline) {
        DBConnector.offline = offline;
    }
        
      
    private static void printSQLException(SQLException e){
        System.out.println("SQLException: "+e.getMessage());
        System.out.println("SQLState: "+e.getSQLState());
        System.out.println("VendorError: "+e.getErrorCode());
    }
}
