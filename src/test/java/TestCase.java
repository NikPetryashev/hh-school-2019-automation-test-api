import models.VacanciesDto;
import models.Vacancy;
import org.junit.After;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

public class TestCase {
    final String urlEndpoint="https://api.hh.ru/vacancies";

    private boolean isContains(String text, String findText) {
        return text.toLowerCase().contains(findText.toLowerCase());
    }

    @After
    public void printNewrow(){
        System.out.println();
    }

    //Позитивные тесты
    //проверка, доступна ли страница
    @Test
    public void testStatusResponse() throws IOException {
        URL url = new URL(urlEndpoint);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println("Доступ к странице " + urlEndpoint);
        System.out.println(connection.getResponseMessage() + " " + statusCode);
        assertEquals( connection.getResponseCode(),200);
    }

    //проверка на корректность поиска
    @Test
    public void testFindVacancy() {
        String findText="java";
        RestTemplate restTemplate = new RestTemplate();
        VacanciesDto vacanciesDto = restTemplate.getForObject(urlEndpoint + "?text=" + findText, VacanciesDto.class);

        System.out.println("Поиск по всем полям вакансий, содержащих слово " + findText.toUpperCase() + ". Найдено " + vacanciesDto.getFound());
        boolean isContainsText=false;
        for(Vacancy vacancy:vacanciesDto.getItems()){
            isContainsText=isContains(vacancy.name,findText)
                    ||isContains(vacancy.snippet.requirement,findText)
                                ||isContains(vacancy.snippet.responsibility,findText);
           if(!isContainsText)
                break;
        }
        assertTrue(isContainsText);
    }

    //проверка на корректность поиска с атрибутами
    @Test
    public void testFindVacancyWithParameter() {
        String findText="Java";
        RestTemplate restTemplate = new RestTemplate();
        VacanciesDto vacanciesDto = restTemplate.getForObject(urlEndpoint + "?text=!" + findText + "&search_field=name", VacanciesDto.class);
        System.out.println("Поиск по полю name вакансий, содержащих слово " + findText.toUpperCase() + ". Найдено " + vacanciesDto.getFound());
        boolean isContainsText=false;
        for(Vacancy vacancy:vacanciesDto.getItems()){
            isContainsText=isContains(vacancy.name,findText);
            if(!isContainsText) {
                break;
            }
        }
        assertTrue(isContainsText);
    }

    //поиск вакансии по словосочетанию
    @Test
    public void testFindVacancyByPhrase() {
        String findText="\"java developer\"";
        RestTemplate restTemplate = new RestTemplate();
        VacanciesDto vacanciesDto = restTemplate.getForObject(urlEndpoint + "?text=!" + findText + "&search_field=name", VacanciesDto.class);

        System.out.println("Поиск по полю name вакансий, содержащих фразу " + findText.toUpperCase() + ". Найдено " + vacanciesDto.getFound());
        boolean isContainsText1=false;
        boolean isContainsText2=false;
        String[] subStr=(findText
                .substring(1,findText.length()-1))
                .split(" ");
        for(Vacancy vacancy:vacanciesDto.getItems()){
            isContainsText1=isContains(vacancy.name,subStr[0]);
            isContainsText2=isContains(vacancy.name,subStr[1]);
            if(!isContainsText1 && !isContainsText2)
                break;
        }
        assertTrue(isContainsText1 && isContainsText2);
    }

    //поиск с учетом порядка слов в словосочетанию
    @Test
    public void testFindVacancyByPhraseReverse() {
        String findText1="java developer";
        String findText2="developer java";
        RestTemplate restTemplate = new RestTemplate();
        VacanciesDto vacanciesDto1 = restTemplate.getForObject(urlEndpoint + "?text=" + findText1, VacanciesDto.class);
        VacanciesDto vacanciesDto2 = restTemplate.getForObject(urlEndpoint + "?text=" + findText2, VacanciesDto.class);

        System.out.println("Поиск по всем полям вакансий с учетом порядка слов");
        assertEquals(vacanciesDto1.getFound(), vacanciesDto2.getFound());
    }

