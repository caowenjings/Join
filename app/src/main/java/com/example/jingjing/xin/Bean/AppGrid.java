package com.example.jingjing.xin.Bean;

/**
 * Created by jingjing on 2018/5/3.
 */

public class AppGrid {
    private int icon;
    private String iconName;

    public AppGrid(String iconname, int icon) {
        this.iconName=iconname;
        this.icon=icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}