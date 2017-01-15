package com.hariofspades.educorp.listAdapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.hariofspades.educorp.R;
import com.hariofspades.educorp.model.orm.EventsORM;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.hariofspades.educorp.activities.Dashboard.drawableToBitmap;

/**
 * Created by Hari on 15/01/17.
 */

public class RawEventsAdapter extends RecyclerView.Adapter<RawEventsAdapter.MyViewHolder>  {


    private ArrayList<EventsORM> members=new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    ColorGenerator generator;

    public RawEventsAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.events_card, parent, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventsORM orm=members.get(position);
        holder.tutor.setText(orm.getEventName());
        holder.range.setText(orm.getFrom()+" to "+orm.getTo());
        holder.duration.setText(orm.getDuration());
        holder.frequency.setText(orm.getFrequency());
        generator= ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(orm.getEventName().charAt(0)),
                        generator.getRandomColor());
        holder.imageView.setImageDrawable(drawable);
    }


    @Override
    public int getItemCount() {
        return members.size();
    }

    public void setFamily_Members(ArrayList<EventsORM> members){
        this.members=members;
        notifyItemRangeChanged(0,members.size());

    }

    public void addOneMember(EventsORM event){
        members.add(0,event);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tutor,range,duration,frequency;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tutor=(TextView)itemView.findViewById(R.id.tutor);
            range=(TextView)itemView.findViewById(R.id.range);
            duration=(TextView)itemView.findViewById(R.id.duration);
            frequency=(TextView)itemView.findViewById(R.id.frequency);
            imageView=(ImageView)itemView.findViewById(R.id.picture);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
