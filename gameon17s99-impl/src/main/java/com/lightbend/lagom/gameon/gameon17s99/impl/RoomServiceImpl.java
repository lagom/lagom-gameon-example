/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.lightbend.lagom.gameon.gameon17s99.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.gameon.gameon17s99.api.RoomService;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Ack;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

import static com.google.common.primitives.Ints.asList;

public class RoomServiceImpl implements RoomService {

    public static final Ack ACK = new Ack(TreePVector.from(asList(1, 2)));

    @Inject
    public RoomServiceImpl() {
    }

    @Override
    public ServiceCall<Source<GameOnRoomRequest, ?>, Source<GameOnRoomResponse, NotUsed>> room() {
        return requestStream -> CompletableFuture.completedFuture(Source.single(ACK));
    }
}
