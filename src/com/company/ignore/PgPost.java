package com.company.ignore;

import javax.sql.DataSource;
import java.util.Date;

/**
 * Created by Glen on 7/25/2015.
 */
public class PgPost implements Post {

    private final DataSource dbase;
    private final int id;
    public PgPost(DataSource data, int id)
    {
        this.dbase = data;
        this.id = id;
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public Date date() {
        return null;
    }

    @Override
    public String title() {
        return null;
    }
}
