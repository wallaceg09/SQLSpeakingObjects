package com.company.migration;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Glen on 7/26/2015.
 */
//TODO: Convert connections to pooled connections
public final class PostGreSQLMigrator implements IMigrator{

    private static final String MIGRATION_TABLE_NAME = "Migrations";

    private static final String APPLIED_MIGRATIONS_QUERYSTRING = "SELECT \"id\" from \"" + MIGRATION_TABLE_NAME +"\"";

    private static final String INSERT_MIGRATION_QUERYSTRING = "INSERT INTO \"" + MIGRATION_TABLE_NAME +  "\" (\"id\", \"name\") values (?, ?)";

//    private static final String MIGRATION_TABLE_EXISTS_QUERYSTRING = "SELECT EXISTS(SELECT 1 FROM pg_catalog.pg_class c JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace WHERE n.nspname = ? and c.relname = ?)";//TODO: Most likely not needed

    private static final String CREATE_MIGRATION_TABLE_QUERYSTRING = "CREATE TABLE IF NOT EXISTS \"" + MIGRATION_TABLE_NAME + "\" (id uuid, name character varying, PRIMARY KEY(id))";

    private Map<UUID, AbstractMigration> migrations;//Map for fast indexing based on the UUID

    private List<Serializable> objects;//TODO: This should take a Class<Serializable>, not an instanced object

    public PostGreSQLMigrator(){
        this.migrations = new HashMap<UUID, AbstractMigration>();
        this.objects = new ArrayList<Serializable>();
    }

    /**
     * Registers a migration to be applied to the database.
     *
     * @param migration
     *
     * {@see com.company.migration.IMigrator#registerMigration}
     */
    @Override
    public void registerMigration(AbstractMigration migration){
        if(migrations.get(migration.getUUID()) == null){
            migrations.put(migration.getUUID(), migration);
        }
    }

    /**
     * Registers a serializable object for migration control.
     *
     * @param object
     */
    public void registerObject(Serializable object){
        if(!objects.contains(object)){
            objects.add(object);
        }
    }

    /**
     * Migrates the database based on the registered {@link AbstractMigration}s.
     * NOTE: This method does NOT close the connection. The connection must be closed in a higher scope.
     *
     * @param conn Connection to a data source.
     * @throws SQLException
     *
     * {@see com.company.migration.IMigrator#run}
    */
    @Override
    public void run(Connection conn) throws SQLException {

        //Ensure that the database has the Migration metadata required to perform
        validateMigrationsTable(conn);

        //Determine which migrations have been applied to the database
        markAppliedMigrations(conn);

        //Get all migrations that have not been applied to the database
        List<AbstractMigration> migrationsNotApplied = getMigrationsNotApplied();

        //Apply all migrations that have not been applied to the database
        applyMigrations(conn, migrationsNotApplied);
    }

    /**
     * Collects all registered migrations that have not been applied to the database, and stores them into a list.
     *
     * @return List of migrations that need to be applied to the database.
     * @throws SQLException
     */
    private List<AbstractMigration> getMigrationsNotApplied() {
        LinkedList<AbstractMigration> migrationsNotApplied = new LinkedList<AbstractMigration>();

        for(AbstractMigration migration : migrations.values()){
            if(!migration.isApplied()){
                migrationsNotApplied.add(migration);
            }
        }

        return migrationsNotApplied;
    }

    /**
     * Marks all migrations that have already been applied to the database.
     *
     * @param conn Connection to a data source.
     * @throws SQLException
     */
    private void markAppliedMigrations(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        //Get the migrations that have already been applied to the database
        PreparedStatement pstmt = conn.prepareStatement(APPLIED_MIGRATIONS_QUERYSTRING);
        try{
            ResultSet rset = pstmt.executeQuery();
            try{
                //Mark every migration that has been applied
                while(rset.next()){
                    UUID id = (UUID)rset.getObject(1);
                    migrations.get(id).setApplied(true);
                }
            }catch(SQLException sqle){
                throw new SQLException(sqle);
            }finally{
                rset.close();
            }
        }catch(SQLException sqle){
            throw new SQLException(sqle);
        }finally{
            try{
                pstmt.close();
            }
            catch (SQLException sqle){
                //TODO: Log exception
            }
        }
    }

    /**
     * Applies all migrations that have not already been applied to the database.
     *
     * @param conn Connection to a data source.
     * @param migrationsNotApplied List of migrations that have not been applied to the database.
     * @throws SQLException
     */
    private void applyMigrations(Connection conn, List<AbstractMigration> migrationsNotApplied)throws SQLException{

        conn.setAutoCommit(false);
        for(AbstractMigration migration : migrationsNotApplied){
            try{
                //Apply migration
                migration.up(conn);

                //TODO: Move saving the migration metadata to its own method.
                //Log migration in migration file
                PreparedStatement pstmt = conn.prepareStatement(INSERT_MIGRATION_QUERYSTRING);
                pstmt.setObject(1, migration.getUUID());
                pstmt.setString(2, migration.getName());

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
            }catch (SQLException sqle){
                //Rollback and throw an exception if there is an error
                conn.rollback();
                throw new SQLException(sqle);
            }
        }

        conn.commit();
    }

    /**
     * Creates the table that holds the migrations that have been applied to the database, if the table does not exist
     * already.
     *
     * @param conn Connection to a data source.
     * @throws SQLException
     */
    private void validateMigrationsTable(Connection conn)throws SQLException{
        //Create migration table if it does not exist
        PreparedStatement pstmt = conn.prepareStatement(CREATE_MIGRATION_TABLE_QUERYSTRING);
        try{
            pstmt.executeUpdate();
        }catch(SQLException sqle){
            throw new SQLException(sqle);
        }finally{
            try{
                pstmt.close();
            }catch(SQLException sqle){
                //TODO: Log exception
            }
        }
    }

}

