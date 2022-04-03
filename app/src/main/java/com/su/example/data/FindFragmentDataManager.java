package com.su.example.data;

import com.su.example.config.Config;
import com.su.solid._abstract.SolidBaseData;

public class FindFragmentDataManager implements SolidBaseData {
    @Override
    public int solidId() {
        return Config.SOLID_ID_FRAGMENT_FIND;
    }
}
