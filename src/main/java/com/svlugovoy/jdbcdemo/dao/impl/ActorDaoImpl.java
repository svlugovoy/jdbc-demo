package com.svlugovoy.jdbcdemo.dao.impl;

import com.svlugovoy.jdbcdemo.dao.ActorDao;
import com.svlugovoy.jdbcdemo.dao.MovieDao;
import com.svlugovoy.jdbcdemo.domain.Actor;
import com.svlugovoy.jdbcdemo.domain.Director;
import com.svlugovoy.jdbcdemo.exception.DaoOperationException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActorDaoImpl implements ActorDao {

    public static final String SAVE_SQL = "INSERT INTO actors (first_name, last_name, birthday, gender, instagram) VALUES (?, ?, ?, ?, ?);";
    public static final String FIND_ALL_SQL = "SELECT * FROM actors";
    public static final String FIND_BY_ID_SQL = "SELECT * FROM actors WHERE id = ?";
    public static final String UPDATE_SQL = "UPDATE actors SET first_name = ?, last_name = ?, birthday = ?, gender = ?, instagram = ? WHERE id = ?";
    public static final String DELETE_SQL = "DELETE FROM actors WHERE id = ?";
    public static final String FIND_IN_MOVIES_FROM_DIRECTOR_SQL = "SELECT actors.* FROM actors INNER JOIN actors_movies ON actors.id = actors_movies.actors_id" +
            " INNER JOIN movies ON actors_movies.movies_id = movies.id WHERE movies.directors_id = ?;";

    private DataSource dataSource;
    private MovieDao movieDao;

    public ActorDaoImpl(DataSource dataSource, MovieDao movieDao) {
        this.dataSource = dataSource;
        this.movieDao = new MovieDaoImpl(dataSource);
    }

    @Override
    public Long save(Actor actor) {
        Objects.requireNonNull(actor);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, actor.getFirst_name());
            preparedStatement.setString(2, actor.getLast_name());
            preparedStatement.setDate(3, Date.valueOf(actor.getBirthday()));
            preparedStatement.setString(4, String.valueOf(actor.getGender()));
            preparedStatement.setString(5, actor.getInstagram());
            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                long id = generatedKey.getLong("id");
                actor.setId(id);
                return id;
            } else {
                throw new DaoOperationException("No Id returned after save actor");
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Error saving actor", e);
        }
    }

    @Override
    public List<Actor> findAll(boolean withMovieList) {
        List<Actor> actors = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL);
            while (resultSet.next()) {
                Actor actor = new Actor();
                actor.setId(resultSet.getLong("id"));
                actor.setFirst_name(resultSet.getString("first_name"));
                actor.setLast_name(resultSet.getString("last_name"));
                actor.setBirthday(resultSet.getDate("birthday").toLocalDate());
                actor.setGender(resultSet.getString("gender").toCharArray()[0]);
                actor.setInstagram(resultSet.getString("instagram"));
                if (withMovieList) {
                    actor.setMovies(movieDao.findAllMoviesWithActor(actor));
                }
                actors.add(actor);
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Error during find all actors", e);
        }
        return actors;
    }

    @Override
    public Actor findById(Long id) {
        Objects.requireNonNull(id);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Actor actor = new Actor();
                actor.setId(resultSet.getLong("id"));
                actor.setFirst_name(resultSet.getString("first_name"));
                actor.setLast_name(resultSet.getString("last_name"));
                actor.setBirthday(resultSet.getDate("birthday").toLocalDate());
                actor.setGender(resultSet.getString("gender").toCharArray()[0]);
                actor.setInstagram(resultSet.getString("instagram"));
                actor.setMovies(movieDao.findAllMoviesWithActor(actor));
                return actor;
            } else {
                throw new DaoOperationException("Can not find actor with id " + id);
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Error during find actor", e);
        }
    }

    @Override
    public void update(Actor actor) {
        Objects.requireNonNull(actor);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1, actor.getFirst_name());
            preparedStatement.setString(2, actor.getLast_name());
            preparedStatement.setDate(3, Date.valueOf(actor.getBirthday()));
            preparedStatement.setString(4, String.valueOf(actor.getGender()));
            preparedStatement.setString(5, actor.getInstagram());
            preparedStatement.setLong(6, actor.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows != 1) {
                throw new DaoOperationException("Can not update actor with id " + actor.getId());
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Error during update actor", e);
        }
    }

    @Override
    public void remove(Long id) {
        Objects.requireNonNull(id);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoOperationException("Error during delete actor with id = " + id, e);
        }
    }

    @Override
    public List<Actor> findActorsInFilmsFromDirector(Director director) {
        Objects.requireNonNull(director);
        List<Actor> actors = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_IN_MOVIES_FROM_DIRECTOR_SQL);
            preparedStatement.setLong(1, director.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Actor actor = new Actor();
                actor.setId(resultSet.getLong("id"));
                actor.setFirst_name(resultSet.getString("first_name"));
                actor.setLast_name(resultSet.getString("last_name"));
                actor.setBirthday(resultSet.getDate("birthday").toLocalDate());
                actor.setGender(resultSet.getString("gender").toCharArray()[0]);
                actor.setInstagram(resultSet.getString("instagram"));
                actor.setMovies(movieDao.findAllMoviesWithActor(actor));
                actors.add(actor);
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Error during find actors in films from director", e);
        }
        return actors;
    }

}
