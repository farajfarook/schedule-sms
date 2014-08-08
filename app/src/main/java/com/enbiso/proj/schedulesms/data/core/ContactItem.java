package com.enbiso.proj.schedulesms.data.core;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ImageView;

import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.AbstractModel;

import java.util.Map;

/**
 * Created by farflk on 7/30/2014.
 */
public class ContactItem extends AbstractModel{
    private String name;
    private String phone;
    private Uri photo;

    @Override
    public ContentValues getContentValues() {
        ContentValues contentValues = super.getContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        if (photo != null){
            contentValues.put("photo", photo.toString());
        }
        return contentValues;
    }

    @Override
    public void populateWith(Map<String, Object> data) {
        super.populateWith(data);
        name = fetchData(data, "name");
        phone = fetchData(data, "phone");
        String photoUrl = fetchData(data, "photo");
        if(photoUrl != null) {
            photo = Uri.parse(photoUrl);
        }
    }

    public ContactItem() {
    }

    public ContactItem(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public ContactItem(String phone) {
        this.phone = phone;
    }

    public ContactItem(String name, String phone, Uri photo) {
        this.name = name;
        this.phone = phone;
        this.photo = photo;
    }

    public Uri getPhoto(Context context) {
        if(photo == null){
            return null;
        }else {
            return photo;
        }
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public String getName(String defaultName) {
        if(name == null){
            return defaultName;
        }else {
            return name;
        }
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ContactItem) {
            ContactItem contactItem = (ContactItem)o;
            return this.phone.equals(contactItem.getPhone());
        }else{
            return false;
        }
    }
}
