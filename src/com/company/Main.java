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
            conn = source.getConnection();

            migrator.run(conn);
        } catch (SQLException e) {
            if (conn != null) try {
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }


//        System.out.println(ReflectionTest.getSerialVersionUID());
    }
}
