package com.company.Migrations;

import com.company.migration.AbstractMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Glen on 7/30/2015.
 */
public class SecondTestMigration extends AbstractMigration{

    public SecondTestMigration(String name) {
        super(name);
    }

    public SecondTestMigration(){
        super("201507300929_SecondTestMigration");
    }

    @Override
    public void up(Connection conn) throws SQLException {
        String createQueryString = "ALTER TABLE \"TestMigration\" RENAME COLUMN \"column2\" TO  \"column3\"";
        PreparedStatement pstmt = conn.prepareStatement(createQueryString);
        try{
            pstmt.executeUpdate();
        }catch(SQLException sqle){
            throw new SQLException(sqle);
        }finally {
            try{
                pstmt.close();
            }catch(SQLException sqle){
                //TODO: Log exception
            }
        }
    }
}
