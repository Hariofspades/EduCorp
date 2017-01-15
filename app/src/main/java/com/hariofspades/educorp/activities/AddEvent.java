package com.hariofspades.educorp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.hariofspades.educorp.R;
import com.hariofspades.educorp.Validation.NotEmpty;
import com.hariofspades.educorp.interfaces.APIServices;
import com.hariofspades.educorp.model.ParentProfile;
import com.hariofspades.educorp.model.Tutor;
import com.hariofspades.educorp.model.TutorModel;
import com.hariofspades.educorp.model.orm.ParentORM;
import com.hariofspades.educorp.util.Field;
import com.hariofspades.educorp.util.Form;
import com.hariofspades.educorp.util.HelperSharedPreferences;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddEvent extends AppCompatActivity {

    private static final String TAG = "AddEvent";
    @BindView(R.id.input_name)AutoCompleteTextView name;
    @BindView(R.id.from)Button from;
    @BindView(R.id.to)Button to;
    @BindView(R.id.input_duration)EditText duration;
    @BindView(R.id.input_freqency)EditText frequency;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.fab)FloatingActionButton fab;

    ArrayList<String> autoArray;
    Form mForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        ButterKnife.bind(this);
        initView();
        initValidations();
        callAPIforAutoComplete();
        ClickListners();

    }

    private void callAPIforAutoComplete() {
        autoArray=new ArrayList<>();
        final Retrofit tutorRetrofit = new Retrofit.Builder()
                .baseUrl(" http://hackerearth.0x10.info/api/educorp/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIServices getTutors=tutorRetrofit.create(APIServices.class);
        Map<String, String> data = new HashMap<>();
        data.put("api_key", HelperSharedPreferences.getSharedPreferencesString(
                this,"api_key",""
        ));
        final Observable<TutorModel> parentRx = getTutors.listTutors(data);
        parentRx.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TutorModel>() {

                    @Override
                    public void onCompleted() {

                        Log.i(TAG,"Completed Executing");
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (AddEvent.this,android.R.layout.simple_list_item_1,
                                        autoArray);
                        name.setDropDownBackgroundResource(R.color.white);
                        name.setThreshold(2);//will start working from first character
                        name.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(TutorModel data) {
                       for(Tutor mode:data.getTutor())
                        autoArray.add(mode.getName());
                    }
                });
        Log.i(TAG,data.toString());
    }

    private void initValidations() {
        mForm=new Form(this);
        mForm.addField(Field.using(name).validate(NotEmpty.build(this)));
        mForm.addField(Field.using(duration).validate(NotEmpty.build(this)));
        mForm.addField(Field.using(frequency).validate(NotEmpty.build(this)));
    }

    private void ClickListners() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mForm.isValid()) {
                    if(!from.getText().toString().toLowerCase().equals("from")
                            ||!to.getText().toString().toLowerCase().equals("to")) {
                        Intent intent = new Intent();
                        intent.putExtra("name", name.getText().toString());
                        intent.putExtra("from", from.getText().toString());
                        intent.putExtra("to", to.getText().toString());
                        intent.putExtra("duration", duration.getText().toString());
                        intent.putExtra("frequency", frequency.getText().toString());
                        setResult(48, intent);
                        finish();
                    }else
                        Snackbar.make(view,"Please fill from and to",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ShowDatePickerDialog(from);
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDatePickerDialog(to);
            }
        });
    }

    private void ShowDatePickerDialog(final Button button){
        final Calendar calender = Calendar.getInstance();
        int mYear = calender.get(Calendar.YEAR);
        int mMonth = calender.get(Calendar.MONTH);
        int mDay = calender.get(Calendar.DAY_OF_MONTH);
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        button.setText(dayOfMonth + " "
                                + getMonth(monthOfYear + 1).substring(0,3) + ", " + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(new Date().getTime());
        dpd.show();
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    private void initView() {
        setupToolbar(toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });
    }
}
