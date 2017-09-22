package com.codegemz.elfi.apimanagers;

/**
 * Created by adrobnych on 4/2/16.
 */
public class StateDTO {

    private int id;

    private String name;

    private String value1;

    private String value2;

    public StateDTO(String name, String value1, String value2) {
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }
}
