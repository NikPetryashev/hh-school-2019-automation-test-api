import models.VacanciesDto;
import models.Vacancy;
import models.VacancyDto;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class TestCase {
    @Test
    public void testUserInfoResponse() {
        RestTemplate restTemplate = new RestTemplate();
        VacanciesDto vacancies = restTemplate.getForObject("https://api.hh.ru/vacancies", VacanciesDto.class);
        System.out.println(vacancies);
    }

   /* private RestTemplate restTemplate = new RestTemplate();

    public List<VacancyDto> getVacancies() {
        String url = "https://api.hh.ru/vacancies";

        try {
            VacanciesDto response = restTemplate.getForObject(new URI(url), VacanciesDto.class);
            return response.getItems();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }*/

}
