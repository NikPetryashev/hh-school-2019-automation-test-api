package models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VacanciesDto {
    private List<Vacancy> items;
    private Integer found;

    public VacanciesDto() {
    }

    public VacanciesDto(List<Vacancy> items, Integer found) {
        this.items = items;
        this.found = found;
    }

    public List<Vacancy> getItems() {
        return items;
    }

    public void setItems(List<Vacancy> items) {
        this.items = items;
    }

    public Integer getFound() {
        return found;
    }

    public void setFound(Integer found) {
        this.found = found;
    }
}
