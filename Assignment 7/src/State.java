public class State {
    String id;
    String name;

    public State(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public State() {

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "id is "+id+" name is "+name;
    }
}
