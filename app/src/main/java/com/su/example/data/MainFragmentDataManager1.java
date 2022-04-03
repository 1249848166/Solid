package com.su.example.data;

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

public class MainFragmentDataManager1 implements SolidBaseData {

    private final List<ListSolidItem> items=new ArrayList<>();

    @SolidData(bindId = Config.BIND_ID_LIST)
    void getListItems(SolidCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                items.clear();
                items.add(new ListSolidItem1("这是多个页面测试","已经扩展了框架规模，可以进行多绑定"));
                items.add(new ListSolidItem1("比如说","比如说，可以一个view绑定多个data，或者多个view与多个data绑定"));
                items.add(new ListSolidItem1("生命周期内数据互通互调","你可以自己设计注销时机，从而保证provider作用范围"));
                items.add(new ListSolidItem1("提醒","尽量不要将注销放在当前页面之外，否则会导致页面无法回收造成内存泄露等严重问题"));
                callback.onDataGet(items);
            }
        }).start();
    }

    @SolidData(bindId = Config.BIND_ID_CLICK)
    void onItemSelect(Object data,String msg){
        items.remove((int)data);
        Solid.getInstance().call(solidId(),Config.BIND_ID_REFRESH, Solid.CallType.CALL_TYPE_DATA_TO_VIEW);
    }

    @SolidDataProvider(id = Config.PROVIDER_ID_LIST)
    List<ListSolidItem> provideListItems(){
        return items;
    }

    @Override
    public int solidId() {
        return Config.SOLID_ID_FRAGMENT_MAIN;
    }
}
