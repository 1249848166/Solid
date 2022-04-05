package com.su.example.service;

import com.su.example.model.list.ListSolidItem;
import com.su.solid.service.interf.Observable;
import com.su.solid.service.interf.Observer;

import java.util.List;

public class ListItemObservable implements Observable<List<ListSolidItem>> {

    private List<ListSolidItem> dataSource;

    @Override
    public List<ListSolidItem> getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(List<ListSolidItem> dataSource) {
        this.dataSource=dataSource;
    }

    @Override
    public void notice(Observer<List<ListSolidItem>> observer) {
        observer.observe(this);
    }
}
