package service;

import models.VacanciesDto;
import models.VacancyDto;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ExchangeClient {
    private RestTemplate restTemplate = new RestTemplate();

    public List<VacancyDto> getVacancies() {
        String url = "https://api.hh.ru/vacancies";

        try {
            VacanciesDto response = restTemplate.getForObject(new URI(url), VacanciesDto.class);
            return response.getItems();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
