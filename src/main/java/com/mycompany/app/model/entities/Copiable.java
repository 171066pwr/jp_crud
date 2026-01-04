package com.mycompany.app.model.entities;

public interface Copiable<T> {
    void copyFrom(T entity);
}
