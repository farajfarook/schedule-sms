package com.enbiso.proj.schedulesms.form.onetime;

import android.content.Context;

import com.enbiso.proj.schedulesms.form.AbstractPopulator;

/**
 * Created by farflk on 7/23/2014.
 */
public class OnetimePopulator extends AbstractPopulator {

    public OnetimePopulator(Context context) {
        super(context);
    }

    public void setupNew(){
        new OnetimeNewWizardDialog(context).show();
    }
}
