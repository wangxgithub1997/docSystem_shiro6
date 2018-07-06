$(function () {
    $("#update_adminPwd").click(function () {
        var postData = "oldPwd="+$.trim($("#oldPwd").val())+"&newPwd="+$.trim($("#newPwd").val())+"&confirmPwd="+$.trim($("#confirmPwd").val());
        //alert(postData)
        $.post("/adminInfoController_updateAdminPasswrod.action",postData,function (data) {
            if(data == 1){
                $("#updatepwd").modal("hide");
                showInfo("修改成功");
                window.location.href = "/adminLogin.jsp";
            }else if(data == -1){
                showInfo("原密码错误");
            }else if(data == -2){
                showInfo("确认密码不一致");
            }else{
                showInfo("修改失败");
            }
        });
    });
});
function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal("show");
}