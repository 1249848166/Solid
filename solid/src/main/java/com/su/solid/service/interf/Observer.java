package com.su.solid.service.interf;

import android.view.View;

public interface Observer<T> {
    void setConsumer(Object consumer);
    void observe(Observable<T> observable);
}
