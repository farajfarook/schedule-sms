package com.enbiso.proj.schedulesms.form.onetime;

import android.content.Context;

import com.enbiso.proj.schedulesms.form.AbstractPopulator;
import com.enbiso.proj.schedulesms.form.ContactItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farflk on 7/23/2014.
 */
public class OnetimePopulator extends AbstractPopulator {

    private List<ContactItem> receivers;
    private OnetimeNewWizardDialog wizardDialog;

    public OnetimePopulator(Context context) {
        super(context);
        this.receivers = new ArrayList<ContactItem>();
    }

    public void setupNew(){
        wizardDialog = new OnetimeNewWizardDialog(context);
        wizardDialog.show();
    }

    public boolean addReceiver(ContactItem contactItem) {
        if(!receivers.contains(contactItem)) {
            Boolean ret = receivers.add(contactItem);
            wizardDialog.updateReceiverList(receivers);
            return ret;
        }else{
            return false;
        }
    }

    public boolean removeReceiver(ContactItem contactItem) {
        Boolean ret = receivers.remove(contactItem);
        wizardDialog.updateReceiverList(receivers);
        return ret;
    }

    public List<ContactItem> getReceivers() {
        return receivers;
    }
}
