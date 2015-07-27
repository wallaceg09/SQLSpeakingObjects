package com.company.ignore;

import java.io.ObjectStreamClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Glen on 7/26/2015.
 */
public class ReflectionTest extends DatabaseObject{
    public int id;
    public String name;
    public float aFloat;

    public static long getSerialVersionUID(){
        ObjectStreamClass stream = ObjectStreamClass.lookup(ReflectionTest.class);
        return stream.getSerialVersionUID();
    }

    private void create(Connection conn) throws SQLException {
        String queryString = "INSERT INTO \"?\" (\"?\", \"?\") VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(queryString);
        stmt.setString(1, ReflectionTest.class.getName());
        stmt.setString(2, "name");
        stmt.setString(3, "aFloat");
        stmt.setString(4, name);
        stmt.setFloat(5, aFloat);
    }

    private void read(){

    }

    private void update(){

    }

    private void delete(){

    }

}
