let stompClient = null;

export function connect(endPoint) {
    const socket = new SockJS(endPoint);
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

export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    this.setConnected(false);
    console.log("Disconnected");
}

export function setConnected(connected) {
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

export function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

export function sendOrderForm() {
    stompClient.send("/app/orderEntry", {}, JSON.stringify({
        'price': $("#price").val(),
        'initialQuantity': $("#quantity").val(),
        'orderType': $("input[type='radio'][name='orderType']:checked").val()
    }));
}

export function showOpenOrders(openOrders) {
    let tableBodyId = $("#greetingsThree");
    tableBodyId.empty();
    for (let i = 0; i < openOrders.length; i++) {
        tableBodyId.append("<tr></tr>");
        tableBodyId.append("<td>" + openOrders[i]["orderType"] + "</td>");
        tableBodyId.append("<td>" + openOrders[i]["price"] + "</td>");
        tableBodyId.append("<td>" + openOrders[i]["currentQuantity"] + "</td>");
    }
}

export function showGreeting(messages) {
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

