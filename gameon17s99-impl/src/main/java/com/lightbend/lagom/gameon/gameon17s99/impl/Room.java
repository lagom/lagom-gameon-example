package com.lightbend.lagom.gameon.gameon17s99.impl;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.RoomCommand;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Chat;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Event;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Location;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern COMMAND = Pattern.compile("\\A/(\\S+)");
    private static final String UNKNOWN_COMMAND = "Unknown command: ";

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

    private void handleCommand(RoomCommand message) {
        Optional<String> command = parseCommand(message);
        if (command.isPresent()) {
            switch (command.get()) {
                case "look":
                    handleLookCommand(message);
                default:
                    handleUnknownCommand(message);
            }
        } else handleChat(message);
    }

    private Optional<String> parseCommand(RoomCommand message) {
        Matcher matcher = COMMAND.matcher(message.getContent());
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    private void handleLookCommand(RoomCommand lookCommand) {
        Location location = Location.builder()
                .playerId(lookCommand.getUserId())
                .name(NAME)
                .fullName(FULL_NAME)
                .description(DESCRIPTION)
                .exits(EXITS)
                .commands(COMMANDS)
                .roomInventory(INVENTORY)
                .build();
        reply(location);
    }

    private void handleUnknownCommand(RoomCommand unknownCommand) {
        Event unknownCommandResponse = Event.builder()
                .playerId(unknownCommand.getUserId())
                .content(HashTreePMap.singleton(unknownCommand.getUserId(), UNKNOWN_COMMAND + unknownCommand.getContent()))
                .bookmark(Optional.empty())
                .build();
        reply(unknownCommandResponse);
    }

    private void handleChat(RoomCommand chatCommand) {
        Chat chatResponse = Chat.builder()
                .playerId(GameOnRoomResponse.PlayerResponse.ALL_PLAYERS)
                .username(chatCommand.getUsername())
                .content(chatCommand.getContent())
                .bookmark(Optional.empty())
                .build();
        reply(chatResponse);
    }

    private void reply(GameOnRoomResponse response) {
        sender().tell(response, self());
    }
}
