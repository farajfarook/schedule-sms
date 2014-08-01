package com.enbiso.proj.schedulesms.form.schedule;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.core.Schedule;
import com.enbiso.proj.schedulesms.form.wizard.NewWizardDialog;

import java.util.List;

/**
 * Created by farflk on 8/1/2014.
 */
public class ScheduleListAdapter extends ArrayAdapter<Schedule> {

    private int resourceId;
    private List<Schedule> schedules;
    private Context context;

    public ScheduleListAdapter(Context context, List<Schedule> schedules) {
        super(context, R.layout.fragment_schedule_list_item, schedules);
        this.context = context;
        this.schedules = schedules;
        this.resourceId = R.layout.fragment_schedule_list_item;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = initView(convertView);
        ((TextView)convertView.findViewById(R.id.schedule_item_message)).setText(schedules.get(position).getMessage(25) + "...");
        ((TextView)convertView.findViewById(R.id.schedule_item_number)).setText(schedules.get(position).getReceiverString(25) + "...");
        if(schedules.get(position).getRepeatEnable().equals(String.valueOf(false))) {
            ((ImageView) convertView.findViewById(R.id.schedule_item_icon)).setImageResource(R.drawable.alarm);
        }
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new NewWizardDialog(context, schedules.get(position)).show();
                return false;
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
