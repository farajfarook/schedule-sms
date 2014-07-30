package com.enbiso.proj.schedulesms.form.onetime;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.enbiso.proj.schedulesms.MainActivity;
import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.form.ContactItem;
import com.enbiso.proj.schedulesms.form.WizardDialog;

import java.util.List;

/**
 * Created by farflk on 7/24/2014.
 */
public class OnetimeNewWizardDialog extends WizardDialog {

    public OnetimeNewWizardDialog(Context context) {
        super(context);
        steps.add(new DialogStepOne());
        steps.add(new DialogStepTwo());
    }

    @Override
    public void initialize() {
        super.initialize();
        setTitle("One time Schedule Wizard");
        setIcon(R.drawable.drawer_onetime);
    }

    public void updateReceiverList(List<ContactItem> contactItems){
        if(currentStep == 0){
            ((ListView) dialog.findViewById(R.id.new_wizard_receiver_list)).setAdapter(new OnetimeReceiverListAdapter(context, contactItems));
        }
    }

    class DialogStepOne extends DialogStep{

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
                    nextStep();
                }
            });
            setLeftButton("Cancel", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            ((ListView) dialog.findViewById(R.id.new_wizard_contact_list)).setAdapter(new OnetimeContactListAdapter(context, ContactItem.fetchContactItems(context, null)));
            ((ListView) dialog.findViewById(R.id.new_wizard_receiver_list)).setAdapter(new OnetimeReceiverListAdapter(context, ((MainActivity) context).getOnetimePopulator().getReceivers()));
            ((EditText)dialog.findViewById(R.id.new_wizard_phone)).setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    String phone = ((EditText) dialog.findViewById(R.id.new_wizard_phone)).getText().toString();
                    if (!phone.equals("")) {
                        ((ListView) dialog.findViewById(R.id.new_wizard_contact_list)).setAdapter(new OnetimeContactListAdapter(context, ContactItem.fetchContactItems(context, phone)));
                        return true;
                    } else {
                        return false;
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
                        ContactItem contactItem = new ContactItem("No name", phone);
                        ((MainActivity) context).getOnetimePopulator().addReceiver(contactItem);
                    }
                }
            });
        }
    }

    class DialogStepTwo extends DialogStep{

        @Override
        public int getResource() {
            return R.layout.fragment_onetime_new_wizard_step_2;
        }

        @Override
        public void setup() {
            super.setup();
            setRightButton("Finish", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
