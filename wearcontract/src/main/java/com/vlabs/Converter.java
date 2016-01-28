package com.vlabs;

public interface Converter<Input, Output> {

    Output convert(Input input);

}
