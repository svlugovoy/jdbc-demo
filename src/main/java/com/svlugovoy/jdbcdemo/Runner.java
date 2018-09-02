package com.svlugovoy.jdbcdemo;

import com.svlugovoy.jdbcdemo.dao.*;
import com.svlugovoy.jdbcdemo.dao.impl.ActorDaoImpl;
import com.svlugovoy.jdbcdemo.dao.impl.DirectorDaoImpl;
import com.svlugovoy.jdbcdemo.dao.impl.MovieDaoImpl;
import com.svlugovoy.jdbcdemo.domain.Actor;
import com.svlugovoy.jdbcdemo.domain.Director;
import com.svlugovoy.jdbcdemo.exception.DaoOperationException;
import com.svlugovoy.jdbcdemo.util.FileReader;
import com.svlugovoy.jdbcdemo.util.JdbcUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class Runner {

    private final static String TABLE_INITIALIZATION_SQL_FILE = "db/migration/table_initialization.sql";
    private final static String TABLE_POPULATION_SQL_FILE = "db/migration/table_population.sql";

    private static DataSource dataSource;
    private static ActorDao actorDao;
    private static DirectorDao directorDao;

    public static void main(String[] args) {
        initDatasource();
        initTablesInDB();
        populateTablesInDB();
        initDaos();

        List<Actor> actors = actorDao.findAll(false);
        actors.forEach(System.out::println);
        System.out.println("#####***######");

        List<Actor> actorsWithMovies = actorDao.findAll(true);
        actorsWithMovies.forEach(System.out::println);
        System.out.println("#####***######");

        Actor newActor = Actor.builder()
                .first_name("Salma").last_name("Hayek").birthday(LocalDate.of(1966, Month.SEPTEMBER, 2))
                .gender('f').build();
        Long savedId = actorDao.save(newActor);
        Actor newActorById = actorDao.findById(savedId);
        System.out.println(newActor);
        System.out.println(newActorById);
        System.out.println("#####***######");

        Actor actorById = actorDao.findById(5L);
        System.out.println(actorById);
        actorById.setFirst_name("UPDATED");
        actorDao.update(actorById);
        Actor updatedActorById = actorDao.findById(5L);
        System.out.println(updatedActorById);
        System.out.println("#####***######");

        actorDao.remove(5L);
        try {
            actorDao.findById(5L);
            throw new RuntimeException("Should not be here");
        } catch (DaoOperationException e){
            System.out.println("Actor deleted successful");
        }
        System.out.println("#####***######");

        Director director = directorDao.findById(1L);
        List<Actor> actorsInFilmsFromDirector = actorDao.findActorsInFilmsFromDirector(director);
        actorsInFilmsFromDirector.forEach(System.out::println);

    }

    private static void initDatasource(){
        dataSource = JdbcUtil.createPostgresDataSource(
                "jdbc:postgresql://localhost:5432/testdb","test", "test");
    }

    private static void initTablesInDB() {
        String createTablesSql = FileReader.readWholeFileFromResources(TABLE_INITIALIZATION_SQL_FILE);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            connection.setReadOnly(false); // just for fun
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ); // just for fun
            Statement statement = connection.createStatement();
            statement.execute(createTablesSql);
            connection.commit();
        } catch (SQLException e) {
            throw new DaoOperationException("Shit happened during tables init.", e);
        }
    }

    private static void populateTablesInDB() {
        String createTablesSql = FileReader.readWholeFileFromResources(TABLE_POPULATION_SQL_FILE);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.execute(createTablesSql);
            connection.commit();
        } catch (SQLException e) {
            throw new DaoOperationException("Shit happened during tables population.", e);
        }
    }

    private static void initDaos() {
        actorDao = new ActorDaoImpl(dataSource, new MovieDaoImpl(dataSource));
        directorDao = new DirectorDaoImpl(dataSource);
    }

}