    //поиск вакансии с применением булевого оператора NOT
    @Test
    public void testFindVacancyWithBooleanParameter() {
        String findText="java";
        String withoutText="developer";
        RestTemplate restTemplate = new RestTemplate();
        VacanciesDto vacanciesDto = restTemplate.getForObject(urlEndpoint + "?search_field=name&text=" + findText + " NOT " + withoutText, VacanciesDto.class);

        System.out.println("Поиск по полю name вакансий, содержащих слово " + findText.toUpperCase()
                + " исключая слово " + withoutText.toUpperCase() + ". Найдено " + vacanciesDto.getFound());
        boolean isContainsText=false;
        for(Vacancy vacancy:vacanciesDto.getItems()){
            isContainsText=(isContains(vacancy.name,findText) && !isContains(vacancy.name,withoutText));
            if(!isContainsText)
                break;
        }
        assertTrue(isContainsText);
    }


    //**************************************************
    //Негативные тесты
    //проверка c множественным значением поля text
    @Test
    public void testStatusResponseBadRequest() throws IOException {
        String findText = "java";
        URL url = new URL(urlEndpoint + "?text=" + findText + "&text=developer");
        System.out.println("Запрос Get c множественным значением поля text");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println(" Статус ответа: " + connection.getResponseMessage() + " " + statusCode);
        assertEquals( connection.getResponseCode(),400);
        connection.disconnect();
    }
    //проверка c произвольным валидным набором символов в поле text
    @Test
    public void testStatusResponseRandomSymbolsText() throws IOException {
        String findText = "sdgafывъёЁЙйапои1234567890";
        URL url = new URL(urlEndpoint + "?text=" + findText);
        System.out.println("Запрос Get c произвольным валидным набором символов в поле text");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println(" Статус ответа: " + connection.getResponseMessage() + " " + statusCode);
        assertEquals( connection.getResponseCode(),200);
        connection.disconnect();
    }
    //проверка поля text c набором спецсимволов
    @Test
    public void testStatusResponseSpecialSymbolsText() throws IOException {
        String findText = "`~!@#$%^&*()+=-\"№;:?|\\/'<>_";
        URL url = new URL(urlEndpoint + "?text=" + findText);
        System.out.println("Запрос Get с набором спецсимволов в поле text");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println(" Статус ответа: " + connection.getResponseMessage() + " " + statusCode);
        assertEquals( connection.getResponseCode(),200);
        connection.disconnect();
    }

