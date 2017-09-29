/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.lightbend.lagom.gameon.gameon17s99.impl;

import akka.NotUsed;
import akka.japi.function.Function;
import akka.stream.javadsl.Source;
import akka.stream.testkit.TestPublisher;
import akka.stream.testkit.TestSubscriber;
import akka.stream.testkit.javadsl.TestSink;
import akka.stream.testkit.javadsl.TestSource;
import com.lightbend.lagom.gameon.gameon17s99.api.RoomService;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest.*;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Chat;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Location;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.startServer;
import static java.util.concurrent.TimeUnit.SECONDS;

public class RoomServiceIntegrationTest {
    private static ServiceTest.TestServer server;

    @BeforeClass
    public static void setUp() {
        server = startServer(defaultSetup());
    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    @Test
    public void sendsAckOnConnection() throws Exception {
        try (GameOnTester tester = new GameOnTester()) {
            tester.expectAck();
        }
    }

    @Test
    public void respondsToHelloWithLocation() throws Exception {
        try (GameOnTester tester = new GameOnTester()) {
            tester.expectAck();

            RoomHello hello = RoomHello.builder()
                    .roomId("<roomId>")
                    .username("<username>")
                    .userId("<userId>")
                    .version(2)
                    .recovery(false)
                    .build();
            tester.send(hello);

            Location location = Location.builder()
                    .playerId("<userId>")
                    .name(Room.NAME)
                    .fullName(Room.FULL_NAME)
                    .description(Room.DESCRIPTION)
                    .exits(Room.EXITS)
                    .commands(Room.COMMANDS)
                    .roomInventory(Room.INVENTORY)
                    .build();
            tester.expect(location);
        }
    }

    @Test
    public void acceptsGoodbyeQuietly() throws Exception {
        try (GameOnTester tester = new GameOnTester()) {
            tester.expectAck();

            RoomGoodbye goodbye = RoomGoodbye.builder()
                    .roomId("<roomId>")
                    .username("<username>")
                    .userId("<userId>")
                    .build();
            tester.send(goodbye);

            tester.expectNoResponse();
        }
    }

    @Test
    public void acceptsJoinQuietly() throws Exception {
        try (GameOnTester tester = new GameOnTester()) {
            tester.expectAck();

            RoomJoin join = RoomJoin.builder()
                    .roomId("<roomId>")
                    .username("<username>")
                    .userId("<userId>")
                    .version(2)
                    .build();
            tester.send(join);

            tester.expectNoResponse();
        }
    }

    @Test
    public void acceptsPartQuietly() throws Exception {
        try (GameOnTester tester = new GameOnTester()) {
            tester.expectAck();

            RoomPart part = RoomPart.builder()
                    .roomId("<roomId>")
                    .username("<username>")
                    .userId("<userId>")
                    .build();
            tester.send(part);

            tester.expectNoResponse();
        }
    }

    @Test
    public void broadcastsChatMessages() throws Exception {
        try (GameOnTester tester = new GameOnTester()) {
            tester.expectAck();

            RoomCommand chatMessage = RoomCommand.builder()
                    .roomId("<roomId>")
                    .username("chatUser")
                    .userId("<userId>")
                    .content("Hello, world")
                    .build();
            tester.send(chatMessage);

            Chat chat = Chat.builder()
                    .playerId(GameOnRoomResponse.PlayerResponse.ALL_PLAYERS)
                    .username("chatUser")
                    .content("Hello, world")
                    .bookmark(Optional.empty())
                    .build();
            tester.expect(chat);
        }
    }

    // This test utility encapsulates some of the messy Akka Streams TestKit
    // code and provides a simpler API that can be used from the tests.
    // Use it in a try-with-resources block to ensure the connections are closed.
    private class GameOnTester implements AutoCloseable {
        private final RoomService client = server.client(RoomService.class);

        // We will need to capture the request probe once the stream is materialized
        private final CompletableFuture<TestPublisher.Probe<GameOnRoomRequest>> futureRequestProbe =
                new CompletableFuture<>();

        // The request stream can be used to send messages to the room
        private final Source<GameOnRoomRequest, TestPublisher.Probe<GameOnRoomRequest>> requestStream =
                // Create a request probe source
                TestSource.<GameOnRoomRequest>probe(server.system())
                        // capture the probe by completing the promise
                        .mapMaterializedValue(captureRequestProbe(futureRequestProbe));

        // The response stream receives message from the room
        private final Source<GameOnRoomResponse, NotUsed> responseStream =
                client.room().invoke(requestStream).toCompletableFuture().get(5, SECONDS);

        // The response probe allows us to make assertions about the received messages
        private final TestSubscriber.Probe<GameOnRoomResponse> responseProbe =
                responseStream.runWith(TestSink.probe(server.system()), server.materializer());

        // Once the end-to-end stream is materialized, we can get the request probe
        private final TestPublisher.Probe<GameOnRoomRequest> requestProbe =
                futureRequestProbe.get(5, SECONDS);

        private GameOnTester() throws InterruptedException, ExecutionException, TimeoutException {}

        void expectAck() {
            responseProbe.request(1);
            responseProbe.expectNext(RoomServiceImpl.ACK);
        }

        @Override
        public void close() throws Exception {
            requestProbe.sendComplete();
        }

        void send(GameOnRoomRequest request) {
            requestProbe.sendNext(request);
        }

        void expect(GameOnRoomResponse response) {
            responseProbe.request(1);
            responseProbe.expectNext(response);
        }

        void expectNoResponse() {
            responseProbe.expectNoMsg(FiniteDuration.create(2, SECONDS));
        }

        private <T> Function<TestPublisher.Probe<T>, TestPublisher.Probe<T>> captureRequestProbe(
                CompletableFuture<TestPublisher.Probe<T>> futureRequestProbe) {
            return requestProbe -> {
                futureRequestProbe.complete(requestProbe);
                return requestProbe;
            };
        }
    }
}
