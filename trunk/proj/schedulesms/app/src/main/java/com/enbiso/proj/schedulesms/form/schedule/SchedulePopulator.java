package com.enbiso.proj.schedulesms.form.schedule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Schedule;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;
import com.enbiso.proj.schedulesms.form.AbstractPopulator;
import com.enbiso.proj.schedulesms.form.wizard.NewWizardDialog;
//import com.github.amlcurran.showcaseview.ShowcaseView;
//import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
//import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by farflk on 7/23/2014.
 */
public class SchedulePopulator extends AbstractPopulator {

    private ScheduleHelper scheduleHelper;

    public SchedulePopulator(Context context) {
        super(context);
        this.scheduleHelper = DatabaseHelper.getInstance().getHelper(ScheduleHelper.class);
    }

    @Override
    public void setup(View rootView) {
        super.setup(rootView);
        List<Schedule> schedules = (List<Schedule>)(List<?>)scheduleHelper.findAll();
        Collections.reverse(schedules);
        final ListView listView = ((ListView) rootView.findViewById(R.id.schedule_list));
        listView.setAdapter(new ScheduleListAdapter(context, schedules));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Schedule schedule = (Schedule)listView.getItemAtPosition(i);
                AlertDialog.Builder alertOptions = new AlertDialog.Builder(context);
                List<String> optsList = new ArrayList<String>();

                if(!schedule.get_state().equalsIgnoreCase("completed")) {
                    if(schedule.get_state().equalsIgnoreCase("active")){
                        optsList.add("Deactivate");
                    }else if(schedule.get_state().equalsIgnoreCase("inactive")){
                        optsList.add("Activate");
                    }
                    optsList.add("Edit");
                }
                optsList.add("Delete");
                final String[] options = optsList.toArray(new String[]{});
                alertOptions.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, options), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(options[i].equalsIgnoreCase("EDIT")){
                            new NewWizardDialog(context, schedule).show();
                        }else if(options[i].equalsIgnoreCase("DELETE")){
                            AlertDialog.Builder deleteConfirm = new AlertDialog.Builder(context);
                            deleteConfirm.setTitle("Delete confirmation.");
                            deleteConfirm.setMessage("Are you sure that you want to delete the schedule?");
                            deleteConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(context, "Schedule deleted.", Toast.LENGTH_SHORT).show();
                                    scheduleHelper.delete(schedule.get_id());
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
                        }else if(options[i].equalsIgnoreCase("ACTIVATE")){
                            schedule.set_state("active");
                            scheduleHelper.createOrUpdate(schedule);
                            ((MainActivity) context).getSchedulePopulator().resetup();
                        }else if(options[i].equalsIgnoreCase("DEACTIVATE")){
                            schedule.set_state("inactive");
                            scheduleHelper.createOrUpdate(schedule);
                            ((MainActivity) context).getSchedulePopulator().resetup();
                        }
                    }
                });
                alertOptions.setCancelable(true);
                alertOptions.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.dismiss();
                    }
                });
                alertOptions.show();
            }
        });
/*
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        if(schedules.size() == 1){
            if(!settings.getBoolean("help_schedule_introduce", false)) {
                new ShowcaseView.Builder((MainActivity) context)
                        .setTarget(new ViewTarget(R.id.schedule_item_icon, (MainActivity)context))
                        .setContentTitle("Congratulations!")
                        .setContentText("You have created a new SMS Schedule. By 'Long Pressing' the schedule you can alter the configurations.")
                        .hideOnTouchOutside()
                        .build();
                settings.edit().putBoolean("help_schedule_introduce", true).commit();
            }
        }*/
    }

    public void setupNew(){
        new NewWizardDialog(context).show();
    }
}
