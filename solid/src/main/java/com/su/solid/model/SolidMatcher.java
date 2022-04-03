package com.su.solid.model;

import java.util.ArrayList;
import java.util.List;

public class SolidMatcher {
    private int solidId;
    private final List<BindItemInfo> bindItemInfoList=new ArrayList<>();

    public SolidMatcher(int solidId) {
        this.solidId = solidId;
    }

    public int getSolidId() {
        return solidId;
    }

    public void setSolidId(int solidId) {
        this.solidId = solidId;
    }

    public List<BindItemInfo> getBindItemInfoList() {
        return bindItemInfoList;
    }

    public void addBindItemInfo(BindItemInfo info){
        bindItemInfoList.add(info);
    }
}
