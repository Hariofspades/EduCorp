package com.hariofspades.educorp.model.orm;

import com.orm.SugarRecord;

/**
 * Created by Hari on 15/01/17.
 */

public class EventsORM  extends SugarRecord{

    private String eventName,froms,tos,duration,frequency,id;

    public EventsORM(){

    }

    public String getDuration() {
        return duration;
    }

    public String getEventName() {
        return eventName;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getFrom() {
        return froms;
    }

    public String getTo() {
        return tos;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setFrom(String froms) {
        this.froms = froms;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTo(String tos) {
        this.tos = tos;
    }
}
