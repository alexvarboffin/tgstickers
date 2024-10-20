package com.walhalla.stickers.adapter;

public class HeaderItem implements Item {
    private String header;

    public HeaderItem(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    @Override
    public boolean isHeader() {
        return true;
    }

    @Override
    public String getContent() {
        return header;
    }
}
