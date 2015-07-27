package com.company.migration;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Glen on 7/27/2015.
 */
public interface IMigrator {

    /**
     * Registers a migration to be applied to the database
     *
     * @param migration
     */
    public void registerMigration(AbstractMigration migration);

    /**
     * Migrates the database based on the registered {@link AbstractMigration}s.
     *
     * @param conn Connection to a data source
     * @throws SQLException
     */
    public void run(Connection conn) throws SQLException;
}
