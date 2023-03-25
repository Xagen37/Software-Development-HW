package model;

public class Task {
    private final int id;
    private String name;
    private String desc;
    private boolean isDone;

    public Task(int id, String name, String desc, boolean isDone) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.isDone = isDone;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDesc() { return desc; }

    public void setDesc(String desc) { this.desc = desc; }

    public boolean isDone() { return isDone; }

    public void setDone(boolean done) { isDone = done; }
}
