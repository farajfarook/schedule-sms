package com.enbiso.proj.schedulesms.form.wizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.enbiso.proj.schedulesms.R;

import java.util.List;

/**
 * Created by farflk on 7/30/2014.
 */
public class ReceiverListAdapter extends ArrayAdapter<ContactItem> {

    protected Context context;
    protected int resourceId;
    protected List<ContactItem> contactItems;
    private NewWizardDialog wizardDialog;

    public ReceiverListAdapter(Context context, List<ContactItem> contactItems, NewWizardDialog wizardDialog) {
        super(context, R.layout.receiver_list_item, contactItems);
        this.context = context;
        this.resourceId = R.layout.receiver_list_item;
        this.contactItems = contactItems;
        this.wizardDialog = wizardDialog;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = initView(convertView);
        final ContactItem contactItem = contactItems.get(position);
        ((TextView)convertView.findViewById(R.id.receiver_name)).setText(contactItem.getName());
        ((TextView)convertView.findViewById(R.id.receiver_number)).setText(contactItem.getNumber());
        ((ImageButton)convertView.findViewById(R.id.receiver_remove)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wizardDialog.removeReceiver(contactItem);
                wizardDialog.updateReceiverList();
            }
        });
        return convertView;
    }


    private View initView(View convertView){
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return vi.inflate(resourceId, null);
        }else{
            return convertView;
        }
    }
}
