package com.lightbend.lagom.gameon.gameon17s99.api.protocol;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;

public interface GameOnRoomRequest extends GameOnMessage {
    @JsonIgnore
    String getRoomId();

    interface RoomRequestBuilder<R extends GameOnRoomRequest> {
        RoomRequestBuilder<R> roomId(String roomId);

        R build();
    }

    class RoomCommand implements GameOnRoomRequest {
        @Nonnull String roomId;
        @Nonnull String username;
        @Nonnull String userId;
        @Nonnull String content;

        @java.beans.ConstructorProperties({"roomId", "username", "userId", "content"})
        RoomCommand(String roomId, String username, String userId, String content) {
            this.roomId = roomId;
            this.username = username;
            this.userId = userId;
            this.content = content;
        }

        public static RoomCommandBuilder builder() {return new RoomCommandBuilder();}

        @Nonnull
        public String getRoomId() {return this.roomId;}

        @Nonnull
        public String getUsername() {return this.username;}

        @Nonnull
        public String getUserId() {return this.userId;}

        @Nonnull
        public String getContent() {return this.content;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof RoomCommand)) return false;
            final RoomCommand other = (RoomCommand) o;
            if (this.getRoomId() == null ? other.getRoomId() != null : !this.getRoomId().equals(other.getRoomId()))
                return false;
            if (this.getUsername() == null ? other.getUsername() != null : !this.getUsername().equals(other.getUsername()))
                return false;
            if (this.getUserId() == null ? other.getUserId() != null : !this.getUserId().equals(other.getUserId()))
                return false;
            if (this.getContent() == null ? other.getContent() != null : !this.getContent().equals(other.getContent()))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getRoomId() == null ? 43 : this.getRoomId().hashCode());
            result = result * PRIME + (this.getUsername() == null ? 43 : this.getUsername().hashCode());
            result = result * PRIME + (this.getUserId() == null ? 43 : this.getUserId().hashCode());
            result = result * PRIME + (this.getContent() == null ? 43 : this.getContent().hashCode());
            return result;
        }

        public String toString() {return "GameOnRoomRequest.RoomCommand(roomId=" + this.getRoomId() + ", username=" + this.getUsername() + ", userId=" + this.getUserId() + ", content=" + this.getContent() + ")";}

        public static class RoomCommandBuilder implements RoomRequestBuilder<RoomCommand> {
            private String roomId;
            private String username;
            private String userId;
            private String content;

            RoomCommandBuilder() {}

            public RoomCommandBuilder roomId(String roomId) {
                this.roomId = roomId;
                return this;
            }

            public RoomCommandBuilder username(String username) {
                this.username = username;
                return this;
            }

            public RoomCommandBuilder userId(String userId) {
                this.userId = userId;
                return this;
            }

            public RoomCommandBuilder content(String content) {
                this.content = content;
                return this;
            }

            public RoomCommand build() {
                return new RoomCommand(roomId, username, userId, content);
            }

            public String toString() {return "GameOnRoomRequest.RoomCommand.RoomCommandBuilder(roomId=" + this.roomId + ", username=" + this.username + ", userId=" + this.userId + ", content=" + this.content + ")";}
        }
    }

    class RoomHello implements GameOnRoomRequest {
        @Nonnull String roomId;
        @Nonnull String username;
        @Nonnull String userId;
        int version;
        boolean recovery;

        @java.beans.ConstructorProperties({"roomId", "username", "userId", "version", "recovery"})
        RoomHello(String roomId, String username, String userId, int version, boolean recovery) {
            this.roomId = roomId;
            this.username = username;
            this.userId = userId;
            this.version = version;
            this.recovery = recovery;
        }

        public static RoomHelloBuilder builder() {return new RoomHelloBuilder();}

        @Nonnull
        public String getRoomId() {return this.roomId;}

        @Nonnull
        public String getUsername() {return this.username;}

        @Nonnull
        public String getUserId() {return this.userId;}

        public int getVersion() {return this.version;}

        public boolean isRecovery() {return this.recovery;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof RoomHello)) return false;
            final RoomHello other = (RoomHello) o;
            if (this.getRoomId() == null ? other.getRoomId() != null : !this.getRoomId().equals(other.getRoomId()))
                return false;
            if (this.getUsername() == null ? other.getUsername() != null : !this.getUsername().equals(other.getUsername()))
                return false;
            if (this.getUserId() == null ? other.getUserId() != null : !this.getUserId().equals(other.getUserId()))
                return false;
            if (this.getVersion() != other.getVersion()) return false;
            if (this.isRecovery() != other.isRecovery()) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getRoomId() == null ? 43 : this.getRoomId().hashCode());
            result = result * PRIME + (this.getUsername() == null ? 43 : this.getUsername().hashCode());
            result = result * PRIME + (this.getUserId() == null ? 43 : this.getUserId().hashCode());
            result = result * PRIME + this.getVersion();
            result = result * PRIME + (this.isRecovery() ? 79 : 97);
            return result;
        }

        public String toString() {return "GameOnRoomRequest.RoomHello(roomId=" + this.getRoomId() + ", username=" + this.getUsername() + ", userId=" + this.getUserId() + ", version=" + this.getVersion() + ", recovery=" + this.isRecovery() + ")";}

        public static class RoomHelloBuilder implements RoomRequestBuilder<RoomHello> {
            private String roomId;
            private String username;
            private String userId;
            private int version;
            private boolean recovery;

            RoomHelloBuilder() {}

            public RoomHelloBuilder roomId(String roomId) {
                this.roomId = roomId;
                return this;
            }

            public RoomHelloBuilder username(String username) {
                this.username = username;
                return this;
            }

            public RoomHelloBuilder userId(String userId) {
                this.userId = userId;
                return this;
            }

            public RoomHelloBuilder version(int version) {
                this.version = version;
                return this;
            }

            public RoomHelloBuilder recovery(boolean recovery) {
                this.recovery = recovery;
                return this;
            }

            public RoomHello build() {
                return new RoomHello(roomId, username, userId, version, recovery);
            }

            public String toString() {return "GameOnRoomRequest.RoomHello.RoomHelloBuilder(roomId=" + this.roomId + ", username=" + this.username + ", userId=" + this.userId + ", version=" + this.version + ", recovery=" + this.recovery + ")";}
        }
    }

    class RoomGoodbye implements GameOnRoomRequest {
        @Nonnull String roomId;
        @Nonnull String username;
        @Nonnull String userId;

        @java.beans.ConstructorProperties({"roomId", "username", "userId"})
        RoomGoodbye(String roomId, String username, String userId) {
            this.roomId = roomId;
            this.username = username;
            this.userId = userId;
        }

        public static RoomGoodbyeBuilder builder() {return new RoomGoodbyeBuilder();}

        @Nonnull
        public String getRoomId() {return this.roomId;}

        @Nonnull
        public String getUsername() {return this.username;}

        @Nonnull
        public String getUserId() {return this.userId;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof RoomGoodbye)) return false;
            final RoomGoodbye other = (RoomGoodbye) o;
            if (this.getRoomId() == null ? other.getRoomId() != null : !this.getRoomId().equals(other.getRoomId()))
                return false;
            if (this.getUsername() == null ? other.getUsername() != null : !this.getUsername().equals(other.getUsername()))
                return false;
            if (this.getUserId() == null ? other.getUserId() != null : !this.getUserId().equals(other.getUserId()))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getRoomId() == null ? 43 : this.getRoomId().hashCode());
            result = result * PRIME + (this.getUsername() == null ? 43 : this.getUsername().hashCode());
            result = result * PRIME + (this.getUserId() == null ? 43 : this.getUserId().hashCode());
            return result;
        }

        public String toString() {return "GameOnRoomRequest.RoomGoodbye(roomId=" + this.getRoomId() + ", username=" + this.getUsername() + ", userId=" + this.getUserId() + ")";}

        public static class RoomGoodbyeBuilder implements RoomRequestBuilder<RoomGoodbye> {
            private String roomId;
            private String username;
            private String userId;

            RoomGoodbyeBuilder() {}

            public RoomGoodbyeBuilder roomId(String roomId) {
                this.roomId = roomId;
                return this;
            }

            public RoomGoodbyeBuilder username(String username) {
                this.username = username;
                return this;
            }

            public RoomGoodbyeBuilder userId(String userId) {
                this.userId = userId;
                return this;
            }

            public RoomGoodbye build() {
                return new RoomGoodbye(roomId, username, userId);
            }

            public String toString() {return "GameOnRoomRequest.RoomGoodbye.RoomGoodbyeBuilder(roomId=" + this.roomId + ", username=" + this.username + ", userId=" + this.userId + ")";}
        }
    }

    class RoomJoin implements GameOnRoomRequest {
        @Nonnull String roomId;
        @Nonnull String username;
        @Nonnull String userId;
        int version;

        @java.beans.ConstructorProperties({"roomId", "username", "userId", "version"})
        RoomJoin(String roomId, String username, String userId, int version) {
            this.roomId = roomId;
            this.username = username;
            this.userId = userId;
            this.version = version;
        }

        public static RoomJoinBuilder builder() {return new RoomJoinBuilder();}

        @Nonnull
        public String getRoomId() {return this.roomId;}

        @Nonnull
        public String getUsername() {return this.username;}

        @Nonnull
        public String getUserId() {return this.userId;}

        public int getVersion() {return this.version;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof RoomJoin)) return false;
            final RoomJoin other = (RoomJoin) o;
            if (this.getRoomId() == null ? other.getRoomId() != null : !this.getRoomId().equals(other.getRoomId()))
                return false;
            if (this.getUsername() == null ? other.getUsername() != null : !this.getUsername().equals(other.getUsername()))
                return false;
            if (this.getUserId() == null ? other.getUserId() != null : !this.getUserId().equals(other.getUserId()))
                return false;
            if (this.getVersion() != other.getVersion()) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getRoomId() == null ? 43 : this.getRoomId().hashCode());
            result = result * PRIME + (this.getUsername() == null ? 43 : this.getUsername().hashCode());
            result = result * PRIME + (this.getUserId() == null ? 43 : this.getUserId().hashCode());
            result = result * PRIME + this.getVersion();
            return result;
        }

        public String toString() {return "GameOnRoomRequest.RoomJoin(roomId=" + this.getRoomId() + ", username=" + this.getUsername() + ", userId=" + this.getUserId() + ", version=" + this.getVersion() + ")";}

        public static class RoomJoinBuilder implements RoomRequestBuilder<RoomJoin> {
            private String roomId;
            private String username;
            private String userId;
            private int version;

            RoomJoinBuilder() {}

            public RoomJoinBuilder roomId(String roomId) {
                this.roomId = roomId;
                return this;
            }

            public RoomJoinBuilder username(String username) {
                this.username = username;
                return this;
            }

            public RoomJoinBuilder userId(String userId) {
                this.userId = userId;
                return this;
            }

            public RoomJoinBuilder version(int version) {
                this.version = version;
                return this;
            }

            public RoomJoin build() {
                return new RoomJoin(roomId, username, userId, version);
            }

            public String toString() {return "GameOnRoomRequest.RoomJoin.RoomJoinBuilder(roomId=" + this.roomId + ", username=" + this.username + ", userId=" + this.userId + ", version=" + this.version + ")";}
        }
    }

    class RoomPart implements GameOnRoomRequest {
        @Nonnull String roomId;
        @Nonnull String username;
        @Nonnull String userId;

        @java.beans.ConstructorProperties({"roomId", "username", "userId"})
        RoomPart(String roomId, String username, String userId) {
            this.roomId = roomId;
            this.username = username;
            this.userId = userId;
        }

        public static RoomPartBuilder builder() {return new RoomPartBuilder();}

        @Nonnull
        public String getRoomId() {return this.roomId;}

        @Nonnull
        public String getUsername() {return this.username;}

        @Nonnull
        public String getUserId() {return this.userId;}

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof RoomPart)) return false;
            final RoomPart other = (RoomPart) o;
            if (this.getRoomId() == null ? other.getRoomId() != null : !this.getRoomId().equals(other.getRoomId()))
                return false;
            if (this.getUsername() == null ? other.getUsername() != null : !this.getUsername().equals(other.getUsername()))
                return false;
            if (this.getUserId() == null ? other.getUserId() != null : !this.getUserId().equals(other.getUserId()))
                return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.getRoomId() == null ? 43 : this.getRoomId().hashCode());
            result = result * PRIME + (this.getUsername() == null ? 43 : this.getUsername().hashCode());
            result = result * PRIME + (this.getUserId() == null ? 43 : this.getUserId().hashCode());
            return result;
        }

        public String toString() {return "GameOnRoomRequest.RoomPart(roomId=" + this.getRoomId() + ", username=" + this.getUsername() + ", userId=" + this.getUserId() + ")";}

        public static class RoomPartBuilder implements RoomRequestBuilder<RoomPart> {
            private String roomId;
            private String username;
            private String userId;

            RoomPartBuilder() {}

            public RoomPartBuilder roomId(String roomId) {
                this.roomId = roomId;
                return this;
            }

            public RoomPartBuilder username(String username) {
                this.username = username;
                return this;
            }

            public RoomPartBuilder userId(String userId) {
                this.userId = userId;
                return this;
            }

            public RoomPart build() {
                return new RoomPart(roomId, username, userId);
            }

            public String toString() {return "GameOnRoomRequest.RoomPart.RoomPartBuilder(roomId=" + this.roomId + ", username=" + this.username + ", userId=" + this.userId + ")";}
        }
    }
}
