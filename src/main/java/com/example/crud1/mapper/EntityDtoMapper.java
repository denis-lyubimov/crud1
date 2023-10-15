package com.example.crud1.mapper;

import java.util.List;

public interface EntityDtoMapper<T, V> {
    V entityToDto(T t);

    List<V> entityListToDtoList(List<T> t);

    T dtoToEntity(V v);

    List<T> dtoListToEntityList(List<V> v);
}
