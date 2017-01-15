package com.hariofspades.educorp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hari on 15/01/17.
 */

public class TutorProfileModel {

    @SerializedName("profile")
    @Expose
    private List<TProfile> profile = null;

    public List<TProfile> getTProfile() {
        return profile;
    }

    public void setTProfile(List<TProfile> profile) {
        this.profile = profile;
    }
}
