/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.lightbend.lagom.gameon.gameon17s99.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

public interface RoomService extends Service {
    ServiceCall<NotUsed, Done> room();

    @Override
    default Descriptor descriptor() {
        return named("gameon17s99").withCalls(
                pathCall("/gameon17s99", this::room)
        ).withAutoAcl(true);
    }
}
