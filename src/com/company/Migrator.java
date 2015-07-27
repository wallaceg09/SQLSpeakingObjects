package com.company;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Glen on 7/26/2015.
 */
public class Migrator {

    private static final String APPLIED_MIGRATIONS_QUERYSTRING = "SELECT \"id\" from \"Migrations\"";

    private Map<UUID, Migration> migrations;//Map for fast indexing based on the UUID

    private List<Serializable> objects;//TODO: This should take a Class<Serializable>, not an instanced object

    public Migrator(){
        this.migrations = new HashMap<UUID, Migration>();
        this.objects = new ArrayList<Serializable>();
    }

    /**
     * Registers a migration to be applied to the database
     *
     * @param migration
     */
    public void registerMigration(Migration migration){
        if(migrations.get(migration.getUUID()) == null){
            migrations.put(migration.getUUID(), migration);
        }
    }

    /**
     * Registers a serializable object for migration control
     *
     * @param object
     */
    public void registerObject(Serializable object){
        if(!objects.contains(object)){
            objects.add(object);
        }
    }

    /**
     * Migrates the database based on the registered migrations
     *
     * @param conn Connection to a data source
     * @throws SQLException
     */
    public void run(Connection conn) throws SQLException {
        //Determine which migrations have been applied to the database
        markAppliedMigrations(conn);

        //Get all migrations that have not been applied to the database
        List<Migration> migrationsNotApplied = getMigrationsNotApplied();

        //Apply all migrations that have not been applied to the database
        applyMigrations(conn, migrationsNotApplied);
    }

    /**
     * Collects all registered migrations that have not been applied to the database, and stores them into a list
     *
     * @return List of migrations that need to be applied to the database
     * @throws SQLException
     */
    private List<Migration> getMigrationsNotApplied() {
        LinkedList<Migration> migrationsNotApplied = new LinkedList<Migration>();

        for(Migration migration : migrations.values()){
            if(!migration.isApplied()){
                migrationsNotApplied.add(migration);
            }
        }

        return migrationsNotApplied;
    }

    /**
     * Marks all migrations that have already been applied to the database.
     *
     * @param conn Connection to a data source
     * @throws SQLException
     */
    private void markAppliedMigrations(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        //Get the migrations that have already been applied to the database
        ResultSet rset = conn.prepareStatement(APPLIED_MIGRATIONS_QUERYSTRING).executeQuery();

        //Mark every migration that has been applied
        while(rset.next()){
            UUID id = (UUID)rset.getObject(1);
            migrations.get(id).setApplied(true);
        }
        rset.close();
    }

    /**
     * Applies all migrations that have not already been applied to the database
     *
     * @param conn Connection to a data source
     * @param migrationsNotApplied List of migrations that have not been applied to the database
     * @throws SQLException
     */
    private void applyMigrations(Connection conn, List<Migration> migrationsNotApplied)throws SQLException{
        conn.setAutoCommit(false);
        for(Migration migration : migrationsNotApplied){
            try{
                //Apply migration
                migration.up();
            }catch (SQLException sqle){
                //Rollback and throw an exception if there is an error
                conn.rollback();
                throw new SQLException(sqle);
            }

        }
        conn.commit();
    }
}

