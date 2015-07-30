package com.company;

import com.company.Migrations.SecondTestMigration;
import com.company.Migrations.TestMigration;
import com.company.migration.PostGreSQLMigrator;
import org.postgresql.ds.PGConnectionPoolDataSource;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        PGConnectionPoolDataSource source = new PGConnectionPoolDataSource();
        source.setServerName("localhost");
        source.setDatabaseName("Test");
        source.setPortNumber(5432);

        PostGreSQLMigrator migrator = new PostGreSQLMigrator();
        migrator.registerMigration(new TestMigration());
        migrator.registerMigration(new SecondTestMigration());

        Connection conn = null;
        try {
            conn = source.getConnection("nge", "nge");

            migrator.run(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                conn.close();
            } catch (SQLException e) {
                //TODO: Log exception
            }
        }
    }
}
