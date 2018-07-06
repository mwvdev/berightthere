define(["map", "sockjs", "stomp", "module"], function(map, sockjs, stomp, module) {
    var config = module.config();

    var stompClient;
    function initialize(tripIdentifier) {
        stompClient = stomp.over(new sockjs('/berightthere'));
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/' + tripIdentifier, function(location) {
                map.addLocation(JSON.parse(location.body));
            });
        });
    }

    initialize(config.tripIdentifier);
});
