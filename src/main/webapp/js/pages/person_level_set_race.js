
var race_myArray = [];
var raceCodeArray = []

jQuery(function($){


	$('#race-dialog-button').on('click',function(e){
		e.preventDefault();

        var dialog = $( "#race-dialog" ).removeClass('hide').dialog({
            modal: true,
            title: "请选择民族",
            height : "450",
            width : "1000"
        });
	})




	$('#okBtn-race').on('click',function(e){
        e.preventDefault();
        if($('#selected-race').html() != ''){
            $('#race-input').val('')
            $('#race-input').val($('#selected-race').html().split(":")[1])
            init();
            $('#race-dialog').dialog("destroy");
        }
    })


    $('#closeBtn-race').on('click',function(e){
        e.preventDefault();
        init();
        raceCodeArray = [];
        $('#race-input').val('请选择');
        $('#race-dialog').dialog("destroy");
    })


    function init(){
        toggleClass();
        race_myArray = [];
        $("#race-btn1").text('').val('').css("display","none")
        $("#race-btn2").text('').val('').css("display","none")
        $("#race-btn3").text('').val('').css("display","none")
        $("#race-btn4").text('').val('').css("display","none")
        $("#race-btn5").text('').val('').css("display","none")
        $("#race-btn6").text('').val('').css("display","none")
        $("#race-btn7").text('').val('').css("display","none")
        $("#race-btn8").text('').val('').css("display","none")
        $("#race-btn9").text('').val('').css("display","none")
        $("#race-btn10").text('').val('').css("display","none")
        $("#race-btn11").text('').val('').css("display","none")
        $("#race-btn12").text('').val('').css("display","none")
        $('#selected-race').html('')
    }



    var  inArray = function(arr, item) {
        for(var i = 0; i < arr.length; i++) {
            if(arr[i] == item) {
                return true;
            }
        }
        return false;
    };



    $('#optionBtn-race button').each(function(){
        $(this).on('click',function(e){

            if(inArray(race_myArray,$(this).html())){
                race_myArray.splice(race_myArray.indexOf($(this).html()),1)
                $(this).removeClass('btn btn-warning').addClass('btn')
                loadSelectedStr()

                //
                raceCodeArray.splice(raceCodeArray.indexOf($(this).val()),1)
            }
            else{
                race_myArray.push($(this).html())
                $(this).removeClass('btn').addClass('btn btn-warning')
                loadSelectedStr()

                raceCodeArray.push($(this).val())
            }

        })
    })


    function loadSelectedStr(){
        selectedStr = "您已经选择:"
        for(var i = 0; i < race_myArray.length ; i++){
            selectedStr += race_myArray[i] + ","
        }
        $('#selected-race').html(selectedStr)
    }


    function toggleClass(){
        $('#optionBtn-race button').each(function(){
            $(this).removeClass('btn btn-warning').addClass('btn')
        })
    }



    function a(){
        $('#optionBtn-race button').each(function(){
            if(inArray(race_myArray,$(this).html())){
                $(this).removeClass('btn').addClass('btn btn-warning')
            }
        })
    }




	$("#ad").on('click',function(e){
		toggleClass()
        $("#race-btn1").text('阿昌族').val("39").css("display","")
        $("#race-btn2").text('布依族').val("09").css("display","")
        $("#race-btn3").text('白族').val("14").css("display","")
        $("#race-btn4").text('布朗族').val("34").css("display","")
        $("#race-btn5").text('保安族').val("47").css("display","")
        $("#race-btn6").text('朝鲜族').val("10").css("display","")
        $("#race-btn7").text('侗族').val("12").css("display","")
        $("#race-btn8").text('傣族').val("18").css("display","")
        $("#race-btn9").text('东乡族').val("26").css("display","")
        $("#race-btn10").text('达斡尔族').val("31").css("display","")
        $("#race-btn11").text('德昂族').val("46").css("display","")
        $("#race-btn12").text('独龙族').val("51").css("display","")
        a()
    });

    $('#ej').on('click',function(e){
    	toggleClass()
    	$("#race-btn1").text('俄罗斯族').val("44").css("display","")
        $("#race-btn2").text('鄂温克族').val("45").css("display","")
        $("#race-btn3").text('鄂伦春族').val("52").css("display","")
        $("#race-btn4").text('高山族').val("23").css("display","")
        $("#race-btn5").text('汉族').val("01").css("display","")
        $("#race-btn6").text('回族').val("03").css("display","")
        $("#race-btn7").text('哈尼族').val("16").css("display","")
        $("#race-btn8").text('哈萨克族').val("17").css("display","")
        $("#race-btn9").text('赫哲族').val("53").css("display","")
        $("#race-btn10").text('景颇族').val("28").css("display","")
        $("#race-btn11").text('京族').val("49").css("display","")
        $("#race-btn12").text('基诺族').val("56").css("display","")
        a()
    });

    $('#km').on('click',function(e){
    	toggleClass()
    	$("#race-btn1").text('柯尔克孜族').val("29").css("display","")
        $("#race-btn2").text('黎族').val("19").css("display","")
        $("#race-btn3").text('傈傈族').val("20").css("display","")
        $("#race-btn4").text('拉祜族').val("24").css("display","")
        $("#race-btn5").text('珞巴族').val("55").css("display","")
        $("#race-btn6").text('蒙古族').val("02").css("display","")
        $("#race-btn7").text('苗族').val("06").css("display","")
        $("#race-btn8").text('满族').val("11").css("display","")
        $("#race-btn9").text('仫佬族').val("32").css("display","")
        $("#race-btn10").text('毛南族').val("36").css("display","")
        $("#race-btn11").text('门巴族').val("54").css("display","")
        $("#race-btn12").text('').val("").css("display","none")
        a()
    })

    $('#nt').on('click',function(e){
    	toggleClass()
    	$("#race-btn1").text('纳西族').val("27").css("display","")
        $("#race-btn2").text('怒族').val("42").css("display","")
        $("#race-btn3").text('普米族').val("40").css("display","")
        $("#race-btn4").text('羌族').val("33").css("display","")
        $("#race-btn5").text('畲族').val("22").css("display","")
        $("#race-btn6").text('水族').val("25").css("display","")
        $("#race-btn7").text('撒拉族').val("35").css("display","")
        $("#race-btn8").text('土家族').val("15").css("display","")
        $("#race-btn9").text('土族').val("30").css("display","")
        $("#race-btn10").text('塔吉克族').val("41").css("display","")
        $("#race-btn11").text('塔塔尔族').val("50").css("display","")
        $("#race-btn12").text('').val("").css("display","none")
        a()
    })

    $('#wz').on('click',function(e){
    	toggleClass()
    	$("#race-btn1").text('维吾尔族').val("05").css("display","")
        $("#race-btn2").text('佤族').val("21").css("display","")
        $("#race-btn3").text('乌孜别克族').val("43").css("display","")
        $("#race-btn4").text('锡伯族').val("38").css("display","")
        $("#race-btn5").text('彝族').val("07").css("display","")
        $("#race-btn6").text('瑶族').val("13").css("display","")
        $("#race-btn7").text('仡佬族').val("37").css("display","")
        $("#race-btn8").text('裕固族').val("48").css("display","")
        $("#race-btn9").text('藏族').val("04").css("display","")
        $("#race-btn10").text('壮族').val("08").css("display","")
        $("#race-btn11").text('').val("").css("display","none")
        $("#race-btn12").text('').val("").css("display","none")
        a()
    })

})