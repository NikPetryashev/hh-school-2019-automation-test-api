package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Employer {
    public Integer id;
    public String name;
    public String url;
    public String alternate_url;
    public String vacancies_url;
    //logo_urls
    public Boolean trusted;
}
