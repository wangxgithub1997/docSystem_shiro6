/**
 * 添加读者
 */
$(function () {
    //弹出添加读者模态框
    $("#btn_add").click(function () {
        $.get("/admin/readerTypeManageController_getAllReaderTypes.action", function (data) {
            // 循环遍历每个读者分类，每个名称生成一个option对象，添加到<select>中
            var $addreaderType = $("#addreaderType");
            for (var index in data) {
                $addreaderType.append(
                    "<option value=" + data[index].id + " >" + data[index].name + "</option>");
            }
            $("#addModal").modal("show");
        }, "json");
    });
    //添加
    $('#addReader').click(function () {
        if (!validAddReader()) {
            return;
        }
        var postData =
            "paperNO=" + $.trim($("#addPaperNO").val())
            + "&name=" + $.trim($("#addName").val())
            + "&phone=" + $.trim($("#addPhone").val())
            + "&readerType=" + $.trim($("#addreaderType").val())
            + "&email=" + $.trim($("#addEmail").val());
        $.post('/admin/readerManageController_addReader.action',
            postData,
            function (data) {
                if (data == 1) {
                    $("#addModal").modal("hide");//关闭模糊框
                    showInfo("添加成功");
                } else if (data == -1) {
                    $("#addModal").modal("hide");//关闭模糊框
                    showInfo("该读者已存在");
                } else {
                    $("#addModal").modal("hide");//关闭模糊框
                    showInfo("添加失败");
                }
            }, "json");
    });
    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });
});


function validAddReader() {
    var flag = true;
    var paperNO = $.trim($("#addPaperNO").val());
    if (paperNO == "") {
        $('#addPaperNO').parent().addClass("has-error");
        $('#addPaperNO').next().text("请输入读者证件号");
        $("#addPaperNO").next().show();
        flag = false;
    } else {
        $('#addPaperNO').parent().removeClass("has-error");
        $('#addPaperNO').next().text("");
        $("#addPaperNO").next().hide();
    }
    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var name = $.trim($("#addName").val());
    if (name == "") {
        $('#addName').parent().addClass("has-error");
        $('#addName').next().text("请输入真实姓名");
        $("#addName").next().show();
        flag = false;
    } else if (!reg.test(name)) {
        $('#addName').parent().addClass("has-error");
        $('#addName').next().text("真实姓名必须为中文");
        $("#addName").next().show();
        flag = false;
    } else {
        $('#addName').parent().removeClass("has-error");
        $('#addName').next().text("");
        $("#addName").next().hide();
    }

    var phone = $.trim($("#addPhone").val());
    if (phone == "") {
        $('#addPhone').parent().addClass("has-error");
        $('#addPhone').next().text("请输入联系号码");
        $("#addPhone").next().show();
        flag = false;
    } else if (!(/^1[34578]\d{9}$/.test(phone))) {
        //电话号码格式的校验
        $('#addPhone').parent().addClass("has-error");
        $('#addPhone').next().text("手机号码有误");
        $("#addPhone").next().show();
        return false;
    } else {
        $('#addPhone').parent().removeClass("has-error");
        $('#addPhone').next().text("");
        $("#addPhone").next().hide();
    }

    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    var email = $.trim($("#addEmail").val());
    if (email == "") {
        $('#addEmail').parent().addClass("has-error");
        $('#addEmail').next().text("请输入邮箱");
        $("#addEmail").next().show();
        flag = false;
    } else if (!reg.test(email)) {
        //邮箱格式的校验
        $('#addEmail').parent().addClass("has-error");
        $('#addEmail').next().text("邮箱格式有误");
        $("#addEmail").next().show();
        return false;
    } else {
        $('#addEmail').parent().removeClass("has-error");
        $('#addEmail').next().text("");
        $("#addEmail").next().hide();
    }
    var readerType = $.trim($("#addreaderType").val());
    if (readerType == -1) {
        $('#addreaderType').parent().addClass("has-error");
        $('#addreaderType').next().text("请选择读者类型");
        $("#addreaderType").next().show();
        flag = false;
    } else {
        $('#addreaderType').parent().removeClass("has-error");
        $('#addreaderType').next().text("");
        $("#addreaderType").next().hide();
    }
    return flag;
}

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}