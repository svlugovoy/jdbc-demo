package com.svlugovoy.jdbcdemo.dao;

import com.svlugovoy.jdbcdemo.domain.Director;

public interface DirectorDao {

    Director findById(Long id);

}
