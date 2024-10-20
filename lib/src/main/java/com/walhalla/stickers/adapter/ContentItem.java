package com.walhalla.stickers.adapter;

public class ContentItem implements Item {
    private String content;

    public ContentItem(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean isHeader() {
        return false;
    }
}
