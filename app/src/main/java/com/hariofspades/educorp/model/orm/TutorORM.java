package com.hariofspades.educorp.model.orm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hariofspades.educorp.model.Tutor;
import com.orm.SugarRecord;

/**
 * Created by Hari on 15/01/17.
 */

public class TutorORM extends SugarRecord {

    @SerializedName("id")
    @Expose
    private String iD;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    private String address;
    private String profilePicUrl;
    private String zipcode;
    private String bio;
    private String students;


    public TutorORM(String id,String name,String email,String accountType,
                     String address,String profilePicUrl,String zipcode,
                     String bio,String students){
        this.iD=id;
        this.name=name;
        this.email=email;
        this.accountType=accountType;
        this.address=address;
        this.profilePicUrl=profilePicUrl;
        this.zipcode=zipcode;
        this.students=students;
        this.bio=bio;
    }

    public TutorORM(){

    }
    public String getID() {
        return iD;
    }

    public void setID(String id) {
        this.iD = id;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAddress() {
        return address;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getStudents() {
        return students;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}

