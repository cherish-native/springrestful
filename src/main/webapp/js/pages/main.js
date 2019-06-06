/**
 * @author yuchen_19997_200486@126.com
 * @date 2019-03
 * about menuLoad & loginOut function
 */
jQuery(function($){


    $(document).on('click','#signOut',function(event){
        signOut()
    })
})


function getUserInfo(){
    $.ajax(
        "user/getUserInfo"
        , {
            async: true
            , cache: false
            , dataType: "json"
            , data: {}
            , type: "GET"
            , success: function (data, textStatus, xhr) {
                if (textStatus == "success") {
                    if(data.success){
                        $('#loginUser').text(data.rows.userName)
                    }
                    else {
                        window.location.href = "index.html"
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

function signOut() {
    $.ajax(
        "user/loginOut"
        , {
            async: true
            , cache: false
            , dataType: "json"
            , data: {}
            , type: "POST"
            , success: function (data, textStatus, xhr) {
                if (textStatus == "success") {
                    if(data.success){
                        window.location.href = "index.html"
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