let stompClient = null;
var ctx = $("#trade-graph");
var myChart = new Chart(ctx, {
    data: {
        labels: [],
        datasets: [
            {
                type: 'line',
                fill: true,
                label: 'Price',
                data: [],
                borderColor: "rgba(51, 255, 153, 0.5)",
                backgroundColor: "rgba(51, 255, 153, 0.2)",
            },
            {
                type: 'line',
                label: 'Vwap',
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    },
                    title: {
                        display: true,
                        text: 'Price Over Time'
                    }
                },
                data: [],
                borderColor: "rgba(232, 97, 97, 1)",
                backgroundColor: "rgba(232, 97, 97, 0.5)",
            },
        ]
    },
    options: {
        plugins: {
            title: {
                display: true,
                text: 'Price Over Time',
            }
        }
    }
});

$(function () {
    if (stompClient == null) {
        connectWebsocket();
    }

    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $(".user-order-data-container .user-order-data-tabs li").click(function () {
        const $userOrderDataViews = $(this).closest(".user-order-data-container");

        $userOrderDataViews.find(".user-order-data-tabs li.user-order-data-tabs.active").removeClass("active");
        $(this).addClass("user-order-data-tabs active");

        const $userOrderDataToShow = $(this).attr('rel');

        $userOrderDataViews.find(".user-order-data.active").slideUp(300, showNewUserData);

        function showNewUserData() {
            $(this).removeClass("active");
            $("#" + $userOrderDataToShow).slideDown(300, function () {
                $(this).addClass("active");
            });
        }
    });
})

$("#submit-order-form").click(function () {
    if (validateOrderForm()) {
        sendOrderForm();
    }
});

$("#submit-simulation-form").click(function () {
    if (validateSimulationForm()) {
        sendSimulationForm();
    }
});

function validateOrderForm() {
    const price = $.trim($('#price').val());
    const priceValues = $.trim($('#price').val()).split(".");
    const decimals = priceValues[1] != null ? priceValues[1] : "1";
    if (!price || decimals.length > 5) {
        return false;
    }

    return $.trim($('#quantity').val());


}

function validateSimulationForm() {
    const nrOfOrders = $.trim($('#generate-orders').val());
    if (!nrOfOrders) {
        return false;
    }

    const ordersPerSecond = $.trim($('#orders-per-second').val());
    if (parseFloat(ordersPerSecond) > 5000) {
        return false;
    }
    if (!ordersPerSecond) {
        $('#orders-per-second').val("0");
    }

    return true;
}

function connectWebsocket() {
    const socket = new SockJS('/trading-simulator');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/openOrders', function (openOrders) {
            updateUserOpenOrders(JSON.parse(openOrders.body).dataList);
        });
        stompClient.subscribe('/topic/filledOrders', function (filledOrders) {
            updateUserFilledOrders(JSON.parse(filledOrders.body).dataList);
        });
        stompClient.subscribe('/topic/userTrades', function (userTrades) {
            updateUserTrades(JSON.parse(userTrades.body).dataList);
        });
        stompClient.subscribe('/topic/orderBook', function (orderBook) {
            updateOrderBook(JSON.parse(orderBook.body).dataList);
        });
        stompClient.subscribe('/topic/trades', function (trades) {
            updateTrades(JSON.parse(trades.body).dataList);
        });
        stompClient.subscribe('/topic/tradeGraph', function (tradeDataPoint) {
            updatePriceGraph(JSON.parse(tradeDataPoint.body));
        });
        stompClient.subscribe('/topic/tradeMetrics', function (metrics) {
            updateTradeEngineMetrics(JSON.parse(metrics.body));
        });
    });
}

function updateUserOpenOrders(openOrders) {
    let tableBodyId = $("#user-order-data-open-order");
    tableBodyId.empty();
    for (let i = 0; i < openOrders.length; i++) {
        let orderType = openOrders[i]["orderType"] == 1 ? "BUY" : "SELL";
        tableBodyId.append("<tr>" +
            "</tr><td>" + orderType + "</td> " +
            "<td>" + openOrders[i]["priceAsDouble"] + "</td>" +
            "<td>" + openOrders[i]["initialQuantity"] + "</td>" +
            "<td>" + openOrders[i]["currentQuantity"] + "</td>" +
            "<td>" + openOrders[i]["timeStamp"] + "</td>" +
            "</tr>");
    }
}

