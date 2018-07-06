define(["jquery", "sockjs", "stomp", "module"], function($, sockjs, stomp, module) {
    var config = module.config();

    var stompClient;
    function initialize(tripIdentifier) {
        stompClient = stomp.over(new sockjs('/berightthere'));
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/' + tripIdentifier, function(location) {
                $(document).trigger('new-location', JSON.parse(location.body));
            });
        });
    }

    initialize(config.tripIdentifier);
});
