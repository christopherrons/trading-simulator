let stompClient = null;

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).dataList);
        });
        stompClient.subscribe('/topic/openOrders', function (openOrders) {
            showOpenOrders(JSON.parse(openOrders.body).dataList);
        });
        stompClient.subscribe('/topic/orderBook', function (orderBook) {
            showOrderBook(JSON.parse(orderBook.body).dataList);
        });
        stompClient.subscribe('/topic/trades', function (trades) {
            showTrades(JSON.parse(trades.body).dataList);
        });
    });
}

function sendSimulationForm() {
    stompClient.send("/app/simulationEntry", {}, JSON.stringify({
        'ordersToGenerate': $("#generate-orders").val(),
        'ordersPerSecond': $("#orders-per-second").val(),
    }));
}

function sendOrderForm() {
    stompClient.send("/app/orderEntry", {}, JSON.stringify({
        'price': $("#price").val(),
        'initialQuantity': $("#quantity").val(),
        'orderType': $("input[type='radio'][name='order-type']:checked").val()
    }));
}

function showTrades(trades) {
    let tableBodyId = $("#trades");
    tableBodyId.empty();
    for (let i = 0; i < trades.length; i++) {
        tableBodyId.append("<tr></tr>");
        tableBodyId.append("<td>" + trades[i]["price"] + "</td>");
        tableBodyId.append("<td>" + trades[i]["quantity"] + "</td>");
        tableBodyId.append("<td>" + trades[i]["timeStamp"] + "</td>");
    }
}

function showOrderBook(orderBook) {
    let tableBodyId = $("#order-book");
    tableBodyId.empty();
    for (let i = 0; i < orderBook.length; i++) {
        tableBodyId.append("<tr></tr>");
        let orderType = orderBook[i]["orderType"] == 1 ? "BID" : "ASK";
        tableBodyId.append("<td>" + orderType + "</td>");
        tableBodyId.append("<td>" + orderBook[i]["price"] + "</td>");
        tableBodyId.append("<td>" + orderBook[i]["currentQuantity"] + "</td>");
        tableBodyId.append("<td>" + orderBook[i]["timeStamp"] + "</td>");
    }
}

function showOpenOrders(openOrders) {
    let tableBodyId = $("#user-data");
    tableBodyId.empty();
    for (let i = 0; i < openOrders.length; i++) {
        tableBodyId.append("<tr></tr>");
        tableBodyId.append("<td>" + openOrders[i]["orderType"] + "</td>");
        tableBodyId.append("<td>" + openOrders[i]["price"] + "</td>");
        tableBodyId.append("<td>" + openOrders[i]["currentQuantity"] + "</td>");
    }
}

function showGreeting(messages) {
    let tableBodyId = $("#greetingsTwo");
    for (let i = 0; i < messages.length; i++) {
        let message = messages[i];
        tableBodyId.append("<tr></tr>");
        for (let key in message) {
            if (message.hasOwnProperty(key)) {
                tableBodyId.append("<td>" + message[key] + "</td>");
            }
        }
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    connect();
    $("#submit-order-form").click(function () {
        sendOrderForm();
    });
    $("#submit-simulation-form").click(function () {
        sendSimulationForm();
    });
});
