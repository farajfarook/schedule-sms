package com.enbiso.proj.schedulesms.form.wizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.enbiso.proj.schedulesms.R;
import com.enbiso.proj.schedulesms.form.WizardDialog;

import java.util.List;

/**
 * Created by farflk on 7/30/2014.
 */
public class TemplateListAdapter extends ArrayAdapter<TemplateItem> {

    private Context context;
    private int resourceId;
    private List<TemplateItem> templateItems;
    private NewWizardDialog wizardDialog;

    public TemplateListAdapter(Context context, List<TemplateItem> templateItems, NewWizardDialog wizardDialog) {
        super(context, R.layout.template_list_item, templateItems);
        this.context = context;
        this.resourceId = R.layout.template_list_item;
        this.templateItems = templateItems;
        this.wizardDialog = wizardDialog;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = initView(convertView);
        final TemplateItem templateItem = templateItems.get(position);
        ((TextView)convertView.findViewById(R.id.template_name)).setText(templateItem.getName());
        ((TextView)convertView.findViewById(R.id.template_content)).setText(templateItem.getContent().substring(0,30) + "...");
        ((ImageButton)convertView.findViewById(R.id.template_select)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) wizardDialog.findViewById(R.id.new_wizard_message)).setText(templateItem.getContent());
            }
        });
        return convertView;
    }


    private View initView(View convertView){
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return vi.inflate(resourceId, null);
        }else{
            return convertView;
        }
    }
}
