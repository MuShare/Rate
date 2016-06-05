$(document).ready(function(){
    $("#head").load("head.html");
    $("#foot").load("foot.html");
    
    $("#register-submit").click(function () {
        var uname=$("#register-uname").val();
        var email=$("#register-email").val();
        var telephone=$("#register-telephone").val();
        var password=$("#register-password").val();
        var validate=true;
        if(uname==null||uname=="") {
            $("#register-uname").parent().addClass("has-error");
            validate=false;
        } else {
            $("#register-uname").parent().removeClass("has-error");
        }
        if(email==null||email==""||!isEmailAddress(email)) {
            $("#register-email").parent().addClass("has-error");
            validate=false;
        } else {
            $("#register-email").parent().removeClass("has-error");
        }
        if(telephone==null||telephone==""||!isInteger(telephone)) {
            $("#register-telephone").parent().addClass("has-error");
            validate=false;
        } else {
            $("#register-telephone").parent().removeClass("has-error");
        }
        if(password==null||password=="") {
            $("#register-password").parent().addClass("has-error");
            validate=false;
        } else {
            $("#register-password").parent().removeClass("has-error");
        }
        if(validate) {
            UserService.register(uname, email, telephone, password, function (uid) {
                if(uid) {
                    $.messager.popup("Register success, please Sign In!");
                    setTimeout(function () {
                        location.href="../../login.html";
                    }, 1000);
                }
            });
        } else {
            $.messager.popup("Check you invalidate values and try again!");
        }
    });

});