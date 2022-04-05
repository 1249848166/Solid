package com.su.solid.service.interf;

public interface Observable<T> {
    T getDataSource();
    void setDataSource(T dataSource);
    void notice(Observer<T> observer);
}
