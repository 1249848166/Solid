package com.su.example.model.list;

public class ListSolidItem1 implements ListSolidItem{
    private String title;
    private String content;

    public ListSolidItem1(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getType() {
        return 1;
    }
}
