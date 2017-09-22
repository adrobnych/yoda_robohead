package com.codegemz.elfi.apimanagers;

/**
 * Created by adrobnych on 6/2/15.
 */
public class AlgorithmBundleDTO {
    public AlgorithmBundleDTO(String name, String url) {
        //this._id = _id;
        this.name = name;
        this.url = url;
    }

    public AlgorithmBundleDTO() {
        //this._id = 0;
        this.name = "";
        this.url = "";
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private int _id;
    private String name;
    private String url;
}
