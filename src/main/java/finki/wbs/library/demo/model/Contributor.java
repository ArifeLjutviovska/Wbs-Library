package finki.wbs.library.demo.model;

import java.util.List;

public class Contributor {

    private String Name;
    private String Label;
    private List<String> hasContributed;
    private String type; //organization or person

    public Contributor() {
    }

    public Contributor(String name, List<String> hasContributed, String type,String Label) {
        Name = name;
        this.hasContributed = hasContributed;
        this.type = type;
        this.Label=Label;

    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<String> getHasContributed() {
        return hasContributed;
    }

    public void setHasContributed(List<String> hasContributed) {
        this.hasContributed = hasContributed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
