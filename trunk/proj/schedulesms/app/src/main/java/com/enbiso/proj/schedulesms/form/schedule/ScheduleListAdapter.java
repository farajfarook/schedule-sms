package com.enbiso.proj.schedulesms.form.schedule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Schedule;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;
import com.enbiso.proj.schedulesms.form.wizard.NewWizardDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by farflk on 8/1/2014.
 */
public class ScheduleListAdapter extends ArrayAdapter<Schedule> {

    private int resourceId;
    private List<Schedule> schedules;
    private Context context;
    private ScheduleHelper scheduleHelper;

    public ScheduleListAdapter(Context context, List<Schedule> schedules) {
        super(context, R.layout.fragment_schedule_list_item, schedules);
        this.context = context;
        this.schedules = schedules;
        this.resourceId = R.layout.fragment_schedule_list_item;
        this.scheduleHelper = DatabaseHelper.getInstance().getHelper(ScheduleHelper.class);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = initView(convertView);

        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        ((TextView) convertView.findViewById(R.id.schedule_item_message)).setText(schedules.get(position).getMessage(65));
        ((TextView)convertView.findViewById(R.id.schedule_item_number)).setText(schedules.get(position).getReceiverNameString(25));
        String scheduleInfo = "";
        if(schedules.get(position).getRepeatEnable().equals(String.valueOf(false))) {
            if(schedules.get(position).get_state().equalsIgnoreCase("active")) {
                ((ImageView) convertView.findViewById(R.id.schedule_item_icon)).setImageResource(R.drawable.schedule_single);
            }else{
                ((ImageView) convertView.findViewById(R.id.schedule_item_icon)).setImageResource(R.drawable.schedule_single_inactive);
            }
            scheduleInfo = "One time execute at " + dateTimeFormat.format(schedules.get(position).getScheduleDate().getTime());
        }else{
            if(schedules.get(position).get_state().equalsIgnoreCase("active")) {
                ((ImageView) convertView.findViewById(R.id.schedule_item_icon)).setImageResource(R.drawable.schedule_repeat);
            }else{
                ((ImageView) convertView.findViewById(R.id.schedule_item_icon)).setImageResource(R.drawable.schedule_repeat_inactive);
            }
            scheduleInfo = "Each " + schedules.get(position).getRepeatValue() + " " +  schedules.get(position).getRepeatType() + "\n";
            scheduleInfo += dateTimeFormat.format(schedules.get(position).getScheduleDate().getTime()) + " - " + dateFormat.format(schedules.get(position).getRepeatValidTillDate().getTime());
        }
        ((TextView)convertView.findViewById(R.id.schedule_item_schedule_info)).setText(scheduleInfo);
        if(schedules.get(position).get_state().equalsIgnoreCase("active")) {
            ((TextView)convertView.findViewById(R.id.schedule_item_schedule_info)).setTextColor(0xffe9594a);
        }else{
            ((TextView)convertView.findViewById(R.id.schedule_item_schedule_info)).setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertOptions = new AlertDialog.Builder(context);
                String[] options = {"Activate", "Edit", "Delete", "Cancel"};
                if(schedules.get(position).get_state().equalsIgnoreCase("active")){
                    options[0] = "Deactivate";
                }
                alertOptions.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, options), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                if(schedules.get(position).get_state().equalsIgnoreCase("completed")){
                                    Toast.makeText(context, "This Schedule is already Completed. Edit the schedule and activate it.", Toast.LENGTH_SHORT).show();
                                }else if(schedules.get(position).get_state().equalsIgnoreCase("inactive")){
                                    schedules.get(position).set_state("active");
                                    scheduleHelper.createOrUpdate(schedules.get(position));
                                    ((MainActivity) context).getSchedulePopulator().resetup();
                                }else if(schedules.get(position).get_state().equalsIgnoreCase("active")){
                                    schedules.get(position).set_state("inactive");
                                    scheduleHelper.createOrUpdate(schedules.get(position));
                                    ((MainActivity) context).getSchedulePopulator().resetup();
                                }
                                break;
                            case 1:
                                new NewWizardDialog(context, schedules.get(position)).show();
                                break;
                            case 2:
                                AlertDialog.Builder deleteConfirm = new AlertDialog.Builder(context);
                                deleteConfirm.setTitle("Delete confirmation.");
                                deleteConfirm.setMessage("Are you sure that you want to delete the schedule?");
                                deleteConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context, "Schedule deleted.", Toast.LENGTH_SHORT).show();
                                        scheduleHelper.delete(schedules.get(position).get_id());
                                        ((MainActivity) context).getSchedulePopulator().resetup();
                                    }
                                });
                                deleteConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                deleteConfirm.show();
                                dialogInterface.dismiss();
                                break;
                            case 3:
                                dialogInterface.dismiss();
                                break;
                        }
                    }
                });
                alertOptions.show();
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
