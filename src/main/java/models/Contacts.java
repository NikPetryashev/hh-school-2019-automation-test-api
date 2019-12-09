package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contacts {
    public String name;
    public String email;
    public List<Phones> phones;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Phones {
    public String comment;
    public Integer city;
    public Integer number;
    public Integer country;
}
