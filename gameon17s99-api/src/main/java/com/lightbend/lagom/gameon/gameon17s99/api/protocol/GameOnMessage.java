package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import akka.util.ByteString;
import com.lightbend.lagom.javadsl.api.deser.DeserializationException;

import javax.annotation.Nonnull;
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

    final class ParsedMessage {
        @Nonnull String target;
        @Nonnull Optional<String> recipient;
        @Nonnull String payload;

        @java.beans.ConstructorProperties({"target", "recipient", "payload"})
        public ParsedMessage(String target, Optional<String> recipient, String payload) {
            this.target = target;
            this.recipient = recipient;
            this.payload = payload;
        }

        @Nonnull
        public String getTarget() {return this.target;}

        @Nonnull
        public Optional<String> getRecipient() {return this.recipient;}

        @Nonnull
        public String getPayload() {return this.payload;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof ParsedMessage)) return false;
            final ParsedMessage other = (ParsedMessage) o;
            if (this.getTarget() == null ? other.getTarget() != null : !this.getTarget().equals(other.getTarget()))
                return false;
            if (this.getRecipient() == null ? other.getRecipient() != null : !this.getRecipient().equals(other.getRecipient()))
                return false;
            if (this.getPayload() == null ? other.getPayload() != null : !this.getPayload().equals(other.getPayload()))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getTarget() == null ? 43 : this.getTarget().hashCode());
            result = result * PRIME + (this.getRecipient() == null ? 43 : this.getRecipient().hashCode());
            result = result * PRIME + (this.getPayload() == null ? 43 : this.getPayload().hashCode());
            return result;
        }

        public String toString() {return "GameOnMessage.ParsedMessage(target=" + this.getTarget() + ", recipient=" + this.getRecipient() + ", payload=" + this.getPayload() + ")";}
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
