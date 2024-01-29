package com.pintor.sse_practice.global.response;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class DataModel<T> extends EntityModel {

    public DataModel(T data) {
        super(data);
    }

    public static <T> DataModel<T> of(T data,
                                      WebMvcLinkBuilder selfLink) {

        DataModel dataModel = new DataModel<>(data);
        dataModel.add(selfLink.withSelfRel());

        return dataModel;
    }
}
