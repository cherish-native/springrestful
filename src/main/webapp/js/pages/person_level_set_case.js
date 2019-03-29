var case_myArray = [];
var caseCodeArray = []

jQuery(function($){
    $('#case-dialog-button').on('click',function(e){
        e.preventDefault();

        var dialog = $( "#case-dialog" ).removeClass('hide').dialog({
            modal: true,
            title: "请选择案件",
            height : "700",
            width : "1000"
        });
    })



    $('#okBtn-case').on('click',function(e){
        e.preventDefault();
        if($('#selected-case').html() != ''){
            $('#case-input').val('')
            $('#case-input').val($('#selected-case').html().split(":")[1])
            init();
            $('#case-dialog').dialog("destroy");
        }
    })


    $('#closeBtn-case').on('click',function(e){
        e.preventDefault();
        init();
        caseCodeArray = [];
        $('#case-input').val('请选择');
        $('#case-dialog').dialog("destroy");
    })


    function init(){
        toggleClass();
        case_myArray = [];
        $("#case-btn1").text('').val('').css("display","none")
        $("#case-btn2").text('').val('').css("display","none")
        $("#case-btn3").text('').val('').css("display","none")
        $("#case-btn4").text('').val('').css("display","none")
        $("#case-btn5").text('').val('').css("display","none")
        $("#case-btn6").text('').val('').css("display","none")
        $("#case-btn7").text('').val('').css("display","none")
        $("#case-btn8").text('').val('').css("display","none")
        $("#case-btn9").text('').val('').css("display","none")
        $("#case-btn10").text('').val('').css("display","none")
        $("#case-btn11").text('').val('').css("display","none")
        $("#case-btn12").text('').val('').css("display","none")
        $("#case-btn13").text('').val('').css("display","none")
        $("#case-btn14").text('').val('').css("display","none")
        $("#case-btn15").text('').val('').css("display","none")
        $('#selected-case').html('')
    }



    var  inArray = function(arr, item) {
        for(var i = 0; i < arr.length; i++) {
            if(arr[i] == item) {
                return true;
            }
        }
        return false;
    };



    $('#optionBtn-case button').each(function(){
        $(this).on('click',function(e){

            if(inArray(case_myArray,$(this).html())){
                case_myArray.splice(case_myArray.indexOf($(this).html()),1)
                $(this).removeClass('btn btn-warning').addClass('btn')
                loadSelectedStr()

                //
                caseCodeArray.splice(caseCodeArray.indexOf($(this).val()),1)
            }
            else{
                case_myArray.push($(this).html())
                $(this).removeClass('btn').addClass('btn btn-warning')
                loadSelectedStr()

                caseCodeArray.push($(this).val())
            }

        })
    })


    function loadSelectedStr(){
        selectedStr = "您已经选择:"
        for(var i = 0; i < case_myArray.length ; i++){
            selectedStr += case_myArray[i] + ","
        }
        $('#selected-case').html(selectedStr)
    }


    function toggleClass(){
        $('#optionBtn-case button').each(function(){
            $(this).removeClass('btn btn-warning').addClass('btn')
        })
    }



    function a(){
        $('#optionBtn-case button').each(function(){
            if(inArray(case_myArray,$(this).html())){
                $(this).removeClass('btn').addClass('btn btn-warning')
            }
        })
    }

    $("#qinfan_caichan_an").on('click',function(e){
        toggleClass()
        $("#case-btn1").text('抢劫案').val("05000100").css("display","")
        $("#case-btn2").text('盗窃案').val("05000200").css("display","")
        $("#case-btn3").text('诈骗案').val("05000300").css("display","")
        $("#case-btn4").text('抢夺案').val("05000400").css("display","")
        $("#case-btn5").text('聚众哄抢案').val("05000500").css("display","")
        $("#case-btn6").text('侵占案').val("05000600").css("display","")
        $("#case-btn7").text('职务侵占案').val("05000700").css("display","")
        $("#case-btn8").text('挪用资金案').val("05000800").css("display","")
        $("#case-btn9").text('挪用特定款物案').val("05000900").css("display","")
        $("#case-btn10").text('敲诈勒索案').val("05001000").css("display","")
        $("#case-btn11").text('故意毁坏财物案').val("05001100").css("display","")
        $("#case-btn12").text('破坏生产经营案').val("05001200").css("display","")
        $("#case-btn13").text('拒不支付劳动报酬案').val("05001300").css("display","")
        $("#case-btn14").text('').val("").css("display","none")
        $("#case-btn15").text('').val("").css("display","none")
        a()
    });
})


