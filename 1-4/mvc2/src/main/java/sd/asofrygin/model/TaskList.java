package sd.asofrygin.model;

import java.util.List;

public class TaskList {
    private int id;
    private String name;
    private List<Task> taskList;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<Task> getTaskList() { return taskList; }

    public void setTaskList(List<Task> taskList) { this.taskList = taskList; }
}
