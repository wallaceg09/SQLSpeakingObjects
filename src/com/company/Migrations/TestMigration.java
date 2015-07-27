package com.company.Migrations;

import com.company.migration.AbstractMigration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Glen on 7/27/2015.
 */
public class TestMigration extends AbstractMigration{

    public TestMigration(String name) {
        super(name);
    }

    public TestMigration(){
        super("201304271609_TestMigration");
    }

    @Override
    public void up(Connection conn) throws SQLException {
        String createQueryString = "CREATE TABLE IF NOT EXISTS \"TestMigration\" (ID serial, COLUMN1 real, COLUMN2 decimal, PRIMARY KEY (ID))";
        conn.prepareStatement(createQueryString).execute();
    }
}
