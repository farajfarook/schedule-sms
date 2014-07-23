package com.enbiso.proj.schedulesms.navigation;

/**
 * Created by farflk on 7/23/2014.
 */
public class DrawerItem {
    private int name;
    private int icon;

    public DrawerItem(int name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public int getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
}
