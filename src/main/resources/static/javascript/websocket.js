var beRightThereWebSocket = (function() {
    var stompClient;
    function initialize(tripIdentifier) {
        stompClient = Stomp.over(new SockJS('/berightthere'));
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/' + tripIdentifier, function(location) {
                $(document).trigger('new-location', JSON.parse(location.body));
            });
        });
    }

    return {
        initialize: initialize
    };
}());
