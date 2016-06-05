$(document).ready(function () {
    $("#head").load("head.html");
    $("#foot").load("foot.html");
    
    //登录提交
    $("#login-submit").click(function () {
        var email=$("#login-email").val();
        var password=$("#login-password").val();
        var validate=true;
        if(validate==null||validate==""||!isEmailAddress(email)) {
            $("#login-email").parent().addClass("has-error");
            validate=false;
        } else {
            $("#login-email").parent().removeClass("has-error");
        }
        if(password==null||password=="") {
            $("#login-password").parent().addClass("has-error");
            validate=false;
        } else {
            $("#login-password").parent().removeClass("has-error");
        }
        if(validate) {
            UserService.login(email, password, function (success) {
                if(success) {
                    location.href="../../subscribe.html";
                } else {
                    $.messager.popup("Email or password wrong!");
                }
            });
        }
    });

});