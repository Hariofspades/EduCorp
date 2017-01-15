package com.hariofspades.educorp.model;

/**
 * Created by Hari on 14/01/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Signup {

    @SerializedName("status")
    @Expose
    private String status="";
    @SerializedName("error")
    @Expose
    private String error="";
    @SerializedName("message")
    @Expose
    private String message="";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
