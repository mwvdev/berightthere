requirejs.config({
    baseUrl: '/webjars',
    map: {
          '*': { 'jquery': 'jquery-private' },
          'jquery-private': { 'jquery': 'jquery' }
        },
    paths: {
        jquery: 'jquery/jquery.min',
        'jquery-private': '../javascript/jquery-private',
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

requirejs(["jquery", "leaflet", "map", "sockjs", "stomp", "websocket"]);
