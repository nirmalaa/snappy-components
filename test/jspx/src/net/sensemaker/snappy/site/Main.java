package net.sensemaker.snappy.site;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: May 10, 2008
 * Time: 1:09:58 PM
 */
public class Main {

    private String panel = "basic";

    public String goIntro(){panel = "intro"; return null;}
    public String goBasic(){panel = "basic"; return null;}
    public String goSorting(){panel = "sorting"; return null;}
    public String goSelection(){panel = "selection"; return null;}
    public String goCustom(){panel = "custom"; return null;}
    public String goHuge(){panel = "huge"; return null;}
    private boolean basic;
    private boolean info;
    private boolean sorting;
    private boolean selection;

    public String getPanel() {
        return panel;
    }

    public boolean isBasic() {
        return "basic".equals(panel);
    }

    public boolean isIntro() {
        return "intro".equals(panel);
    }

    public boolean isSorting() {
        return "sorting".equals(panel);
    }

    public boolean isSelection() {
        return "selection".equals(panel);
    }

    public boolean isCustom(){
        return "custom".equals(panel);
    }

    public boolean isHuge(){
        return "huge".equals(panel);
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    public void setBasic(boolean basic) {
        this.basic = basic;
    }

    public void setInfo(boolean info) {
        this.info = info;
    }

    public void setSorting(boolean sorting) {
        this.sorting = sorting;
    }

    public void setSelection(boolean selection) {
        this.selection = selection;
    }
}
