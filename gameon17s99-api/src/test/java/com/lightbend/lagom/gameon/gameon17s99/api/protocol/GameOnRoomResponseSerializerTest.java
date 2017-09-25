package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import akka.util.ByteString;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.*;
import com.lightbend.lagom.javadsl.api.deser.DeserializationException;
import org.junit.Test;
import org.pcollections.HashTreePMap;
import org.pcollections.TreePVector;

import java.util.Optional;

import static com.google.common.primitives.Ints.asList;
import static com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.PlayerResponse.ALL_PLAYERS;
import static org.junit.Assert.assertEquals;

public class GameOnRoomResponseSerializerTest {
    private final GameOnRoomResponseSerializer serializer = new GameOnRoomResponseSerializer();

    @Test(expected = DeserializationException.class)
    public void deserializeInvalidMessage() {
        serializer.deserialize(ByteString.fromString("invalid"));
    }

    @Test(expected = DeserializationException.class)
    public void deserializeUnknownTargetTypeMessage() {
        serializer.deserialize(ByteString.fromString("invalid,<playerId>,{}"));
    }

    @Test
    public void deserializeAckResponse() {
        ByteString message = ByteString.fromString("ack,{\n" +
                "    \"version\": [ 1, 2 ]\n" +
                "}"
        );

        Ack expected = new Ack(TreePVector.from(asList(1, 2)));

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeLocationResponse() {
        ByteString message = ByteString.fromString("player,<playerId>,{\n" +
                "    \"type\": \"location\",\n" +
                "    \"name\": \"Room name\",\n" +
                "    \"fullName\": \"Room's descriptive full name\",\n" +
                "    \"description\": \"Lots of text about what the room looks like\",\n" +
                "    \"exits\": {\n" +
                "        \"shortDirection\" : \"currentDescription for Player\",\n" +
                "        \"N\" :  \"a dark entranceway\"\n" +
                "    },\n" +
                "    \"commands\": {\n" +
                "        \"/custom\" : \"Description of what command does\"\n" +
                "    },\n" +
                "    \"roomInventory\": [\"itemA\",\"itemB\"]\n" +
                "}"
        );

        Location expected = new GameOnRoomResponse.Location(
                "<playerId>",
                "Room name",
                "Room's descriptive full name",
                "Lots of text about what the room looks like",
                HashTreePMap.<String, String>empty()
                        .plus("shortDirection", "currentDescription for Player")
                        .plus("N", "a dark entranceway"),
                HashTreePMap.singleton("/custom", "Description of what command does"),
                TreePVector.<String>empty()
                        .plus("itemA")
                        .plus("itemB")
        );

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeChatResponse() {
        ByteString message = ByteString.fromString("player,*,{\n" +
                "  \"type\": \"chat\",\n" +
                "  \"username\": \"<username>\",\n" +
                "  \"content\": \"<message>\",\n" +
                "  \"bookmark\": \"String representing last message seen\"\n" +
                "}\n"
        );

        Chat expected = new Chat(
                ALL_PLAYERS,
                "<username>",
                "<message>",
                Optional.of("String representing last message seen")
        );

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeEventResponse() {
        ByteString message = ByteString.fromString("player,<playerId>,{\n" +
                "    \"type\": \"event\",\n" +
                "    \"content\": {\n" +
                "        \"*\": \"general text for everyone\",\n" +
                "        \"<playerId>\": \"specific to player\"\n" +
                "        },\n" +
                "    \"bookmark\": \"String representing last message seen\"\n" +
                "}"
        );

        Event expected = new Event(
                "<playerId>",
                HashTreePMap.<String, String>empty()
                        .plus(ALL_PLAYERS, "general text for everyone")
                        .plus("<playerId>", "specific to player"),
                Optional.of("String representing last message seen")
        );

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeExitResponse() {
        ByteString message = ByteString.fromString("playerLocation,<playerId>,{\n" +
                "    \"type\": \"exit\",\n" +
                "    \"content\": \"You exit through door xyz... \",\n" +
                "    \"exitId\": \"N\"\n" +
                // exit not supported
                "}"
        );

        Exit expected = new Exit(
                "<playerId>",
                "You exit through door xyz... ",
                "N"
        );

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void roundTripAckResponse() {
        assertSerializationRoundTripIsEqual(new Ack(TreePVector.from(asList(1, 2))));
    }

    @Test
    public void roundTripLocationResponse() {
        assertSerializationRoundTripIsEqual(
                new GameOnRoomResponse.Location(
                        "<playerId>",
                        "Room name",
                        "Room's descriptive full name",
                        "Lots of text about what the room looks like",
                        HashTreePMap.<String, String>empty()
                                .plus("shortDirection", "currentDescription for Player")
                                .plus("N", "a dark entranceway"),
                        HashTreePMap.singleton("/custom", "Description of what command does"),
                        TreePVector.<String>empty()
                                .plus("itemA")
                                .plus("itemB")
                )
        );
    }

    @Test
    public void roundTripChatResponse() {
        assertSerializationRoundTripIsEqual(
                new Chat(
                        ALL_PLAYERS,
                        "<username>",
                        "<message>",
                        Optional.of("String representing last message seen")
                )
        );
    }

    @Test
    public void roundTripEventResponse() {
        assertSerializationRoundTripIsEqual(
                new Event(
                        "<playerId>",
                        HashTreePMap.<String, String>empty()
                                .plus(ALL_PLAYERS, "general text for everyone")
                                .plus("<playerId>", "specific to player"),
                        Optional.of("String representing last message seen")
                )
        );
    }

    @Test
    public void roundTripExitResponse() {
        assertSerializationRoundTripIsEqual(
                new Exit(
                        "<playerId>",
                        "You exit through door xyz... ",
                        "N"
                )
        );
    }

    private void assertSerializationRoundTripIsEqual(GameOnRoomResponse expected) {
        assertEquals(expected, serializer.deserialize(serializer.serialize(expected)));
    }
}
