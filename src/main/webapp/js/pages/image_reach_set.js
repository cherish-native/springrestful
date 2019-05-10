/**
 * @date 2019-05
 * loginIn
 */

jQuery(function($) {

    $(document).on('click','#set-score-btn',function(e){
        set_score()
    })

    $(document).on('click','#set-image-reach-btn',function(e){
        set_image_reach()
    })

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

    $.ajax(
       "/pages/image/levelScore"
       , {
           async: false
           , cache: false
           , dataType: "json"
           , data: {yx_min:yx_min,yx_max:yx_max,lh_min:lh_min,lh_max:lh_max,yb_min:yb_min,yb_max:yb_max,jc_min:jc_min,jc_max:jc_max,hc_min:hc_min,hc_max:hc_max}
           , type: "POST"
           , success: function (data, textStatus) {
               if (textStatus == "success") {
                   if(data.success){
                       alert("图形质量范围等级设置成功");
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

    $.ajax(
        "/pages/image/fingerLevel"
        , {
            async: false
            , cache: false
            , dataType: "json"
            , data: {value_a:arr_a,value_b:arr_b,value_c:arr_c}
            , type: "POST"
            , success: function (data, textStatus) {
                if (textStatus == "success") {
                    if(data.success){
                        alert("图形质量范围等级设置成功");
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