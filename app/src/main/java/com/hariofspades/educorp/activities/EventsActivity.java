package com.hariofspades.educorp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hariofspades.educorp.R;
import com.hariofspades.educorp.listAdapter.RawEventsAdapter;
import com.hariofspades.educorp.model.orm.EventsORM;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 48;

    @BindView(R.id.fab)FloatingActionButton fab;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.eventList)RecyclerView mEventList;

    ArrayList<EventsORM> arrayList;
    RawEventsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);
        initViews();
        acquireBundle();
        ClickListners();
    }

    private void acquireBundle() {
        Bundle bundle=getIntent().getExtras();
        if(bundle.getInt("type")==0) {
            PopulateParentEvents();
        }else if(bundle.getInt("type")==1)
        {
            PopulateTutorEvents();
            fab.setVisibility(View.GONE);
        }
    }

    private void PopulateParentEvents() {
        arrayList=new ArrayList<>();
        EventsORM orm=new EventsORM();
        orm.setEventName("APJ Abdul Kalam");
        orm.setFrom("01 Feb, 2017");
        orm.setTo("01 Mar, 2017");
        orm.setDuration("1 Hour");
        orm.setFrequency("Daily");
        arrayList.add(orm);
        mAdapter.setFamily_Members(arrayList);
        mEventList.setAdapter(mAdapter);
    }

    private void PopulateTutorEvents() {
        arrayList=new ArrayList<>();
        EventsORM orm=new EventsORM();
        orm.setEventName("Hari Vignesh");
        orm.setFrom("01 Feb, 2017");
        orm.setTo("01 Mar, 2017");
        orm.setDuration("1 Hour");
        orm.setFrequency("Daily");
        arrayList.add(orm);
        mAdapter.setFamily_Members(arrayList);
        mEventList.setAdapter(mAdapter);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        setupToolbar(toolbar);
        mAdapter=new RawEventsAdapter(this);
        mEventList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void ClickListners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(
                        EventsActivity.this,AddEvent.class),REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            try {
                EventsORM orm = new EventsORM();
                orm.setEventName(data.getStringExtra("name"));
                orm.setFrom(data.getStringExtra("from"));
                orm.setTo(data.getStringExtra("to"));
                orm.setDuration(data.getStringExtra("duration"));
                orm.setFrequency(data.getStringExtra("frequency"));
                arrayList.add(0, orm);
                mAdapter.setFamily_Members(arrayList);
                mEventList.setAdapter(mAdapter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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

}
