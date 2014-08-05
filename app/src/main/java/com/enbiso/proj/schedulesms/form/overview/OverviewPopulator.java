package com.enbiso.proj.schedulesms.form.overview;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Message;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;
import com.enbiso.proj.schedulesms.data.core.Schedule;
import com.enbiso.proj.schedulesms.form.AbstractPopulator;
import com.enbiso.proj.schedulesms.form.schedule.ScheduleListAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by farflk on 7/23/2014.
 */
public class OverviewPopulator extends AbstractPopulator {

    public OverviewPopulator(Context context) {
        super(context);
    }


    @Override
    public void setup(View rootView) {
        super.setup(rootView);
        List<Message> messages = (List<Message>)(List<?>) DatabaseHelper.getInstance().getHelper(MessageHelper.class).findAll();
        Collections.reverse(messages);
        ((ListView) rootView.findViewById(R.id.message_list)).setAdapter(new MessageListAdapter(context, messages));
    }
}
