package com.example.aabhaa.models;

public class OnboardingItem {
    int image;
    String title;
    String subtitle;

    String titleNp;

    String subtitleNp;

    public OnboardingItem(int image, String title, String subtitle , String titleNp , String subtitleNp) {
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.titleNp = titleNp;
        this.subtitleNp = subtitleNp;
    }

    public int getImage() { return image; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }

    public String getTitleNp() { return titleNp; }
    public String getSubtitleNp() { return subtitleNp
            ; }

}
