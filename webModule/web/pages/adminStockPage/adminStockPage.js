/******************************************************************************/
var refreshRate = 2000; // milli seconds
var ADMIN_UPDATE_DATA_SERVLET_URL = buildUrlWithContextPath("adminStockPageUpdateData");
/******************************************************************************/
function updateStockChart(chartData)
{
    $("#stockChartHolder").empty();

    var reversed = chartData.slice(1, chartData.length).reverse();
    reversed.unshift(chartData[0]);

    var chart = new CanvasJS.Chart("stockChartHolder",{
        backgroundColor: "#fdfdfd",

        title: { text:"Stock Price History"},
        axisX: { title: "Time"},
        axisY: { title: "Price"},
        data: [{
            lineColor:"#6b6969",
            type: "line",
            dataPoints: reversed, color:"#90d8db"
        }]
    });
    chart.render();
}
/******************************************************************************/
function refreshStockData(stockData)
{

    $("#mainTitle").empty();
    $("#mainTitle").append(stockData.symbol + " Data:")

    $("#symbolContainer").empty();
    $("#symbolContainer").append("<span class='toBold'>Symbol: </span>" + stockData.symbol);

    $("#companyNameContainer").empty();
    $("#companyNameContainer").append("<span class='toBold'>Company Name: </span>" + stockData.companyName);

    $("#valueContainer").empty();
    $("#valueContainer").append("<span class='toBold'>Value: </span>" + stockData.currValue);

    $("#cycleContainer").empty();
    $("#cycleContainer").append("<span class='toBold'>Cycle: </span>" + stockData.transactionsCycle);

    var tableBodyElement = document.getElementById("transactionHistoryTb");

    if (tableBodyElement.length !== 1)
        $("#transactionHistoryTb").empty();

    var dataForChart = [];
    dataForChart.push({label:"Init", y:parseInt(stockData.initValue)}); // Stock's price begins from it init value.

    stockData.transactionHistory.forEach( trade =>
    {
        var newRow = tableBodyElement.insertRow(-1);

        var dateCell = newRow.insertCell(0);
        var amountCell = newRow.insertCell(1);
        var valueCell = newRow.insertCell(2);

        dateCell.innerHTML = trade.date;
        amountCell.innerHTML = trade.amount;
        valueCell.innerHTML = trade.value;

        dataForChart.push({label:trade.date, y:parseInt(trade.value)});
    })

    updateStockChart(dataForChart);

    $("#buyCommandsListContainer").empty();

    $.each(stockData.buyOffers || [], function (index, offer)
    {
        $("<div class='cmdObject'> <span class='toBold'>Date: </span>" + offer.date +
        "<br> <span class='toBold'>Way: </span>" + offer.way + "<br> <span class='toBold'>Value: </span>" +
        offer.offerValue + "<br> <span class='toBold'>Amount: </span> " + offer.amount +
        "<br> <span class='toBold'>Intitator: </span>" + offer.initiatorUserName +
            "<br><span class='toBold'>Type: </span>" + offer.type +"</div>").appendTo("#buyCommandsListContainer");
    });

    var scroller = $("#buyCommandsListContainer");
    var height = scroller[0].scrollHeight - $(scroller).height();

    $("#sellCommandsListContainer").empty();

    $.each(stockData.sellOffers || [], function (index, offer)
    {
        $("<div class='cmdObject'> <span class='toBold'>Date: </span>" + offer.date +
            "<br> <span class='toBold'>Way: </span>" + offer.way + "<br> <span class='toBold'>Value: </span>" +
            offer.offerValue + "<br> <span class='toBold'>Amount: </span> " + offer.amount +
            "<br> <span class='toBold'>Intitator: </span>" + offer.initiatorUserName +
            "<br><span class='toBold'>Type: </span>" + offer.type +"</div>").appendTo("#sellCommandsListContainer");
    });

    var scroller = $("#sellCommandsListContainer");
    var height = scroller[0].scrollHeight - $(scroller).height();
}
/******************************************************************************/
function ajaxAdminData()
{
    $.ajax({
        url: ADMIN_UPDATE_DATA_SERVLET_URL,
        success: function (stockData)
        {
            refreshStockData(stockData);
        }
    })
}
/******************************************************************************/
$(function()
{
    ajaxAdminData();
    setInterval(ajaxAdminData, refreshRate);
});
/******************************************************************************/
function Time()
{
    var date = new Date();
    var hour = date.getHours();
    var minute = date.getMinutes();
    var seconds = date.getSeconds();
    var period = "";

    if (hour >= 12)
        period = "PM";
    else
        period = "AM";

    if (hour == 0)
        hour = 12;
    else
    {
        if (hour > 12)
        {
            hour = hour - 12;
        }
    }

    hour = update(hour);
    minute = update(minute);
    seconds = update(seconds);

    document.getElementById("digitalClock").innerHTML = hour + " : " + minute +
        " : " + seconds + " " + period;

    setTimeout(Time, 1000);

}
/******************************************************************************/
function update(t)
{
    if (t < 10)
        return "0" + t;
    else
        return t;
}
/******************************************************************************/
$(function()
{
   Time();
});
/******************************************************************************/