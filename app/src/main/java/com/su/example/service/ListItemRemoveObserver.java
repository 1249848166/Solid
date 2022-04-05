package com.su.example.service;

import com.su.example.config.Config;
import com.su.example.model.list.ListSolidItem;
import com.su.solid.service.interf.Observable;
import com.su.solid.service.interf.Observer;
import com.su.solid.solid.Solid;

import java.util.List;

public class ListItemRemoveObserver implements Observer<Integer> {

    @Override
    public void setConsumer(Object consumer) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void observe(Observable<Integer> observable) {
        final int posiion=observable.getDataSource();
        final List<ListSolidItem> items= (List<ListSolidItem>) Solid.getInstance()
                .queryProviderData(Config.SOLID_ID_FRAGMENT_FIND,Config.PROVIDER_ID_LIST);
        items.remove(posiion);
    }
}
