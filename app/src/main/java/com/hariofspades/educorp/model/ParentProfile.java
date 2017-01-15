package com.hariofspades.educorp.model;

import android.provider.ContactsContract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Hari on 14/01/17.
 */

public class ParentProfile {

    @SerializedName("profile")
    @Expose
    private List<PProfile> profile = null;

    public List<PProfile> getProfile() {
        return profile;
    }

    public void setProfile(List<PProfile> profile) {
        this.profile = profile;
    }
}
