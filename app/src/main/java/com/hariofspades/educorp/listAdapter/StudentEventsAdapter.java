package com.hariofspades.educorp.listAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hariofspades.educorp.R;
import com.hariofspades.educorp.model.orm.ParentORM;

import java.util.ArrayList;

/**
 * Created by Hari on 15/01/17.
 */

public class StudentEventsAdapter extends RecyclerView.Adapter<StudentEventsAdapter.MyViewHolder> {


    private ArrayList<String> members=new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;

    public StudentEventsAdapter(Context context) {
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.list_item, parent, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemName.setText(members.get(position).trim());
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void setFamily_Members(ArrayList<String> members){
        this.members=members;
        notifyItemRangeChanged(0,members.size());

    }

    public void setOneitem(String name){
        members.add(0,name);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView itemName;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemName=(TextView)itemView.findViewById(R.id.itemName);

        }

        @Override
        public void onClick(View v) {

        }
    }

    public void removeAt(int position) {
        members.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, members.size());
    }

}
