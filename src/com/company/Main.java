package com.company;

import com.company.Migrations.TestMigration;
import com.company.migration.Migrator;
import org.postgresql.ds.PGConnectionPoolDataSource;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        PGConnectionPoolDataSource source = new PGConnectionPoolDataSource();
//        source.setServerName("PostgreSQL 9.3");
        source.setServerName("localhost");
        source.setDatabaseName("Test");
        source.setPortNumber(5432);

        Migrator migrator = new Migrator();
        migrator.registerMigration(new TestMigration());

        Connection conn = null;
        try {
            conn = source.getConnection("nge", "nge");

//            migrator.run(conn);
            migrator.validateMigrationsTable(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                //TODO: Log exception
            }
        }


//        System.out.println(ReflectionTest.getSerialVersionUID());
    }
}
