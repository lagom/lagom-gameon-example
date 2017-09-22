/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.lightbend.lagom.gameon.gameon17s99.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.gameon.gameon17s99.api.RoomService;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class RoomServiceImpl implements RoomService {
    @Inject
    public RoomServiceImpl() {
    }

    @Override
    public ServiceCall<NotUsed, Done> room() {
        return request -> CompletableFuture.completedFuture(Done.getInstance());
    }
}
