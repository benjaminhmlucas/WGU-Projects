/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CustomerDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Small-Dell
 */
public class StatementMaker {
    
    static Statement stmt = null;
    static ResultSet rs = null;
    static int rowsAffected = -1;
    
    public static ResultSet makeSelectStatement(String sqlStatement){
        try {
            if(stmt == null){
                stmt = DBConnector.getStaticStatement();
            } 
            rs =  stmt.executeQuery(sqlStatement);
        } catch (SQLException ex) {
            System.out.println("SQLException: "+ex.getMessage());
            System.out.println("SQLState: "+ex.getSQLState());
            System.out.println("VendorError: "+ex.getErrorCode());
        }
        return rs;
    }
    public static int makeUpdateDeleteOrInsertStatement(String sqlStatement){
        try {
            if(stmt == null){
                stmt = DBConnector.getStaticStatement();
            } 
            rowsAffected =  stmt.executeUpdate(sqlStatement);
        } catch (SQLException ex) {
            System.out.println("SQLException: "+ex.getMessage());
            System.out.println("SQLState: "+ex.getSQLState());
            System.out.println("VendorError: "+ex.getErrorCode());
        }
        return rowsAffected;
    }
    
}


