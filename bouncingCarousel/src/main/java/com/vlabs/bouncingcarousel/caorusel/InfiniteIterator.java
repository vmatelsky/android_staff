package com.vlabs.bouncingcarousel.caorusel;

public interface InfiniteIterator<Type> {

    InfiniteIterator<Type> next();
    InfiniteIterator<Type> previous();
    Type item();

}