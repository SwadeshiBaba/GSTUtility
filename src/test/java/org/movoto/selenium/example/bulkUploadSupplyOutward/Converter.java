package org.movoto.selenium.example.bulkUploadSupplyOutward;

public interface Converter<T, X, U> {
    U convert(T source, X input);
}