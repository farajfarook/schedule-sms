package com.enbiso.proj.schedulesms.form.wizard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.data.DatabaseHelper;
import com.enbiso.proj.schedulesms.data.core.Schedule;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;
import com.enbiso.proj.schedulesms.form.WizardDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by farflk on 7/24/2014.
 */
public class NewWizardDialog extends WizardDialog {

    private List<ContactItem> receivers;
    private String message = "";
    private Calendar scheduleCalender;
    private String[] repeatTypes;
    private int repeatTypeSelected;
    private boolean repeatEnabled;
    private int repeatValue;
    private Calendar repeatValidTillCalender;
    private Schedule scheduleObject;

    public NewWizardDialog(Context context) {
        super(context);
        this.scheduleObject = new Schedule();
        this.steps.add(new DialogStepOneAddNumber());
        this.steps.add(new DialogStepTwoSetMessage());
        this.steps.add(new DialogStepThreeScheduleDate());
        this.steps.add(new DialogStepFourScheduleTime());
        this.steps.add(new DialogStepFiveRepeat());
        this.steps.add(new DialogStepSixConfirm());
        this.receivers = new ArrayList<ContactItem>();
        this.scheduleCalender = Calendar.getInstance(TimeZone.getDefault());
        this.repeatValidTillCalender = Calendar.getInstance(TimeZone.getDefault());
        this.repeatTypes = context.getResources().getStringArray(R.array.repeat_list);
        this.repeatTypeSelected = 0;
        this.repeatEnabled = true;
        this.repeatValue = 1;
    }

    public NewWizardDialog(Context context, Schedule schedule){
        this(context);
        this.scheduleObject = schedule;
        this.message = schedule.getMessage();
        this.receivers = schedule.getReceivers();
        this.repeatEnabled = Boolean.parseBoolean(schedule.getRepeatEnable());
        this.repeatValue = Integer.parseInt(schedule.getRepeatValue());
        for (int i = 0; i < repeatTypes.length; i++) {
            if(repeatTypes[i].toUpperCase().equals(schedule.getRepeatType())){
                this.repeatTypeSelected = i;
            }
        }
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try{
            Date date = dateFormat.parse(schedule.getScheduleDate());
            this.scheduleCalender.setTime(date);
            date = dateFormat.parse(schedule.getRepeatValidTillDate());
            this.repeatValidTillCalender.setTime(date);
        }catch (ParseException e){ }
    }

    public boolean addReceiver(ContactItem contactItem) {
        if(!receivers.contains(contactItem)) {
            return receivers.add(contactItem);
        }else{
            return false;
        }
    }

    public boolean removeReceiver(ContactItem contactItem) {
        return receivers.remove(contactItem);
    }

    public void updateReceiverList(){
        ((ListView) dialog.findViewById(R.id.new_wizard_receiver_list)).setAdapter(new ReceiverListAdapter(context, receivers, this) {
        });
    }

    public void updateContactList(String name){
        ((ListView) dialog.findViewById(R.id.new_wizard_contact_list)).setAdapter(new ContactListAdapter(context, ContactItem.fetchContactItems(context, name), this));
    }

    public void updateTemplateList(){
        ((ListView) dialog.findViewById(R.id.new_wizard_template_list)).setAdapter(new TemplateListAdapter(context, TemplateItem.fetchTemplateItems(context), this));
    }

    public void updateValidTill(){
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ((TextView) dialog.findViewById(R.id.new_wizard_repeat_validtill)).setText(dateFormat.format(repeatValidTillCalender.getTime()));
    }

    @Override
    public void initialize() {
        super.initialize();
        setTitle("SMS Scheduling Wizard");
        setIcon(R.drawable.wizard_icon);
    }

    class DialogStepOneAddNumber extends DialogStep{

        @Override
        public int getResource() {
            return R.layout.new_wizard_step_add_number;
        }

