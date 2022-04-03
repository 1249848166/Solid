package com.su.example.data;

import android.os.SystemClock;
import android.util.Log;

import com.su.example.config.Config;
import com.su.example.model.list.ListSolidItem;
import com.su.example.model.list.ListSolidItem1;
import com.su.solid._abstract.SolidBaseData;
import com.su.solid.annotation.SolidData;
import com.su.solid.annotation.SolidDataProvider;
import com.su.solid.callback.SolidCallback;
import com.su.solid.solid.Solid;

import java.util.ArrayList;
import java.util.List;

public class ListDataManager implements SolidBaseData {

    private final List<ListSolidItem> listSolidItemList =new ArrayList<>();

    @SolidDataProvider(id = Config.PROVIDER_ID_LIST)
    List<ListSolidItem> provideItemListData(){
        return listSolidItemList;
    }

    @SolidData(bindId = Config.BIND_ID_LIST)
    void getListData(SolidCallback callback){
        new Thread(() -> {
            final List<ListSolidItem1> listSolidItem1s =new ArrayList<>();
            listSolidItem1s.add(new ListSolidItem1("第一行数据","使用这个框架可以隐式的对view和data隔离交互"));
            listSolidItem1s.add(new ListSolidItem1("第二行数据","交互的耦合在框架内部已经实现"));
            listSolidItem1s.add(new ListSolidItem1("第三行数据","这是一个还在更新中的框架"));
            listSolidItem1s.add(new ListSolidItem1("第四行数据","希望能够取代mvp框架，减少很多定义和繁琐操作"));
            listSolidItemList.clear();
            listSolidItemList.addAll(listSolidItem1s);
            callback.onDataGet(null);
        }).start();
    }

    @SolidData(bindId = Config.BIND_ID_CLICK)
    void removeItem(Object data,String msg){
        Log.e("size",listSolidItemList.size()+"");
        listSolidItemList.remove((int)data);
        Log.e("size",listSolidItemList.size()+"");
        Solid.getInstance().call(solidId(),Config.BIND_ID_REFRESH, Solid.CallType.CALL_TYPE_DATA_TO_VIEW);
    }

    @SolidData(bindId = Config.BIND_ID_REFRESH)
    void onRefresh(SolidCallback callback){
        callback.onDataGet(null);
    }

    @Override
    public int solidId() {
        return Config.SOLID_ID_LIST;
    }
}
