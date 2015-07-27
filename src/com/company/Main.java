package com.company;

import org.postgresql.ds.PGConnectionPoolDataSource;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        PGConnectionPoolDataSource source = new PGConnectionPoolDataSource();
//        source.setServerName("PostgreSQL 9.3");
        source.setServerName("localhost");
        source.setDatabaseName("Test");
        source.setPortNumber(5432);

//        System.out.println(ReflectionTest.getSerialVersionUID());
    }
}
