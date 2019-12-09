package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vacancy {
    public Integer id;
    //premium
    public String name;
    //department has_test response_letter_required
    public Area area;
    public Salary salary;
    public Type type;
    public Address address;
    //response_url //sort_point_distance
    public Employer employer;
    //published_at created_at archived apply_alternate_url insider_interview
    public String url;
    public String alternate_url;
    //relations
    public Snippet snippet;
  //  public Contacts contacts;
}
