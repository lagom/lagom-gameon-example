/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.lightbend.lagom.gameon.gameon17s99.api;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequestSerializer;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponseSerializer;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

public interface RoomService extends Service {
    ServiceCall<Source<GameOnRoomRequest, ?>, Source<GameOnRoomResponse, NotUsed>> room();

    @Override
    default Descriptor descriptor() {
        return named("gameon17s99")
                .withCalls(
                        pathCall("/gameon17s99", this::room)
                )
                .withMessageSerializer(GameOnRoomRequest.class, new GameOnRoomRequestSerializer())
                .withMessageSerializer(GameOnRoomResponse.class, new GameOnRoomResponseSerializer())
                .withAutoAcl(true);
    }
}
