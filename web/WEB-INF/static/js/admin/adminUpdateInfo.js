$(function () {
    $("#admin_updateInfo").click(function () {

        if (!validUpdateAdminInfo()) {
            return;
        }

        var  postData = "name="+$.trim($("#name").val()) +"&phone="+$.trim($("#phone").val());
        //alert(postData);
        $.post("/adminInfoController_updateAdminInfo.action",postData,function (data) {
            if(data == 1){
                //关闭之前的模态框
                $("#updateinfo").modal("hide");
                //弹出修改成功的模态框
                showInfo("修改成功");
            }else if(data == -1){
                //关闭之前的模态框
                $("#updateinfo").modal("hide");
                showInfo("修改失败");
            }else{
                $("#updateinfo").modal("hide");
                showInfo("修改失败");
            }
        });
    });
    //什么时候刷新？
    //监听模态框的关闭
    $("#modal_info").on("hide.bs.modal",function () {
        location.reload();//刷新当前的页面
    });
});

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal("show");
}


function validUpdateAdminInfo() {
    var flag = true;

    var username = $.trim($("#username").val());
    if (username == "") {
        $('#username').parent().addClass("has-error");
        $('#username').next().text("请输入用户名");
        $("#username").next().show();
        flag = false;
    } else if (username.length < 2 || username.length > 15) {
        $("#username").parent().addClass("has-error");
        $("#username").next().text("用户名长度必须在2~15之间");
        $("#username").next().show();
        flag = false;
    } else {
        $('#username').parent().removeClass("has-error");
        $('#username').next().text("");
        $("#username").next().hide();
    }


    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var name = $.trim($("#name").val());
    if (name == "") {
        $('#name').parent().addClass("has-error");
        $('#name').next().text("请输入真实姓名");
        $("#name").next().show();
        flag = false;
    } else if (!reg.test(name)) {
        $('#name').parent().addClass("has-error");
        $('#name').next().text("真实姓名必须为中文");
        $("#name").next().show();
        flag = false;
    } else {
        $('#name').parent().removeClass("has-error");
        $('#name').next().text("");
        $("#name").next().hide();
    }

    var phone = $.trim($("#phone").val());
    if (phone == "") {
        $('#phone').parent().addClass("has-error");
        $('#phone').next().text("请输入联系号码");
        $("#phone").next().show();
        flag = false;
    } else if (!(/^1[34578]\d{9}$/.test(phone))) {
        //电话号码格式的校验
        $('#phone').parent().addClass("has-error");
        $('#phone').next().text("手机号码有误");
        $("#phone").next().show();
        return false;
    } else {
        $('#phone').parent().removeClass("has-error");
        $('#phone').next().text("");
        $("#phone").next().hide();
    }
    return flag;
}