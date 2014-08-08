package com.enbiso.proj.schedulesms.form.history;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.SearchEntry;
import com.enbiso.proj.schedulesms.data.core.Message;
import com.enbiso.proj.schedulesms.data.core.MessageHelper;
import com.enbiso.proj.schedulesms.form.AbstractPopulator;
import com.enbiso.proj.schedulesms.form.wizard.NewWizardDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by farflk on 7/23/2014.
 */
public class HistoryPopulator extends AbstractPopulator {

    public HistoryPopulator(Context context) {
        super(context);
    }


    @Override
    public void setup(View rootView) {
        super.setup(rootView);
        List<Message> messages = (List<Message>)(List<?>) DatabaseHelper.getInstance().getHelper(MessageHelper.class).findAll();
        Collections.reverse(messages);
        ((ListView) rootView.findViewById(R.id.message_list)).setAdapter(new MessageListAdapter(context, messages));
    }

    public void setupClearHistory(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Clear sent message history");
        builder.setMessage("Are you sure that you want to delete the sent message history?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MessageHelper messageHelper = DatabaseHelper.getInstance().getHelper(MessageHelper.class);
                List<SearchEntry> keys = new ArrayList<SearchEntry>();
                List<String> states = new ArrayList<String>();
                states.add("delivered");
                states.add("failed");
                keys.add(new SearchEntry(SearchEntry.Type.STRING, "_state", SearchEntry.Search.IN, states));
                messageHelper.delete(keys);
                resetup();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
