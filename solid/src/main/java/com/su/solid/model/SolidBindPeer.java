package com.su.solid.model;

import com.su.solid._abstract.SolidBaseData;
import com.su.solid._abstract.SolidBaseView;

import java.lang.ref.WeakReference;

public class SolidBindPeer {
    private int solidId;
    private SolidBaseView view;
    private SolidBaseData data;

    public SolidBindPeer(int solidId, SolidBaseView view, SolidBaseData data) {
        this.solidId = solidId;
        this.view = view;
        this.data = data;
    }

    public void setSolidId(int solidId) {
        this.solidId = solidId;
    }

    public void setView(SolidBaseView view) {
        this.view = view;
    }

    public void setData(SolidBaseData data) {
        this.data = data;
    }

    public int getSolidId() {
        return solidId;
    }

    public SolidBaseView getView() {
        return view;
    }

    public SolidBaseData getData() {
        return data;
    }
}
