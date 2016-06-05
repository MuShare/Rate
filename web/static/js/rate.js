var start=request("start");
var end=request("end");
var from=request("from");
var to=request("to");

$(document).ready(function(){

    $("#head").load("head.html");
    $("#foot").load("foot.html");

    //日期初始化
    $("#change-date-start, #change-date-end").datetimepicker({
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

    fillValue({
        "change-date-start": start,
        "change-date-end": end
    });

    //变更汇率时间
    $("#change-date-submit").click(function () {
        start=$("#change-date-start").val();
        end=$("#change-date-end").val();
        searchRateHistoey(start, end, from, to);
    });

    searchRateHistoey(start, end, from, to);
});

/**
 * 查询汇率历史
 * @param start
 * @param end
 * @param from
 * @param to
 */
function searchRateHistoey(start, end, from, to) {
    RateService.getHistoryRate(start, end, from, to, function (data) {
        $("#history-graph").highcharts({
            chart: {
                zoomType: 'x'
            },
            title: {
                text: data.inCurrency+" to "+data.outCurrency+" exchange rate over time"
            },
            subtitle: {
                text: "From "+start+" to "+end
            },
            xAxis: {
                type: 'datetime'
            },
            yAxis: {
                title: {
                    text: 'Exchange rate'
                }
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: {
                            x1: 0,
                            y1: 0,
                            x2: 0,
                            y2: 1
                        },
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 2
                    },
                    lineWidth: 1,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },

            series: [{
                type: 'area',
                name: data.inCurrency+" to "+data.outCurrency,
                pointInterval: 24*3600*1000,
                pointStart: data.time,
                data: data.data
            }]
        });

    });
}