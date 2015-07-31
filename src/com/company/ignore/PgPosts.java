//package com.company.ignore;
//
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Date;
//
//import com.jcabi.jdbc.JdbcSession;
//import com.jcabi.jdbc.ListOutcome;
//
//import javax.sql.DataSource;
//
///**
// * Created by Glen on 7/25/2015.
// */
//final class PgPosts implements Posts{
//
//    private final DataSource db;
//
//    public PgPosts(DataSource database){
//        this.db = database;
//    }
//
//    @Override
//    public Iterable<Post> iterate() {
//        try {
//            return new JdbcSession(this.db)
//                    .sql("SELECT id FROM post")
//                    .select(
//                            new ListOutcome<Post>(
//                                    new ListOutcome.Mapping<Post>() {
//                                        @Override
//                                        public Post map(final ResultSet rset) {
//                                            try {
//                                                return new PgPost(db, rset.getInt(1));
//                                            } catch (SQLException e) {
//                                                e.printStackTrace();
//                                            }
//                                            return null;
//                                        }
//                                    }
//                            )
//                    );
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public Post add(Date date, String title) {
//        return null;
//    }
//}
