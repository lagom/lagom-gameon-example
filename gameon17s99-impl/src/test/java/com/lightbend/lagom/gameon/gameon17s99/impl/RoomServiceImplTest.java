/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.lightbend.lagom.gameon.gameon17s99.impl;

import akka.NotUsed;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.SourceQueueWithComplete;
import akka.stream.testkit.TestSubscriber;
import akka.stream.testkit.javadsl.TestSink;
import com.lightbend.lagom.gameon.gameon17s99.api.RoomService;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomRequest;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse;
import com.lightbend.lagom.gameon.gameon17s99.api.protocol.GameOnRoomResponse.Ack;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.TreePVector;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.google.common.primitives.Ints.asList;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.startServer;
import static java.util.concurrent.TimeUnit.SECONDS;

public class RoomServiceImplTest {
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
    public void shouldAckOnConnection() throws InterruptedException, ExecutionException, TimeoutException {
        RoomService client = server.client(RoomService.class);
        Source<GameOnRoomRequest, SourceQueueWithComplete<GameOnRoomRequest>> requestStream =
                Source.queue(1, OverflowStrategy.fail());
        Source<GameOnRoomResponse, NotUsed> responseStream =
                client.room().invoke(requestStream).toCompletableFuture().get(5, SECONDS);

        TestSubscriber.Probe<GameOnRoomResponse> probe =
                responseStream.runWith(TestSink.probe(server.system()), server.materializer());

        probe.request(1);
        probe.expectNext(new Ack(TreePVector.from(asList(1, 2))));
    }
}
