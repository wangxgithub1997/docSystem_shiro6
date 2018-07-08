$(function () {
     $("#login_submit").click(function () {
         var postData = "paperNO="+$.trim($("#username").val()) + "&password="+$.trim($("#password").val());
         $.post("/reader/readerLoginController_login.action",postData,function (data) {
             if(data == 1){
                 window.location.href = "/reader/toIndex";
             }else if(data == -1){
                 showInfo("账号不存在");
             }else if(data == -2){
                 showInfo("密码错误");
            }
         });
     });

    //测试代码login_submit2
   /* $("#login_submit2").click(function () {
        var postData = "paperNO="+$.trim($("#username").val()) + "&password="+$.trim($("#password").val());
           ajax({
               method:"post",
               url:"/reader/readerLoginController_login.action",
               params:postData,
               callback:function (data) {
                   if(data == 1){
                       window.location.href = "/reader/toIndex";
                   }else if(data == -1){
                       showInfo("账号不存在");
                   }else if(data == -2){
                       showInfo("密码错误");
                   }
               }
           });
    });

*/

});
function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal("show");
}