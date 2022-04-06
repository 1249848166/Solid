package com.su.solid.model;

import com.su.solid.service.interf.Observer;

public class ObserverModel {
    private int solidId;
    private int serviceId;
    private Observer observer;

    public ObserverModel(int solidId, int serviceId, Observer observer) {
        this.solidId = solidId;
        this.serviceId = serviceId;
        this.observer = observer;
    }

    public int getSolidId() {
        return solidId;
    }

    public void setSolidId(int solidId) {
        this.solidId = solidId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }
}
