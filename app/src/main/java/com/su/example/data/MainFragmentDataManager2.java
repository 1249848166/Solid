package com.su.example.data;

import com.su.example.config.Config;
import com.su.solid._abstract.SolidBaseData;
import com.su.solid.annotation.SolidData;
import com.su.solid.callback.SolidCallback;

public class MainFragmentDataManager2 implements SolidBaseData {

    @SolidData(bindId = Config.BIND_ID_REFRESH)
    void refreshList(SolidCallback callback){
        callback.onDataGet(null);
    }

    @Override
    public int solidId() {
        return Config.SOLID_ID_FRAGMENT_MAIN;
    }
}
