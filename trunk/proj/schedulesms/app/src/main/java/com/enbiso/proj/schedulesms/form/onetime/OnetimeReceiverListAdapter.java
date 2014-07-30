package com.enbiso.proj.schedulesms.form.onetime;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.form.ContactItem;
import com.enbiso.proj.schedulesms.form.ContactListAdapter;
import com.enbiso.proj.schedulesms.form.ReceiverListAdapter;

import java.util.List;

/**
 * Created by farflk on 7/30/2014.
 */
public class OnetimeReceiverListAdapter extends ReceiverListAdapter {
    public OnetimeReceiverListAdapter(Context context, List<ContactItem> contactItems) {
        super(context, contactItems);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);
        ((ImageButton)convertView.findViewById(R.id.receiver_remove)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).getOnetimePopulator().removeReceiver(contactItems.get(position));
            }
        });
        return convertView;
    }
}
