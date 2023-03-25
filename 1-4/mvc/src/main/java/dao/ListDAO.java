package dao;

import model.Task;
import model.TaskList;

import java.util.List;

public interface ListDAO {
    List<TaskList> getLists();

    int addList(TaskList list);

    int removeList(TaskList list);

    int addTask(int listId, Task task);

    int switchTask(int taskId);
}
