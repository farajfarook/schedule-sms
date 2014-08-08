package com.enbiso.proj.schedulesms.form.history;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
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
        ImageView imageView = ((ImageView)convertView.findViewById(R.id.message_item_photo));
        if(messages.get(position).getReceivers().get(0).getPhoto(context) != null){
            imageView.setImageURI(messages.get(position).getReceivers().get(0).getPhoto(context));
        }else{
            imageView.setImageResource(R.drawable.contact);
        }
        float brightness = 50;
        float[] colorMatrix = {
                0.33f, 0.33f, 0.33f, 0, brightness, //red
                0.33f, 0.33f, 0.33f, 0, brightness, //green
                0.33f, 0.33f, 0.33f, 0, brightness, //blue
                0, 0, 0, 1, 0    //alpha
        };
        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorFilter);

        ((TextView)convertView.findViewById(R.id.message_item_number)).setText(messages.get(position).getReceiverNameString(25));
        ((TextView)convertView.findViewById(R.id.message_item_message)).setText(messages.get(position).getMessage(65));
        ((TextView)convertView.findViewById(R.id.message_item_state)).setText(messages.get(position).get_state().toUpperCase());
        if(messages.get(position).get_state().equalsIgnoreCase("sent")) {
            ((ImageView) convertView.findViewById(R.id.message_item_icon)).setImageResource(R.drawable.message_send);
        }else if(messages.get(position).get_state().equalsIgnoreCase("failed")){
            ((ImageView) convertView.findViewById(R.id.message_item_icon)).setImageResource(R.drawable.message_alert);
        }else if(messages.get(position).get_state().equalsIgnoreCase("delivered")){
            ((ImageView) convertView.findViewById(R.id.message_item_icon)).setImageResource(R.drawable.message_delivered);
        }else if(messages.get(position).get_state().equalsIgnoreCase("sending")){
            ((ImageView) convertView.findViewById(R.id.message_item_icon)).setImageResource(R.drawable.message_progress);
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
