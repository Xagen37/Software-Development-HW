package sd.asofrygin.model;

public class Task {
    private int id;
    private String name;
    private String isDone;

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDone() { return isDone; }

    public void setDone(String done) { isDone = done; }
}
