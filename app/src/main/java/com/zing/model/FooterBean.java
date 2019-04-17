package com.zing.model;



public class FooterBean {
    int icon, icon_selected;
    boolean selected;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon_selected() {
        return icon_selected;
    }

    public void setIcon_selected(int icon_selected) {
        this.icon_selected = icon_selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public FooterBean(int icon, int i, boolean b) {
        this.icon = icon;
        this.icon_selected = i;

        this.selected = b;
    }
}
