package com.svlugovoy.jdbcdemo.dao.impl;

import com.svlugovoy.jdbcdemo.dao.DirectorDao;
import com.svlugovoy.jdbcdemo.domain.Actor;
import com.svlugovoy.jdbcdemo.domain.Director;
import com.svlugovoy.jdbcdemo.exception.DaoOperationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DirectorDaoImpl implements DirectorDao {

    public static final String FIND_BY_ID_SQL = "SELECT * FROM directors WHERE id = ?";

    private DataSource dataSource;

    public DirectorDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Director findById(Long id) {
        Objects.requireNonNull(id);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Director director = new Director();
                director.setId(resultSet.getLong("id"));
                director.setFirst_name(resultSet.getString("first_name"));
                director.setLast_name(resultSet.getString("last_name"));
                director.setBirthday(resultSet.getDate("birthday").toLocalDate());
                director.setGender(resultSet.getString("gender").toCharArray()[0]);
                director.setInstagram(resultSet.getString("instagram"));
                return director;
            } else {
                throw new DaoOperationException("Can not find director with id " + id);
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Error during find director operation", e);
        }
    }
}
