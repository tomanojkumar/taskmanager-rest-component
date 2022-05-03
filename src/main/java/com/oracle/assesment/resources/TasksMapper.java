package com.oracle.assesment.resources;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;



public class TasksMapper implements ResultSetMapper<Task> {
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String DATE = "date";

    public Task map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {
        return new Task(resultSet.getInt(ID), resultSet.getString(DESCRIPTION), resultSet.getString(DATE));
    }
}
