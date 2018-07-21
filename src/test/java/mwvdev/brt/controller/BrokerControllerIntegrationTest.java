package mwvdev.brt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mwvdev.brt.TripTestHelper;
import mwvdev.brt.configuration.WebSocketConfiguration;
import mwvdev.brt.entity.TripEntity;
import mwvdev.brt.service.location.TripService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@JsonTest
public class BrokerControllerIntegrationTest {

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

    @Test
    public void canGetLocations() throws InterruptedException, IOException {
        String subscriptionId = "SubscriptionId";
        String sessionId = "SessionId";
        String destination = "/app/trip." + tripIdentifier + ".locations";

        TripEntity tripEntity = TripTestHelper.createTripEntity(tripIdentifier);
        given(tripService.getLocations(tripIdentifier)).willReturn(tripEntity.getLocations());
        String expectedPayload = objectMapper.writeValueAsString(tripEntity.getLocations());

        StompHeaderAccessor headers = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        headers.setSubscriptionId(subscriptionId);
        headers.setSessionId(sessionId);
        headers.setDestination(destination);
        headers.setSessionAttributes(new HashMap<>());
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], headers.getMessageHeaders());

        clientOutboundChannel.addInterceptor(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                messages.add(message);

                return message;
            }

        });

        this.clientInboundChannel.send(message);

        Message<?> actualMessage = messages.poll(10, TimeUnit.SECONDS);
        StompHeaderAccessor actualHeaders = StompHeaderAccessor.wrap(actualMessage);

        assertThat(actualHeaders.getSubscriptionId(), is(subscriptionId));
        assertThat(actualHeaders.getSessionId(), is(sessionId));
        assertThat(actualHeaders.getDestination(), is(destination));

        String actualPayload = new String((byte[]) actualMessage.getPayload(), Charset.forName("UTF-8"));

        assertThat(actualPayload, is(expectedPayload));
    }

    @Configuration
    @ComponentScan(
            basePackages = "mwvdev.brt",
            includeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {BrokerController.class, WebSocketConfiguration.class} ) },
            useDefaultFilters = false)
    static class TestConfiguration {

    }
}