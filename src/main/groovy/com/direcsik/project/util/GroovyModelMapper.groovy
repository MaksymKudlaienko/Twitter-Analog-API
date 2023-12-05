package com.direcsik.project.util

import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component

@Component
class GroovyModelMapper extends ModelMapper {

    @Override
    <T> T map(Object source, Class<T> targetClass) {
        T result = super.map(source, targetClass)
        result.setMetaClass(targetClass.getMetaClass())        //This row is necessary, due to the fact, that Groovy has problems with model mapper library and cannot update meta class properly
        return result
    }
}
