var user;
var isEnable=true;
var isOnce=false;
var sendEmail=true;
var sendSMS=true;
var modifyingSid;

$(document).ready(function () {
    $("#head").load("head.html");
    $("#foot").load("foot.html");

    checkSession(function (_user) {
        if(_user==null) {
            location.href="index.html";
            return;
        }

        user=_user;
        loadSubscribes();
    });


    $("#add-subscribe-enable").bootstrapSwitch({
        state: isEnable
    }).on("switchChange.bootstrapSwitch", function (event, state) {
        isEnable=state;
    })

    $("#add-subscribe-once").bootstrapSwitch({
        state: isOnce
    }).on("switchChange.bootstrapSwitch", function (event, state) {
        isOnce=state;
    })

    $("#add-subscribe-email").bootstrapSwitch({
        state: sendEmail
    }).on("switchChange.bootstrapSwitch", function (event, state) {
        sendEmail=state;
    })

    $("#add-subscribe-sms").bootstrapSwitch({
        state: sendSMS
    }).on("switchChange.bootstrapSwitch", function (event, state) {
        sendSMS=state;
    })


    //加载支持的货币
    CurrencyService.getCurrencyList(function (currencies) {
        for(var i in currencies) {
            var data= {
                cid: currencies[i].cid,
                code: currencies[i].code,
                src: "static/img/currency/"+currencies[i].code+".png"
            };

            $("#add-subscribe-from-list").mengular(".add-subscribe-from-list-template", data);
            $("#add-subscribe-from-list li[data-id="+data.cid+"]").click(function () {
                var code=$(this).attr("data-code");
                $("#add-subscribe-from-button img").attr("src", "static/img/currency/"+code+".png");
                $("#add-subscribe-from-button .add-subscribe-code").text(code);
                $("#add-subscribe-from").val($(this).attr("data-id"));
            });

            $("#add-subscribe-to-list").mengular(".add-subscribe-to-list-template", data);
            $("#add-subscribe-to-list li[data-id="+data.cid+"]").click(function () {
                var code=$(this).attr("data-code");
                $("#add-subscribe-to-button img").attr("src", "static/img/currency/"+code+".png");
                $("#add-subscribe-to-button .add-subscribe-code").text(code);
                $("#add-subscribe-to").val($(this).attr("data-id"));
            });
        }
    });
    
    //新增订阅提交
    $("#add-subscribe-submit").click(function () {
        var sname=$("#add-subscribe-sname").val();
        var min=$("#add-subscribe-min").val();
        var max=$("#add-subscribe-max").val();
        var from=$("#add-subscribe-from").val();
        var to=$("#add-subscribe-to").val();
        console.log(from+", "+to);
        var validate=true;
        if(sname==null||sname=="") {
            $("#add-subscribe-sname").parent().addClass("has-error");
            validate=false;
        } else {
            $("#add-subscribe-sname").parent().removeClass("has-error");
        }
        if(min==null||min==""||!isNum(min)) {
            $("#add-subscribe-min").parent().addClass("has-error");
            validate=false;
        } else {
            $("#add-subscribe-min").parent().removeClass("has-error");
        }
        if(max==null||max==""||!isNum(max)) {
            $("#add-subscribe-max").parent().addClass("has-error");
            validate=false;
        } else {
            $("#add-subscribe-max").parent().removeClass("has-error");
        }
        if(from==""||from==null) {
            $("#add-subscribe-from-button").addClass("btn-danger");
            validate=false;
        } else {
            $("#add-subscribe-from-button").removeClass("btn-danger");
        }
        if(to==""||to==null) {
            $("#add-subscribe-to-button").addClass("btn-danger");
            validate=false;
        } else {
            $("#add-subscribe-to-button").removeClass("btn-danger");
        }
        if(from==to&&from!="") {
            $("#add-subscribe-from-button").addClass("btn-danger");
            $("#add-subscribe-to-button").addClass("btn-danger");
            validate=false;
        } else {
            $("#add-subscribe-from-button").removeClass("btn-danger");
            $("#add-subscribe-to-button").removeClass("btn-danger");
        }
        if(validate) {
            UserService.addSubscribe(sname, min, max, isEnable, isOnce, sendEmail, sendSMS, from, to, user.uid, function (sid) {
                if(sid) {
                    loadSubscribes();
                    $("#add-subscribe-modal").modal("hide");
                }
            });
        } else {
            $.messager.popup("Check Parameters!");
        }
    });
});

function loadSubscribes() {
    UserService.getSubscribes(function (subscribes) {
        $("#subscribe-list").mengularClear();
        var start=getThisYearStart();
        var end=getThisYearEnd();
        for(var i in subscribes) {
            $("#subscribe-list").mengular(".subscribe-list-template", {
                sid: subscribes[i].sid,
                sname: subscribes[i].sname,
                from: subscribes[i].fromCurrency.code,
                fromCid: subscribes[i].fromCurrency.cid,
                to: subscribes[i].toCurrency.code,
                toCid: subscribes[i].toCurrency.cid,
                max: subscribes[i].max,
                min: subscribes[i].min,
                current: subscribes[i].current,
                start: start,
                end: end
            });
            
            //编辑订阅
            $("#"+subscribes[i].sid+" .subscribe-list-edit").click(function () {
                modifyingSid=$(this).parent().parent().attr("id");
                $("#edit-subscribe-modal").modal("show");
            });

            //删除订阅
            $("#"+subscribes[i].sid+" .subscribe-list-remove").click(function () {
                var sid=$(this).parent().parent().attr("id");
                var sname=$("#"+sid+" .subscribe-list-sname").text();
                $.messager.confirm("Warning", "Confirm to delete subscribe "+sname+"?", function () {
                    UserService.deleteSuscribe(sid, function (success) {
                        if(success) {
                            $("#"+sid).remove();
                            $.messager.popup(sname+" has been deleted!");
                        } else {
                            $.messager.popup("Delete Failed.");
                        }
                    })
                });
            });
        }

        fillText({
            "subscribe-count": subscribes.length
        })
    });
}