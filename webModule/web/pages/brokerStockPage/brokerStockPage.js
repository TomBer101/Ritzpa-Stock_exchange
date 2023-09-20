/******************************************************************************/
var refreshRate = 2000;
var STOCK_DATA_SERVLET_URL = buildUrlWithContextPath("stockData4Broker");
var USER_ALERT_SERVLET_URL = buildUrlWithContextPath("alertData");
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
function refreshBrokerData(stockData)
{
    var myAmount = stockData.amount;
    var currStockDTO = stockData.stockDTO;

    $("#mainTitle").empty();
    $("#mainTitle").append(currStockDTO.symbol + " Data:")

    $("#symbolContainer").empty();
    $("#symbolContainer").append("<span class='toBold'>Symbol:  </span>" + currStockDTO.symbol);

    $("#companyNameContainer").empty();
    $("#companyNameContainer").append("<span class='toBold'>Company Name:  </span>" + currStockDTO.companyName);

    $("#valueContainer").empty();
    $("#valueContainer").append("<span class='toBold'>Value:  </span>" + currStockDTO.currValue);

    $("#cycleContainer").empty();
    $("#cycleContainer").append("<span class='toBold'>Cycle:  </span>" + currStockDTO.transactionsCycle);

    $("#amount").empty();
    $("#amount").append(myAmount);

    if( myAmount === 0)
        $("#sRadio").prop('disabled', true);
    else
        $("#sRadio").prop('disabled', false);

    var tableBodyElement = document.getElementById("transactionHistoryTb");
    if (tableBodyElement.length !== 1)
        $("#transactionHistoryTb").empty();

    var dataForChart = [];
    dataForChart.push({label:"Init", y:parseInt(currStockDTO.initValue)}); // Stock's price begins from it init value.

    currStockDTO.transactionHistory.forEach(trade =>
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
}
/******************************************************************************/
function ajaxRefreshBrokerData(){
    $.ajax({
        url:STOCK_DATA_SERVLET_URL,
        method:"GET",
        success:function(data)
        {
            console.log("In Success Stock DATA");
            refreshBrokerData(data);
        },
        error:function(e)
        {
            console.log("didnt refresh stock data.")
        }

    })
}
/******************************************************************************/
function makeAlert(alertData)
{
    var msg = "";

    $.each(alertData || [], function(index, data)
    {
        msg += (data.oppositeUserName + " ");

        if (data.way === "BUY")
            msg += "bought from ";
        else
            msg += "sold to ";

        msg += ("you " + data.amount + " stocks of " + data.symbol + ", for " + data.price + " per stock. " + data.leftOvers
            + " still waiting.\n");
    });

    var z;
    alert(msg);
}
/******************************************************************************/
function ajaxCheckForAlert()
{
    $.ajax({
        url: USER_ALERT_SERVLET_URL,
        dataType: 'json',
        success: function(dataAlert)
        {
            makeAlert(dataAlert);
        }
    })
    return false;
}
/******************************************************************************/
$(function ()
{
    ajaxRefreshBrokerData();
    setInterval(ajaxRefreshBrokerData, refreshRate);
    setInterval(ajaxCheckForAlert, refreshRate);
});
/******************************************************************************/
function appendTrades(currTrades, way, stockSymbol)
{
    var otherSide = (way === "BUY" )? "seller" : "buyer" ; // suppose to be th same
    $('#currTrades').empty();

    var others = [];

    currTrades.subCommandTrades.forEach( trade =>
        {
            $('<li class="subTrade">' + trade.amount + " Stocks with " + trade[otherSide] +
                ". with value of: " + trade.currPrice + '</li>').appendTo($("#currTrades"));

        });

    var scroller = $("#currTrades");
    var height = scroller[0].scrollHeight - $(scroller).height();
}
/******************************************************************************/
function appendMessage(currTrades, way, type, stockSymbol)
{
    var leftOvers = parseInt(currTrades.inWaitingList);
    var currAmount = parseInt(currTrades.offerStockAmount);

    if (type === "IOC")
    {
        if (leftOvers === currAmount)
        {
            $("#msg-holder").append("No trades had been made.");
        }
        else
        {
            $("#msg-holder").append("0 stocks are waiting. ")
            appendTrades(currTrades, way, stockSymbol);
        }
    }
    else
    {
        switch (leftOvers)
        {
            case parseInt(currAmount):
                $("#msg-holder").append("Sorry. No trade had been made.");
                break;
            case(0):
                $("#msg-holder").append("YAY! All the command is finished!")
                appendTrades(currTrades, way, stockSymbol);
                break;
            default:
                $("#msg-holder").append(leftOvers + " stocks are waiting. ")
                appendTrades(currTrades, way, stockSymbol);
                breake;
        }
    }
}
/******************************************************************************/
$(function(){
    var formHolder = document.getElementById("makeCommandHolder");
    var currTradesList = document.getElementById("commandRes");

    $("#makeCommandForm").submit(function()
        {
            var myAmount = parseInt(document.getElementById("amount").innerText);
            var stockSymbol = document.getElementById("symbol");

            $("#msg-holder").empty();
            $("#currTrades").empty();

            var way = document.getElementsByName('way');
            var flag1 = false;
            for(var i = 0; i < way.length; i++){ // if way was selected
                if(way[i].checked){
                    flag1 = true;
                    way = way[i].value;
                    break;
                }
            }

            var type = document.getElementsByName('type');
            var flag2 = false
            for(var i = 0; i < type.length; i++){
                if(type[i].checked){
                    flag2 = true;
                    type = type[i].value;
                    break;
                }
            }

            if(!flag1 || !flag2)
                $("#msg-holder").append("Missing information.");

            else if(document.getElementById("stockAmount").value.length == 0 || document.getElementById("stockAmount").value <= 0)
                $("#msg-holder").append("Amount of stock has to be greater then 0.");
            else if(document.getElementById("priceLimit").disabled === false && parseInt(document.getElementById("priceLimit").value) <= 0)
                $("#msg-holder").append("Price limit has to be greater then 0.");
            else if(way === "SELL" && document.getElementById("stockAmount").value > myAmount)
                $("#msg-holder").append("You cannot sell more than you have...");

            else
            {
                $.ajax({
                    data:$(this).serialize(),
                    url:this.action,
                    method: "POST",
                    timeout: 2000,
                    success:function(currTrades)
                    {
                        console.log("in sucsses");
                        appendMessage(currTrades, way, type, stockSymbol); // I send the way to print the opposite User.

                        $("#makeCommandForm")[0].reset();
                    },
                    error:function(t)
                    {
                        console.log("bad");
                    }
                });
                return false;
            }
            return false;
        });
    return false;
});
/******************************************************************************/
function disableLimit()
{
    document.getElementById("priceLimit").disabled =  true;
}
/******************************************************************************/
function enableLimit()
{
    document.getElementById("priceLimit").disabled =  false;
}
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
