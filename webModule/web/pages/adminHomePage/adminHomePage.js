/******************************************************************************/
var refreshRate = 2000; // milli seconds
var USER_LIST_SERVLET_URL = buildUrlWithContextPath("usersList");
var STOCK_LIST_SERVLET_URL = buildUrlWithContextPath("stocksList");
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
function refreshStocksList(stocks)
{
    $('#stocksList').empty();

    $.each(stocks || [], function (index, stock)
    {
        $('<li class=\'s\' id=' + stock.symbol + "  onclick='showStock(" + stock.symbol +")'>" + "Symbol: " + stock.symbol + "<br>" + "Company: " + stock.companyName + "<br> Value: "
            + stock.currValue + "<br> Cycle: " + stock. transactionsCycle + "</li>").appendTo($('#stocksList'));
    });
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
function showStock(symbol)
{
    $.ajax({
        data: "symbol=" + symbol.getAttribute('id'),
        url:"http://localhost:8080/webModule_Web_exploded/pages/adminHomePage/StockInfoServlet",
        method: "GET",
        timeout: 2000,
        error: function(errorMassage)
        {
            console.log(errorMassage);
        },
        success: function (nextPageUrl)
        {
            window.location.replace(nextPageUrl);
        }
    });
    return false;
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
$(function()
{
    updateUserTitle();
    ajaxUsersList();
    ajaxStocksList();
    ajaxChatList();
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxStocksList, refreshRate);
    setInterval(ajaxChatList, refreshRate);
});
/******************************************************************************/
function updateUserTitle()
{
    var titleElement = document.getElementById("mainTitle");
    titleElement.innerText = "Welcome " + sessionStorage.getItem("username").valueOf();
}
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