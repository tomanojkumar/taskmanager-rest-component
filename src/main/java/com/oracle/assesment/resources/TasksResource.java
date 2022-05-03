package com.oracle.assesment.resources;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.codahale.metrics.annotation.Timed;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class TasksResource {
    private final TasksService tasksService;;

    public TasksResource(TasksService TasksService) {
        this.tasksService = TasksService;
    }

    @GET
    @Timed
    public Representation<List<Task>> getTasks() {
        return new Representation<List<Task>>(HttpStatus.OK_200, tasksService.getTasks());
    }

    @GET
    @Timed
    @Path("{id}")
    public Representation<Task> getTask(@PathParam("id") final int id) {
        return new Representation<Task>(HttpStatus.OK_200, tasksService.getTask(id));
    }

    @POST
    @Timed
    public Representation<Task> createTask(@NotNull @Valid final Task Task) {
        return new Representation<Task>(HttpStatus.OK_200, tasksService.createTask(Task));
    }

    @PUT
    @Timed
    @Path("{id}")
    public Representation<Task> editTask(@NotNull @Valid final Task Task,
                                         @PathParam("id") final int id) {
        Task.setId(id);
        return new Representation<Task>(HttpStatus.OK_200, tasksService.editTask(Task));
    }

    @DELETE
    @Timed
    @Path("{id}")
    public Representation<String> deleteTask(@PathParam("id") final int id) {
        return new Representation<String>(HttpStatus.OK_200, tasksService.deleteTask(id));
    }
}