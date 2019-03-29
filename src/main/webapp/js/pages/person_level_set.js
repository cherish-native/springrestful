

var myArray = [];
var addressCodeArray = []

var regex = /^[0-9]+.?[0-9]*$/;

jQuery(function($){
    $( "#hukou-dialog-button" ).on('click', function(e) {
        e.preventDefault();

        var dialog = $( "#hukou-dialog" ).removeClass('hide').dialog({
            modal: true,
            title: "请选择户籍地",
            height : "510",
            width : "1000"
        });
        /**
         dialog.data( "uiDialog" )._title = function(title) {
								title.html( this.options.title );
							};
         **/
    });

    $('#okBtn').on('click',function(e){
        e.preventDefault();
        if($('#selected').html() != ''){
            $('#hukou-input').val('')
            $('#hukou-input').val($('#selected').html().split(":")[1])
            init();
            $('#hukou-dialog').dialog("destroy");
        }
    })


    $('#closeBtn').on('click',function(e){
        e.preventDefault();
        init();
        addressCodeArray = [];
        $('#hukou-input').val('请选择');
        $('#hukou-dialog').dialog("destroy");
    })


    function init(){
        toggleClass();
        myArray = [];
        $("#btn1").text('').val('').css("display","none")
        $("#btn2").text('').val('').css("display","none")
        $("#btn3").text('').val('').css("display","none")
        $("#btn4").text('').val('').css("display","none")
        $("#btn5").text('').val('').css("display","none")
        $("#btn6").text('').val('').css("display","none")
        $("#btn7").text('').val('').css("display","none")
        $('#selected').html('')
    }



    var  inArray = function(arr, item) {
        for(var i = 0; i < arr.length; i++) {
            if(arr[i] == item) {
                return true;
            }
        }
        return false;
    };



    $('#optionBtn button').each(function(){
        $(this).on('click',function(e){

            if(inArray(myArray,$(this).html())){
                myArray.splice(myArray.indexOf($(this).html()),1)
                $(this).removeClass('btn btn-warning').addClass('btn')
                loadSelectedStr()

                //
                addressCodeArray.splice(addressCodeArray.indexOf($(this).val()),1)
            } else{
                myArray.push($(this).html())
                $(this).removeClass('btn').addClass('btn btn-warning')
                loadSelectedStr()

                addressCodeArray.push($(this).val())
            }

        })
    })


    function loadSelectedStr(){
        selectedStr = "您已经选择:"
        for(var i = 0; i < myArray.length ; i++){
            selectedStr += myArray[i] + ","
        }
        $('#selected').html(selectedStr)
    }


    function toggleClass(){
        $('#optionBtn button').each(function(){
            $(this).removeClass('btn btn-warning').addClass('btn')
        })
    }



    function a(){
        $('#optionBtn button').each(function(){
            if(inArray(myArray,$(this).html())){
                $(this).removeClass('btn').addClass('btn btn-warning')
            }
        })
    }

    $("#huabei").on('click',function(e){
        toggleClass()
        $("#btn1").text('北京').val("11").css("display","")
        $("#btn2").text('天津').val("12").css("display","")
        $("#btn3").text('河北').val("13").css("display","")
        $("#btn4").text('山西').val("14").css("display","")
        $("#btn5").text('内蒙古').val("15").css("display","")
        $("#btn6").text('').css("display","none")
        $("#btn7").text('').css("display","none")
        a()
    });

    $("#dongbei").on('click',function(e){
        toggleClass()
        $("#btn1").text('辽宁').val("21").css("display","")
        $("#btn2").text('吉林').val("22").css("display","")
        $("#btn3").text('黑龙江').val("23").css("display","")
        $("#btn4").text('').css("display","none")
        $("#btn5").text('').css("display","none")
        $("#btn6").text('').css("display","none")
        $("#btn7").text('').css("display","none")
        a()
    });

    $("#huadong").on('click',function(e){
        toggleClass()
        $("#btn1").text('上海').val("31").css("display","")
        $("#btn2").text('江苏').val("32").css("display","")
        $("#btn3").text('浙江').val("33").css("display","")
        $("#btn4").text('安徽').val("34").css("display","")
        $("#btn5").text('福建').val("35").css("display","")
        $("#btn6").text('江西').val("36").css("display","")
        $("#btn7").text('山东').val("37").css("display","")
        a()
    });

    $("#huazhonghuanan").on('click',function(e){
        toggleClass()
        $("#btn1").text('河南').val("41").css("display","")
        $("#btn2").text('湖北').val("42").css("display","")
        $("#btn3").text('湖南').val("43").css("display","")
        $("#btn4").text('广东').val("44").css("display","")
        $("#btn5").text('广西').val("45").css("display","")
        $("#btn6").text('海南').val("46").css("display","")
        $("#btn7").text('').css("display","none")
        a()
    });

    $("#xinan").on('click',function(e){
        toggleClass()
        $("#btn1").text('重庆').val("50").css("display","")
        $("#btn2").text('四川').val("51").css("display","")
        $("#btn3").text('贵州').val("52").css("display","")
        $("#btn4").text('云南').val("53").css("display","")
        $("#btn5").text('西藏').val("54").css("display","")
        $("#btn6").text('').css("display","none")
        $("#btn7").text('').css("display","none")
        a()
    });

    $("#xibei").on('click',function(e){
        toggleClass()
        $("#btn1").text('陕西').val("61").css("display","")
        $("#btn2").text('甘肃').val("62").css("display","")
        $("#btn3").text('青海').val("63").css("display","")
        $("#btn4").text('宁夏').val("64").css("display","")
        $("#btn5").text('新疆').val("65").css("display","")
        $("#btn6").text('兵团').val("66").css("display","")
        $("#btn7").text('').css("display","none")

        a()
    });

    $("#gangaotai").on('click',function(e){
        toggleClass()
        $("#btn1").text('台湾').val("70").css("display","")
        $("#btn2").text('香港').val("80").css("display","")
        $("#btn3").text('澳门').val("90").css("display","")
        $("#btn4").text('').css("display","none")
        $("#btn5").text('').css("display","none")
        $("#btn6").text('').css("display","none")
        $("#btn7").text('').css("display","none")
        a()
    });

    $('#min-age').on('blur',function (e) {
        if(($('#min-age').val().trim().length>0) && !regex.test($('#min-age').val())){
            alert("年龄请输入数字")
            $('#min-age').val('')
            $('#min-age').focus()
            return
        }
    })

    $('#max-age').on('blur',function (e) {
        if(($('#max-age').val().trim().length>0) &&!regex.test($('#max-age').val())){
            alert("年龄请输入数字")
            $('#max-age').val('')
            $('#max-age').focus()
            return
        }
    })
    
    
    $('#set-btn').on('click',function (e) {

        var strAddressCode = ""
        var strRaceCode = ""
        var strCaseCode = ""
        var strGender = ""
        var strMinAge = ""
        var strMaxAge = ""
        var strCriminalRecord = ""

        if(addressCodeArray.length > 0){
            var length = addressCodeArray.length
            for(var i=0; i<length;i++){
                strAddressCode += addressCodeArray[i] + ","
            }
        }

        if(raceCodeArray.length > 0){
            var length = raceCodeArray.length
            for(var i=0;i<length;i++){
                strRaceCode += raceCodeArray[i]+ ","
            }
        }

        if(caseCodeArray.length > 0){
            var length = caseCodeArray.length
            for(var i=0;i<length;i++){
                strCaseCode += caseCodeArray[i]+ ","
            }
        }

        if($('#gender').val() != '-1'){
            strGender = $('#gender').val()
        }

        if($('#min-age').val().trim() != ''){
            strMinAge = $('#min-age').val().trim()
        }

        if($('#max-age').val().trim() != ''){
            strMaxAge = $('#max-age').val().trim()
        }

        if($('#criminal-record-input').val() != '-1'){
            strCriminalRecord = $('#criminal-record-input').val()
        }


        var params = {
            addressCode:strAddressCode,
            raceCode:strRaceCode,
            caseCode:strCaseCode,
            gender:strGender,
            minAge:strMinAge,
            maxAge:strMaxAge,
            criminalRecord:strCriminalRecord
        }

        if(strAddressCode.length <=0
            && strRaceCode.length <=0
            && strCaseCode.length <=0
            && strGender.length <=0
            && strMinAge.length <=0
            && strMaxAge.length <=0
            && strCriminalRecord.length <=0){
            alert("设置完成! 所有设置项均未选择,所有被捺印人均为C级")
            return
        }

        $.ajax(
            "/pages/main/level/personLevelSet"
            , {
                async: true
                , cache: false
                , dataType: "json"
                , data: params
                , type: "POST"
                , success: function (data, textStatus, xhr) {
                    if (textStatus == "success") {
                        if(data.success){
                            alert("设置成功")
                            window.location.reload()
                        }
                        else {
                            alert(data.message)
                        }
                    }
                }
                , error:function(XMLHttpRequest, textStatus, errorThrown){
                    console.log(textStatus)
                    console.log(errorThrown)
                }

            }
        );
    })

})