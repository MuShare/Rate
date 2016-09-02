/**
 * Created by Alex on 2016/8/15.
 */

var currencies = [];
var pop_template = '<div class="marker"> <div class="ui two column divided grid">' +
    '<div class="row"><div class="column"><div class="ui image tiny">' +
    '<img src="image/%IMAGE%.svg"></div></div><div class="column"><h4 class="currency_code">%CODE%</h4>' +
    '<span class="currency_name">%NAME%</span><h4 class="rate_value">%VALUE%</h4></div></div></div></div></div>';

$(document).ready(function () {

    getCurrencies();

    $("#switch_currency_bt").click(function(){
        $(".ui.modal").modal("show");
    });

    //var earth = new WE.map('earth_div');
    //WE.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(earth);
    //
    //
    //
    //var rate = {
    //    "code" : "USD",
    //    "image" : "us",
    //    "display_name" : "US Dollar",
    //    "value" : 15
    //};
    //
    //
    //var pop_content = pop_template.replace(/%IMAGE%/, rate.image);
    //pop_content = pop_content.replace(/%CODE%/, rate.code);
    //pop_content = pop_content.replace(/%NAME%/, rate.display_name);
    //pop_content = pop_content.replace(/%VALUE%/, rate.value.toString());
    //
    //
    //
    //
    //var marker = WE.marker([35.86166, 104.195397]).addTo(earth);
    //marker.bindPopup("<b>Hello world!</b><br>I am a popup.<br /><span style='font-size:10px;color:#999'>Tip: Another popup is hidden in Cairo..</span>", {maxWidth: 200, maxHeight: 150, closeButton: true}).openPopup();
    //
    //marker.bindPopup(pop_content, {maxWidth: 150, closeButton: true}).openPopup();
    //
    //
    //
    //var marker2 = WE.marker([30.058056, 31.228889]).addTo(earth);
    //marker2.bindPopup("<b>Cairo</b><br>Rebuild!", {maxWidth: 120, closeButton: false});
    //
    //marker2.bindPopup("<b>Cairo</b><br>Rebuild", {maxWidth: 120, closeButton: false});
    //
    //earth.setView([51.505, 0], 2);
});


function getCurrencies(){
    $.ajax({
        type:'GET',
        url:'/api/web/currencies',
        dataType:'json',
        success:function(data){
            $.grep(data.result.currencies, function(currency){
                currencies[currency.cid] = currency;
            });
            getRate('ff808181568824b701568825c7680000');
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
            var earth = new WE.map('earth_div');
            WE.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(earth);
            $.each(data.result.rates, function(cid,rate){
                console.log(cid);
                var pop_content = getPopContent(cid, rate);
                var currency = currencies[cid];
                console.log(currency);
                var marker = WE.marker([currency.latitude, currency.longitude]).addTo(earth);
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
    return pop_content;
}