package edu.vermontstate;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String userName;
    // List of associated family tree database files. This way a user
    // can have more than one family tree projects running.
    private final List<String> treeProjects;

    public User(String userName) {
        this.userName = userName;
        this.treeProjects = new ArrayList<>();
    }

    public String getUserName() { return this.userName; }
    public List<String> getProjects()  { return new ArrayList<>(this.treeProjects); }

    public void addProject(String projectPath) {
        this.treeProjects.add(Objects.requireNonNull(projectPath, "Project path cannot be null"));
    }

    public void removeProject(String projectPath) {
        this.treeProjects.remove(Objects.requireNonNull(projectPath, "Project path cannot be null"));
    }
}
