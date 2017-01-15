package com.hariofspades.educorp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.hariofspades.educorp.R;
import com.hariofspades.educorp.interfaces.APIServices;
import com.hariofspades.educorp.model.LoginModel;
import com.hariofspades.educorp.model.PProfile;
import com.hariofspades.educorp.model.ParentProfile;
import com.hariofspades.educorp.model.TutorProfileModel;
import com.hariofspades.educorp.model.orm.ParentORM;
import com.hariofspades.educorp.model.orm.TutorORM;
import com.hariofspades.educorp.util.HelperSharedPreferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hariofspades.educorp.activities.Login.loginType;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Dashboard";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout)DrawerLayout drawer;
    @BindView(R.id.nav_view)NavigationView navigationView;
    @BindView(R.id.userName)TextView userName;
    @BindView(R.id.email)TextView profileType;
    @BindView(R.id.my_profile)RelativeLayout myProfile;
    @BindView(R.id.my_events)RelativeLayout myEvents;
    @BindView(R.id.profile_pic)CircleImageView mPic;

    ActionBarDrawerToggle toggle;

    String id="",name="",email="",account="",address="",pic="",zip="",students="",bio="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Clicklisteners();


    }

    private void SetProfilePic(String name,String url) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(String.valueOf(name.charAt(0)),
                        ContextCompat.getColor(this, R.color.colorAccent));
        Drawable draw = new BitmapDrawable(drawableToBitmap(drawable));
        if(url.equals("")) {
            mPic.setImageDrawable(draw);
        }else {
            Glide
                    .with(this)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.weather)
                    .error(draw)
                    .dontAnimate()
                    .into(mPic);
        }
    }

    private void Clicklisteners() {
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateType().equals("Parent"))
                {
                    startActivity(new Intent(Dashboard.this,ProfileParentActivity.class));
                }
                else{
                    startActivity(new Intent(Dashboard.this,ProfileTutorActivity.class));
                }
            }
        });

        myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this,EventsActivity.class);
                if(validateType().equals("Parent"))
                {
                    intent.putExtra("type",0);

                }
                else{
                    intent.putExtra("type",1);
                }
                startActivity(intent);
            }
        });
    }

    private void GetTutorProfileDetails() {
        final Retrofit profile = new Retrofit.Builder()
                .baseUrl(" http://hackerearth.0x10.info/api/educorp/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIServices parentProfileService=profile.create(APIServices.class);
        Map<String, String> data = new HashMap<>();
        data.put("query", "list");
        data.put("api_key",HelperSharedPreferences.getSharedPreferencesString(
                this,"api_key",""
        ));
        Log.i(TAG,data.toString());
        final Observable<TutorProfileModel> parentRx = parentProfileService.listTutProfile(data);
        parentRx.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TutorProfileModel>() {

                    @Override
                    public void onCompleted() {
                        Log.i(TAG,"Completed Executing");
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.i(TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(TutorProfileModel data) {
                        Log.i(TAG,data.getTProfile().get(0).getName());
                        userName.setText(data.getTProfile().get(0).getName().toUpperCase());
                        profileType.setText(validateType());
                        if(AddAccountID(data.getTProfile().get(0).getId())) {
                            try {
                                TutorORM saveProfile = new TutorORM(
                                        data.getTProfile().get(0).getId(),
                                        data.getTProfile().get(0).getName(),
                                        data.getTProfile().get(0).getEmail(),
                                        data.getTProfile().get(0).getAccountType(),
                                        data.getTProfile().get(0).getProfile().getAddress(),
                                        data.getTProfile().get(0).getProfile().getProfilePicUrl(),
                                        data.getTProfile().get(0).getProfile().getZipcode(),
                                        data.getTProfile().get(0).getProfile().getStudents(),
                                        data.getTProfile().get(0).getProfile().getBio()
                                );
                                saveProfile.save();
                            }catch (Exception e){
                                TutorORM saveProfile = new TutorORM(
                                        data.getTProfile().get(0).getId(),
                                        data.getTProfile().get(0).getName(),
                                        data.getTProfile().get(0).getEmail(),
                                        data.getTProfile().get(0).getAccountType(),
                                       "",
                                        "",
                                        "",
                                        "",
                                        "");
                                saveProfile.save();
                            }

                        }else{
                            List<TutorORM> notes = TutorORM.find(TutorORM.class, "id = ?",
                                    data.getTProfile().get(0).getId());
                            if (notes.size() > 0) {
                                TutorORM updateProfile = notes.get(0);
                                    updateProfile.setID(data.getTProfile().get(0).getId());
                                    updateProfile.setName(data.getTProfile().get(0).getName());
                                    updateProfile.setEmail(data.getTProfile().get(0).getEmail());
                                    updateProfile.setAccountType(data.getTProfile().get(0).getAccountType());
                                    updateProfile.setAddress(data.getTProfile().get(0).getProfile().getAddress());
                                    updateProfile.setZipcode(data.getTProfile().get(0).getProfile().getZipcode());
                                    updateProfile.setBio(data.getTProfile().get(0).getProfile().getBio());
                                    updateProfile.setStudents(data.getTProfile().get(0).getProfile().getStudents());
                                    if(data.getTProfile().get(0).getProfile().getProfilePicUrl()!=null)
                                        updateProfile.setProfilePicUrl(data.getTProfile().get(0).
                                                getProfile().getProfilePicUrl());
                                    else
                                        updateProfile.setProfilePicUrl("");
                                    updateProfile.save();

                            }
                        }
                        try {
                            SetProfilePic(String.valueOf(data.getTProfile().get(0).getName().toUpperCase().charAt(0)),
                                    data.getTProfile().get(0).getProfile().getProfilePicUrl());
                        }catch (Exception e){
                            SetProfilePic(String.valueOf(data.getTProfile().get(0).getName().toUpperCase().charAt(0)),
                                    "");
                        }
                    }
                });
    }

    private String validateType() {
        if(HelperSharedPreferences.getSharedPreferencesString(this,
                "type","").equals("0"))
            return "Parent";
        else
            return "Tutor";
    }

    private void GetParentProfileDetails() {
        final Retrofit profile = new Retrofit.Builder()
                .baseUrl(" http://hackerearth.0x10.info/api/educorp/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIServices parentProfileService=profile.create(APIServices.class);
        Map<String, String> data = new HashMap<>();
        data.put("query", "list");
        data.put("api_key",HelperSharedPreferences.getSharedPreferencesString(
                this,"api_key",""
        ));
        Log.i(TAG,data.toString());
        final Observable<ParentProfile> parentRx = parentProfileService.listProfile(data);
        parentRx.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ParentProfile>() {

                    @Override
                    public void onCompleted() {
                        Log.i(TAG,"Completed Executing");
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.i(TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(ParentProfile data) {
                        userName.setText(data.getProfile().get(0).getName().toUpperCase());
                        profileType.setText(validateType());
                        try {
                            SetProfilePic(String.valueOf(data.getProfile().get(0).getName().toUpperCase().charAt(0)),
                                    data.getProfile().get(0).getProfile().getProfilePicUrl());
                        }catch (Exception e){
                            SetProfilePic(String.valueOf(data.getProfile().get(0).getName().toUpperCase().charAt(0)),
                                    "");
                        }
                        if(AddAccountID(data.getProfile().get(0).getId())) {
                            try {
                                ParentORM saveProfile = new ParentORM(
                                        data.getProfile().get(0).getId(),
                                        data.getProfile().get(0).getName(),
                                        data.getProfile().get(0).getEmail(),
                                        data.getProfile().get(0).getAccountType(),
                                        data.getProfile().get(0).getProfile().getAddress(),
                                        data.getProfile().get(0).getProfile().getProfilePicUrl(),
                                        data.getProfile().get(0).getProfile().getZipcode(),
                                        data.getProfile().get(0).getProfile().getStudent().getName(),
                                        data.getProfile().get(0).getProfile().getStudent().getSchool(),
                                        data.getProfile().get(0).getProfile().getStudent().getGrade(),
                                        data.getProfile().get(0).getProfile().getSubjects()
                                );
                                saveProfile.save();
                            }catch (Exception e){
                                ParentORM saveProfile = new ParentORM(
                                        data.getProfile().get(0).getId(),
                                        data.getProfile().get(0).getName(),
                                        data.getProfile().get(0).getEmail(),
                                        data.getProfile().get(0).getAccountType(),
                                        "", "", "", "", "", "",""
                                );
                                saveProfile.save();
                            }
                        }else{
                            List<ParentORM> notes = ParentORM.find(ParentORM.class, "id = ?",
                                    data.getProfile().get(0).getId());
                            if (notes.size() > 0) {
                                try {
                                    ParentORM updateProfile = notes.get(0);
                                    updateProfile.setID(data.getProfile().get(0).getId());
                                    updateProfile.setName(data.getProfile().get(0).getName());
                                    updateProfile.setEmail(data.getProfile().get(0).getEmail());
                                    updateProfile.setAccountType(data.getProfile().get(0).getAccountType());
                                    updateProfile.setAddress(data.getProfile().get(0).getProfile().getAddress());
                                    updateProfile.setZipcode(data.getProfile().get(0).getProfile().getZipcode());
                                    updateProfile.setStudentName(data.getProfile().get(0).getProfile().getStudent().getName());
                                    updateProfile.setStudentSchool(data.getProfile().get(0).getProfile().getStudent().getSchool());
                                    updateProfile.setGrade(data.getProfile().get(0).getProfile().getStudent().getGrade());
                                    updateProfile.setSubjects(data.getProfile().get(0).getProfile().getSubjects());
                                    updateProfile.setProfilePicUrl(data.getProfile().get(0).
                                            getProfile().getProfilePicUrl());
                                    updateProfile.save();
                                }catch (Exception e){
                                    ParentORM updateProfile = notes.get(0);
                                    updateProfile.setID(data.getProfile().get(0).getId());
                                    updateProfile.setName(data.getProfile().get(0).getName());
                                    updateProfile.setEmail(data.getProfile().get(0).getEmail());
                                    updateProfile.setAccountType(data.getProfile().get(0).getAccountType());
                                    updateProfile.setAddress("");
                                    updateProfile.setZipcode("");
                                    updateProfile.setStudentName("");
                                    updateProfile.setStudentSchool("");
                                    updateProfile.setGrade("");
                                    updateProfile.setSubjects("");
                                    updateProfile.setProfilePicUrl("");
                                    updateProfile.save();
                                }
                            }
                        }
                    }
                });
    }


    private boolean AddAccountID(String id) {
        if(HelperSharedPreferences.getSharedPreferencesString(this,
                "account_id","").equals("")) {
            HelperSharedPreferences.putSharedPreferencesString(this,
                    "account_id", id);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(HelperSharedPreferences.getSharedPreferencesString(this,
                "api_key","").equals("")){
            startActivity(new Intent(this,Login.class));
            finish();
        }else{
            if(validateType().equals("Parent")) {
                GetParentProfileDetails();
            }
            else {
                GetTutorProfileDetails();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_linkedn) {
            // Handle the camera action
            String url = "https://in.linkedin.com/in/harivignesh";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        }else if(id==R.id.nav_medium){
            String url = "https://medium.com/@harivigneshjayapalan";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));

        }else if(id==R.id.nav_github){
            String url = "https://github.com/Hariofspades";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 96; // Replaced the 1 by a 96
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 96; // Replaced the 1 by a 96

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
