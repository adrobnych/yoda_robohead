package com.codegemz.elfi.spec.db;

import android.database.sqlite.SQLiteDatabase;

import com.codegemz.elfi.model.Algorithm.Algorithm;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by adrobnych on 5/29/15.
 */
public class TestDBHelper {
    private static final String DB_NAME = "unit_tests.db";
    private ConnectionSource connectionSource;
    private String databaseUrl = "jdbc:sqlite:" + DB_NAME;

    // create a connection source to our database

    public ConnectionSource getConnectionSource() {
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl);
            return connectionSource;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}