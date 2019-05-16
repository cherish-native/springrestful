var case_myArray = [];
var caseCodeArray = []

jQuery(function($){
    var selectedCodeCaseClass = [];
    $("#case-dialog").dialog({
        title:'请选择案件类别',
        width:1000,
        height:500,
        modal:true,
        buttons : [
            {
                text: "清空已选",
                handler : function () {
                    selectedCodeCaseClass = [];
                    $('#code_caseclass_selected_table').empty();
                }
            },
            {
                text: "确定",
                handler : function () {
                    var selected = "";
                    for(var i=0;i<selectedCodeCaseClass.length;i++){
                        selected += selectedCodeCaseClass[i].split("-")[0] + ",";
                    }
                    if(selected != ''){
                        alert(selected);
                        $('#case-dialog').dialog('close')
                    }else{
                        alert("已选案件类别为空")
                    }

                }
            },
            {
                text : "取消",
                handler : function () {
                    $('#case-dialog').dialog('close')
                }
            }
        ]
    });

    $('#code_caseclass_combo').combobox({
        prompt:'输入关键字后自动搜索',
        mode:'remote',
        url:'code/fuzzySearchCodeCaseClass',
        editable:true,
        hasDownArrow:false,
        valueField:'id',
        textField:'text',
        onBeforeLoad: function(param){
            if(param == null || param.q == null || param.q.replace(/ /g, '') == ''){
                var value = $(this).combobox('getValue');
                if(value){// 修改的时候才会出现q为空而value不为空
                    param.id = value;
                    return true;
                }
                return false;
            }
        },
        onSelect : function (param) {
            addCodeCaseClassToTable('code_caseclass_selected_table', param.text, param.id);
        }
    });

    /**
     * 添加行政区划到table
     * @param tableid
     * @param name
     * @param code
     */
    function addCodeCaseClassToTable(tableid, name, code) {
        if(inArray(selectedCodeCaseClass, code+"-"+name)){
            // alert('已选择')
            return;
        }else{
            selectedCodeCaseClass.push(code+"-"+name);
        }
        //计算以选择区域每一项的宽度
        var columnCount = parseInt(($('#hukou-dialog').width()/300)+'');
        var columnWidth = parseInt(($('#hukou-dialog').width()/columnCount+""));
        var lastTr = $('#'+ tableid +' tr:last');
        var curColumnCount = lastTr.find('td').length;
        if(curColumnCount != 0 && curColumnCount < columnCount){
            //未超过最大列数,最后一行添加一个td
            lastTr.append('<td style="width: '+ columnWidth +'px"><div code="' + code + '" class="area_select_td_item">' + name + '</div></td>');
        }else{
            //已超过最大列数，添加一行一列
            $('#'+ tableid).append('<tr><td style="width: '+ columnWidth +'px"><div code="' + code + '" class="area_select_td_item">' + name + '</div></td></tr>');
        }
        //找到添加的td添加双击监听
        var lastTd = $('#'+ tableid +' tr:last td:last');
        lastTd.on("dblclick",function(){
            removeCodeCaseClassFromTable(tableid, this);
        });
    }

    /**
     * 删除code
     * @param tableid
     * @param ele
     */
    function removeCodeCaseClassFromTable(tableid, ele) {
        var code = $(ele).find('div').attr('code');
        var name = $(ele).find('div').html();
        var newArray = [];
        for(var i=0;i<selectedCodeCaseClass.length;i++){
            if(selectedCodeCaseClass[i] != code+"-"+name){
                newArray.push(selectedCodeCaseClass[i]);
            }
        }
        selectedCodeCaseClass = [];
        $('#' + tableid).empty();
        for(var i=0;i<newArray.length;i++){
            var codeName = newArray[i].split("-");
            addCodeCaseClassToTable(tableid, codeName[1], codeName[0]);
        }
    }
    
    $('#case-dialog-button').on('click',function(e){
        $("#case-dialog").dialog("open"); // 打开dialog
        $('#case-dialog').window('center');

        //加载一级行政区划列表
        var parentCodeCaseClassList = listParentCodeCaseClass("-1");
        if(parentCodeCaseClassList.length > 0){
            var codeHtml = "";
            for(var i=0;i<parentCodeCaseClassList.length;i++){
                codeHtml += '<div class="area_select_item" code="'+ parentCodeCaseClassList[i].code +'">' + parentCodeCaseClassList[i].name + '</div>'
            }
            $('#code_caseclass_list').html(codeHtml);
            $('#code_caseclass_list .area_select_item').on("click",function(){
                $('#code_caseclass_children').empty();
                $('#code_caseclass_children3').empty();
                $('#code_caseclass_children4').empty();
                codeCaseClassItemClick('code_caseclass_children', this);
            });

            $('#code_caseclass_list .area_select_item').on("dblclick",function(){
                addCodeCaseClassToTable('code_caseclass_selected_table', $(this).html(), $(this).attr('code'));
            });
        }
    });

    function codeCaseClassItemClick(divid,ele){
        var parentCode = $(ele).attr('code');
        var childrenCodeAreaList = listParentCodeCaseClass(parentCode);
        if(childrenCodeAreaList.length > 0){
            var childrenHtml = "";
            for(var i=0;i<childrenCodeAreaList.length;i++){
                childrenHtml += '<div class="area_select_item" code="'+ childrenCodeAreaList[i].code +'">' + childrenCodeAreaList[i].name + '</div>'
            }
            $('#' + divid).empty();
            $('#' + divid).html(childrenHtml);
            $('#' + divid+ ' .area_select_item').on("click",function(){
                if(divid == 'code_caseclass_children'){
                    codeCaseClassItemClick('code_caseclass_children3', this);
                }else{
                    codeCaseClassItemClick('code_caseclass_children4', this);
                }

            });
            $('#' + divid + ' .area_select_item').on("dblclick",function(){
                addCodeCaseClassToTable('code_caseclass_selected_table', $(this).html(), $(this).attr('code'));
            });
        }
    }

    function listParentCodeCaseClass(parentCode){
        var result = [];
        $.ajax(
            "code/listCodeCaseClass"
            , {
                async: false,
                dataType: "json",
                data: {
                    'parentCode' : parentCode
                }
                , type: "POST"
                , success: function (data) {
                    result = data;
                }
                , error:function(XMLHttpRequest, textStatus, errorThrown){
                    console.log(textStatus)
                    console.log(errorThrown)
                }

            }
        );
        return result;
    }



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


