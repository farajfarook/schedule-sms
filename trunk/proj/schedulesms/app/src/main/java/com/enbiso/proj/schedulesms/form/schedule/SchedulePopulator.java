package com.enbiso.proj.schedulesms.form.schedule;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Schedule;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;
import com.enbiso.proj.schedulesms.form.AbstractPopulator;

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
        ((ListView) rootView.findViewById(R.id.schedule_list)).setAdapter(new ScheduleListAdapter(context, schedules));
    }
}
