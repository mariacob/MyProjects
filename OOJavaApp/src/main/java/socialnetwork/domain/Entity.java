package socialnetwork.domain;

// Abstract class entity
public class Entity<ID>{
    private ID id;
    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }
}