package sd.asofrygin.dao;

import sd.asofrygin.model.Task;
import sd.asofrygin.model.TaskList;

import java.util.List;

public interface ListDAO {
    List<TaskList> getLists();

    int addList(TaskList list);

    int removeList(int listId);

    int addTask(int listId, Task task);

    int switchTask(int taskId);
}
