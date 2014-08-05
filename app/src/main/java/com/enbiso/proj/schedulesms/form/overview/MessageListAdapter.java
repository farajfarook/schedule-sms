package com.enbiso.proj.schedulesms.form.overview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Message;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;

import java.util.List;

/**
 * Created by farflk on 8/5/2014.
 */
public class MessageListAdapter extends ArrayAdapter<Message> {

    private int resourceId;
    private List<Message> messages;
    private Context context;
    private MessageHelper messageHelper;

    public MessageListAdapter(Context context, List<Message> messages) {
        super(context, R.layout.fragment_overview_message_list_item, messages);
        this.context = context;
        this.messages = messages;
        this.resourceId = R.layout.fragment_overview_message_list_item;
        this.messageHelper = DatabaseHelper.getInstance().getHelper(MessageHelper.class);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = initView(convertView);
        ((TextView)convertView.findViewById(R.id.message_item_message)).setText(messages.get(position).getMessage(25));
        ((TextView)convertView.findViewById(R.id.message_item_number)).setText(messages.get(position).getReceiverString(25));
        if(messages.get(position).get_state().equalsIgnoreCase("sent")) {
            ((ImageView) convertView.findViewById(R.id.message_item_icon)).setImageResource(R.drawable.message_sent);
        }else if(messages.get(position).get_state().equalsIgnoreCase("failed")){
            ((ImageView) convertView.findViewById(R.id.message_item_icon)).setImageResource(R.drawable.message_failed);
        }
        return  convertView;
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
