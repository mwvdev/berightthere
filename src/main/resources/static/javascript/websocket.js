define(["map.core", "map.events", "sockjs", "stomp", "module"], function(mapCore, mapEvents, sockjs, stomp, module) {
    var config = module.config();
    var eventEmitter = mapCore.getEventEmitter();

    var stompClient;
    function initialize(tripIdentifier) {
        function connectedCallback(stompClient, reconnected, frame) {
            stompClient.subscribe('/topic/' + tripIdentifier, function(message) {
                eventEmitter.emit(mapEvents.websocket.locationReceived, JSON.parse(message.body));
            });

            if (reconnected) {
                stompClient.subscribe("/app/trip." + tripIdentifier + ".locations", function(message) {
                    eventEmitter.emit(mapEvents.websocket.reconnected, JSON.parse(message.body));
                });
            }
        }

        function errorCallback(stompClient, frame) {
            setTimeout(function() {
                console.log("An error occurred, attempting reconnect.");

                stompClient = createStompClient(true);
            }, 2000);
        }

        function createStompClient(reconnected) {
            var stompClient = stomp.over(new sockjs('/berightthere'), { heartbeat: false });
            stompClient.connect({}, connectedCallback.bind(this, stompClient, reconnected), errorCallback.bind(this, stompClient));

            return stompClient;
        }

        stompClient = createStompClient(false);
    }

    initialize(config.tripIdentifier);
});
