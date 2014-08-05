package com.enbiso.proj.schedulesms.form.wizard;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.form.WizardDialog;

import java.util.List;

/**
 * Created by Faraj on 8/6/2014.
 */

public class ContactListUpdateAsync extends AsyncTask<Integer, Integer, Integer> {

    private Context context;
    private String searchName;
    private List<ContactItem> contactItems;
    private NewWizardDialog wizardDialog;

    public ContactListUpdateAsync(Context context, String searchName, NewWizardDialog wizardDialog) {
        this.context = context;
        this.searchName = searchName;
        this.wizardDialog = wizardDialog;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        contactItems = ContactItem.fetchContactItems(context, searchName);
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        ((ListView) wizardDialog.findViewById(R.id.new_wizard_contact_list)).setAdapter(new ContactListAdapter(context, contactItems, wizardDialog));
    }
}
