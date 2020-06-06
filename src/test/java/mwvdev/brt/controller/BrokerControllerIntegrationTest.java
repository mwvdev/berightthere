package mwvdev.brt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mwvdev.brt.TripTestHelper;
import mwvdev.brt.configuration.WebSocketConfiguration;
import mwvdev.brt.model.Trip;
import mwvdev.brt.service.trip.TripService;
import mwvdev.brt.service.trip.UnknownTripException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = { BrokerController.class, WebSocketConfiguration.class })
@JsonTest
class BrokerControllerIntegrationTest {

    @Autowired
    private AbstractSubscribableChannel clientInboundChannel;

    @Autowired
    private AbstractSubscribableChannel clientOutboundChannel;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TripService tripService;

    private final BlockingQueue<Message<?>> messages = new ArrayBlockingQueue<>(64);

    private static final String tripIdentifier = "0c98b95e-848f-4589-a7f9-dcc7dde95725";
    private static final String subscriptionId = "SubscriptionId";
    private static final String sessionId = "SessionId";
    private static final String destination = "/app/trip." + tripIdentifier + ".locations";

    @Test
    void canGetLocations() throws InterruptedException, IOException {
        Trip trip = TripTestHelper.createTrip(tripIdentifier);
        when(tripService.getTrip(tripIdentifier)).thenReturn(trip);
        String expectedPayload = objectMapper.writeValueAsString(trip.getLocations());

        clientOutboundChannel.addInterceptor(createChannelInterceptor(messages));

        this.clientInboundChannel.send(createMessage(createHeader(subscriptionId, sessionId, destination)));

        Message<?> actualMessage = messages.poll(10, TimeUnit.SECONDS);
        verifyMessage(expectedPayload, actualMessage);
    }

    @Test
    void getLocations_WhenUnknownTrip_ReturnsEmptyCollection() throws InterruptedException, JsonProcessingException {
        when(tripService.getTrip(tripIdentifier)).thenThrow(UnknownTripException.class);
        String expectedPayload = objectMapper.writeValueAsString(Collections.emptyList());

        clientOutboundChannel.addInterceptor(createChannelInterceptor(messages));

        this.clientInboundChannel.send(createMessage(createHeader(subscriptionId, sessionId, destination)));

        Message<?> actualMessage = messages.poll(10, TimeUnit.SECONDS);
        verifyMessage(expectedPayload, actualMessage);

    }

    private ChannelInterceptor createChannelInterceptor(BlockingQueue<Message<?>> messages) {
        return new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                messages.add(message);

                return message;
            }

        };
    }

    private Message<byte[]> createMessage(StompHeaderAccessor headers) {
        return MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());
    }

    private StompHeaderAccessor createHeader(String subscriptionId, String sessionId, String destination) {
        StompHeaderAccessor header = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        header.setSubscriptionId(subscriptionId);
        header.setSessionId(sessionId);
        header.setDestination(destination);
        header.setSessionAttributes(new HashMap<>());

        return header;
    }

    private void verifyMessage(String expectedPayload, Message<?> actualMessage) {
        assertThat(actualMessage, is(notNullValue()));
        StompHeaderAccessor actualHeaders = StompHeaderAccessor.wrap(actualMessage);
        assertThat(actualHeaders.getSubscriptionId(), is(subscriptionId));
        assertThat(actualHeaders.getSessionId(), is(sessionId));
        assertThat(actualHeaders.getDestination(), is(destination));

        String actualPayload = getPayload(actualMessage);
        assertThat(actualPayload, is(expectedPayload));
    }

    private String getPayload(Message<?> actualMessage) {
        return new String((byte[]) actualMessage.getPayload(), StandardCharsets.UTF_8);
    }

}
