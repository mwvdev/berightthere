define(["map.core", "map.events", "sockjs", "stomp", "module"], function(mapCore, mapEvents, sockjs, stomp, module) {
    var config = module.config();
    var eventEmitter = mapCore.getEventEmitter();

    var stompClient;
    function initialize(tripIdentifier) {
        stompClient = stomp.over(new sockjs('/berightthere'));
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/' + tripIdentifier, function(location) {
                eventEmitter.emit(mapEvents.location.received, JSON.parse(location.body));
            });
        });
    }

    initialize(config.tripIdentifier);
});
