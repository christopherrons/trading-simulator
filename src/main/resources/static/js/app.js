let stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#orderForm").show();
        $("#userData").show();
        $("#simulationForm").show();
    } else {
        $("#orderForm").hide();
        $("#userData").hide();
        $("#simulationForm").hide();
    }
    $("#greetings").html("");
    $("#greetingsTwo").html("");
    $("#greetingsThree").html("");
}

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).dataList);
        });
        stompClient.subscribe('/topic/openOrders', function (openOrders) {
            showOpenOrders(JSON.parse(openOrders.body).dataList);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function sendOrderForm() {
    stompClient.send("/app/orderEntry", {}, JSON.stringify({
        'price': $("#price").val(),
        'initialQuantity': $("#quantity").val(),
        'orderType': $("input[type='radio'][name='orderType']:checked").val()
    }));
}

function showOpenOrders(openOrders) {
    let tableBodyId = $("#greetingsThree");
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
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
    $("#submitOrderForm").click(function () {
        sendOrderForm();
    });
});
