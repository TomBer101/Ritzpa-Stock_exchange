
$(function ()   // on load
{
    $('#loginForm').submit(function ()
    {

        $("#error-placeholder").empty();

        var textFieldValue = document.getElementById('userNameTF');
        var brokerRadioValue = document.getElementById('bRadio');
        var adminRadioValue = document.getElementById('aRadio');

        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function (errorObject)
            {
                console.error("Failed to login!");
                $("#error-placeholder").append(errorObject.responseText);
            },
            success: function(nextPageUrl)
            {
                sessionStorage.setItem("username", textFieldValue.value);
                window.location.replace(nextPageUrl);
            }
            });
            return false;
    });
});