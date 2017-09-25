package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import akka.util.ByteString;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.*;
import com.lightbend.lagom.javadsl.api.deser.DeserializationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameOnRoomRequestSerializerTest {
    private final GameOnRoomRequestSerializer serializer = new GameOnRoomRequestSerializer();

    @Test(expected = DeserializationException.class)
    public void deserializeInvalidMessage() {
        serializer.deserialize(ByteString.fromString("invalid"));
    }

    @Test(expected = DeserializationException.class)
    public void deserializeUnknownTargetTypeMessage() {
        serializer.deserialize(ByteString.fromString("invalid,<roomId>,{}"));
    }

    @Test
    public void deserializeRoomCommandRequest() {
        ByteString message = ByteString.fromString("room,<roomId>,{\n" +
                "    \"username\": \"<username>\",\n" +
                "    \"userId\": \"<userId>\",\n" +
                "    \"content\": \"<message>\"\n" +
                "}"
        );

        RoomCommand expected = new RoomCommand("<roomId>", "<username>", "<userId>", "<message>");

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomHelloRequest() {
        ByteString message = ByteString.fromString("roomHello,<roomId>,{\n" +
                "    \"username\": \"<username>\",\n" +
                "    \"userId\": \"<userId>\",\n" +
                "    \"version\": 2\n" +
                "}"
        );

        RoomHello expected = new RoomHello("<roomId>", "<username>", "<userId>", 2, false);

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomHelloRequestWithRecoveryFalse() {
        ByteString message = ByteString.fromString("roomHello,<roomId>,{\n" +
                "    \"username\": \"<username>\",\n" +
                "    \"userId\": \"<userId>\",\n" +
                "    \"version\": 2,\n" +
                "    \"recovery\": false\n" +
                "}"
        );

        RoomHello expected = new RoomHello("<roomId>", "<username>", "<userId>", 2, false);

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomHelloRequestWithRecoveryTrue() {
        ByteString message = ByteString.fromString("roomHello,<roomId>,{\n" +
                "    \"username\": \"<username>\",\n" +
                "    \"userId\": \"<userId>\",\n" +
                "    \"version\": 2,\n" +
                "    \"recovery\": true\n" +
                "}"
        );

        RoomHello expected = new RoomHello("<roomId>", "<username>", "<userId>", 2, true);

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomGoodbyeRequest() {
        ByteString message = ByteString.fromString("roomGoodbye,<roomId>,{\n" +
                "    \"username\": \"<username>\",\n" +
                "    \"userId\": \"<userId>\"\n" +
                "}"
        );

        RoomGoodbye expected = new RoomGoodbye("<roomId>", "<username>", "<userId>");

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomJoinRequest() {
        ByteString message = ByteString.fromString("roomJoin,<roomId>,{\n" +
                "    \"username\": \"<username>\",\n" +
                "    \"userId\": \"<userId>\",\n" +
                "    \"version\": 2\n" +
                "}"
        );

        RoomJoin expected = new RoomJoin("<roomId>", "<username>", "<userId>", 2);

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomPartRequest() {
        ByteString message = ByteString.fromString("roomPart,<roomId>,{\n" +
                "    \"username\": \"<username>\",\n" +
                "    \"userId\": \"<userId>\"\n" +
                "}"
        );

        RoomPart expected = new RoomPart("<roomId>", "<username>", "<userId>");

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void roundTripRoomCommandRequest() {
        assertSerializationRoundTripIsEqual(new RoomCommand("<roomId>", "<username>", "<userId>", "<message>"));
    }

    @Test
    public void roundTripRoomHelloRequestWithRecoveryFalse() {
        assertSerializationRoundTripIsEqual(new RoomHello("<roomId>", "<username>", "<userId>", 2, false));
    }

    @Test
    public void roundTripRoomHelloRequestWithRecoveryTrue() {
        assertSerializationRoundTripIsEqual(new RoomHello("<roomId>", "<username>", "<userId>", 2, true));
    }

    @Test
    public void roundTripRoomGoodbyeRequest() {
        assertSerializationRoundTripIsEqual(new RoomGoodbye("<roomId>", "<username>", "<userId>"));
    }

    @Test
    public void roundTripRoomJoinRequest() {
        assertSerializationRoundTripIsEqual(new RoomJoin("<roomId>", "<username>", "<userId>", 2));
    }

    @Test
    public void roundTripRoomPartRequest() {
        assertSerializationRoundTripIsEqual(new RoomPart("<roomId>", "<username>", "<userId>"));
    }

    private void assertSerializationRoundTripIsEqual(GameOnRoomRequest expected) {
        assertEquals(expected, serializer.deserialize(serializer.serialize(expected)));
    }
}
