let stompClient = null;
var ctx = $("#trade-graph");
var myChart = new Chart(ctx, {
    data: {
        labels: [],
        datasets: [
            {
                type: 'line',
                label: 'Price',
                data: [],
                borderColor: "rgba(51, 255, 153, 0.5)",
                backgroundColor: "rgba(51, 255, 153, 1)",
            },
            {
                type: 'line',
                label: 'Vwap',
                data: [],
                borderColor: "rgba(232, 97, 97, 1)",
                backgroundColor: "rgba(232, 97, 97, 0.5)",
            },
            {
                type: 'bar',
                label: 'Volume',
                data: [],
                borderColor: "rgba(232, 97, 97, 1)",
                backgroundColor: "rgba(232, 97, 97, 0.5)",
                options: {
                    scales: {
                        yAxes: [{
                            ticks: {
                                min: 5,
                                max: 10,
                            }
                        }]
                    }
                }
            },
        ]
    },
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
            showMetrics(JSON.parse(metrics.body).dataList);
        });
    });
}

function showOpenOrders(openOrders) {
    let tableBodyId = $("#user-data");
    tableBodyId.empty();
    for (let i = 0; i < openOrders.length; i++) {
        tableBodyId.append("<tr></tr>");
        let orderType = openOrders[i]["orderType"] == 1 ? "BID" : "ASK";
        tableBodyId.append("<td>" + orderType + "</td>");
        tableBodyId.append("<td>" + openOrders[i]["price"] + "</td>");
        tableBodyId.append("<td>" + openOrders[i]["currentQuantity"] + "</td>");
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

function updateGraph(tradeDataPoint) {
    myChart.data.datasets[0].data[myChart.data.datasets[0].data.length] = tradeDataPoint["price"];
    myChart.data.datasets[1].data[myChart.data.datasets[1].data.length] = tradeDataPoint["vwap"];
    myChart.data.datasets[2].data[myChart.data.datasets[2].data.length] = tradeDataPoint["quantity"];
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

function showMetrics(trades) {
    let tableBodyId = $("#metrics");
    tableBodyId.empty();
    for (let i = 0; i < trades.length; i++) {
        tableBodyId.append("<tr></tr>");
        tableBodyId.append("<td>" + trades[i]["tradesGenerated"] + "</td>");
        tableBodyId.append("<td>" + trades[i]["tradesPerSecond"] + "</td>");
        tableBodyId.append("<td>" + trades[i]["ordersGenerated"] + "</td>");
        tableBodyId.append("<td>" + trades[i]["ordersPerSecond"] + "</td>");
    }
}

