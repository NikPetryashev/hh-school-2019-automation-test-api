package models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vacancy {
    private String name;

    public Vacancy() {
    }

    public Vacancy(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Vacancy{" +
                "name=" + name +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
