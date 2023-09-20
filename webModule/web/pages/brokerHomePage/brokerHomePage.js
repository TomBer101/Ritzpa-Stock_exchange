/******************************************************************************/
var refreshRate = 2000; // milli seconds
var USER_LIST_SERVLET_URL = buildUrlWithContextPath("usersList");
var STOCK_LIST_SERVLET_URL = buildUrlWithContextPath("stocksList");
var USER_DATA_SERVLET_URL = buildUrlWithContextPath("userData");
var USER_ALERT_SERVLET_URL = buildUrlWithContextPath("alertData");
var CHAT_LIST_SERVLET_URL = buildUrlWithContextPath("chatList");
/******************************************************************************/
function refreshUsersList(users)
{
    $('#usersList').empty();

    $.each(users || [], function(index, user)
    {
        $('<li class="userOnline"> <span class="userNameBold">' + user.key + '</span><span class="userRole">' + user.value + '</span></li>')
            .appendTo($('#usersList'));
    });
}
/******************************************************************************/
function refreshUserData(userData)
{
    $("#currentCurrency").empty();
    $("#currentCurrency").append(userData.currency);

    var tableBodyElement = document.getElementById("userHistoryTb");

    if (tableBodyElement.length !== 1)
        $("#userHistoryTb").empty();

    userData.actionHistory.forEach( action =>
    {
        var newRow =  tableBodyElement.insertRow(-1);

        var typeCell = newRow.insertCell(0);
        var dateCell = newRow.insertCell(1);
        var amountCell = newRow.insertCell(2);
        var currencyBeforeCell = newRow.insertCell(3);
        var currencyAfterCell = newRow.insertCell(4);
        var symbolCell = newRow.insertCell(5);

        typeCell.innerHTML = action.type;
        dateCell.innerHTML = action.date;
        amountCell.innerHTML = action.amount;
        currencyBeforeCell.innerHTML = action.currencyBefore;
        currencyAfterCell.innerHTML = action.currencyAfter;
        symbolCell.innerHTML = action.symbol;
    })
}
/******************************************************************************/
function appendChatEntry(index, entry)
{
    var entryElement = createChatEntry(entry);
    $("#chatarea").append(entryElement).append("<br>");
}
/******************************************************************************/
function createChatEntry(entry)
{
    return $("<div class=\"msg\"><span class='userNameBold'>" + entry.userName + "</span>: " + entry.chatString + "</div>");
}
/******************************************************************************/
function ajaxChatList()
{
    $.ajax({
        url: CHAT_LIST_SERVLET_URL,
        dataType: 'json',
        success: function(stocks)
        {
            refreshChatList(stocks);
        }
    })
}
/******************************************************************************/
function refreshChatList(chatData)
{
    $('#chatarea').empty();
    $.each(chatData || [], appendChatEntry);

    var scroller = $("#chatarea");
    var height = scroller[0].scrollHeight - $(scroller).height();
}
/******************************************************************************/
function showStock(symbol)
{
    $.ajax({
        data: "symbol=" + symbol.getAttribute('id'),
        url:"http://localhost:8080/webModule_Web_exploded/pages/brokerHomePage/StockInfoServlet",
        method: "GET",
        timeout: 2000,
        error: function()
        {
            console.error("Should not have reach here");
        },
        success: function (nextPageUrl)
        {
            window.location.replace(nextPageUrl);
        }
    });
    return false;
}
/******************************************************************************/
function refreshStocksList(stocks)
{
    $('#stocksList').empty();

    $.each(stocks || [], function (index, stock)
    {
        $('<li class=\'s\' + id='  + stock.symbol + "  onclick='showStock(" + stock.symbol +")'>" + "Symbol: " + stock.symbol + "<br>" + "Company: " + stock.companyName + "<br> Value: "
            + stock.currValue + "<br> Cycle: " + stock. transactionsCycle + "</li>").appendTo($('#stocksList'));
    });
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

    alert(msg);
}
/******************************************************************************/
function updateUserTitle()
{
    var titleElement = document.getElementById("mainTitle");
    titleElement.innerText = "Welcome " + sessionStorage.getItem("username").valueOf();
}
/******************************************************************************/
function ajaxUsersList()
{
    $.ajax({
        url: USER_LIST_SERVLET_URL,
        success: function(users)
        {
            refreshUsersList(users);
        }
    })
}
/******************************************************************************/
function ajaxUserData()
{
    $.ajax({
        url: USER_DATA_SERVLET_URL,
        success: function(data)
        {
            refreshUserData(data);
        }
    })
}
/******************************************************************************/
function ajaxStocksList()
{
    $.ajax({
        url: STOCK_LIST_SERVLET_URL,
        dataType: 'json',
        success: function(stocks)
        {
            refreshStocksList(stocks);
        }
    })
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
$(function()
{
    updateUserTitle();
    ajaxUsersList();
    ajaxStocksList();
    ajaxUserData();
    ajaxChatList();

    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxStocksList, refreshRate);
    setInterval(ajaxUserData, refreshRate);
    setInterval(ajaxCheckForAlert, refreshRate);
    setInterval(ajaxChatList, refreshRate);
});
/******************************************************************************/
$(function(){
    var d = document.getElementById("issueStockHolder");
    $("#issueStockForm").submit(function()
    {
        var symbol = document.getElementById("symbol");
        var companyName = document.getElementById("companyName");
        var stockAmount = document.getElementById("stocksAmount");
        var companyVal = document.getElementById("companyValue");

        if(symbol.value.length === 0 || companyVal.value.length === 0 ||
            companyName.value.length === 0 || stockAmount.value.length === 0)
            $('error-holder').append("Missing Information");

        if(stockAmount.value == 0)
            $('error-holder').append("Amount of stocks should be grater then zero");
        else
        {
            $.ajax({
                data:$(this).serialize(),
                url:this.action,
                timeout:2000,
                error: function(errorObject)
                {
                    console.error("Failed to add Stock!");
                    $("#error-holder").append(errorObject.responseText);
                },
                success: function(r)
                {
                    $("#issueStockForm")[0].reset();
                   d.style.display = "none";
                }
            });
            return false;
        }
    });
});
/******************************************************************************/
$(function()
{
    $("#chargeMoney").submit(function()
    {
        var moneyAmount = document.getElementById("moneyAmount");

        if(moneyAmount.value.length === 0)
            $('error-holder').append("Missing Information"); // TODO : more informative message?
        else
        {
            $.ajax({
                data: $(this).serialize(),
                url: this.action,
                timeout: 2000,
                error: function(errorObject)
                {
                    console.error("Failed to load Money!");
                    $("#error-holder").append(errorObject.responseText);
                },
                success: function(r)
                {
                    $('#chargeMoney')[0].reset();
                }
            });
            return false;
        }
        return false;
    });
});
/******************************************************************************/
var refreshRate = 2000;
/******************************************************************************/
$(function()
{
    $('#uploadFileForm').submit(function()
    {
        $('#error-holder').empty();
        var file2Load = this[0].files[0];

        if (file2Load === undefined)
            $('#error-holder').append("No files was selected.");
        else
        {
            var formData = new FormData();
            formData.append("myFile", file2Load);

            $.ajax({
                method: 'POST',
                data: formData,
                url: this.action,
                processData: false,
                contentType: false,
                timeout: 4000,
                error: function(e)
                {
                    $("#error-holder").append(e.responseText);
                },
                success: function(r)
                {
                    $('#error-holder').text(r);
                }
            });
            return false;
        }
        return false;
    });
    return false;
});
/******************************************************************************/
$(function() { // onload...do
    //add a function to the submit event
    $("#chatform").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            method: "POST",
            url: this.action,
            timeout: 2000,
            error: function()
            {
                console.error("Failed to submit");
            },
            success: function(r)
            {
            }
        });

        $("#userstring").val("");
        return false;
    });
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
