requirejs.config({
    baseUrl: '/webjars',
    paths: {
        leaflet: 'leaflet/leaflet',
        map: '../javascript/map',
        sockjs: 'sockjs-client/sockjs.min',
        stomp: 'stomp-websocket/stomp.min',
        websocket: '../javascript/websocket'
    },
    shim: {
        stomp: {
            exports: 'Stomp'
        }
    }
});

requirejs(["leaflet", "map", "sockjs", "stomp", "websocket"]);