function updateUserFilledOrders(filledOrders) {
    let tableBodyId = $("#user-order-data-filled-order");
    tableBodyId.empty();
    for (let i = 0; i < filledOrders.length; i++) {
        let orderType = filledOrders[i]["orderType"] == 1 ? "BUY" : "SELL";
        tableBodyId.append("<tr>" +
            "</tr><td>" + orderType + "</td> " +
            "<td>" + filledOrders[i]["priceAsDouble"] + "</td>" +
            "<td>" + filledOrders[i]["initialQuantity"] + "</td>" +
            "<td>" + filledOrders[i]["timeStamp"] + "</td>" +
            "</tr>");
    }
}

function updateUserTrades(userTrades) {
    let tableBodyId = $("#user-order-data-trades");
    tableBodyId.empty();
    for (let i = 0; i < userTrades.length; i++) {
        tableBodyId.append("<tr>" +
            "<td>" + userTrades[i]["orderType"] + "</td>" +
            "<td>" + userTrades[i]["priceAsDouble"] + "</td>" +
            "<td>" + userTrades[i]["quantity"] + "</td>" +
            "<td>" + userTrades[i]["timeStamp"] + "</td>" +
            "</tr>");
    }
}

function updateOrderBook(orderBook) {
    let tableBodyId = $("#order-book");
    tableBodyId.empty();
    addBuyOrders(tableBodyId, orderBook[0].dataList);
    addSellOrders(tableBodyId, orderBook[1].dataList);
}

function addBuyOrders(tableBodyId, buyOrders) {
    for (let i = 0; i < buyOrders.length; i++) {
        tableBodyId.append("<tr id=tempBuy" + i + ">" +
            "<td>" + "BID" + "</td>" +
            "<td>" + buyOrders[i]["priceAsDouble"] + "</td>" +
            "<td>" + buyOrders[i]["currentQuantity"] + "</td>" +
            "<td>" + buyOrders[i]["timeStampHourMiniteSecond"] + "</td>" +
            "</tr>");
        $("#tempBuy" + i).css("background-color", "rgba(229, 252, 212, 0.5)")
    }
    $("#tempBuy" + (buyOrders.length - 1)).css("border-bottom", "1px solid #dddddd");
}

function addSellOrders(tableBodyId, sellOrders) {
    for (let i = 0; i < sellOrders.length; i++) {
        tableBodyId.append("<tr id=tempSell" + i + ">" +
            "<td>" + "ASK" + "</td>" +
            "<td>" + sellOrders[i]["priceAsDouble"] + "</td>" +
            "<td>" + sellOrders[i]["currentQuantity"] + "</td>" +
            "<td>" + sellOrders[i]["timeStampHourMiniteSecond"] + "</td>" +
            "</tr>");
        $("#tempSell" + i).css("background-color", "rgba(253, 216, 215, 0.5)")
    }
}

function updateTrades(trades) {
    let tableBodyId = $("#trades");
    tableBodyId.empty();
    for (let i = 0; i < trades.length; i++) {
        tableBodyId.append("<tr>" +
            "</tr><td>" + trades[i]["priceAsDouble"] + "</td>" +
            "<td>" + trades[i]["quantity"] + "</td>" +
            "<td>" + trades[i]["timeStampHourMiniteSecond"] + "</td>" +
            "</tr>");
    }
}

function updatePriceGraph(tradeDataPoint) {
    myChart.data.datasets[0].data[myChart.data.datasets[0].data.length] = tradeDataPoint["price"];
    myChart.data.datasets[1].data[myChart.data.datasets[1].data.length] = tradeDataPoint["vwap"];
    myChart.data.labels[myChart.data.labels.length] = tradeDataPoint["timeStamp"];
    myChart.update();
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
        'currentQuantity': $("#quantity").val(),
        'orderAction': "1",
        'orderType': $("input[type='radio'][name='order-type']:checked").val()
    }));
}

function updateTradeEngineMetrics(metrics) {
    $("#display-orders-generated").empty().append(metrics["ordersGenerated"]);
    $("#display-orders-per-second").empty().append(metrics["ordersPerSecond"]);
    $("#display-trades-matched").empty().append(metrics["tradesMatched"]);
    $("#display-trades-per-second").empty().append(metrics["tradesPerSecond"]);
}

