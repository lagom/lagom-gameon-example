package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import akka.util.ByteString;
import akka.util.ByteStringBuilder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnMessage.GameOnMessageFormat;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnMessage.MessageWithRecipientFormat;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.*;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.RoomCommand.RoomCommandBuilder;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.RoomGoodbye.RoomGoodbyeBuilder;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.RoomHello.RoomHelloBuilder;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.RoomJoin.RoomJoinBuilder;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.RoomPart.RoomPartBuilder;
import com.lightbend.lagom.javadsl.api.deser.DeserializationException;
import com.lightbend.lagom.javadsl.api.deser.MessageSerializer.NegotiatedDeserializer;
import com.lightbend.lagom.javadsl.api.deser.MessageSerializer.NegotiatedSerializer;
import com.lightbend.lagom.javadsl.api.deser.SerializationException;
import com.lightbend.lagom.javadsl.api.deser.StrictMessageSerializer;
import com.lightbend.lagom.javadsl.api.transport.MessageProtocol;
import com.lightbend.lagom.javadsl.api.transport.NotAcceptable;
import com.lightbend.lagom.javadsl.api.transport.UnsupportedMediaType;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.io.IOException;
import java.util.List;

import static com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnMessage.ParsedMessage;
import static com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnMessage.parse;

public class GameOnRoomRequestSerializer implements
        StrictMessageSerializer<GameOnRoomRequest>,
        NegotiatedSerializer<GameOnRoomRequest, ByteString>,
        NegotiatedDeserializer<GameOnRoomRequest, ByteString> {

    private static final ByteString COMMA = ByteString.fromString(",");
    private final ObjectMapper objectMapper;
    private final PSequence<GameOnMessageFormat<? extends GameOnRoomRequest>> messageFormats;

    public GameOnRoomRequestSerializer() {
        this.objectMapper = new ObjectMapper();

        // Default configuration copied from Lagom's JacksonObjectMapperProvider
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        messageFormats = TreePVector.<GameOnMessageFormat<? extends GameOnRoomRequest>>empty()
                .plus(new RoomCommandFormat())
                .plus(new RoomHelloFormat())
                .plus(new RoomGoodbyeFormat())
                .plus(new RoomJoinFormat())
                .plus(new RoomPartFormat());
    }

    @Override
    public ByteString serialize(GameOnRoomRequest request) throws SerializationException {
        try {
            return formatForRequest(request).serialize(request);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public GameOnRoomRequest deserialize(ByteString bytes) throws DeserializationException {
        ParsedMessage message = parse(bytes);
        try {
            return formatForTarget(message.getTarget()).deserialize(message);
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }

    @Override
    public NegotiatedSerializer<GameOnRoomRequest, ByteString> serializerForRequest() {
        return this;
    }

    @Override
    public NegotiatedDeserializer<GameOnRoomRequest, ByteString> deserializer(MessageProtocol protocol)
            throws UnsupportedMediaType {
        return this;
    }

    @Override
    public NegotiatedSerializer<GameOnRoomRequest, ByteString> serializerForResponse(
            List<MessageProtocol> acceptedMessageProtocols
    ) throws NotAcceptable {
        return this;
    }

    private GameOnMessageFormat<? extends GameOnRoomRequest> formatForRequest(GameOnRoomRequest request) {
        for (GameOnMessageFormat<? extends GameOnRoomRequest> format : messageFormats) {
            if (format.canFormat(request)) return format;
        }
        throw new SerializationException("Unknown request type: " + request);
    }

    private GameOnMessageFormat<? extends GameOnRoomRequest> formatForTarget(String target) {
        for (GameOnMessageFormat<? extends GameOnRoomRequest> format : messageFormats) {
            if (format.canFormat(target)) return format;
        }
        throw new DeserializationException("Unknown request type: " + target);
    }

    private class RoomCommandFormat extends MessageWithRecipientFormat<RoomCommand> {
        RoomCommandFormat() {
            super(RoomCommand.class, "room");
        }

        @Override
        ByteString serializeTyped(RoomCommand request) throws IOException {
            ByteStringBuilder builder = ByteString.createBuilder()
                    .append(ByteString.fromString(targetName))
                    .append(COMMA)
                    .append(ByteString.fromString(request.getRoomId()))
                    .append(COMMA);
            objectMapper.writeValue(builder.asOutputStream(), request);
            return builder.result();
        }

        @Override
        RoomCommand deserialize(String roomId, String payload) throws IOException {
            RoomCommandBuilder builder = RoomCommand.builder();
            builder.roomId(roomId);
            objectMapper.readerForUpdating(builder).readValue(payload);
            return builder.build();
        }
    }

    private class RoomHelloFormat extends MessageWithRecipientFormat<RoomHello> {
        RoomHelloFormat() {
            super(RoomHello.class, "roomHello");
        }

        @Override
        ByteString serializeTyped(RoomHello request) throws IOException {
            ByteStringBuilder builder = ByteString.createBuilder()
                    .append(ByteString.fromString(targetName))
                    .append(COMMA)
                    .append(ByteString.fromString(request.getRoomId()))
                    .append(COMMA);
            objectMapper.writeValue(builder.asOutputStream(), request);
            return builder.result();
        }

        @Override
        RoomHello deserialize(String roomId, String payload) throws IOException {
            RoomHelloBuilder builder = RoomHello.builder();
            builder.roomId(roomId);
            objectMapper.readerForUpdating(builder).readValue(payload);
            return builder.build();
        }
    }

    private class RoomGoodbyeFormat extends MessageWithRecipientFormat<RoomGoodbye> {
        RoomGoodbyeFormat() {
            super(RoomGoodbye.class, "roomGoodbye");
        }

        @Override
        ByteString serializeTyped(RoomGoodbye request) throws IOException {
            ByteStringBuilder builder = ByteString.createBuilder()
                    .append(ByteString.fromString(targetName))
                    .append(COMMA)
                    .append(ByteString.fromString(request.getRoomId()))
                    .append(COMMA);
            objectMapper.writeValue(builder.asOutputStream(), request);
            return builder.result();
        }

        @Override
        RoomGoodbye deserialize(String roomId, String payload) throws IOException {
            RoomGoodbyeBuilder builder = RoomGoodbye.builder();
            builder.roomId(roomId);
            objectMapper.readerForUpdating(builder).readValue(payload);
            return builder.build();
        }
    }

    private class RoomJoinFormat extends MessageWithRecipientFormat<RoomJoin> {
        RoomJoinFormat() {
            super(RoomJoin.class, "roomJoin");
        }

        @Override
        ByteString serializeTyped(RoomJoin request) throws IOException {
            ByteStringBuilder builder = ByteString.createBuilder()
                    .append(ByteString.fromString(targetName))
                    .append(COMMA)
                    .append(ByteString.fromString(request.getRoomId()))
                    .append(COMMA);
            objectMapper.writeValue(builder.asOutputStream(), request);
            return builder.result();
        }

        @Override
        RoomJoin deserialize(String roomId, String payload) throws IOException {
            RoomJoinBuilder builder = RoomJoin.builder();
            builder.roomId(roomId);
            objectMapper.readerForUpdating(builder).readValue(payload);
            return builder.build();
        }
    }

    private class RoomPartFormat extends MessageWithRecipientFormat<RoomPart> {
        RoomPartFormat() {
            super(RoomPart.class, "roomPart");
        }

        @Override
        ByteString serializeTyped(RoomPart request) throws IOException {
            ByteStringBuilder builder = ByteString.createBuilder()
                    .append(ByteString.fromString(targetName))
                    .append(COMMA)
                    .append(ByteString.fromString(request.getRoomId()))
                    .append(COMMA);
            objectMapper.writeValue(builder.asOutputStream(), request);
            return builder.result();
        }

        @Override
        RoomPart deserialize(String roomId, String payload) throws IOException {
            RoomPartBuilder builder = RoomPart.builder();
            builder.roomId(roomId);
            objectMapper.readerForUpdating(builder).readValue(payload);
            return builder.build();
        }
    }
}
