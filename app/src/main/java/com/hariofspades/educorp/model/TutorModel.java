package com.hariofspades.educorp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hari on 15/01/17.
 */

public class TutorModel {

    @SerializedName("tutor")
    @Expose
    private List<Tutor> tutor = null;

    public List<Tutor> getTutor() {
        return tutor;
    }

    public void setTutor(List<Tutor> tutor) {
        this.tutor = tutor;
    }
}
