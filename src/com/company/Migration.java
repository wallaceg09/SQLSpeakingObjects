package com.company;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Glen on 7/26/2015.
 */
public abstract class Migration {

    /**
     *
     */
    private UUID id;
    private String name;
    //TODO: Date?

    private boolean applied;//Should not be persisted to the database.

    /**
     * Returns the unique identifier associated with this Migration.
     *
     * @return The {@link UUID}of the Migration.
     */
    public UUID getUUID(){
        return id;
    }

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
     * This method is package-private because it should only be called by the {@link Migrator}that
     * handles this migration.
     * @param applied
     */
    void setApplied(boolean applied){
        this.applied = applied;
    }

    public abstract void up(Connection conn) throws SQLException;

//    public abstract void down() throws SQLException;//TODO: Might implement later, but it's not important now

}
