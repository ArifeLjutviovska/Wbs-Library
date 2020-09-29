package finki.wbs.library.demo.model;

import java.util.List;

public class Author {

    private String Name;
    private String Label;
    private List<String> hasCreated;
    private String type; //organization or person

    public Author() {
    }

    public Author(String name, List<String> hasCreated, String type,String Label) {
        Name = name;
        this.hasCreated = hasCreated;
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



    public List<String> getHasCreated() {
        return hasCreated;
    }

    public void setHasCreated(List<String> hasCreated) {
        this.hasCreated = hasCreated;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
