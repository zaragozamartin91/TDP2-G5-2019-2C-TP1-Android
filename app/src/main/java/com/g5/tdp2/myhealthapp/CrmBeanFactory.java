package com.g5.tdp2.myhealthapp;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public enum CrmBeanFactory {
    INSTANCE;

    private List<Object> beans = new ArrayList<>();

    public void addBean(Object obj) {
        beans.add(obj);
    }

    public <T> T getBean(Class<T> clazz) {
        try {
            Object obj = beans.stream().filter(clazz::isInstance).findFirst().orElseThrow(
                    () -> new RuntimeException("No existe un bean de tipo " + clazz)
            );
            return clazz.cast(obj);
        } catch (Throwable e) {
            // el android sdk obliga a capturar la excepcion
            throw new NoSuchElementException(e.getMessage());
        }
    }
}
