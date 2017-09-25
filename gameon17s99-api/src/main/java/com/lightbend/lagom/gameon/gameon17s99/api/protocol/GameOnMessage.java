package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import akka.util.ByteString;
import com.lightbend.lagom.javadsl.api.deser.DeserializationException;
import lombok.NonNull;
import lombok.Value;

import java.io.IOException;
import java.util.Optional;

interface GameOnMessage {
    static ParsedMessage parse(ByteString bytes) {
        return parse(bytes.utf8String());
    }

    static ParsedMessage parse(String message) {
        int payloadStart = message.indexOf('{');
        if (payloadStart != -1) {
            String targetAndRecipient = message.substring(0, payloadStart);
            int targetEnd = targetAndRecipient.indexOf(',');
            if (targetEnd != -1) {
                String target = targetAndRecipient.substring(0, targetEnd);
                int recipientStart = targetEnd + 1;
                Optional<String> recipient = (recipientStart < payloadStart) ?
                        Optional.of(targetAndRecipient.substring(recipientStart, payloadStart - 1)) :
                        Optional.empty();
                String payload = message.substring(payloadStart);

                return new ParsedMessage(target, recipient, payload);
            }
        }

        throw new DeserializationException("Invalid format: " + message);
    }

    @Value
    final class ParsedMessage {
        @NonNull String target;
        @NonNull Optional<String> recipient;
        @NonNull String payload;
    }

    abstract class GameOnMessageFormat<M extends GameOnMessage> {
        final Class<M> messageClass;
        final String targetName;

        GameOnMessageFormat(Class<M> messageClass, String targetName) {
            this.messageClass = messageClass;
            this.targetName = targetName;
        }

        boolean canFormat(GameOnMessage message) {
            return messageClass.isInstance(message);
        }

        boolean canFormat(String target) {
            return targetName.equals(target);
        }

        ByteString serialize(GameOnMessage message) throws IOException {
            return serializeTyped(messageClass.cast(message));
        }

        abstract ByteString serializeTyped(M message) throws IOException;

        abstract M deserialize(ParsedMessage messageDetails) throws IOException;
    }

    abstract class MessageWithRecipientFormat<M extends GameOnMessage> extends GameOnMessageFormat<M> {
        MessageWithRecipientFormat(Class<M> messageClass, String targetName) {
            super(messageClass, targetName);
        }

        @Override
        M deserialize(ParsedMessage message) throws IOException {
            if (!message.getRecipient().isPresent())
                throw new DeserializationException("Invalid message (missing recipient): " + message);
            return deserialize(message.getRecipient().get(), message.getPayload());
        }

        abstract M deserialize(String recipient, String payload) throws IOException;
    }
}