    //проверка поля text c разным набором символов
    @Test
    public void testStringLength0() throws IOException {
        String findText="h".repeat(0);
        URL url = new URL(urlEndpoint+"?text="+findText);
        System.out.println("Запрос Get с пустым полем text");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println(" Статус ответа: " + connection.getResponseMessage() + " " + statusCode);
        assertEquals( connection.getResponseCode(),200);
        connection.disconnect();
    }
    @Test
    public void testStringLength1() throws IOException {
        String findText="h".repeat(1);
        URL url = new URL(urlEndpoint+"?text="+findText);
        System.out.println("Запрос Get с одним символом в поле text");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println(" Статус ответа: " + connection.getResponseMessage() + " " + statusCode);
        assertEquals( connection.getResponseCode(),200);
        connection.disconnect();
    }
    @Test
    public void testStringLength255() {
        String findText="h".repeat(255);
        System.out.println("Запрос Get с " + findText.length() + " символом в поле text");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VacanciesDto> responseEntity = restTemplate.getForEntity(urlEndpoint + "?text=" + findText, VacanciesDto.class);
        System.out.println(" Поиск по всем полям вакансий. Найдено " + responseEntity.getBody().getFound());
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println(" Статус ответа: " + statusCode.name() + " " + statusCode.value());
        assertTrue(statusCode.value()==200 & responseEntity.getBody().getFound()==0);
    }
    @Test
    public void testStringLength256() {
        String findText="h".repeat(256);  //при количестве символом 256 и более запрос отвечает со статусом 200,и полным набором вакансий
        // ожидаемый ответ 0 вакансий
        System.out.println("Запрос Get с " + findText.length() + " символом в поле text");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VacanciesDto> responseEntity = restTemplate.getForEntity(urlEndpoint + "?text=" + findText, VacanciesDto.class);
        System.out.println(" Поиск по всем полям вакансий. Найдено " + responseEntity.getBody().getFound());
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println(" Статус ответа: " + statusCode.name() + " " + statusCode.value());
        assertTrue(statusCode.value()==200 & responseEntity.getBody().getFound()==0);
    }
    @Test
    public void testStringLength257() {
        String findText="h".repeat(257);  //при количестве символом 256 и более запрос отвечает со статусом 200,и полным набором вакансий
        // ожидаемый ответ 0 вакансий
        System.out.println("Запрос Get с " + findText.length() + " символом в поле text");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VacanciesDto> responseEntity = restTemplate.getForEntity(urlEndpoint + "?text=" + findText, VacanciesDto.class);
        System.out.println(" Поиск по всем полям вакансий. Найдено " + responseEntity.getBody().getFound());
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println(" Статус ответа: " + statusCode.name() + " " + statusCode.value());
        assertTrue(statusCode.value()==200 & responseEntity.getBody().getFound()==0);
    }
    @Test
    public void testStringLength32kb() throws IOException {
        String findText="h".repeat(32*1024);
        URL url = new URL(urlEndpoint+"?text="+findText);
        System.out.println("Запрос Get с " + findText.length() + " символом в поле text");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println(" Статус ответа: " + connection.getResponseMessage() + " " + statusCode);
        assertTrue(connection.getResponseCode()>=400 && connection.getResponseCode()<500);
        connection.disconnect();
    }

    //проверка поля text на инъекции
    @Test
    public void testStatusResponseInjectionXSS() throws IOException {
        String findText = "<script>alert(\"Hello, XSS!\")</script>";
        URL url = new URL(urlEndpoint+"?text="+findText);
        System.out.println("Запрос Get на XSS инъекцию");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println(" Статус ответа: " + connection.getResponseMessage() + " " + statusCode);
        assertEquals( connection.getResponseCode(),200);
        connection.disconnect();
    }

    //проверка поля text на инъекции
    @Test
    public void testStatusResponseInjectionSQL() {
        String findText = "java' OR '1'='1'";
        System.out.println("Запрос Get на SQL инъекцию");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<VacanciesDto> responseEntity = restTemplate.getForEntity(urlEndpoint + "?text=" + findText, VacanciesDto.class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println(" Статус ответа: " + statusCode.name() + " " + statusCode.value());
        assertEquals(statusCode.value(),200);
    }
    //проверка поля text на инъекции
    @Test
    public void testStatusResponseInjectionSQL1() throws IOException {
        String findText = "java\"";
        URL url = new URL(urlEndpoint+"?text="+findText);
        System.out.println("Запрос Get на SQL инъекцию");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println(" Статус ответа: " + connection.getResponseMessage() + " " + statusCode);
        assertEquals( connection.getResponseCode(),200);
        connection.disconnect();
    }
    //проверка поля text на инъекции
    @Test
    public void testStatusResponseInjectionHTML() throws IOException {
        String findText = "java</body>";
        URL url = new URL(urlEndpoint+"?text="+findText);
        System.out.println("Запрос Get на HTML инъекцию");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        int statusCode = connection.getResponseCode();
        System.out.println(" Статус ответа: " + connection.getResponseMessage() + " " + statusCode);
        assertEquals( connection.getResponseCode(),200);
        connection.disconnect();
    }

}
