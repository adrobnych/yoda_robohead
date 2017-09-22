package com.codegemz.elfi.spec;

import com.codegemz.elfi.model.Algorithm.Algorithm;
import com.codegemz.elfi.apicontracts.AlgorithmType;
import com.codegemz.elfi.spec.db.TestDBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.junit.*;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by adrobnych on 5/29/15.
 */
public class AlgorithmTest {

    static Dao<Algorithm, Integer> algorithmDao;
    static ConnectionSource connectionSource;

    @BeforeClass
    public static void setUpDatabaseLayer() throws SQLException {
        connectionSource = new TestDBHelper().getConnectionSource();
        TableUtils.createTableIfNotExists(connectionSource, Algorithm.class);

        try {
            algorithmDao = DaoManager.createDao(connectionSource, Algorithm.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Before
    public void clearTable(){
        try {
            TableUtils.clearTable(connectionSource, Algorithm.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void canStoreNewAlgorithm(){

        Algorithm algorithm = new Algorithm(
                "new_algorithm",
                AlgorithmType.Parallel.toString(),
                "com.codegemz.elfi.coreapp.DEFAULT_BUNDLE"
        );

        try {
            algorithmDao.create(algorithm);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(algorithm.getId() != 0);

        Algorithm stored_algorithm = null;
        try {
            stored_algorithm = algorithmDao.queryForId((Integer)algorithm.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertEquals("new_algorithm", stored_algorithm.getName());
        assertEquals("Parallel", stored_algorithm.getType());
        assertEquals("com.codegemz.elfi.coreapp.DEFAULT_BUNDLE", stored_algorithm.getAlgorithm_bundle_name());

    }


    @Test
    public void shouldNotAllowSavingWithoutAlgorithmBundle(){
        Algorithm algorithm = new Algorithm(
                "new_algorithm",
                AlgorithmType.Parallel.toString(),
                null
        );

        try {
            algorithmDao.create(algorithm);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(algorithm.getId() == 0);

    }

}
