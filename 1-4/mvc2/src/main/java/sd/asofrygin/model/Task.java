package sd.asofrygin.model;

public class Task {
    private int id;
    private String name;
    private String desc;
    private String isDone;

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDesc() { return desc; }

    public void setDesc(String desc) { this.desc = desc; }

    public String getDone() { return isDone; }

    public void setDone(String done) { isDone = done; }
}
