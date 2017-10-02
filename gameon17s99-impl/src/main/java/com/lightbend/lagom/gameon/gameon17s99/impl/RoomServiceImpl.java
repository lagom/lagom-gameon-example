/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.lightbend.lagom.gameon.gameon17s99.impl;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.stream.javadsl.Source;
import akka.util.Timeout;
import com.lightbend.lagom.gameon.bazaar.api.BazaarService;
import com.lightbend.lagom.gameon.gameon17s99.api.RoomService;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Ack;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import org.pcollections.TreePVector;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.PatternsCS.ask;
import static com.google.common.primitives.Ints.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class RoomServiceImpl implements RoomService {
    static final Ack ACK = new Ack(TreePVector.from(asList(1, 2)));

    private static final Timeout ASK_TIMEOUT = Timeout.apply(5, TimeUnit.SECONDS);

    private final ActorRef roomActor;

    @Inject
    public RoomServiceImpl(ActorSystem system, BazaarService bazaarService) {
        roomActor = system.actorOf(Room.props(bazaarService), "room");
    }

    @Override
    public ServiceCall<Source<GameOnRoomRequest, ?>, Source<GameOnRoomResponse, NotUsed>> room() {
        return requestStream -> completedFuture(
                Source
                        // Send an "ack" message immediately after connection
                        .single(ACK)
                        // Then send each request message to the room and wait for a response
                        .concat(requestStream.mapAsync(1, this::forwardToRoomActor))
        );
    }

    private CompletionStage<GameOnRoomResponse> forwardToRoomActor(GameOnRoomRequest request) {
        return ask(roomActor, request, ASK_TIMEOUT).thenApply(response -> (GameOnRoomResponse) response);
    }
}
