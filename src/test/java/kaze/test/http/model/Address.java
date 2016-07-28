package kaze.test.http.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class Address {
  
  @Size(min=7, max=7)
  public String zip;
  
  @NotEmpty
  public String pref;
}
