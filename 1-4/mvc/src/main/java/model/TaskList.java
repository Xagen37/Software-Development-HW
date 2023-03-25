package model;

import java.util.List;

public class TaskList {
    private final int id;
    private String name;
    private List<Task> taskList;

    public TaskList(int id, String name, List<Task> taskList) {
        this.id = id;
        this.name = name;
        this.taskList = taskList;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<Task> getTaskList() { return taskList; }

    public void setTaskList(List<Task> taskList) { this.taskList = taskList; }
}
