
package org.java7recipes.chapter11.recipe11_07;

import org.java7recipes.chapter11.recipe11_01.CreateConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Recipe 11-7:
 * 
 * Example of transaction management by committing everything after all
 * transactions have successfully completed...otherwise rolling back all transactions.
 * 
 * @author juneau
 */
public class TransactionExample {
    public static Connection conn = null;

    public static void main(String[] args) {
        boolean successFlag = false;
        try {
            CreateConnection createConn = new CreateConnection();
            conn = createConn.getConnection();
            conn.setAutoCommit(false);
            queryDbRecipes();
            successFlag = insertRecord(
                    "11-6",
                    "Simplifying and Adding Security with Prepared Statements",
                    "Working with Prepared Statements",
                    "Recipe Text");

            if (successFlag = true){

                successFlag = insertRecord(
                        null,
                        "Simplifying and Adding Security with Prepared Statements",
                        "Working with Prepared Statements",
                        "Recipe Text");
            }

            // Commit Transactions
            if (successFlag == true)
                conn.commit();  
            else
                conn.rollback();

            conn.setAutoCommit(true);
            queryDbRecipes();
        } catch (java.sql.SQLException ex) {
            System.out.println(ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
    
    private static void queryDbRecipes(){
        String sql = "SELECT ID, RECIPE_NUM, NAME, DESCRIPTION " +
                     "FROM RECIPES";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString(2) + ": " + rs.getString(3) + 
                                " - " + rs.getString(4));
            }           
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
    }
    
    private static boolean insertRecord(String recipeNumber,
                              String title,
                              String description,
                              String text){
        String sql = "INSERT INTO RECIPES VALUES(" +
                     "RECIPES_SEQ.NEXTVAL, ?,?,?,?)";
        boolean success = false;
        PreparedStatement pstmt = null;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, recipeNumber);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setString(4, text);
            pstmt.executeUpdate();
            System.out.println("Record successfully inserted.");
            success = true;
        } catch (SQLException ex){
            success = false;
            ex.printStackTrace();
        } finally {
            if (pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return success;
        
    }
    
    
}
