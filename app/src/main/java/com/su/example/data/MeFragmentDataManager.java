package com.su.example.data;

import com.su.example.config.Config;
import com.su.example.model.list.ListSolidItem;
import com.su.solid._abstract.SolidBaseData;
import com.su.solid.annotation.SolidData;
import com.su.solid.callback.SolidCallback;
import com.su.solid.solid.Solid;

import java.util.List;

public class MeFragmentDataManager implements SolidBaseData {


    @Override
    public int solidId() {
        return Config.SOLID_ID_FRAGMENT_ME;
    }
}
