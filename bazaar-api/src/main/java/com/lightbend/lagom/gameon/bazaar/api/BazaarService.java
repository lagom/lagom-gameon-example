package com.lightbend.lagom.gameon.bazaar.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

public interface BazaarService extends Service {
    ServiceCall<NotUsed, String> bazaar();

    ServiceCall<ItemMessage, Done> useItem();

    @Override
    default Descriptor descriptor() {
        return named("bazaar")
                .withCalls(
                        pathCall("/api/bazaar", this::bazaar),
                        pathCall("/api/bazaar", this::useItem)
                );
    }
}
