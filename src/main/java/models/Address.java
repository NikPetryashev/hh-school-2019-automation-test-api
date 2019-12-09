package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    public String city;
    public String street;
    public String building;
    public String description;
    public Double lat;
    public Double lng;
    public String raw;
    //public Metro metro;
   // public Metro metro_stations;
    public Integer id;
}
