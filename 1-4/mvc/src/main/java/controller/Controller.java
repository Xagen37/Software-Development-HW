package controller;

import dao.ListDAO;

@org.springframework.stereotype.Controller
public class Controller {
    private final ListDAO listDao;

    public Controller(ListDAO listDao) {
        this.listDao = listDao;
    }

    public String addList() {

    }

    public String removeList() {

    }

    public String getLists() {

    }

    public String addTask() {

    }

    public String switchTask() {

    }
}
