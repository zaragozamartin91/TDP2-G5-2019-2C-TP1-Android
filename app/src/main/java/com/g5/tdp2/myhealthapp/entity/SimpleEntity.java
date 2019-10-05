package com.g5.tdp2.myhealthapp.entity;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class SimpleEntity {
    private long id;
    private String name;

    public SimpleEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() { return id; }

    public String getName() { return name; }

    public static <T extends SimpleEntity> List<T> fromNames(Collection<String> names, Class<T> seClass) {
        AtomicLong id = new AtomicLong(0L);
        return names.stream().map(name -> {
            try {
                Constructor<T> cons = seClass.getConstructor(long.class, String.class);
                return cons.newInstance(id.getAndIncrement(), name);
            } catch (Exception e) {
                throw new TypeNotPresentException("No hay un constructor adecuado para " + seClass, e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleEntity that = (SimpleEntity) o;

        if (id != that.id) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
