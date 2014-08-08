package com.enbiso.proj.schedulesms.data.core;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.widget.ListView;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.AbstractHelper;
import com.enbiso.proj.schedulesms.data.AbstractModel;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.form.wizard.ContactListAdapter;
import com.enbiso.proj.schedulesms.form.wizard.NewWizardDialog;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by farflk on 7/24/2014.
 */
public class ContactItemHelper extends AbstractHelper{

    public ContactItemHelper(Context context) {
        super(context);
        this.tableName = "core_tbl_contact_item";

        this.columns.add("name TEXT");
        this.columns.add("phone TEXT");
        this.columns.add("photo TEXT");
    }

    @Override
    protected AbstractModel getModelInstance() {
        return new ContactItem();
    }

    private boolean updated = false;

    public void fetchAndUpdate(){
        if(!updated) {
            ContactListUpdateAsync contactListUpdateAsync = new ContactListUpdateAsync();
            contactListUpdateAsync.execute();
            updated = true;
        }
    }

    public class ContactListUpdateAsync extends AsyncTask<Integer, ContactItem, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //get the photo
                    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
                    Uri photoUri = null;
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), contactUri);
                    if (inputStream != null) {
                        photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                    }
                    // get the phone number
                    Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        ContactItem contactItem = new ContactItem(name, phone, photoUri);
                        contactItem.set_id(phone);
                        publishProgress(contactItem);
                    }
                    pCur.close();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(ContactItem... values) {
            ContactItem contactItem = values[0];
            if(getBy("_id", contactItem.get_id()) == null) {
                create(contactItem);
            }
        }
    }
}
