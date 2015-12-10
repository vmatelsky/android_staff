package com.vlabs.bouncingcarousel;

public final class Maybe<T> {
    public static final Maybe<?> NOTHING = new Maybe<Object>(null);

    private final T mValue;

    private Maybe(final T value) {
        mValue = value;
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Maybe)) {
            return false;
        }

        final Maybe other = (Maybe) object;

        if (isDefined()) {
            if (other.isDefined()) {
                if (mValue == null) {
                    return other.mValue == null;
                } else {
                    return mValue.equals(other.mValue);
                }
            } else {
                return false;
            }
        } else {
            return !other.isDefined();
        }
    }

    @Override
    public int hashCode() {
        if (isDefined()) {
            return 1 + (mValue == null ? 0 : mValue.hashCode());
        } else {
            return 0;
        }
    }

    public static <T> Maybe<T> nullIsNothing(final T value) {
        if (value == null) {
            return nothing();
        } else {
            return just(value);
        }
    }

    public static <T> Maybe<T> just(final T value) {
        return new Maybe<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> Maybe<T> nothing() {
        return (Maybe<T>)NOTHING;
    }

    public boolean isDefined() {
        return this != NOTHING;
    }

    public T get() {
        if (isDefined()) return mValue;

        throw new RuntimeException("Value is not defined.");
    }

    public T or(final T defaultValue) {
        return isDefined() ? mValue : defaultValue;
    }

}