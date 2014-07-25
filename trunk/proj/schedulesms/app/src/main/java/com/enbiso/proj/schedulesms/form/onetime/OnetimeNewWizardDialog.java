package com.enbiso.proj.schedulesms.form.onetime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.form.WizardDialog;

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
        setTitle("hello");
        setIcon(R.drawable.drawer_onetime);
    }

    class DialogStepOne extends DialogStep{

        @Override
        public int getResource() {
            return R.layout.fragment_onetime_new_wizard_step_1;
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
