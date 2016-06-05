var searchHistory=true;

$(document).ready(function(){

    $("#head").load("head.html");
    $("#foot").load("foot.html");

    //切换查询模式
    $("#change-search-mode li").click(function () {
        $("#change-search-mode li").removeClass("active");
        $(this).addClass("active");
        switch ($(this).index()) {
            case 0:
                $("#search-rate-start, #search-rate-end").parent().show();
                $("#current-rate").hide();
                searchHistory=true;
                break;
            case 1:
                $("#search-rate-start, #search-rate-end").parent().hide();
                $("#current-rate").show();
                searchHistory=false;
                break;
        }
    });

    //加载支持的货币
    CurrencyService.getCurrencyList(function (currencies) {
        for(var i in currencies) {
            var data= {
                cid: currencies[i].cid,
                code: currencies[i].code,
                src: "static/img/currency/"+currencies[i].code+".png"
            };

            $("#search-rate-from-list").mengular(".search-rate-from-list-template", data);
            $("#search-rate-from-list li[data-id="+data.cid+"]").click(function () {
                var code=$(this).attr("data-code");
                $("#search-rate-from-button img").attr("src", "static/img/currency/"+code+".png");
                $("#search-rate-from-button .search-rate-code").text(code);
                $("#search-rate-from").val($(this).attr("data-id"));
            });

            $("#search-rate-to-list").mengular(".search-rate-to-list-template", data);
            $("#search-rate-to-list li[data-id="+data.cid+"]").click(function () {
                var code=$(this).attr("data-code");
                $("#search-rate-to-button img").attr("src", "static/img/currency/"+code+".png");
                $("#search-rate-to-button .search-rate-code").text(code);
                $("#search-rate-to").val($(this).attr("data-id"));
            });
            $("#supported-currency").mengular(".supported-currency-template", data);
        }
    });

    //日期初始化
    $("#search-rate-start, #search-rate-end").datetimepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0,
        showMeridian: 1
    });

    //搜索历史记录
    $("#search-rate-submit").click(function () {
        var start=$("#search-rate-start").val();
        var end=$("#search-rate-end").val();
        var from=$("#search-rate-from").val();
        var to=$("#search-rate-to").val();
        var validate=true;
        if(searchHistory) {
            if(start==""||start==null) {
                $("#search-rate-start").parent().addClass("has-error");
                validate=false;
            } else {
                $("#search-rate-start").parent().removeClass("has-error");
            }
            if(end==""||end==null) {
                $("#search-rate-end").parent().addClass("has-error");
                validate=false;
            } else {
                $("#search-rate-end").parent().removeClass("has-error");
            }
        }
        if(from==""||from==null) {
            $("#search-rate-from-button").addClass("btn-danger");
            validate=false;
        } else {
            $("#search-rate-from-button").removeClass("btn-danger");
        }
        if(to==""||to==null) {
            $("#search-rate-to-button").addClass("btn-danger");
            validate=false;
        } else {
            $("#search-rate-to-button").removeClass("btn-danger");
        }
        if(from==to&&from!="") {
            $("#search-rate-from-button").addClass("btn-danger");
            $("#search-rate-to-button").addClass("btn-danger");
            validate=false;
        } else {
            $("#search-rate-from-button").removeClass("btn-danger");
            $("#search-rate-to-button").removeClass("btn-danger");
        }
        if(validate) {
            if(searchHistory) {
                location.href="rate.html?start="+start+"&end="+end+"&from="+from+"&to="+to;
            } else {
                RateService.getCurrentRate(from, to, function(rate) {
                    var fromCode=$("#search-rate-from-list li[data-id="+from+"] span").text();
                    var toCode=$("#search-rate-from-list li[data-id="+to+"] span").text();
                    $("#current-rate h5").text("1 "+fromCode+" = "+rate+" "+toCode);
                });
            }
        }

    });

});