        @Override
        public void setup() {
            super.setup();
            setRightButton("Next", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(receivers.size() <= 0){
                        Toast.makeText(context, "Atleast one receiver should be set.", Toast.LENGTH_SHORT).show();
                    }else {
                        nextStep();
                    }
                }
            });
            setLeftButton("Cancel", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            updateContactList(null);
            updateReceiverList();
            ((EditText)dialog.findViewById(R.id.new_wizard_phone)).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    String phone = ((EditText) dialog.findViewById(R.id.new_wizard_phone)).getText().toString();
                    if (!phone.equals("")) {
                        updateContactList(phone);
                    }
                    return false;
                }
            });
            ((ImageButton)dialog.findViewById(R.id.new_wizard_phone_add)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone = ((EditText) dialog.findViewById(R.id.new_wizard_phone)).getText().toString();
                    if (phone.equals("")) {
                        Toast.makeText(context, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                    } else {
                        ContactItem contactItem = new ContactItem(phone);
                        if(addReceiver(contactItem)) {
                            updateReceiverList();
                            ((EditText) dialog.findViewById(R.id.new_wizard_phone)).setText("");
                        }
                    }
                }
            });
        }
    }

    class DialogStepTwoSetMessage extends DialogStep{

        @Override
        public int getResource() {
            return R.layout.new_wizard_step_set_message;
        }

        @Override
        public void setup() {
            super.setup();
            updateTemplateList();
            ((EditText)findViewById(R.id.new_wizard_message)).setText(message);
            setRightButton("Next", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    message = ((EditText) findViewById(R.id.new_wizard_message)).getText().toString();
                    nextStep();
                }
            });
            setLeftButton("Previous", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    message = ((EditText)findViewById(R.id.new_wizard_message)).getText().toString();
                    previousStep();
                }
            });
        }
    }

    class DialogStepThreeScheduleDate extends DialogStep{

        @Override
        public int getResource() {
            return R.layout.new_wizard_step_schedule_date;
        }

        @Override
        public void setup() {
            super.setup();
            final DatePicker datePicker = ((DatePicker)findViewById(R.id.new_wizard_schedule_date));
            datePicker.updateDate(scheduleCalender.get(Calendar.YEAR), scheduleCalender.get(Calendar.MONTH), scheduleCalender.get(Calendar.DAY_OF_MONTH));
            setRightButton("Next", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scheduleCalender.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                    scheduleCalender.set(Calendar.MONTH, datePicker.getMonth());
                    scheduleCalender.set(Calendar.YEAR, datePicker.getYear());
                    nextStep();
                }
            });
            setLeftButton("Previous", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scheduleCalender.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                    scheduleCalender.set(Calendar.MONTH, datePicker.getMonth());
                    scheduleCalender.set(Calendar.YEAR, datePicker.getYear());
                    previousStep();
                }
            });
        }
    }

    class DialogStepFourScheduleTime extends DialogStep{

        @Override
        public int getResource() {
            return R.layout.new_wizard_step_schedule_time;
        }

        @Override
        public void setup() {
            super.setup();
            final TimePicker timePicker = ((TimePicker)findViewById(R.id.new_wizard_schedule_time));
            timePicker.setCurrentHour(scheduleCalender.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(scheduleCalender.get(Calendar.MINUTE));
            setRightButton("Next", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scheduleCalender.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    scheduleCalender.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    nextStep();
                }
            });
            setLeftButton("Previous", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scheduleCalender.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    scheduleCalender.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    previousStep();
                }
            });
        }
    }

    class DialogStepFiveRepeat extends DialogStep{

        @Override
        public int getResource() {
            return R.layout.new_wizard_step_repeat;
        }

        @Override
        public void setup() {
            super.setup();
            ((CheckBox)dialog.findViewById(R.id.new_wizard_repeat_enable)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    dialog.findViewById(R.id.new_wizard_repeat_value).setEnabled(b);
                    dialog.findViewById(R.id.new_wizard_repeat_type).setEnabled(b);
                    dialog.findViewById(R.id.new_wizard_repeat_validtill_change).setEnabled(b);
                    repeatEnabled = b;
                }
            });

            ((Spinner)dialog.findViewById(R.id.new_wizard_repeat_type)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    repeatTypeSelected = ((Spinner)dialog.findViewById(R.id.new_wizard_repeat_type)).getSelectedItemPosition();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    repeatTypeSelected = 0;
                }
            });

            ((Spinner)dialog.findViewById(R.id.new_wizard_repeat_type)).setSelection(repeatTypeSelected);
            ((EditText)dialog.findViewById(R.id.new_wizard_repeat_value)).setText(repeatValue + "");
            ((CheckBox)dialog.findViewById(R.id.new_wizard_repeat_enable)).setChecked(repeatEnabled);
            dialog.findViewById(R.id.new_wizard_repeat_value).setEnabled(repeatEnabled);
            dialog.findViewById(R.id.new_wizard_repeat_type).setEnabled(repeatEnabled);
            dialog.findViewById(R.id.new_wizard_repeat_validtill_change).setEnabled(repeatEnabled);

            updateValidTill();
            ((ImageButton) dialog.findViewById(R.id.new_wizard_repeat_validtill_change)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                            android.R.style.Theme_Holo_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            repeatValidTillCalender.set(Calendar.DAY_OF_MONTH, day);
                            repeatValidTillCalender.set(Calendar.MONTH, month);
                            repeatValidTillCalender.set(Calendar.YEAR, year);
                            updateValidTill();
                        }
                    },
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.setCancelable(false);
                    datePickerDialog.setTitle("Select a valid till date");
                    datePickerDialog.show();
                }
            });

            setRightButton("Next", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    repeatValue = Integer.parseInt(((EditText) dialog.findViewById(R.id.new_wizard_repeat_value)).getText().toString());
                    nextStep();
                }
            });
            setLeftButton("Previous", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    repeatValue = Integer.parseInt(((EditText)dialog.findViewById(R.id.new_wizard_repeat_value)).getText().toString());
                    previousStep();
                }
            });
        }
    }

    class DialogStepSixConfirm extends DialogStep{

        @Override
        public int getResource() {
            return R.layout.new_wizard_step_confirm;
        }

        @Override
        public void setup() {
            super.setup();
            StringBuilder receiverText = new StringBuilder();
            for (int i = 0; i < receivers.size(); i++) {
                receiverText.append(receivers.get(i).getNumber()).append(",");
            }
            ((TextView)findViewById(R.id.new_wizard_confirm_receiver)).setText(receiverText.toString());
            ((TextView)findViewById(R.id.new_wizard_confirm_message)).setText(message);
            String repeatText = "";
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if(!repeatEnabled){
                repeatText = "Single execution on " + dateFormat.format(scheduleCalender.getTime());
            }else{
                repeatText = "Repeat schedule for each '" + repeatValue + "' " + repeatTypes[repeatTypeSelected]
                         + "\n From : " + dateFormat.format(scheduleCalender.getTime())
                         + "\n Until : " + dateFormat.format(repeatValidTillCalender.getTime());
            }
            ((TextView)findViewById(R.id.new_wizard_confirm_schedule_info)).setText(repeatText);
            setRightButton("Finish", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    scheduleObject.setRepeatEnable(String.valueOf(repeatEnabled));
                    scheduleObject.setRepeatType(repeatTypes[repeatTypeSelected].toUpperCase());
                    scheduleObject.setRepeatValue(String.valueOf(repeatValue));
                    scheduleObject.setScheduleDate(dateFormat.format(scheduleCalender.getTime()));
                    scheduleObject.setRepeatValidTillDate(dateFormat.format(repeatValidTillCalender.getTime()));
                    scheduleObject.setReceivers(receivers);
                    scheduleObject.setMessage(message);
                    if (DatabaseHelper.getInstance().getHelper(ScheduleHelper.class).createOrUpdate(scheduleObject)) {
                        Toast.makeText(context, "SMS Schedule saved.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "SMS Schedule saving failed.", Toast.LENGTH_SHORT).show();
                    }
                    ((MainActivity) context).getSchedulePopulator().resetup();
                    dismiss();
                }
            });
            setLeftButton("Previous", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    previousStep();
                }
            });
        }
    }

}
