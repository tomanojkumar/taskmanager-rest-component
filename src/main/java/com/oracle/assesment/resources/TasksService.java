package com.oracle.assesment.resources;

import java.util.List;
import java.util.Objects;


import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


public abstract class TasksService {
    private static final String task_NOT_FOUND = "task id %s not found.";
    private static final String DATABASE_REACH_ERROR =
            "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
    private static final String DATABASE_CONNECTION_ERROR =
            "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
    private static final String DATABASE_UNEXPECTED_ERROR =
            "Unexpected error occurred while attempting to reach the database. Details: ";
    private static final String SUCCESS = "Success...";
    private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting task.";

    @CreateSqlObject
    abstract TasksDao tasksDao();

    public List<Task> getTasks() {
        return tasksDao().getTasks();
    }

    public Task getTask(int id) {
        Task task = tasksDao().getTask(id);
        if (Objects.isNull(task)) {
            throw new WebApplicationException(String.format(task_NOT_FOUND, id), Response.Status.NOT_FOUND);
        }
        return task;
    }

    public Task createTask(Task task) {
        tasksDao().createTask(task);
        return tasksDao().getTask(tasksDao().lastInsertId());
    }

    public Task editTask(Task task) {
        if (Objects.isNull(tasksDao().getTask(task.getId()))) {
            throw new WebApplicationException(String.format(task_NOT_FOUND, task.getId()),
                    Response.Status.NOT_FOUND);
        }
        tasksDao().editTask(task);
        return tasksDao().getTask(task.getId());
    }

    public String deleteTask(final int id) {
        int result = tasksDao().deleteTask(id);
        switch (result) {
            case 1:
                return SUCCESS;
            case 0:
                throw new WebApplicationException(String.format(task_NOT_FOUND, id), Response.Status.NOT_FOUND);
            default:
                throw new WebApplicationException(UNEXPECTED_ERROR, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    public String performHealthCheck() {
        try {
            tasksDao().getTasks();
        } catch (UnableToObtainConnectionException ex) {
            return checkUnableToObtainConnectionException(ex);
        } catch (UnableToExecuteStatementException ex) {
            return checkUnableToExecuteStatementException(ex);
        } catch (Exception ex) {
            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
        }
        return null;
    }

    private String checkUnableToObtainConnectionException(UnableToObtainConnectionException ex) {
        if (ex.getCause() instanceof java.sql.SQLNonTransientConnectionException) {
            return DATABASE_REACH_ERROR + ex.getCause().getLocalizedMessage();
        } else if (ex.getCause() instanceof java.sql.SQLException) {
            return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
        } else {
            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
        }
    }

    private String checkUnableToExecuteStatementException(UnableToExecuteStatementException ex) {
        if (ex.getCause() instanceof java.sql.SQLSyntaxErrorException) {
            return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
        } else {
            return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
        }
    }
}
