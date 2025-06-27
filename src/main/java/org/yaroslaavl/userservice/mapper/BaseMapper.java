package org.yaroslaavl.userservice.mapper;

import org.mapstruct.MappingTarget;

public interface BaseMapper<D, E, I> {

    D toDto (E entity);

    void updateEntity(I inputDto, @MappingTarget E entity);
}
