package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import akka.util.ByteString;
import akka.util.ByteStringBuilder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.pcollections.PCollectionsModule;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnMessage.GameOnMessageFormat;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnMessage.MessageWithRecipientFormat;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.*;
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

public class GameOnRoomResponseSerializer implements
        StrictMessageSerializer<GameOnRoomResponse>,
        NegotiatedSerializer<GameOnRoomResponse, ByteString>,
        NegotiatedDeserializer<GameOnRoomResponse, ByteString> {

    private static final ByteString COMMA = ByteString.fromString(",");
    private final ObjectMapper objectMapper;
    private final PSequence<GameOnMessageFormat<? extends GameOnRoomResponse>> messageFormats;

    public GameOnRoomResponseSerializer() {
        this.objectMapper = new ObjectMapper();

        // Default configuration copied from Lagom's JacksonObjectMapperProvider
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.registerModule(new PCollectionsModule());
        objectMapper.registerModule(new Jdk8Module());

        messageFormats = TreePVector.<GameOnMessageFormat<? extends GameOnRoomResponse>>empty()
                .plus(new AckFormat())
                .plus(new PlayerResponseFormat())
                .plus(new PlayerLocationResponseFormat());
    }

    @Override
    public ByteString serialize(GameOnRoomResponse response) throws SerializationException {
        try {
            return formatForResponse(response).serialize(response);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public GameOnRoomResponse deserialize(ByteString bytes) throws DeserializationException {
        ParsedMessage message = GameOnMessage.parse(bytes);
        try {
            return formatForTarget(message.getTarget()).deserialize(message);
        } catch (IOException e) {
            throw new DeserializationException(e);
        }
    }

    @Override
    public NegotiatedSerializer<GameOnRoomResponse, ByteString> serializerForRequest() {
        return this;
    }

    @Override
    public NegotiatedDeserializer<GameOnRoomResponse, ByteString> deserializer(MessageProtocol protocol)
            throws UnsupportedMediaType {
        return this;
    }

    @Override
    public NegotiatedSerializer<GameOnRoomResponse, ByteString> serializerForResponse(
            List<MessageProtocol> acceptedMessageProtocols
    ) throws NotAcceptable {
        return this;
    }

    private GameOnMessageFormat<? extends GameOnRoomResponse> formatForResponse(GameOnRoomResponse response) {
        for (GameOnMessageFormat<? extends GameOnRoomResponse> format : messageFormats) {
            if (format.canFormat(response)) return format;
        }
        throw new SerializationException("Unknown response type: " + response);
    }

    private GameOnMessageFormat<? extends GameOnRoomResponse> formatForTarget(String target) {
        for (GameOnMessageFormat<? extends GameOnRoomResponse> format : messageFormats) {
            if (format.canFormat(target)) return format;
        }
        throw new DeserializationException("Unknown response type: " + target);
    }

    private class AckFormat extends GameOnMessageFormat<Ack> {
        AckFormat() {
            super(Ack.class, "ack");
        }

        @Override
        ByteString serializeTyped(Ack response) throws IOException {
            ByteStringBuilder builder = ByteString.createBuilder()
                    .append(ByteString.fromString(targetName))
                    .append(COMMA);
            objectMapper.writeValue(builder.asOutputStream(), response);
            return builder.result();
        }

        @Override
        Ack deserialize(ParsedMessage message) throws IOException {
            // ack has no recipient, so parse the messageDetails directly
            return objectMapper.readerFor(Ack.class).readValue(message.getPayload());
        }
    }

    private class PlayerResponseFormat extends MessageWithRecipientFormat<PlayerResponse> {
        PlayerResponseFormat() {
            super(PlayerResponse.class, "player");
        }

        @Override
        ByteString serializeTyped(PlayerResponse response) throws IOException {
            ByteStringBuilder builder = ByteString.createBuilder()
                    .append(ByteString.fromString(targetName))
                    .append(COMMA)
                    .append(ByteString.fromString(response.getPlayerId()))
                    .append(COMMA);
            objectMapper.writeValue(builder.asOutputStream(), response);
            return builder.result();
        }

        @Override
        PlayerResponse deserialize(String playerId, String payload) throws IOException {
            return objectMapper.readerFor(PlayerResponseBuilder.class).<PlayerResponseBuilder>readValue(payload)
                    .playerId(playerId)
                    .build();
        }
    }

    private class PlayerLocationResponseFormat extends MessageWithRecipientFormat<PlayerLocationResponse> {
        PlayerLocationResponseFormat() {
            super(PlayerLocationResponse.class, "playerLocation");
        }

        @Override
        ByteString serializeTyped(PlayerLocationResponse response) throws IOException {
            ByteStringBuilder builder = ByteString.createBuilder()
                    .append(ByteString.fromString(targetName))
                    .append(COMMA)
                    .append(ByteString.fromString(response.getPlayerId()))
                    .append(COMMA);
            objectMapper.writeValue(builder.asOutputStream(), response);
            return builder.result();
        }

        @Override
        PlayerLocationResponse deserialize(String playerId, String payload) throws IOException {
            return objectMapper.readerFor(PlayerLocationResponseBuilder.class).<PlayerLocationResponseBuilder>readValue(payload)
                    .playerId(playerId)
                    .build();
        }
    }
}
