/**
 * @author yuchen_19997_200486@126.com
 * @date 2019-03
 * loginIn
 */

jQuery(function($) {

    $('#username').focus()
    $(document).on('keydown','#username',function(event){
        if (event.keyCode == "13"){
            if($('#username').val().trim()==""){
                alert("请输入用户名")
                $('#username').focus()
                return
            }else{
                $('#password').focus()
            }
        }
    })


    $(document).on('keydown','#password',function(event){
        if (event.keyCode == "13"){
            if($('#password').val().trim()==""){
                alert("请输入密码")
                $('#password').focus()
                return
            }else{
                $('#login-a').focus()
            }
        }

    })

    $(document).on('keydown','#login-a',function(e){
        if (event.keyCode == "13"){
            login($('#username').val(),$('#password').val())
        }
    })


    $(document).on('click','#login-a',function(e){
        login($('#username').val(),$('#password').val())
    })

});

function login(username,password){
    if($('#username').val().trim() =="" && $('#password').val().trim() == ""){
        alert("用户名或密码,不能为空")
        $('#username').focus()
        return
    }else{
        $.ajax(
            "/pages/user/login"
            , {
                async: true
                , cache: false
                , dataType: "json"
                , data: {username:username,password:password}
                , type: "GET"
                , success: function (data, textStatus, xhr) {
                    if (textStatus == "success") {
                        if(data.success){
                            window.location.href = "../../pages/main.html"
                        }
                        else {
                            alert(data.message);
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
}