package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.pcollections.PMap;
import org.pcollections.PSequence;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface GameOnRoomResponse extends GameOnMessage {
    class Ack implements GameOnRoomResponse {
        PSequence<Integer> version;

        @java.beans.ConstructorProperties({"version"})
        public Ack(PSequence<Integer> version) {
            this.version = version;
        }

        public PSequence<Integer> getVersion() {return this.version;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Ack)) return false;
            final Ack other = (Ack) o;
            if (this.getVersion() == null ? other.getVersion() != null : !this.getVersion().equals(other.getVersion()))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getVersion() == null ? 43 : this.getVersion().hashCode());
            return result;
        }

        public String toString() {return "GameOnRoomResponse.Ack(version=" + this.getVersion() + ")";}
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

    class Location implements PlayerResponse {
        @Nonnull String playerId;
        @Nonnull String name;
        @Nonnull String fullName;
        @Nonnull String description;
        @Nonnull PMap<String, String> commands;
        @Nonnull PSequence<String> roomInventory;

        @java.beans.ConstructorProperties({"playerId", "name", "fullName", "description", "commands", "roomInventory"})
        Location(String playerId, String name, String fullName, String description, PMap<String, String> commands, PSequence<String> roomInventory) {
            this.playerId = playerId;
            this.name = name;
            this.fullName = fullName;
            this.description = description;
            this.commands = commands;
            this.roomInventory = roomInventory;
        }

        public static LocationBuilder builder() {return new LocationBuilder();}

        @Nonnull
        public String getPlayerId() {return this.playerId;}

        @Nonnull
        public String getName() {return this.name;}

        @Nonnull
        public String getFullName() {return this.fullName;}

        @Nonnull
        public String getDescription() {return this.description;}

        @Nonnull
        public PMap<String, String> getCommands() {return this.commands;}

        @Nonnull
        public PSequence<String> getRoomInventory() {return this.roomInventory;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Location)) return false;
            final Location other = (Location) o;
            if (this.getPlayerId() == null ? other.getPlayerId() != null : !this.getPlayerId().equals(other.getPlayerId()))
                return false;
            if (this.getName() == null ? other.getName() != null : !this.getName().equals(other.getName()))
                return false;
            if (this.getFullName() == null ? other.getFullName() != null : !this.getFullName().equals(other.getFullName()))
                return false;
            if (this.getDescription() == null ? other.getDescription() != null : !this.getDescription().equals(other.getDescription()))
                return false;
            if (this.getCommands() == null ? other.getCommands() != null : !this.getCommands().equals(other.getCommands()))
                return false;
            if (this.getRoomInventory() == null ? other.getRoomInventory() != null : !this.getRoomInventory().equals(other.getRoomInventory()))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getPlayerId() == null ? 43 : this.getPlayerId().hashCode());
            result = result * PRIME + (this.getName() == null ? 43 : this.getName().hashCode());
            result = result * PRIME + (this.getFullName() == null ? 43 : this.getFullName().hashCode());
            result = result * PRIME + (this.getDescription() == null ? 43 : this.getDescription().hashCode());
            result = result * PRIME + (this.getCommands() == null ? 43 : this.getCommands().hashCode());
            result = result * PRIME + (this.getRoomInventory() == null ? 43 : this.getRoomInventory().hashCode());
            return result;
        }

        public String toString() {return "GameOnRoomResponse.Location(playerId=" + this.getPlayerId() + ", name=" + this.getName() + ", fullName=" + this.getFullName() + ", description=" + this.getDescription() + ", commands=" + this.getCommands() + ", roomInventory=" + this.getRoomInventory() + ")";}

        public static class LocationBuilder implements PlayerResponseBuilder<Location> {
            private String playerId;
            private String name;
            private String fullName;
            private String description;
            private PMap<String, String> commands;
            private PSequence<String> roomInventory;

            LocationBuilder() {}

            public LocationBuilder playerId(String playerId) {
                this.playerId = playerId;
                return this;
            }

            public LocationBuilder name(String name) {
                this.name = name;
                return this;
            }

            public LocationBuilder fullName(String fullName) {
                this.fullName = fullName;
                return this;
            }

            public LocationBuilder description(String description) {
                this.description = description;
                return this;
            }

            public LocationBuilder commands(PMap<String, String> commands) {
                this.commands = commands;
                return this;
            }

            public LocationBuilder roomInventory(PSequence<String> roomInventory) {
                this.roomInventory = roomInventory;
                return this;
            }

            public Location build() {
                return new Location(playerId, name, fullName, description, commands, roomInventory);
            }

            public String toString() {return "GameOnRoomResponse.Location.LocationBuilder(playerId=" + this.playerId + ", name=" + this.name + ", fullName=" + this.fullName + ", description=" + this.description + ", commands=" + this.commands + ", roomInventory=" + this.roomInventory + ")";}
        }
    }

    class Chat implements PlayerResponse {
        @Nonnull String playerId;
        @Nonnull String username;
        @Nonnull String content;
        @Nonnull Optional<String> bookmark;

        @java.beans.ConstructorProperties({"playerId", "username", "content", "bookmark"})
        Chat(String playerId, String username, String content, Optional<String> bookmark) {
            this.playerId = playerId;
            this.username = username;
            this.content = content;
            this.bookmark = bookmark;
        }

        public static ChatBuilder builder() {return new ChatBuilder();}

        @Nonnull
        public String getPlayerId() {return this.playerId;}

        @Nonnull
        public String getUsername() {return this.username;}

        @Nonnull
        public String getContent() {return this.content;}

        @Nonnull
        public Optional<String> getBookmark() {return this.bookmark;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Chat)) return false;
            final Chat other = (Chat) o;
            if (this.getPlayerId() == null ? other.getPlayerId() != null : !this.getPlayerId().equals(other.getPlayerId()))
                return false;
            if (this.getUsername() == null ? other.getUsername() != null : !this.getUsername().equals(other.getUsername()))
                return false;
            if (this.getContent() == null ? other.getContent() != null : !this.getContent().equals(other.getContent()))
                return false;
            if (this.getBookmark() == null ? other.getBookmark() != null : !this.getBookmark().equals(other.getBookmark()))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getPlayerId() == null ? 43 : this.getPlayerId().hashCode());
            result = result * PRIME + (this.getUsername() == null ? 43 : this.getUsername().hashCode());
            result = result * PRIME + (this.getContent() == null ? 43 : this.getContent().hashCode());
            result = result * PRIME + (this.getBookmark() == null ? 43 : this.getBookmark().hashCode());
            return result;
        }

        public String toString() {return "GameOnRoomResponse.Chat(playerId=" + this.getPlayerId() + ", username=" + this.getUsername() + ", content=" + this.getContent() + ", bookmark=" + this.getBookmark() + ")";}

        public static class ChatBuilder implements PlayerResponseBuilder<Chat> {
            private String playerId;
            private String username;
            private String content;
            private Optional<String> bookmark;

            ChatBuilder() {}

            public ChatBuilder playerId(String playerId) {
                this.playerId = playerId;
                return this;
            }

            public ChatBuilder username(String username) {
                this.username = username;
                return this;
            }

            public ChatBuilder content(String content) {
                this.content = content;
                return this;
            }

            public ChatBuilder bookmark(Optional<String> bookmark) {
                this.bookmark = bookmark;
                return this;
            }

            public Chat build() {
                return new Chat(playerId, username, content, bookmark);
            }

            public String toString() {return "GameOnRoomResponse.Chat.ChatBuilder(playerId=" + this.playerId + ", username=" + this.username + ", content=" + this.content + ", bookmark=" + this.bookmark + ")";}
        }
    }

    class Event implements PlayerResponse {
        @Nonnull String playerId;
        @Nonnull PMap<String, String> content;
        @Nonnull Optional<String> bookmark;

        @java.beans.ConstructorProperties({"playerId", "content", "bookmark"})
        Event(String playerId, PMap<String, String> content, Optional<String> bookmark) {
            this.playerId = playerId;
            this.content = content;
            this.bookmark = bookmark;
        }

        public static EventBuilder builder() {return new EventBuilder();}

        @Nonnull
        public String getPlayerId() {return this.playerId;}

        @Nonnull
        public PMap<String, String> getContent() {return this.content;}

        @Nonnull
        public Optional<String> getBookmark() {return this.bookmark;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Event)) return false;
            final Event other = (Event) o;
            if (this.getPlayerId() == null ? other.getPlayerId() != null : !this.getPlayerId().equals(other.getPlayerId()))
                return false;
            if (this.getContent() == null ? other.getContent() != null : !this.getContent().equals(other.getContent()))
                return false;
            if (this.getBookmark() == null ? other.getBookmark() != null : !this.getBookmark().equals(other.getBookmark()))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getPlayerId() == null ? 43 : this.getPlayerId().hashCode());
            result = result * PRIME + (this.getContent() == null ? 43 : this.getContent().hashCode());
            result = result * PRIME + (this.getBookmark() == null ? 43 : this.getBookmark().hashCode());
            return result;
        }

        public String toString() {return "GameOnRoomResponse.Event(playerId=" + this.getPlayerId() + ", content=" + this.getContent() + ", bookmark=" + this.getBookmark() + ")";}

        public static class EventBuilder implements PlayerResponseBuilder<Event> {
            private String playerId;
            private PMap<String, String> content;
            private Optional<String> bookmark;

            EventBuilder() {}

            public EventBuilder playerId(String playerId) {
                this.playerId = playerId;
                return this;
            }

            public EventBuilder content(PMap<String, String> content) {
                this.content = content;
                return this;
            }

            public EventBuilder bookmark(Optional<String> bookmark) {
                this.bookmark = bookmark;
                return this;
            }

            public Event build() {
                return new Event(playerId, content, bookmark);
            }

            public String toString() {return "GameOnRoomResponse.Event.EventBuilder(playerId=" + this.playerId + ", content=" + this.content + ", bookmark=" + this.bookmark + ")";}
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

    class Exit implements PlayerLocationResponse {
        @Nonnull String playerId;
        @Nonnull String content;
        @Nonnull String exitId;

        @java.beans.ConstructorProperties({"playerId", "content", "exitId"})
        Exit(String playerId, String content, String exitId) {
            this.playerId = playerId;
            this.content = content;
            this.exitId = exitId;
        }

        public static ExitBuilder builder() {return new ExitBuilder();}

        @Nonnull
        public String getPlayerId() {return this.playerId;}

        @Nonnull
        public String getContent() {return this.content;}

        @Nonnull
        public String getExitId() {return this.exitId;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Exit)) return false;
            final Exit other = (Exit) o;
            if (this.getPlayerId() == null ? other.getPlayerId() != null : !this.getPlayerId().equals(other.getPlayerId()))
                return false;
            if (this.getContent() == null ? other.getContent() != null : !this.getContent().equals(other.getContent()))
                return false;
            if (this.getExitId() == null ? other.getExitId() != null : !this.getExitId().equals(other.getExitId()))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getPlayerId() == null ? 43 : this.getPlayerId().hashCode());
            result = result * PRIME + (this.getContent() == null ? 43 : this.getContent().hashCode());
            result = result * PRIME + (this.getExitId() == null ? 43 : this.getExitId().hashCode());
            return result;
        }

        public String toString() {return "GameOnRoomResponse.Exit(playerId=" + this.getPlayerId() + ", content=" + this.getContent() + ", exitId=" + this.getExitId() + ")";}

        public static class ExitBuilder implements PlayerLocationResponseBuilder<Exit> {
            private String playerId;
            private String content;
            private String exitId;

            ExitBuilder() {}

            public ExitBuilder playerId(String playerId) {
                this.playerId = playerId;
                return this;
            }

            public ExitBuilder content(String content) {
                this.content = content;
                return this;
            }

            public ExitBuilder exitId(String exitId) {
                this.exitId = exitId;
                return this;
            }

            public Exit build() {
                return new Exit(playerId, content, exitId);
            }

            public String toString() {return "GameOnRoomResponse.Exit.ExitBuilder(playerId=" + this.playerId + ", content=" + this.content + ", exitId=" + this.exitId + ")";}
        }
    }
}
