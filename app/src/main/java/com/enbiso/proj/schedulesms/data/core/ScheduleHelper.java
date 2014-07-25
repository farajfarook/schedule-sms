package com.enbiso.proj.schedulesms.data.core;

import android.content.Context;

import com.enbiso.proj.schedulesms.data.AbstractHelper;
import com.enbiso.proj.schedulesms.data.AbstractModel;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.SearchEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farflk on 7/24/2014.
 */
public class ScheduleHelper extends AbstractHelper{

    public ScheduleHelper(Context context) {
        super(context);
        this.tableName = "core_tbl_schedule";

        this.columns.add("description TEXT");
        this.columns.add("schedule_data TEXT");
        this.columns.add("receivers TEXT");
        this.columns.add("message TEXT");
    }

    @Override
    protected AbstractModel getModelInstance() {
        return new Message();
    }

    @Override
    public AbstractModel populateRelations(AbstractModel abstractModel) {
        Schedule schedule = (Schedule)super.populateRelations(abstractModel);
        List<Message> messages = (List<Message>)(List<?>)DatabaseHelper.getInstance().getHelper(MessageHelper.class).findBy("schedule_id", schedule.get_id());
        schedule.setMessages(messages);
        return schedule;
    }
}
