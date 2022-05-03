package com.oracle.assesment.resources;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(TasksMapper.class)
public interface
TasksDao {

    @SqlQuery("select * from Tasks;")
    public List<Task> getTasks();

    @SqlQuery("select * from Tasks where id = :id")
    public Task getTask(@Bind("id") final int id);

    @SqlUpdate("insert into Tasks(description, date) values(:description, :date)")
    void createTask(@BindBean final Task Task);

    @SqlUpdate("update Tasks set name = coalesce(:description, description), code = coalesce(:date, date) where id = :id")
    void editTask(@BindBean final Task Task);

    @SqlUpdate("delete from Tasks where id = :id")
    int deleteTask(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    public int lastInsertId();
}

