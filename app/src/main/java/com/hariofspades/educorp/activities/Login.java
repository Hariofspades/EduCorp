package com.hariofspades.educorp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hariofspades.educorp.R;
import com.hariofspades.educorp.Validation.IsEmail;
import com.hariofspades.educorp.Validation.Name;
import com.hariofspades.educorp.Validation.NotEmpty;
import com.hariofspades.educorp.interfaces.APIServices;
import com.hariofspades.educorp.model.LoginModel;
import com.hariofspades.educorp.model.Signup;
import com.hariofspades.educorp.util.Field;
import com.hariofspades.educorp.util.Form;
import com.hariofspades.educorp.util.HelperSharedPreferences;
import com.hariofspades.incdeclibrary.IncDecCircular;

import java.util.ArrayList;
import java.util.HashMap;
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

public class Login extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    public static final String TAG = "Login";


    @BindView(R.id.container)ViewPager mViewPager;
    @BindView(R.id.tabs)TabLayout tabLayout;
    @SuppressLint("StaticFieldLeak")
    @BindView(R.id.incdec) IncDecCircular incDec;
    static ProgressBar progressBar;
    static String loginType="0";

    ArrayList<String> type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupArray();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    private void setupArray() {
        type=new ArrayList<>();
        type.add("Parent");
        type.add("Tutor");
        incDec.setConfiguration(LinearLayout.HORIZONTAL,IncDecCircular.TYPE_ARRAY,
                IncDecCircular.DECREMENT,IncDecCircular.INCREMENT);
        incDec.setArrayList(type);
        incDec.setArrayIndexes(0,1,1);
        incDec.enableLongPress(false,false,500);
        incDec.setOnValueChangeListener(new IncDecCircular.OnValueChangeListener() {
            @Override
            public void onValueChange(IncDecCircular view, float oldValue, float newValue) {
                loginType=String.valueOf((int)newValue);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class LoginFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        Button login;

        EditText email,password;

        View rootView;

        Form mForm;

        public LoginFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LoginFragment newInstance(int sectionNumber) {
            LoginFragment fragment = new LoginFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_login, container, false);
            initViews(rootView);
            initValidation();
            clickListeners();
            return rootView;
        }

        private void clickListeners() {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mForm.isValid()){
                        callAPIs();
                    }
                }
            });
        }

        private void callAPIs() {
            final Retrofit loginpRetrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(" http://hackerearth.0x10.info/api/educorp/")
                    .build();
            APIServices loginService=loginpRetrofit.create(APIServices.class);
            Map<String, String> data = new HashMap<>();
            data.put("query", "login");
            data.put("email", email.getText().toString());
            data.put("password",password.getText().toString());
            data.put("type",loginType);
            Log.i(TAG,data.toString());
            final Observable<LoginModel> loginRx = loginService.makeLogin(data);
            loginRx.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LoginModel>() {

                        @Override
                        public void onCompleted() {
                            Log.i(TAG,"Completed Executing");
                            progressBar.setIndeterminate(false);
                            progressBar.setVisibility(View.GONE);
                            clearTexts();
                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.i(TAG,e.getMessage());
                        }

                        @Override
                        public void onNext(LoginModel data) {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setIndeterminate(true);
                            if(!data.getApiKey().equals("")) {
                                HelperSharedPreferences.putSharedPreferencesString(
                                        getActivity(),"api_key",data.getApiKey()
                                );
                                getActivity().finish();
                                startActivity(new Intent(getActivity(),Dashboard.class));
                                HelperSharedPreferences.putSharedPreferencesString(getActivity(),
                                        "type",loginType);
                                Log.i(TAG,data.getApiKey());
                            }
                            else {
                                Snackbar.make(rootView, data.getError(), Snackbar.LENGTH_LONG).show();
                                Log.i(TAG, data.getError());
                            }
                        }

                    });
        }

        private void initViews(View rootView) {
            login=(Button) rootView.findViewById(R.id.button_login);
            email=(EditText)rootView.findViewById(R.id.input_email);
            password=(EditText)rootView.findViewById(R.id.input_password);
        }

        private void initValidation() {
            mForm=new Form(getActivity());
            mForm.addField(Field.using(email).validate(IsEmail.build(getActivity())));
            mForm.addField(Field.using(password).validate(NotEmpty.build(getActivity())));
        }

        private void clearTexts() {
            email.setText("");
            password.setText("");
        }
    }


    public static class SignupFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        View rootView;

        EditText name,sign_email,sign_password;
        TextInputLayout layout_name,layout_sign_email,layout_sign_password;
        Button signup;

        Form mForm;

        public SignupFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SignupFragment newInstance(int sectionNumber) {
            SignupFragment fragment = new SignupFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.signup_fragment, container, false);
            initViews(rootView);
            initValidation();
            ClickListeners();
            return rootView;
        }

        private void initViews(View rootView) {
            name=(EditText)rootView.findViewById(R.id.input_name);
            sign_email=(EditText)rootView.findViewById(R.id.input_email);
            sign_password=(EditText)rootView.findViewById(R.id.input_password);
            signup=(Button)rootView.findViewById(R.id.button_signup);
        }

        private void ClickListeners() {
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mForm.isValid()){
                        CallAPIs();
                    }

                }
            });
        }

        private void CallAPIs() {
            final Retrofit signupRetrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(" http://hackerearth.0x10.info/api/educorp/")
                    .build();
            APIServices signupService=signupRetrofit.create(APIServices.class);
            Map<String, String> data = new HashMap<>();
            data.put("query", "register");
            data.put("name", name.getText().toString());
            data.put("email", sign_email.getText().toString());
            data.put("password",sign_password.getText().toString());
            data.put("type",loginType);
            Log.i(TAG,data.toString());
            final Observable<Signup> signupRx = signupService.makeSignup(data);
            signupRx.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Signup>() {

                        @Override
                        public void onCompleted() {
                            Log.i(TAG,"Completed Executing");
                            progressBar.setIndeterminate(false);
                            progressBar.setVisibility(View.GONE);
                            clearTexts();
                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.i(TAG,e.getMessage());
                        }

                        @Override
                        public void onNext(Signup data) {
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setIndeterminate(true);
                            if(!data.getMessage().equals(""))
                                Snackbar.make(rootView,data.getMessage(),Snackbar.LENGTH_LONG).show();
                            else
                                Snackbar.make(rootView,data.getError(),Snackbar.LENGTH_LONG).show();
                            Log.i(TAG,data.getMessage());
                            Log.i(TAG,data.getStatus());
                            Log.i(TAG,data.getError());
                        }

                    });
        }

        private void clearTexts() {
            name.setText("");
            sign_email.setText("");
            sign_password.setText("");
        }

        private void initValidation() {
            mForm=new Form(getActivity());
            mForm.addField(Field.using(name).validate(Name.build(getActivity())));
            mForm.addField(Field.using(sign_email).validate(IsEmail.build(getActivity())));
            mForm.addField(Field.using(sign_password).validate(NotEmpty.build(getActivity())));
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==0)
                return LoginFragment.newInstance(position);
            else
                return SignupFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "LOGIN";
                case 1:
                    return "SIGNUP";
            }
            return null;
        }
    }

    public static boolean isEmpty(EditText editText){
        if(editText.getText().toString().equals(""))
            return true;
        else
            return false;
    }

    public static void setError(TextInputLayout component, String msg){
        component.setError(msg);
    }
}

