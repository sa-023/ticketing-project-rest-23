package com.company.enums;

public enum Gender { // · To create an enum with space, we use the below method. Ex: STATUS("IN PROGRESS")

    MALE("Male"), FEMALE("Female");
    private final String value;
    Gender(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

}
