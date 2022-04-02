package com.su.example.model.list;

public class ListSolidItem2 implements ListSolidItem{
    private Integer avatar;
    private String name;

    public ListSolidItem2(Integer avatar, String name) {
        this.avatar = avatar;
        this.name = name;
    }

    public Integer getAvatar() {
        return avatar;
    }

    public void setAvatar(Integer avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return 2;
    }
}
