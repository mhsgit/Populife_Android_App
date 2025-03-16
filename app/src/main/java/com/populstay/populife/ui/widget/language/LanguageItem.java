package com.populstay.populife.ui.widget.language;

public class LanguageItem {
    private int id;
    private String languageName;
    private int languageIcon;
    private boolean isSelected;

    public LanguageItem(int id, String languageName, int languageIcon, boolean isSelected) {
        this.id = id;
        this.languageName = languageName;
        this.languageIcon = languageIcon;
        this.isSelected = isSelected;
    }

    public int getId() { return id; }
    public String getLanguageName() { return languageName; }
    public int getLanguageIcon() { return languageIcon; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}