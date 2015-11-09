package com.example.reader;

/**
 * Created by 01011549 on 15/11/05.
 */
public class BaseItem {
    private String mTitle;
    private String mPubDate;
    private String mLink;
    private String mDescription;

    public String getTitle() {
        return mTitle;
    }

    public String getLink() {
        return mLink;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

    public void setPubDate(String description) {
        this.mDescription = description;
    }

}