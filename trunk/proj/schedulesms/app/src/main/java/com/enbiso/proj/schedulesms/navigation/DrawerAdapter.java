package com.enbiso.proj.schedulesms.navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enbiso.proj.schedulesms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farflk on 7/23/2014.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem>{
    List<DrawerItem> drawerItems;
    Context context;
    int resourceId;



    public DrawerAdapter(Context context, List<DrawerItem> drawerItems) {
        super(context, R.layout.fragment_navigation_drawer_item, drawerItems);
        this.drawerItems = drawerItems;
        this.context = context;
        this.resourceId = R.layout.fragment_navigation_drawer_item;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = initView(convertView);
        ((ImageView)convertView.findViewById(R.id.drawer_item_image)).setImageResource(drawerItems.get(position).getIcon());
        ((TextView)convertView.findViewById(R.id.drawer_item_title)).setText(drawerItems.get(position).getName());
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
