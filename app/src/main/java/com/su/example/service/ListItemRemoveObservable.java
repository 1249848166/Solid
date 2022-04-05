package com.su.example.service;

import com.su.solid.service.interf.Observable;
import com.su.solid.service.interf.Observer;

public class ListItemRemoveObservable implements Observable<Integer> {

    private Integer dataSource;

    @Override
    public Integer getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(Integer dataSource) {
        this.dataSource=dataSource;
    }

    @Override
    public void notice(Observer<Integer> observer) {
        observer.observe(this);
    }
}
