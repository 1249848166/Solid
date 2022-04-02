package com.su.example.data;

import com.su.example.model.SimpleTextSolidData;
import com.su.solid._abstract.SolidBaseData;
import com.su.solid.annotation.SolidData;
import com.su.solid.callback.SolidCallback;

import java.util.Collections;

public class SimpleDataManager implements SolidBaseData {

    @SolidData(bindId = 1)
    void getSimpleTextData(SolidCallback callback){
        callback.onDataGet(Collections.singletonList(new SimpleTextSolidData("测试简单的view和data隔离")));
    }

    @Override
    public int solidId() {
        return 0;
    }
}
