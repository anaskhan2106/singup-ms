package com.mapper;

import com.domain.DocumentStore;
import com.dto.DocumentStoreDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class MapperClass {

    @Mappings({
            @Mapping(source = "user_id", target = "driverProfile.userId")
    })
    public abstract DocumentStore documentStoreDtoToDocumentStore(DocumentStoreDto documentStoreDto);
}