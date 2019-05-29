/**
 * @date 2019-05
 * loginIn
 */

jQuery(function($) {

    $.ajax(
        "/pages/image/getLevelScore"
        , {
            async: false
            , cache: false
            , dataType: "json"
            , data: {}
            , type: "GET"
            , success: function (data, textStatus) {
                if (textStatus == "success") {
                    if(data.success){
                        var scoreMap = data.scoreMap
                        if(scoreMap.length>0) {
                            $("#yx-min").val(scoreMap["1"][0])
                            $("#yx-max").val(scoreMap["1"][1])
                            $("#lh-min").val(scoreMap["2"][0])
                            $("#lh-max").val(scoreMap["2"][1])
                            $("#yb-min").val(scoreMap["3"][0])
                            $("#yb-max").val(scoreMap["3"][1])
                            $("#jc-min").val(scoreMap["4"][0])
                            $("#jc-max").val(scoreMap["4"][1])
                            $("#hc-min").val(scoreMap["5"][0])
                            $("#hc-max").val(scoreMap["5"][1])
                        }
                    }
                    else {
                        alert("获取数据失败");
                    }
                }
            }
            , error:function(XMLHttpRequest, textStatus, errorThrown){
                console.log(textStatus)
                console.log(errorThrown)
            }
        }
    );

    $.ajax(
        "/pages/image/getFingerLevel"
        , {
            async: false
            , cache: false
            , dataType: "json"
            , data: {}
            , type: "POST"
            , success: function (data, textStatus) {
                if (textStatus == "success") {
                    if(data.success){
                        var levelMap = data.levelMap
                        for(var key in levelMap) {
                            $("#" + key.toLowerCase() +"-right-m").val(levelMap[key].rm);
                            $("#" + key.toLowerCase() +"-right-s").val(levelMap[key].rs);
                            $("#" + key.toLowerCase() +"-right-z").val(levelMap[key].rz);
                            $("#" + key.toLowerCase() +"-right-h").val(levelMap[key].rh);
                            $("#" + key.toLowerCase() +"-right-x").val(levelMap[key].rx);
                            $("#" + key.toLowerCase() +"-left-m").val(levelMap[key].lm);
                            $("#" + key.toLowerCase() +"-left-s").val(levelMap[key].ls);
                            $("#" + key.toLowerCase() +"-left-z").val(levelMap[key].lz);
                            $("#" + key.toLowerCase() +"-left-h").val(levelMap[key].lh);
                            $("#" + key.toLowerCase() +"-left-x").val(levelMap[key].lx);
                        }
                    }
                    else {
                        alert("获取数据失败");

                    }
                }
            }
            , error:function(XMLHttpRequest, textStatus, errorThrown){
                console.log(textStatus)
                console.log(errorThrown)
            }
        }
    );

    $(document).on('click','#set-score-btn',function(e){
        set_score()
    })

    // $(document).on('click','#set-image-reach-btn',function(e){
    //     set_image_reach()
    // })

});

function set_score(){
    var yx_min = $('#yx-min').val()
    var yx_max = $('#yx-max').val()
    var lh_min = $('#lh-min').val()
    var lh_max = $('#lh-max').val()
    var yb_min = $('#yb-min').val()
    var yb_max = $('#yb-max').val()
    var jc_min = $('#jc-min').val()
    var jc_max = $('#jc-max').val()
    var hc_min = $('#hc-min').val()
    var hc_max = $('#hc-max').val()
    var repeat = $('input[name="repeat"]:checked').val();

    $.ajax(
       "/pages/image/levelScore"
       , {
           async: false
           , cache: false
           , dataType: "json"
           , data: {yx_min:yx_min,yx_max:yx_max,lh_min:lh_min,lh_max:lh_max,yb_min:yb_min,yb_max:yb_max,jc_min:jc_min,jc_max:jc_max,hc_min:hc_min,hc_max:hc_max,repeat:repeat}
           , type: "POST"
           , success: function (data, textStatus) {
               if (textStatus == "success") {
                   if(data.success){
                       set_image_reach()
                   }
                   else {
                       if(data.message != null) alert(data.message);
                       else alert("设置失败");

                   }
               }
           }
           , error:function(XMLHttpRequest, textStatus, errorThrown){
               console.log(textStatus)
               console.log(errorThrown)
           }

       }
    );
}

function set_image_reach(){
    var arr_a = [];
    var arr_b = [];
    var arr_c = [];
    var value_a = $('[id^=a-]');
    var value_b = $('[id^=b-]');
    var value_c = $('[id^=c-]');
    value_a.map(function(index, item){
        arr_a.push(item.value);
    });
    value_b.map(function(index, item){
        arr_b.push(item.value);
    });
    value_c.map(function(index, item){
        arr_c.push(item.value);
    });
    var repeat = $('input[name="repeat"]:checked').val();

    $.ajax(
        "/pages/image/fingerLevel"
        , {
            async: false
            , cache: false
            , dataType: "json"
            , data: {value_a:arr_a,value_b:arr_b,value_c:arr_c,repeat:repeat}
            , type: "POST"
            , success: function (data, textStatus) {
                if (textStatus == "success") {
                    if(data.success){
                        alert(data.message);
                    }
                    else {
                        if(data.message != null) alert(data.message);
                        else alert("设置失败");
                    }
                }
            }
            , error:function(XMLHttpRequest, textStatus, errorThrown){
                console.log(textStatus)
                console.log(errorThrown)
            }
        }
    );
}