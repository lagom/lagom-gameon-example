package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

public interface GameOnRoomRequest extends GameOnMessage {
    @JsonIgnore
    String getRoomId();

    interface RoomRequestBuilder<R extends GameOnRoomRequest> {
        RoomRequestBuilder<R> roomId(String roomId);

        R build();
    }

    @Value
    @Builder
    class RoomCommand implements GameOnRoomRequest {
        @NonNull String roomId;
        @NonNull String username;
        @NonNull String userId;
        @NonNull String content;

        static class RoomCommandBuilder implements RoomRequestBuilder<RoomCommand> {
            // Implemented automatically by Lombok
        }
    }

    @Value
    @Builder
    class RoomHello implements GameOnRoomRequest {
        @NonNull String roomId;
        @NonNull String username;
        @NonNull String userId;
        int version;
        boolean recovery;

        static class RoomHelloBuilder implements RoomRequestBuilder<RoomHello> {
            // Implemented automatically by Lombok
        }
    }

    @Value
    @Builder
    class RoomGoodbye implements GameOnRoomRequest {
        @NonNull String roomId;
        @NonNull String username;
        @NonNull String userId;

        static class RoomGoodbyeBuilder implements RoomRequestBuilder<RoomGoodbye> {
            // Implemented automatically by Lombok
        }
    }

    @Value
    @Builder
    class RoomJoin implements GameOnRoomRequest {
        @NonNull String roomId;
        @NonNull String username;
        @NonNull String userId;
        int version;

        static class RoomJoinBuilder implements RoomRequestBuilder<RoomJoin> {
            // Implemented automatically by Lombok
        }
    }

    @Value
    @Builder
    class RoomPart implements GameOnRoomRequest {
        @NonNull String roomId;
        @NonNull String username;
        @NonNull String userId;

        static class RoomPartBuilder implements RoomRequestBuilder<RoomPart> {
            // Implemented automatically by Lombok
        }
    }
}
