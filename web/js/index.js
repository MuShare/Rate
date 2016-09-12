/**
 * Created by Alex on 2016/8/15.
 */

var currencies = [];
var pop_template = '<div class="marker"> <div class="ui two column divided grid">' +
    '<div class="row"><div class="column"><div class="ui image tiny">' +
    '<img src="image/%IMAGE%.svg"></div></div><div class="column"><h4 class="currency_code">%CODE%</h4>' +
    '<span class="currency_name">%NAME%</span><h4 class="rate_value">%VALUE%</h4><button class="ui button" data-cid=%CID% onclick="getHistory(this)">history</button></div></div></div></div></div>';
var current_cid = 'ff808181568824b701568825c7680000';
var earth;
var markers = [];

$(document).ready(function () {


    var base_vm = new Vue({
        el : '#base_currency',
        data : {
            base_currency : '',
        },
        methods : {
            switch_currency : function(event){
                $("#currencies").modal("show");
            }
        }
    });

    var currencies_vm = new Vue({
        el : '#test',
        data : {
            currencies : ''
        },
        methods: {
            select_currency: function(event){
                var selected_cid = $(event.currentTarget).data('cid');
                base_vm.base_currency = currencies[selected_cid];
                getRate(selected_cid);
                current_cid = selected_cid;
                $("#currencies").modal("hide");
            }
        }
    });




    getCurrencies(currencies_vm, base_vm);
});

function getHistory(button){
    var to = $(button).data('cid');
    $.ajax({
        type:'GET',
        url:'/api/web/rate/history',
        data:{from:current_cid,
        to:to,
        start:'1357084800000',
        end:'1443398400000'},
        dataType:'json',
        success:function(data){
            $('#highcharts').highcharts({
                chart: {
                    zoomType: 'x'
                },
                title: {
                    text: 'USD to EUR exchange rate over time'
                },
                subtitle: {
                    text: document.ontouchstart === undefined ?
                        'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
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
                    name: 'USD to EUR',
                    data: data.result.data
                }]
            });
            $("#charts").modal("show");
        },
        error:function(xhr, status, error){
            console.log(error);
        }
    });


}

function getCurrencies(currencies_vm, base_currency_vm){
    $.ajax({
        type:'GET',
        url:'/api/web/currencies',
        dataType:'json',
        success:function(data){
            currencies_vm.currencies = data.result.currencies;
            $.grep(data.result.currencies, function(currency){
                currencies[currency.cid] = currency;
            });
            base_currency_vm.base_currency = currencies[current_cid];
            initRate(current_cid);
        },
        error:function(xhr, status, error){
            console.log(error);
        }
    })
}

function initRate(fromCid){
    $.ajax({
        type:'GET',
        url:'/api/web/rate/current',
        data:{from:fromCid},
        dataType:'json',
        success:function(data){
            earth = new WE.map('earth_div');
            WE.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(earth);
            $.each(data.result.rates, function(cid,rate){
                var pop_content = getPopContent(cid, rate);
                var currency = currencies[cid];
                var marker = WE.marker([currency.latitude, currency.longitude]).addTo(earth);
                marker.bindPopup(pop_content, {maxWidth: 150, closeButton: true}).openPopup();
                markers[cid] = marker;
            });
            earth.setView([51.505, 0], 2);
        },
        error:function(xhr, status, error){
            console.log(error);
        }
    })
}

function getRate(fromCid){
    $.ajax({
        type:'GET',
        url:'/api/web/rate/current',
        data:{from:fromCid},
        dataType:'json',
        success:function(data){
            var currentMarker = markers[fromCid];
            currentMarker.removeFrom(earth);
            delete markers[fromCid];
            $.each(data.result.rates, function(cid,rate){
                var marker = markers[cid];
                var pop_content = getPopContent(cid, rate);
                var currency = currencies[cid];
                if(marker == null){
                    marker = WE.marker([currency.latitude, currency.longitude]).addTo(earth);
                    markers[cid] = marker;
                }
                marker.bindPopup(pop_content, {maxWidth: 150, closeButton: true}).openPopup();
            });
            earth.setView([51.505, 0], 2);
        },
        error:function(xhr, status, error){
            console.log(error);
        }
    })
}


function getPopContent(cid, rate){
    var currency = currencies[cid];
    var pop_content = pop_template.replace(/%IMAGE%/, currency.icon);
    pop_content = pop_content.replace(/%CODE%/, currency.code);
    pop_content = pop_content.replace(/%NAME%/, currency.name);
    pop_content = pop_content.replace(/%VALUE%/, rate.toString());
    pop_content = pop_content.replace(/%CID%/, currency.cid);
    return pop_content;
}