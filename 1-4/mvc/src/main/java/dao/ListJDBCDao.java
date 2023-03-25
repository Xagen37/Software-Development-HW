package dao;

import model.Task;
import model.TaskList;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.util.List;

public class ListJDBCDao extends JdbcDaoSupport implements ListDAO {
    public ListJDBCDao(DataSource source) {
        super();
        setDataSource(source);
    }

    @Override
    public List<TaskList> getLists() {
        final String queryLists = "SELECT * FROM LISTS";
        final String queryTasks = "SELECT * FROM TASKS WHERE LIST_ID = ";

        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        assert jdbcTemplate != null;
        List<TaskList> lists = jdbcTemplate.query(
                queryLists, new BeanPropertyRowMapper<>(TaskList.class)
        );

        for (TaskList list: lists) {
            list.setTaskList(jdbcTemplate.query(
                    queryTasks + list.getId(), new BeanPropertyRowMapper<>(Task.class)
            ));
        }
        return lists;
    }

    private int jdbcUpdate(final String query) {
        assert getJdbcTemplate() != null;
        return getJdbcTemplate().update(query);
    }

    @Override
    public int addList(TaskList list) {
        final String query = "INSERT INTO LISTS (NAME) VALUES (" + list.getName() + ")";
        return jdbcUpdate(query);
    }

    @Override
    public int removeList(TaskList list) {
        final String queryList = "DELETE FROM LISTS WHERE ID = " + list.getId();
        int updated = jdbcUpdate(queryList);

        final String queryTasks = "DELETE FROM TASKS WHERE LIST_ID = " + list.getId();
        return updated + jdbcUpdate(queryTasks);
    }

    @Override
    public int addTask(int listId, Task task) {
        final String query =
                "INSERT INTO TASKS (NAME, LIST_ID) VALUES (" +
                task.getName() +
                "," +
                listId +
                ")";
        return jdbcUpdate(query);
    }

    @Override
    public int switchTask(int taskId) {
        final String query = "UPDATE TASKS SET DONE = 'T' WHERE ID = " + taskId;
        return jdbcUpdate(query);
    }
}
