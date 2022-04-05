package com.su.example.service;

import com.su.example.fragment.FindFragment;
import com.su.example.model.list.ListSolidItem;
import com.su.solid.service.interf.Observable;
import com.su.solid.service.interf.Observer;

import java.util.List;

public class ListItemObserver implements Observer<List<ListSolidItem>> {

    private FindFragment consumer;

    @Override
    public void setConsumer(Object consumer) {
        this.consumer= (FindFragment) consumer;
    }

    @Override
    public void observe(Observable<List<ListSolidItem>> observable) {
        final List<ListSolidItem> dataSource=observable.getDataSource();
        consumer.refreshList(dataSource);
    }
}
