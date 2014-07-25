package com.enbiso.proj.schedulesms.form.repeat;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.form.WizardDialog;

/**
 * Created by farflk on 7/24/2014.
 */
public class RepeatNewWizardDialog extends WizardDialog {

    public RepeatNewWizardDialog(Context context) {
        super(context);
        steps.add(new DialogStepOne());
        steps.add(new DialogStepTwo());
    }

    class DialogStepOne extends DialogStep{

        @Override
        public int getResource() {
            return R.layout.fragment_onetime_new_wizard_step_1;
        }

        @Override
        public void setup() {
            super.setup();

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
        }
    }

}
