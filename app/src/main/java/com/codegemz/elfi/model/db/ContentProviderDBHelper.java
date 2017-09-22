package com.codegemz.elfi.model.db;

/**
 * Created by adrobnych on 5/24/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.codegemz.elfi.model.Algorithm.Algorithm;
import com.codegemz.elfi.model.Algorithm.AlgorithmBundle;
import com.codegemz.elfi.model.Algorithm.AlgorithmStep;
import com.codegemz.elfi.model.FamilyMember;
import com.codegemz.elfi.model.IndoorLocation;
import com.codegemz.elfi.model.PhraseIntent;
import com.codegemz.elfi.model.State;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class ContentProviderDBHelper extends OrmLiteSqliteOpenHelper {
    public ContentProviderDBHelper(Context context) {
        super(context, "MyDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, State.class);
            TableUtils.createTableIfNotExists(connectionSource, PhraseIntent.class);
            TableUtils.createTableIfNotExists(connectionSource, Algorithm.class);
            TableUtils.createTableIfNotExists(connectionSource, AlgorithmStep.class);
            TableUtils.createTableIfNotExists(connectionSource, AlgorithmBundle.class);
            TableUtils.createTableIfNotExists(connectionSource, FamilyMember.class);
            TableUtils.createTableIfNotExists(connectionSource, IndoorLocation.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, State.class, true);
            TableUtils.createTable(connectionSource, State.class);
            TableUtils.dropTable(connectionSource, PhraseIntent.class, true);
            TableUtils.createTable(connectionSource, PhraseIntent.class);
            TableUtils.dropTable(connectionSource, Algorithm.class, true);
            TableUtils.createTable(connectionSource, Algorithm.class);
            TableUtils.dropTable(connectionSource, AlgorithmStep.class, true);
            TableUtils.createTable(connectionSource, AlgorithmStep.class);
            TableUtils.dropTable(connectionSource, AlgorithmBundle.class, true);
            TableUtils.createTable(connectionSource, AlgorithmBundle.class);
            TableUtils.dropTable(connectionSource, FamilyMember.class, true);
            TableUtils.createTable(connectionSource, FamilyMember.class);
            TableUtils.dropTable(connectionSource, IndoorLocation.class, true);
            TableUtils.createTable(connectionSource, IndoorLocation.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* package-private */
    static final Class<?>[] CLASS_LIST = new Class<?>[] {
            State.class, PhraseIntent.class, Algorithm.class, AlgorithmStep.class, AlgorithmBundle.class,
            FamilyMember.class, IndoorLocation.class};
    public void resetAllTables() throws SQLException {
        for (Class<?> clazz : CLASS_LIST) {
            TableUtils.dropTable(connectionSource, clazz, true);
            TableUtils.createTable(connectionSource, clazz);
        }
    }
}
