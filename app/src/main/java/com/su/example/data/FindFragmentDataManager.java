package com.su.example.data;

import android.os.SystemClock;

import com.su.example.config.Config;
import com.su.example.model.list.ListSolidItem;
import com.su.example.model.list.ListSolidItem1;
import com.su.example.service.ListItemObservable;
import com.su.example.service.ListItemRemoveObserver;
import com.su.solid._abstract.SolidBaseData;
import com.su.solid.annotation.SolidDataProvider;
import com.su.solid.solid.Solid;

import java.util.ArrayList;
import java.util.List;

public class FindFragmentDataManager implements SolidBaseData {

    private final List<ListSolidItem> dataSource=new ArrayList<>();

    public FindFragmentDataManager() {
        //创建并注册observer
        final ListItemRemoveObserver observer=new ListItemRemoveObserver();
        observer.setConsumer(null);
        Solid.getInstance().addObserver(solidId(),Config.SERVICE_ID_REMOVE,observer);
        //获取数据
        getItems();
    }

    private void getItems() {
        new Thread(() -> {
//            SystemClock.sleep(3000);
            dataSource.clear();
            dataSource.add(new ListSolidItem1("测试数据监听","动态更改view显示"));
            dataSource.add(new ListSolidItem1("测试数据监听","动态更改view显示"));
            dataSource.add(new ListSolidItem1("测试数据监听","动态更改view显示"));
            //创建observable并消费
            final ListItemObservable observable=new ListItemObservable();
            observable.setDataSource(dataSource);
            Solid.getInstance().service(Config.SERVICE_ID_LIST,observable);
        }).start();
    }

    @SolidDataProvider(id = Config.PROVIDER_ID_LIST)
    List<ListSolidItem> getListItems(){
        return dataSource;
    }

    @Override
    public int solidId() {
        return Config.SOLID_ID_FRAGMENT_FIND;
    }
}
