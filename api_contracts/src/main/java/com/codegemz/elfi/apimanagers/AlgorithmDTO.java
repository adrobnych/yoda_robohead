package com.codegemz.elfi.apimanagers;

/**
 * Created by adrobnych on 8/23/15.
 */
public class AlgorithmDTO {
    private int _id;
    private String name;
    private String type;
    private String group_name;
    private String value1;
    private String value2;

    public String getAlgorithm_bundle_name() {
        return algorithm_bundle_name;
    }

    public void setAlgorithm_bundle_name(String algorithm_bundle_name) {
        this.algorithm_bundle_name = algorithm_bundle_name;
    }

    private String algorithm_bundle_name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public AlgorithmDTO(String name, String type, String group_name, String value1, String value2, String algorithm_bundle_name) {
//        this._id = _id;
        this.name = name;
        this.type = type;
        this.group_name = group_name;
        this.value1 = value1;
        this.value2 = value2;
        this.algorithm_bundle_name = algorithm_bundle_name;
    }

    public AlgorithmDTO() {
//        this._id = 0;
        this.name = "";
        this.type = "";
        this.group_name = "";
        this.value1 = "";
        this.value2 = "";
        this.algorithm_bundle_name = "";
    }
}
