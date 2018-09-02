package com.svlugovoy.jdbcdemo.dao;

import com.svlugovoy.jdbcdemo.domain.Actor;
import com.svlugovoy.jdbcdemo.domain.Director;

import java.util.List;

public interface ActorDao {

    Long save(Actor actor);

    List<Actor> findAll(boolean withMovieList);

    Actor findById(Long id);

    void update(Actor actor);

    void remove(Long id);

    List<Actor> findActorsInFilmsFromDirector(Director director);

}
