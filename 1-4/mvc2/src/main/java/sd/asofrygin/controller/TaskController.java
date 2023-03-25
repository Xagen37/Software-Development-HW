package sd.asofrygin.controller;

import sd.asofrygin.dao.ListDAO;
import sd.asofrygin.model.Task;
import sd.asofrygin.model.TaskList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class TaskController {
    private final ListDAO listDao;

    public TaskController(ListDAO listDao) {
        this.listDao = listDao;
    }

    @RequestMapping(value = "/add-list", method = RequestMethod.POST)
    public String addList(@ModelAttribute("taskList") TaskList list) {
        listDao.addList(list);
        return "redirect:/get-lists";
    }

    @RequestMapping(value = "/remove-list", method = RequestMethod.POST)
    public String removeList(@ModelAttribute("id") int id) {
        listDao.removeList(id);
        return "redirect:/get-lists";
    }

    @RequestMapping(value = "/get-lists", method = RequestMethod.GET)
    public String getLists(ModelMap map) {
        final List<TaskList> lists = listDao.getLists();
        map.addAttribute("taskLists", lists);
        map.addAttribute("taskList", new TaskList());
        map.addAttribute("task", new Task());
        return "index";
    }

    @RequestMapping(value = "/add-task", method = RequestMethod.POST)
    public String addTask(@ModelAttribute("task") Task task, @ModelAttribute("listId") int listId) {
        listDao.addTask(listId, task);
        return "redirect:/get-lists";
    }

    @RequestMapping(value = "/switch-task", method = RequestMethod.POST)
    public String switchTask(@ModelAttribute("id") int id) {
        listDao.switchTask(id);
        return "redirect:/get-lists";
    }
}
