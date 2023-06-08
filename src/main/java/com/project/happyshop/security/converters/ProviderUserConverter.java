package com.project.happyshop.security.converters;

public interface ProviderUserConverter<T, R> {

    R converter(T t);
}
