package com.svlugovoy.jdbcdemo.dao.impl;

import com.svlugovoy.jdbcdemo.dao.MovieDao;
import com.svlugovoy.jdbcdemo.domain.Actor;
import com.svlugovoy.jdbcdemo.domain.Movie;
import com.svlugovoy.jdbcdemo.exception.DaoOperationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieDaoImpl implements MovieDao {

    public static final String FIND_ALL_WITH_ACTOR_SQL = "SELECT movies.* FROM movies INNER JOIN actors_movies ON movies.id = actors_movies.movies_id" +
            " INNER JOIN actors ON actors_movies.actors_id = actors.id WHERE actors.id = ?;";

    private DataSource dataSource;

    public MovieDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Movie> findAllMoviesWithActor(Actor actor) {
        Objects.requireNonNull(actor);
        List<Movie> movies = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_WITH_ACTOR_SQL);
            preparedStatement.setLong(1, actor.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getLong("id"));
                movie.setName(resultSet.getString("name"));
                movie.setYearOfCreation(resultSet.getInt("year_of_creation"));
                movie.setGenre(resultSet.getString("genre"));
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Error during find movies operation", e);
        }
        return movies;
    }
}
