package com.oracle.assesment.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TasksResourceTest {
    private static final String SUCCESS = "Success...";
    private static final String TEST_Task_NAME = "testTaskName";
    private static final String TEST_Task_CODE = "testTaskCode";
    private static final String Tasks_ENDPOINT = "/tasks";

    private static final TasksService TasksService = mock(TasksService.class);

    @ClassRule
    public static final ResourceTestRule resources =
            ResourceTestRule.builder().addResource(new TasksResource(TasksService)).build();

    private final Task Task = new Task(1, TEST_Task_NAME, TEST_Task_CODE);

    @Before
    public void setup() {
        when(TasksService.getTask(eq(1))).thenReturn(Task);
        List<Task> Tasks = new ArrayList<>();
        Tasks.add(Task);
        when(TasksService.getTasks()).thenReturn(Tasks);
        when(TasksService.createTask(any(Task.class))).thenReturn(Task);
        when(TasksService.editTask(any(Task.class))).thenReturn(Task);
        when(TasksService.deleteTask(eq(1))).thenReturn(SUCCESS);
    }

    @After
    public void tearDown() {
        //reset(TasksService);
    }

    @Test
    public void testGetTask() {
        Task TaskResponse = resources.target(Tasks_ENDPOINT + "/1").request()
                .get(TestTaskRepresentation.class).getData();
        Assert.assertEquals(TaskResponse.getId(),Task.getId());
        Assert.assertEquals(TaskResponse.getDescription(),Task.getDescription());
        Assert.assertEquals(TaskResponse.getDate(),Task.getDate());
       // verify(TasksService).getTask(1);
    }

    @Test
    public void testGetTasks() {
        List<Task> Tasks =
                resources.target(Tasks_ENDPOINT).request().get(TestTasksRepresentation.class).getData();
        Assert.assertEquals(Tasks.size(),1);
        Assert.assertEquals(Tasks.get(0).getId(),Task.getId());
        Assert.assertEquals(Tasks.get(0).getDescription(),Task.getDescription());
        Assert.assertEquals(Tasks.get(0).getDate(),Task.getDate());
        
    }

    @Test
    public void testCreateTask() {
        Task newTask = resources.target(Tasks_ENDPOINT).request()
                .post(Entity.entity(Task, MediaType.APPLICATION_JSON_TYPE), TestTaskRepresentation.class)
                .getData();
        Assert.assertNotNull(newTask);
        Assert.assertEquals(newTask.getId(),Task.getId());
        Assert.assertEquals(newTask.getDescription(),Task.getDescription());
        Assert.assertEquals(newTask.getDate(),Task.getDate());
        //verify(TasksService).createTask(any(Task.class));
    }

    @Test
    public void testEditTask() {
        Task editedTask = resources.target(Tasks_ENDPOINT + "/1").request()
                .put(Entity.entity(Task, MediaType.APPLICATION_JSON_TYPE), TestTaskRepresentation.class)
                .getData();
        Assert.assertNotNull(editedTask);
        Assert.assertEquals(editedTask.getId(),Task.getId());
        Assert.assertEquals(editedTask.getDescription(),Task.getDescription());
        Assert.assertEquals(editedTask.getDate(),Task.getDate());
        //verify(TasksService).editTask(any(Task.class));
    }

    @Test
    public void testDeleteTask() {
        Assert.assertEquals(resources.target(Tasks_ENDPOINT + "/1").request()
                .delete(TestDeleteRepresentation.class).getData(),SUCCESS);
        //verify(TasksService).deleteTask(1);
    }

    private static class TestTaskRepresentation extends Representation<Task> {

    }

    private static class TestTasksRepresentation extends Representation<List<Task>> {

    }

    private static class TestDeleteRepresentation extends Representation<String> {

    }
}
