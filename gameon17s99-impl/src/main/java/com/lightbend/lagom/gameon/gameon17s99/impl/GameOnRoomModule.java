/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.lightbend.lagom.gameon.gameon17s99.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.gameon.bazaar.api.BazaarService;
import com.lightbend.lagom.gameon.gameon17s99.api.RoomService;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the RoomService so that it can be served.
 */
public class GameOnRoomModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(RoomService.class, RoomServiceImpl.class);
        bindClient(BazaarService.class);
    }
}
