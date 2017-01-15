package com.hariofspades.educorp.interfaces;

import com.hariofspades.educorp.model.LoginModel;
import com.hariofspades.educorp.model.ParentProfile;
import com.hariofspades.educorp.model.Signup;
import com.hariofspades.educorp.model.TutorModel;
import com.hariofspades.educorp.model.TutorProfileModel;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Hari on 14/01/17.
 */

public interface APIServices {

    @GET("auth?")
    Observable<Signup> makeSignup(@QueryMap Map<String,String> options);

    @GET("auth?")
    Observable<LoginModel> makeLogin(@QueryMap Map<String,String> options);

    @GET("profile?")
    Observable<ParentProfile> listProfile(@QueryMap Map<String,String> options);

    @GET("list_tutor?")
    Observable<TutorModel> listTutors(@QueryMap Map<String,String> options);

    @GET("profile?")
    Observable<TutorProfileModel> listTutProfile(@QueryMap Map<String,String> options);

}
