package com.enbiso.proj.schedulesms.data.core;

import android.content.Context;

import com.enbiso.proj.schedulesms.data.AbstractHelper;
import com.enbiso.proj.schedulesms.data.AbstractModel;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;

/**
 * Created by farflk on 7/24/2014.
 */
public class MessageHelper extends AbstractHelper{

    public MessageHelper(Context context) {
        super(context);
        this.tableName = "core_tbl_message";

        this.columns.add("schedule_id VARCHAR(100)");
        this.columns.add("scheduled INTEGER");
        this.columns.add("executed INTEGER");
        this.columns.add("receivers TEXT");
        this.columns.add("message TEXT");
    }

    @Override
    protected AbstractModel getModelInstance() {
        return new Message();
    }

    @Override
    public AbstractModel populateRelations(AbstractModel abstractModel) {
        Message message = (Message)super.populateRelations(abstractModel);
        Schedule schedule = (Schedule)DatabaseHelper.getInstance().getHelper(MessageHelper.class).getBy("_id", message.getScheduleId());
        message.setSchedule(schedule);
        return message;
    }
}
