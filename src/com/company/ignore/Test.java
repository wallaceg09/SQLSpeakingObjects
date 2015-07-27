package com.company.ignore;

import org.postgresql.ds.PGConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Glen on 7/25/2015.
 */
public class Test {

    private int id;
    private String Name;

    public int getId(){
        return id;
    }

    public String getName(){
        return Name;
    }

    public void setName(String name){
        this.Name = name;
    }

    Test()
    {

    }

    public static Test load(PGConnectionPoolDataSource source, int id) throws SQLException {
        Connection conn = null;
        Test result = null;
        try{
            conn = source.getConnection("nge", "nge");
            String loadPrepStatementString = "SELECT \"id\", \"Name\" from \"Test\" where id = ?";

            PreparedStatement stmt = conn.prepareStatement(loadPrepStatementString);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            String name = rs.getString(2);

            result = new Test();
            result.id = id;
            result.Name = name;
        }catch(SQLException sqle){
            if(conn != null) conn.close();
            throw new SQLException(sqle);
        }finally {
            if(conn != null) conn.close();
        }

        return result;
    }

    public int save(PGConnectionPoolDataSource source) throws SQLException{
        Connection conn = null;
        int count = 0;
        try{
            conn = source.getConnection("nge", "nge");
            String savePrepStatementString = "INSERT INTO \"Test\" (\"Name\") values (?)";

            PreparedStatement stmt = conn.prepareStatement(savePrepStatementString);
            stmt.setString(1, getName());

            count = stmt.executeUpdate();
        }catch(SQLException sqle){
            if(conn != null) conn.close();
            throw new SQLException(sqle);
        }finally {
            if(conn != null) conn.close();
        }

        return count;
    }

    public int update(PGConnectionPoolDataSource source) throws SQLException {//TODO: Optimise via batch processing
        Connection conn = null;
        int count = 0;
        try{
            conn = source.getConnection("nge", "nge");
            String savePrepStatementString = "UPDATE \"Test\" SET \"Name\" = '?' WHERE \"id\" = ?";

            PreparedStatement stmt = conn.prepareStatement(savePrepStatementString);
            stmt.setString(1, getName());
            stmt.setInt(2, getId());

            count = stmt.executeUpdate();
        }catch(SQLException sqle){
            if(conn != null) conn.close();
            throw new SQLException(sqle);
        }finally {
            if(conn != null) conn.close();
        }

        return count;
    }

}
