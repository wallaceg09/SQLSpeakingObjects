package com.company.ignore;

import java.util.Date;

/**
 * Created by Glen on 7/25/2015.
 */
public interface Posts {
    Iterable<Post> iterate();
    Post add(Date date, String title);

}
