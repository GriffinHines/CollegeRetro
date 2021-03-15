package com.retro.collegeretro.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Belongs to one user. A user can have up to 3 addresses saved.
 * This contains a required line one, an optional line 2, a zip
 * (10 digits), and a state.
 */
@Data
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String primaryLine;
    private String optionalLine;
    private long zipCode;
    private String city;
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryLine() {
        return primaryLine;
    }

    public void setPrimaryLine(String primaryLine) {
        this.primaryLine = primaryLine;
    }

    public String getOptionalLine() {
        return optionalLine;
    }

    public void setOptionalLine(String optionalLine) {
        this.optionalLine = optionalLine;
    }

    public long getZipCode() {
        return zipCode;
    }

    public void setZipCode(long zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
