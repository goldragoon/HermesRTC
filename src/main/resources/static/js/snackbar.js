function showSnackbar(content) {
    // Get the snackbar DIV
    var x = $("#snackbar")

    // Add the "show" class to DIV
    x.addClass("show");
    x.text(content)
    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.removeClass("show"); }, 3000);
}