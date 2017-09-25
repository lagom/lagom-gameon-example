package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.pcollections.PMap;
import org.pcollections.PSequence;

import java.util.Optional;

public interface GameOnRoomResponse extends GameOnMessage {
    @Value
    class Ack implements GameOnRoomResponse {
        PSequence<Integer> version;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @Type(name = "location", value = Location.class),
            @Type(name = "chat", value = Chat.class),
            @Type(name = "event", value = Event.class)
    })
    interface PlayerResponse extends GameOnRoomResponse {
        String ALL_PLAYERS = "*";

        @JsonIgnore
        String getPlayerId();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @Type(name = "location", value = Location.LocationBuilder.class),
            @Type(name = "chat", value = Chat.ChatBuilder.class),
            @Type(name = "event", value = Event.EventBuilder.class)
    })
    interface PlayerResponseBuilder<R extends PlayerResponse> {
        PlayerResponseBuilder<R> playerId(String playerId);

        R build();
    }

    @Value
    @Builder
    class Location implements PlayerResponse {
        @NonNull String playerId;
        @NonNull String name;
        @NonNull String fullName;
        @NonNull String description;
        @NonNull PMap<String, String> exits;
        @NonNull PMap<String, String> commands;
        @NonNull PSequence<String> roomInventory;

        static class LocationBuilder implements PlayerResponseBuilder<Location> {
            // Implemented automatically by Lombok
        }
    }

    @Value
    @Builder
    class Chat implements PlayerResponse {
        @NonNull String playerId;
        @NonNull String username;
        @NonNull String content;
        @NonNull Optional<String> bookmark;

        static class ChatBuilder implements PlayerResponseBuilder<Chat> {
            // Implemented automatically by Lombok
        }
    }

    @Value
    @Builder
    class Event implements PlayerResponse {
        @NonNull String playerId;
        @NonNull PMap<String, String> content;
        @NonNull Optional<String> bookmark;

        static class EventBuilder implements PlayerResponseBuilder<Event> {
            // Implemented automatically by Lombok
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @Type(name = "exit", value = Exit.class)
    })
    interface PlayerLocationResponse extends GameOnRoomResponse {
        @JsonIgnore
        String getPlayerId();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @Type(name = "exit", value = Exit.ExitBuilder.class)
    })
    interface PlayerLocationResponseBuilder<R extends PlayerLocationResponse> {
        PlayerLocationResponseBuilder<R> playerId(String playerId);

        R build();
    }

    @Value
    @Builder
    class Exit implements PlayerLocationResponse {
        @NonNull String playerId;
        @NonNull String content;
        @NonNull String exitId;
        // exit not supported

        static class ExitBuilder implements PlayerLocationResponseBuilder<Exit> {
            // Implemented automatically by Lombok
        }
    }
}
