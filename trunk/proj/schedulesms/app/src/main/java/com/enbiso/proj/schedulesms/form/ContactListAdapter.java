package com.enbiso.proj.schedulesms.form;

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
public abstract class ContactListAdapter extends ArrayAdapter<ContactItem> {

    protected Context context;
    protected int resourceId;
    protected List<ContactItem> contactItems;

    public ContactListAdapter(Context context, List<ContactItem> contactItems) {
        super(context, R.layout.contact_list_item, contactItems);
        this.context = context;
        this.resourceId = R.layout.contact_list_item;
        this.contactItems = contactItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = initView(convertView);
        ((TextView)convertView.findViewById(R.id.contact_name)).setText(contactItems.get(position).getName());
        ((TextView)convertView.findViewById(R.id.contact_number)).setText(contactItems.get(position).getNumber());
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
