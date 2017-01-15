package com.hariofspades.educorp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.hariofspades.educorp.R;
import com.hariofspades.educorp.interfaces.APIServices;
import com.hariofspades.educorp.listAdapter.StudentEventsAdapter;
import com.hariofspades.educorp.model.ParentProfile;
import com.hariofspades.educorp.model.orm.ParentORM;
import com.hariofspades.educorp.model.orm.TutorORM;
import com.hariofspades.educorp.util.HelperSharedPreferences;
import com.vansuita.pickimage.PickImageDialog;
import com.vansuita.pickimage.PickSetup;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
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

import static com.hariofspades.educorp.activities.Dashboard.drawableToBitmap;

public class ProfileTutorActivity extends AppCompatActivity  implements IPickResult {

    private static final String TAG = "ProfileTutorActivity";
    @BindView(R.id.fab)FloatingActionButton fab;
    @BindView(R.id.pic_Edit)FloatingActionButton edit;
    @BindView(R.id.profile_pic)CircleImageView mProfile;
    @BindView(R.id.input_name)EditText mName;
    @BindView(R.id.input_address)EditText mAddress;
    @BindView(R.id.input_pincode)EditText mPincode;
    @BindView(R.id.input_bio)EditText mBio;
    @BindView(R.id.addNewStudent)Button newStudent;
    @BindView(R.id.studentRecyclerView)RecyclerView recyclerView;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.accType)TextView accountType;

    int mode=0;
    Long id;
    String url="";
    StudentEventsAdapter mAdapter;
    String subjectConcatString="";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tutor);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mAdapter=new StudentEventsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupToolbar(toolbar);
        populateAllValues();
        clickListeners();
        newStudent.setEnabled(false);
        edit.setVisibility(View.GONE);
        newStudent.setBackgroundColor(
                ContextCompat.getColor(this,R.color.secondary_text));
        AllEditTexts(false);
        dialog=new ProgressDialog(this);
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
    }

    private void clickListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode==0) {
                    inVokeSnack("You can now edit your profile",view);
                    fab.setImageResource(R.drawable.ic_done_black_24dp);
                    AllEditTexts(true);
                    mode=1;
                    fab.setBackgroundTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(ProfileTutorActivity.this,
                                    R.color.colorGreen)));
                    newStudent.setEnabled(true);
                    edit.setVisibility(View.VISIBLE);
                    newStudent.setBackgroundColor(
                            ContextCompat.getColor(ProfileTutorActivity.this,
                                    R.color.colorAccent));
                }else {
                    newStudent.setEnabled(false);
                    edit.setVisibility(View.GONE);
                    newStudent.setBackgroundColor(
                            ContextCompat.getColor(ProfileTutorActivity.this,
                                    R.color.secondary_text));
                    AllEditTexts(false);
                    mode=0;
                    fab.setImageResource(R.drawable.ic_border_color_black_24dp);
                    fab.setBackgroundTintList(ColorStateList.valueOf(
                            ContextCompat.getColor(ProfileTutorActivity.this,
                                    R.color.colorAccent)));
                    updateItemsinThread();
                }

            }
        });

        newStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.on(ProfileTutorActivity.this, new PickSetup());
            }
        });

    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        // Set up the input
        final EditText input = new EditText(this);
        input.setText(subjectConcatString);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                subjectConcatString=input.getText().toString();
                populateRecyclerView(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void updateItemsinThread() {
        updateDB();
        updateAPI();
    }


    private void updateAPI() {
        final Retrofit profile = new Retrofit.Builder()
                .baseUrl(" http://hackerearth.0x10.info/api/educorp/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIServices parentProfileService=profile.create(APIServices.class);
        Map<String, String> data = new HashMap<>();
        data.put("query", "edit");
        data.put("api_key", HelperSharedPreferences.getSharedPreferencesString(
                this,"api_key",""
        ));
        data.put("address",mAddress.getText().toString());
        data.put("name",mName.getText().toString());
        data.put("zipcode",mPincode.getText().toString());
        data.put("bio",mBio.getText().toString());
        data.put("students",subjectConcatString);
        data.put("profile_pic_url",url);
        Log.i(TAG,data.toString());
        final Observable<ParentProfile> parentRx = parentProfileService.listProfile(data);
        parentRx.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ParentProfile>() {

                    @Override
                    public void onCompleted() {
                        Log.i(TAG,"Completed Executing");
                        Toast.makeText(ProfileTutorActivity.this,
                                "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.i(TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(ParentProfile data) {

                    }
                });
    }

    private void updateDB() {
        List<TutorORM> notes = TutorORM.find(TutorORM.class, "id = ?",
                String.valueOf(id));
        if (notes.size() > 0) {
            TutorORM updateProfile = notes.get(0);
            updateProfile.setId(id);
            updateProfile.setName(mName.getText().toString());
            updateProfile.setAddress(mAddress.getText().toString());
            updateProfile.setZipcode(mPincode.getText().toString());
            updateProfile.setBio(mBio.getText().toString());
            updateProfile.setStudents(subjectConcatString);
            updateProfile.setProfilePicUrl(url);
            updateProfile.save();
        }
    }

    private void populateAllValues() {
        List<TutorORM> books = TutorORM.listAll(TutorORM.class);
        id=books.get(0).getId();
        mName.setText(books.get(0).getName());
        mAddress.setText(books.get(0).getAddress());
        mPincode.setText(books.get(0).getZipcode());
        mBio.setText(books.get(0).getBio());
        accountType.setText(books.get(0).getAccountType());
        SetProfilePic(
                String.valueOf(books.get(0).getName().toUpperCase().charAt(0)
                ),books.get(0).getProfilePicUrl());
        subjectConcatString=books.get(0).getStudents();
        populateRecyclerView(subjectConcatString);
    }

    private void populateRecyclerView(String subjects) {
        String[] array=subjects.split(",");
        ArrayList<String> subject=new ArrayList<>();
        for(String arr:array)
            subject.add(arr);
        mAdapter.setFamily_Members(subject);
        recyclerView.setAdapter(mAdapter);
    }

    private void SetProfilePic(String name,String url) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(String.valueOf(name.charAt(0)),
                        ContextCompat.getColor(this, R.color.colorAccent));
        Drawable draw = new BitmapDrawable(drawableToBitmap(drawable));
        if(url.equals("")) {
            mProfile.setImageDrawable(draw);
        }else {
            Glide
                    .with(this)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.weather)
                    .error(draw)
                    .dontAnimate()
                    .into(mProfile);
        }
    }

    private void AllEditTexts(boolean value) {
        mName.setEnabled(value);
        mAddress.setEnabled(value);
        mBio.setEnabled(value);
        mPincode.setEnabled(value);
    }

    private void inVokeSnack(String message,View view){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_parent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, grabAllValues());
            startActivity(Intent.createChooser(sharingIntent, "Share profile using"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String grabAllValues() {
        String concat="Tutor Name : "+mName.getText().toString()+
                "\nTutor Bio : "+mBio.getText().toString()+
                "\nAddress : "+mAddress.getText().toString()+
                "\nZipcode : "+mPincode.getText().toString()+
                "\nStudents : "+subjectConcatString;
        return concat;
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            //If you want the Bitmap.
            mProfile.setImageBitmap(r.getBitmap());
            dialog.setMessage("Uploading the Image...");
            dialog.show();
            final File file=new File(r.getUri().getPath());
            Log.i(TAG,String.valueOf(r.getUri().getPath()));
            Log.i(TAG,file.getName());

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            r.getBitmap().compress(Bitmap.CompressFormat.PNG, 50, stream);
            final byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            //Image path.
            r.getPath();
            Observable.just(url)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(String s) {
                            dialog.show();
                            String encodedString = Base64.encodeToString(byte_arr, 0);
                            handleUpload(file.getName(),encodedString);
                        }
                    });
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
        }
    }

    private void handleUpload(String file, String str64) {
        AndroidNetworking.post("https://hackerearth.0x10.info/api/educorp/profile_picture")
                .addBodyParameter("filename", file)
                .addBodyParameter("image", str64)
                .setTag("imageUpload")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        dialog.dismiss();
                        Log.i(TAG,response.toString());
                        try {
                            url=response.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        dialog.dismiss();
                        Log.i(TAG,error.toString());
                    }
                });
    }
}
