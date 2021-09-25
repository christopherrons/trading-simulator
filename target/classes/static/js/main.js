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


function connect() {
    const socket = new SockJS('/trading-simulator');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/openOrders', function (openOrders) {
            showOpenOrders(JSON.parse(openOrders.body).dataList);
        });
        stompClient.subscribe('/topic/orderBook', function (orderBook) {
            showOrderBook(JSON.parse(orderBook.body).dataList);
        });
        stompClient.subscribe('/topic/trades', function (trades) {
            showTrades(JSON.parse(trades.body).dataList);
        });
        stompClient.subscribe('/topic/tradeGraph', function (tradeDataPoint) {
            updateGraph(JSON.parse(tradeDataPoint.body));
        });
        stompClient.subscribe('/topic/tradeMetrics', function (metrics) {
            showMetrics(JSON.parse(metrics.body));
        });
    });
}

function showOpenOrders(openOrders) {
    let tableBodyId = $("#user-order-data");
    tableBodyId.empty();
    for (let i = 0; i < openOrders.length; i++) {
        let orderType = openOrders[i]["orderType"] == 1 ? "BID" : "ASK";
        tableBodyId.append("<tr>" +
            "</tr><td>" + orderType + "</td> " +
            "<td>" + openOrders[i]["price"] + "</td>" +
            "<td>" + openOrders[i]["currentQuantity"] + "</td>" +
            "<td>" + openOrders[i]["timeStamp"] + "</td>" +
            "</tr>");
    }
}

function showOrderBook(orderBook) {
    let tableBodyId = $("#order-book");
    tableBodyId.empty();
    addBuyOrders(tableBodyId, orderBook[0].dataList);
    addSellOrders(tableBodyId, orderBook[1].dataList);
}

function addBuyOrders(tableBodyId, buyOrders) {
    for (let i = 0; i < buyOrders.length; i++) {
        tableBodyId.append("<tr id=tempBuy" + i + ">" +
            "<td>" + "BID" + "</td>" +
            "<td>" + buyOrders[i]["price"] + "</td>" +
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
            "<td>" + sellOrders[i]["price"] + "</td>" +
            "<td>" + sellOrders[i]["currentQuantity"] + "</td>" +
            "<td>" + sellOrders[i]["timeStampHourMiniteSecond"] + "</td>" +
            "</tr>");
        $("#tempSell" + i).css("background-color", "rgba(253, 216, 215, 0.5)")
    }
}

function showTrades(trades) {
    let tableBodyId = $("#trades");
    tableBodyId.empty();
    for (let i = 0; i < trades.length; i++) {
        tableBodyId.append("<tr>" +
            "</tr><td>" + trades[i]["price"] + "</td>" +
            "<td>" + trades[i]["quantity"] + "</td>" +
            "<td>" + trades[i]["timeStampHourMiniteSecond"] + "</td>" +
            "</tr>");
    }
}

function updateGraph(tradeDataPoint) {
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
        'orderType': $("input[type='radio'][name='order-type']:checked").val()
    }));
}

function showMetrics(metrics) {
    $("#display-orders-generated").empty().append(metrics["ordersGenerated"]) ;
    $("#display-orders-per-second").empty().append(metrics["ordersPerSecond"]);
    $("#display-trades-matched").empty().append(metrics["tradesMatched"]);
    $("#display-trades-per-second").empty().append(metrics["tradesPerSecond"]);
}

