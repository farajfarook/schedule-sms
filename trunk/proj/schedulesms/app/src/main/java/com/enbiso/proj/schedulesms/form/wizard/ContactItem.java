package com.enbiso.proj.schedulesms.form.wizard;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.enbiso.proj.schedulesms.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farflk on 7/30/2014.
 */
public class ContactItem {
    private String name;
    private String number;

    public ContactItem(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public ContactItem(String number) {
        this.number = number;
        this.name = "unknown";
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public static List<ContactItem> fetchContactItems(Context context, String nameSearch){
        List<ContactItem> contactItems = new ArrayList<ContactItem>();
        ContentResolver contentResolver = ((MainActivity) context).getContentResolver();
        String selection = null;
        String[] selectionArgs = null;
        if(nameSearch != null){
            if(!nameSearch.equals("")) {
                nameSearch = "%" + nameSearch + "%";
                selection = ContactsContract.Contacts.DISPLAY_NAME + " like ? ";
                selectionArgs = new String[1];
                selectionArgs[0] = nameSearch;
            }
        }
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, selection, selectionArgs, null);
        while (cursor.moveToNext()) {
            if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                // get the phone number
                Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                while (pCur.moveToNext()) {
                    String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactItems.add(new ContactItem(name, phone));
                }
                pCur.close();
            }
        }
        return contactItems;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ContactItem) {
            ContactItem contactItem = (ContactItem)o;
            return this.number.equals(contactItem.getNumber());
        }else{
            return false;
        }
    }
}
