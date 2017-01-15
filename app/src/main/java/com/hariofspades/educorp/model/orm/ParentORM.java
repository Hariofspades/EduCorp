package com.hariofspades.educorp.model.orm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Hari on 14/01/17.
 */

public class ParentORM extends SugarRecord{

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
    private String studentName;
    private String studentSchool;
    private String grade;
    private String subjects;


    public ParentORM(){

    }

    public ParentORM(String id,String name,String email,String accountType,
                     String address,String profilePicUrl,String zipcode,
                     String studentName,String studentSchool,String grade,
                     String subjects){
        this.iD=id;
        this.name=name;
        this.email=email;
        this.accountType=accountType;
        this.address=address;
        this.profilePicUrl=profilePicUrl;
        this.zipcode=zipcode;
        this.studentName=studentName;
        this.studentSchool=studentSchool;
        this.grade=grade;
        this.subjects=subjects;
    }


    public String getID() {
        return iD;
    }

    public void setID(String id) {
        this.iD = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAddress() {
        return address;
    }

    public String getGrade() {
        return grade;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentSchool() {
        return studentSchool;
    }

    public String getSubjects() {
        return subjects;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setStudentSchool(String studentSchool) {
        this.studentSchool = studentSchool;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}


