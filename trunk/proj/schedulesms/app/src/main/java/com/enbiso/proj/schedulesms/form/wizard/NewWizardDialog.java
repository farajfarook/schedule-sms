package com.enbiso.proj.schedulesms.form.wizard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.enbiso.proj.schedulesms.data.SearchEntry;
import com.enbiso.proj.schedulesms.data.core.ContactItem;
import com.enbiso.proj.schedulesms.data.core.ContactItemHelper;
import com.enbiso.proj.schedulesms.data.core.Schedule;
import com.enbiso.proj.schedulesms.data.core.ScheduleHelper;
import com.enbiso.proj.schedulesms.form.WizardDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by farflk on 7/24/2014.
 */
public class NewWizardDialog extends WizardDialog {

    private String[] repeatTypes;
    private Schedule schedule;
    private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public NewWizardDialog(Context context) {
        super(context);
        this.schedule = new Schedule();
        this.steps.add(new DialogStepOneAddNumber());
        this.steps.add(new DialogStepTwoSetMessage());
        this.steps.add(new DialogStepThreeScheduleDate());
        this.steps.add(new DialogStepFourScheduleTime());
        this.steps.add(new DialogStepFiveRepeat());
        this.steps.add(new DialogStepSixConfirm());
        this.repeatTypes = context.getResources().getStringArray(R.array.repeat_list);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public NewWizardDialog(Context context, Schedule schedule){
        this(context);
        this.schedule = schedule;

    }

    public void updateReceiverList(){
        ((ListView) dialog.findViewById(R.id.new_wizard_receiver_list)).setAdapter(new ReceiverListAdapter(context, schedule.getReceivers(), this));
    }

    public void updateContactList(String name){
        ContactItemHelper contactItemHelper = DatabaseHelper.getInstance().getHelper(ContactItemHelper.class);
        List<SearchEntry> keys = new ArrayList<SearchEntry>();
        if(name != null){
            name = "%" + name + "%";
        }else{
            name = "%";
        }
        keys.add(new SearchEntry(SearchEntry.Type.STRING, "name", SearchEntry.Search.LIKE, name));
        ((ListView) dialog.findViewById(R.id.new_wizard_contact_list)).setAdapter(new ContactListAdapter(context, (List<ContactItem>)(List<?>)contactItemHelper.find(keys), this));
    }

    public void updateTemplateList(){
        ((ListView) dialog.findViewById(R.id.new_wizard_template_list)).setAdapter(new TemplateListAdapter(context, TemplateItem.fetchTemplateItems(context), this));
    }

    public void updateValidTill(){
        ((TextView) dialog.findViewById(R.id.new_wizard_repeat_validtill)).setText(dateFormat.format(schedule.getRepeatValidTillDate().getTime()));
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
                    if(schedule.getReceivers().size() <= 0){
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
            ((EditText)dialog.findViewById(R.id.new_wizard_phone)).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String phone = ((EditText) dialog.findViewById(R.id.new_wizard_phone)).getText().toString();
                    if (!phone.equals("")) {
                        updateContactList(phone);
                    }
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
                        if(schedule.addReceiver(contactItem)) {
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
            ((EditText)findViewById(R.id.new_wizard_message)).setText(schedule.getMessage());
            setRightButton("Next", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule.setMessage(((EditText) findViewById(R.id.new_wizard_message)).getText().toString());
                    nextStep();
                }
            });
            setLeftButton("Previous", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule.setMessage(((EditText) findViewById(R.id.new_wizard_message)).getText().toString());
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
            datePicker.updateDate(schedule.getScheduleDate().get(Calendar.YEAR), schedule.getScheduleDate().get(Calendar.MONTH), schedule.getScheduleDate().get(Calendar.DAY_OF_MONTH));
            setRightButton("Next", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule.getScheduleDate().set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    schedule.getScheduleDate().set(Calendar.MONTH, datePicker.getMonth());
                    schedule.getScheduleDate().set(Calendar.YEAR, datePicker.getYear());
                    nextStep();
                }
            });
            setLeftButton("Previous", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule.getScheduleDate().set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                    schedule.getScheduleDate().set(Calendar.MONTH, datePicker.getMonth());
                    schedule.getScheduleDate().set(Calendar.YEAR, datePicker.getYear());
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
            timePicker.setCurrentHour(schedule.getScheduleDate().get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(schedule.getScheduleDate().get(Calendar.MINUTE));
            setRightButton("Next", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule.getScheduleDate().set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    schedule.getScheduleDate().set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    nextStep();
                }
            });
            setLeftButton("Previous", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule.getScheduleDate().set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    schedule.getScheduleDate().set(Calendar.MINUTE, timePicker.getCurrentMinute());
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
                    schedule.setRepeatEnable(String.valueOf(b));
                }
            });

            ((Spinner)dialog.findViewById(R.id.new_wizard_repeat_type)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    schedule.setRepeatType(((Spinner) dialog.findViewById(R.id.new_wizard_repeat_type)).getSelectedItem().toString().toUpperCase());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ((Spinner)dialog.findViewById(R.id.new_wizard_repeat_type)).setSelection(schedule.getRepeatTypeSelected(repeatTypes));
            ((EditText)dialog.findViewById(R.id.new_wizard_repeat_value)).setText(schedule.getRepeatValue());
            ((CheckBox)dialog.findViewById(R.id.new_wizard_repeat_enable)).setChecked(Boolean.parseBoolean(schedule.getRepeatEnable()));
            dialog.findViewById(R.id.new_wizard_repeat_value).setEnabled(Boolean.parseBoolean(schedule.getRepeatEnable()));
            dialog.findViewById(R.id.new_wizard_repeat_type).setEnabled(Boolean.parseBoolean(schedule.getRepeatEnable()));
            dialog.findViewById(R.id.new_wizard_repeat_validtill_change).setEnabled(Boolean.parseBoolean(schedule.getRepeatEnable()));

            updateValidTill();
            ((ImageButton) dialog.findViewById(R.id.new_wizard_repeat_validtill_change)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                            android.R.style.Theme_Holo_Dialog_NoActionBar, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            schedule.getRepeatValidTillDate().set(Calendar.DAY_OF_MONTH, day);
                            schedule.getRepeatValidTillDate().set(Calendar.MONTH, month);
                            schedule.getRepeatValidTillDate().set(Calendar.YEAR, year);
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
                    schedule.setRepeatValue(((EditText) dialog.findViewById(R.id.new_wizard_repeat_value)).getText().toString());
                    nextStep();
                }
            });
            setLeftButton("Previous", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule.setRepeatValue(((EditText) dialog.findViewById(R.id.new_wizard_repeat_value)).getText().toString());
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
            ((TextView)findViewById(R.id.new_wizard_confirm_receiver)).setText(schedule.getReceiverString(50));
            ((TextView)findViewById(R.id.new_wizard_confirm_message)).setText(schedule.getMessage());
            String repeatText = "";
            if(!Boolean.parseBoolean(schedule.getRepeatEnable())){
                repeatText = "Single execution on " + dateTimeFormat.format(schedule.getScheduleDate().getTime());
            }else{
                repeatText = "Repeat schedule for each '" + schedule.getRepeatValue() + "' " + schedule.getRepeatType()
                         + "\n From : " + dateTimeFormat.format(schedule.getScheduleDate().getTime())
                         + "\n Until : " + dateTimeFormat.format(schedule.getRepeatValidTillDate().getTime());
            }
            ((TextView)findViewById(R.id.new_wizard_confirm_schedule_info)).setText(repeatText);
            setRightButton("Finish", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule.setNextExecute(schedule.getScheduleDate());//set next execute as the schedule date
                    schedule.set_state("active");
                    if (DatabaseHelper.getInstance().getHelper(ScheduleHelper.class).createOrUpdate(schedule)) {
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
