package com.vlabs;

public interface Converter<Input, Output> {

    class SelfConverter<Input> implements Converter<Input, Input> {

        @Override
        public Input convert(final Input input) {
            return input;
        }
    }

    Output convert(Input input);

}
