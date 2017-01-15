package com.hariofspades.educorp.model;

/**
 * Created by Hari on 14/01/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("api_key")
    @Expose
    private String apiKey="";

    @SerializedName("error")
    @Expose
    private String error="";

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
