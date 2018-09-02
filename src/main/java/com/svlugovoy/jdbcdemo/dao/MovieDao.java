package com.svlugovoy.jdbcdemo.dao;

import com.svlugovoy.jdbcdemo.domain.Actor;
import com.svlugovoy.jdbcdemo.domain.Director;
import com.svlugovoy.jdbcdemo.domain.Movie;

import java.util.List;

public interface MovieDao {

    List<Movie> findAllMoviesWithActor(Actor actor);

}
