package com.company.migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Glen on 7/26/2015.
 */
//TODO: Add methods that will handle standard DDL commands
    //eg: Table.update(), Table.addColumns()
//TODO: Convert connections to pooled connections
public abstract class AbstractMigration {

    /**
     *
     */
    private UUID id;
    private String name;
    //TODO: Date?

    private boolean applied;//Should not be persisted to the database.

    /**
     * @param name The name of the migration. A good convention for naming migrations is to take the date that the migration
     *             was created in YYYYMMDDHHmm format and append an "_" followed by a brief description.
     *             For example a migration created on the date 07-27-2015 (MM-DD-YYY) at 6:37pm with the description
     *             "Creates 'user' table" could be called "201507271837_CreateUserTable."
     */
    public AbstractMigration(String name){
        this.id = UUID.randomUUID();
        this.name = name;
    }

    /**
     * Returns the unique identifier associated with this Migration.
     *
     * @return The {@link UUID}of the Migration.
     */
    public UUID getUUID(){
        return id;
    }

    /**
     * Returns the name associated with this Migration.
     *
     * @return The name of the Migration.
     */
    public String getName() { return name; }

    /**
     * Get whether or not the migration has already been applied to the database.
     *
     * @return true if the migration has already been applied, false otherwise.
     */
    public boolean isApplied(){
        return this.applied;
    }

    /**
     * Set whether or not the migration has already been applied to the database.
     * This method is package-private because it should only be called by the {@link PostGreSQLMigrator}that
     * handles this migration.
     * @param applied
     */
    void setApplied(boolean applied){
        this.applied = applied;
    }

    /**
     * Migration logic. This method should implement the changes desired in the database schema.
     * @param conn
     * @throws SQLException
     */
    public abstract void up(Connection conn) throws SQLException;

//    public abstract void down() throws SQLException;//TODO: Might implement later, but it's not important now

}
