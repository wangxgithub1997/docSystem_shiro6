//设置模态框的修改按钮点击事件
$(function () {
    $("#updateBookType").click(function () {
        if(!validUpdateBookType()){
            return;
        }
        var postData = "id="+$.trim($("#updateBookTypeId").val())+"&name="+$.trim($("#updateBookTypeName").val()) ;
        $.post("/admin/bookTypeManageController_updateBookType.action",postData,function (data) {
            if (data.code == 1) {
                $("#updateModal").modal("hide");
                showInfo("修改成功")
            }else {
                showInfo("修改失败");
            }
        });
    });

    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });
});

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

//先获取图书类型信息
function getBookTypeById(id) {
    $.post("/admin/bookTypeManageController_getBookType.action","id="+id,function (data) {
        //alert(data);
        //val 设置输入框
        if(data.code==1){
            $("#updateBookTypeId").val(data.data.id);
            $("#updateBookTypeName").val(data.data.name);
            $("#updateModal").modal("show");
        }else{
            alert(data.msg);
        }

    },"json");
}



function validUpdateBookType() {
    var flag = true;
    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var bookType = $.trim($("#updateBookTypeName").val());
    if (bookType == "") {
        $('#updateBookTypeName').parent().addClass("has-error");
        $('#updateBookTypeName').next().text("请输入图书分类名称");
        $("#updateBookTypeName").next().show();
        flag = false;
    } else if (!reg.test(bookType)) {
        $('#updateBookTypeName').parent().addClass("has-error");
        $('#updateBookTypeName').next().text("图书分类名称必须为中文");
        $("#updateBookTypeName").next().show();
        flag = false;
    } else {
        $('#updateBookTypeName').parent().removeClass("has-error");
        $('#updateBookTypeName').next().text("");
        $("#updateBookTypeName").next().hide();
    }
    return flag;
}