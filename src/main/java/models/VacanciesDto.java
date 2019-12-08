package models;

import java.util.List;

public class VacanciesDto {
    private List<VacancyDto> items;

    public VacanciesDto(List<VacancyDto> items) {
        this.items = items;
    }

    public List<VacancyDto> getItems() {
        return items;
    }

    public void setItems(List<VacancyDto> items) {
        this.items = items;
    }
}
