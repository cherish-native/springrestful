/**
 * @author yuchen_19997_200486@126.com
 * @date 2019-03
 * LevelSet
 */

var myArray = [];
var addressCodeArray = []

var regex = /^[0-9]+.?[0-9]*$/;

jQuery(function($){
    var selectedCodeArea = [];
    $("#hukou-dialog").dialog({
        title:'请选择户籍地',
        width:1000,
        height:$('body').height() - 60,
        modal:true,
        buttons : [
            {
                text: "清空已选",
                handler : function () {
                    selectedCodeArea = [];
                    $('#code_area_selected_table').empty();
                }
            },
            {
                text: "确定",
                handler : function () {
                    var selected = "";
                    for(var i=0;i<selectedCodeArea.length;i++){
                        selected += selectedCodeArea[i].split("-")[0] + ",";
                    }
                    if(selected != ''){
                        alert(selected);
                        $('#hukou-dialog').dialog('close')
                    }else{
                       alert("已选户籍地为空")
                    }

                }
            },
            {
                text : "取消",
                handler : function () {
                    $('#hukou-dialog').dialog('close')
                }
            }
        ]
    });

    $('#code_area_combo').combobox({
        prompt:'输入关键字后自动搜索',
        mode:'remote',
        url:'code/fuzzySearchCodeArea',
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
            addCodeAreaToTable('code_area_selected_table', param.text, param.id);
        }
    });

    $( "#hukou-dialog-button" ).on('click', function(e) {
        // e.preventDefault();
        $("#hukou-dialog").dialog("open"); // 打开dialog
        $('#hukou-dialog').window('center');

        //加载一级行政区划列表
        var parentCodeAreaList = listParentCodeArea("");
        if(parentCodeAreaList.length > 0){
            var codeHtml = "";
            for(var i=0;i<parentCodeAreaList.length;i++){
                codeHtml += '<div class="area_select_item" fullname="' + parentCodeAreaList[i].name + '" code="'+ parentCodeAreaList[i].code +'">' + parentCodeAreaList[i].name + '</div>'
            }
            $('#code_area_list').html(codeHtml);
            $('#code_area_list .area_select_item').on("click",function(){
                $('#code_area_children').empty();
                $('#code_area_children_3').empty();
                codeAreaItemClick('code_area_children', this);
            });

            $('#code_area_list .area_select_item').on("dblclick",function(){
                addCodeAreaToTable('code_area_selected_table', $(this).attr('fullname'), $(this).attr('code'));
            });
        }
    });

    function codeAreaItemClick(divid,ele){
        var parentCode = $(ele).attr('code');
        var childrenCodeAreaList = listParentCodeArea(parentCode);
        if(childrenCodeAreaList.length > 0){
            var childrenHtml = "";
            for(var i=0;i<childrenCodeAreaList.length;i++){
                childrenHtml += '<div class="area_select_item" fullname="' + childrenCodeAreaList[i].name + '" code="'+ childrenCodeAreaList[i].code +'">' + childrenCodeAreaList[i].singleName + '</div>'
            }
            $('#' + divid).empty();
            $('#' + divid).html(childrenHtml);
            $('#' + divid+ ' .area_select_item').on("click",function(){
                codeAreaItemClick('code_area_children_3', this);
            });
            $('#' + divid + ' .area_select_item').on("dblclick",function(){
                addCodeAreaToTable('code_area_selected_table', $(this).attr('fullname'), $(this).attr('code'));
            });
        }
    }

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
            if(!isSetLevel()){
                alert("所有设置项均未选择,所有被捺印人默认均为C级")
            }
            return
        }

        $.ajax(
            "/pages/main/level/personLevelSet"
            , {
                async: false
                , cache: false
                , dataType: "json"
                , data: params
                , type: "POST"
                , success: function (data, textStatus, xhr) {
                    if (textStatus == "success") {
                        if(data.success){
                            alert(data.message)
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
    });

    /**
     * 加载第一级行政区划代码
     */
    function listParentCodeArea(parentCode) {
        var result = [];
        $.ajax(
            "code/listCodeAreaByParentCode"
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

    /**
     * 添加行政区划到table
     * @param tableid
     * @param name
     * @param code
     */
    function addCodeAreaToTable(tableid, name, code) {
        if(inArray(selectedCodeArea, code+"-"+name)){
            // alert('已选择')
            return;
        }else{
            selectedCodeArea.push(code+"-"+name);
        }
        //计算以选择区域每一项的宽度
        var columnCount = parseInt(($('#hukou-dialog').width()/200)+'');
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
            removeCodeFromTable(tableid, this);
        });
    }

    /**
     * 删除code
     * @param tableid
     * @param ele
     */
    function removeCodeFromTable(tableid, ele) {
        var code = $(ele).find('div').attr('code');
        var name = $(ele).find('div').html();
        var newArray = [];
        for(var i=0;i<selectedCodeArea.length;i++){
            if(selectedCodeArea[i] != code+"-"+name){
                newArray.push(selectedCodeArea[i]);
            }
        }
        selectedCodeArea = [];
        $('#' + tableid).empty();
        for(var i=0;i<newArray.length;i++){
            var codeName = newArray[i].split("-");
            addCodeAreaToTable(tableid, codeName[1], codeName[0]);
        }
    }

    function isInArray(arr, value){
        for(var i=0;i<arr.length;i++){
            if(value == arr[i]){
                return true;
            }
        }
        return false;
    }
})

function isSetLevel(){

    var bStr = false
    $.ajax(
        "/pages/main/level/isSetPersonLevel"
        , {
            async: false
            , cache: false
            , dataType: "json"
            , type: "GET"
            , success: function (data, textStatus, xhr) {
                if (textStatus == "success") {
                    if(data.success){
                        if(data.result > 0){
                            bStr = true
                        }
                    }else {
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
    return bStr;
}