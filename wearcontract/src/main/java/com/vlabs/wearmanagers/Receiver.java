package com.vlabs.wearmanagers;

public interface Receiver<T> {
    void receive(T t);
}
