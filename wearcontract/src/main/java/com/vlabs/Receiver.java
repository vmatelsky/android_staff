package com.vlabs;

public interface Receiver<T> {
    void receive(T t);
}
