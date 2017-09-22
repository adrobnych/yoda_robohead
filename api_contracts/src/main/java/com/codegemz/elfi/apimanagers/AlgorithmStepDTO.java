package com.codegemz.elfi.apimanagers;

/**
 * Created by adrobnych on 8/23/15.
 */
public class AlgorithmStepDTO {
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    private int _id;
    private int priority;
    private String intent;
    private String value1;
    private String value2;

    public String getAlgorithm_name() {
        return algorithm_name;
    }

    public void setAlgorithm_name(String algorithm_name) {
        this.algorithm_name = algorithm_name;
    }

    private String algorithm_name;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
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

    public AlgorithmStepDTO(int priority, String intent, String value1, String value2, String algorithm_name) {
//        this._id = _id;
        this.priority = priority;
        this.intent = intent;
        this.value1 = value1;
        this.value2 = value2;
        this.algorithm_name = algorithm_name;
    }

    public AlgorithmStepDTO() {
//        this._id = 0;
        this.priority = 0;
        this.intent = null;
        this.value1 = null;
        this.value2 = null;
        algorithm_name = "alg_name";
    }
}
