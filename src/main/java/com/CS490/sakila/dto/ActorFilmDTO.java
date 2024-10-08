package com.CS490.sakila.dto;

import java.util.List;

public class ActorFilmDTO {
    private String firstName;
    private String lastName;
    private List<String> top5Films;

    public ActorFilmDTO(String firstName, String lastName, List<String> top5Films) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.top5Films = top5Films;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getTop5Films() {
        return top5Films;
    }

    public void setTop5Films(List<String> top5Films) {
        this.top5Films = top5Films;
    }
}
