package com.enbiso.proj.schedulesms.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by farflk on 3/24/2014.
 */
public class AbstractModel {
    protected String _id;
    protected String _state;
    protected String _created;
    protected String _version;
    protected DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_created() {
        return _created;
    }

    public void set_created(String _created) {
        this._created = _created;
    }

    public String get_version() {
        return _version;
    }

    public void set_version(String _version) {
        this._version = _version;
    }

    public String get_state() {
        return _state;
    }

    public void set_state(String _state) {
        this._state = _state;
    }

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", get_id());
        contentValues.put("_state", get_state());
        contentValues.put("_created", get_created());
        contentValues.put("_version", get_version());
        return contentValues;
    }


    public void populateWith(Map<String, Object> data){
        this._id = fetchData(data, "_id");
        this._state = fetchData(data, "_state");
        this._created = fetchData(data, "_created");
        this._version = fetchData(data, "_version");
    }

    public String fetchData(Map<String, Object> data, String name, String def){
        String value = fetchData(data, name);
        if(value == null){
            value = def;
        }
        return value;
    }

    public String fetchData(Map<String, Object> data, String name){
        try {
            return data.get(name).toString();
        } catch(Exception e) {
            return null;
        }
    }

    public Integer fetchDataInteger(Map<String, Object> data, String name){
        String value = fetchData(data, name);
        if(value == null){
            return null;
        }else{
            try{
                return (int)Double.parseDouble(value);
            }catch (Exception e){
                return null;
            }
        }
    }

    public Double fetchDataDouble(Map<String, Object> data, String name){
        String value = fetchData(data, name);
        if(value == null){
            return null;
        }else{
            try{
                return Double.parseDouble(value);
            }catch (Exception e){
                return null;
            }
        }
    }

    public Calendar fetchDataCalender(Map<String, Object> data, String name){
        String value = fetchData(data, name);
        if(value == null){
            return null;
        }else{
            try{
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateTimeFormat.parse(value));
                return calendar;
            }catch (Exception e){
                return null;
            }
        }
    }

    public void populateWith(Cursor cursor, List<String> columns) {
        Map<String, Object> data = new HashMap<String, Object>();
        for(int i = 0; i < columns.size(); i++){
            String column = columns.get(i);
            String colName = column.substring(0, column.indexOf(' ', 0)).trim();
            int colIndex = cursor.getColumnIndex(colName);
            if(colIndex >= 0){
                data.put(colName, cursor.getString(colIndex));
            }
        }
        populateWith(data);
    }
}
