package com.company.ignore;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.sql.Connection;

/**
 * Created by Glen on 7/26/2015.
 */
public class DatabaseObject implements Serializable{

    protected transient boolean persisted = false;

    public boolean isPersisted(){
        return this.persisted;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new NotImplementedException();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new NotImplementedException();
    }

//    public final boolean verifyIntegrity(Connection conn){
//        String queryString = "SELECT * FROM ";
//        return false;
//    }
}
