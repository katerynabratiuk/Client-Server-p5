package com.github.katerynabratiuk.repository;

import java.util.List;

public interface GenericRepository<E, K> {

    // E - entity, K - key of entity

    E create(E e);
    void delete(K k);
    void update(E e);
    E get(K k);
    List<E> getAll();

}
