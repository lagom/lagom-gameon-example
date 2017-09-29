package com.lightbend.lagom.gameon.gameon17s99.impl;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.RoomCommand;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Chat;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Location;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.util.Optional;

import static com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.RoomHello;

class Room extends AbstractActor {
    static final String NAME = "Room name";
    static final String FULL_NAME = "Room's descriptive full name";
    static final String DESCRIPTION = "Lots of text about what the room looks like";

    static final PMap<String, String> EXITS = HashTreePMap.<String, String>empty()
            .plus("N", "a dark entranceway")
            .plus("S", "a heavy door");

    static final PMap<String, String> COMMANDS = HashTreePMap.<String, String>empty()
            .plus("/ping", "Does this work?");

    static final PSequence<String> INVENTORY = TreePVector.<String>empty()
            .plus("itemA")
            .plus("itemB");

    static Props props() {
        return Props.create(Room.class);
    }

    Room() {
        receive(ReceiveBuilder
                .create()
                .match(RoomHello.class, this::replyWithLocation)
                .match(RoomCommand.class, this::handleCommand)
                .build()
        );

    }

    private void replyWithLocation(RoomHello hello) {
        Location location = Location.builder()
                .playerId(hello.getUserId())
                .name(NAME)
                .fullName(FULL_NAME)
                .description(DESCRIPTION)
                .exits(EXITS)
                .commands(COMMANDS)
                .roomInventory(INVENTORY)
                .build();
        reply(location);
    }

    private void handleCommand(RoomCommand command) {
        reply(handleChat(command));
    }

    private Chat handleChat(RoomCommand command) {
        return Chat.builder()
                .playerId(GameOnRoomResponse.PlayerResponse.ALL_PLAYERS)
                .username(command.getUsername())
                .content(command.getContent())
                .bookmark(Optional.empty())
                .build();
    }

    private void reply(GameOnRoomResponse response) {
        sender().tell(response, self());
    }
}
