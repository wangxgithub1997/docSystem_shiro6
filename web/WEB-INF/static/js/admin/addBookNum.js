function addBookNum(id,isbn) {
    $("#addBookNumId").val(id);
    $("#addBookNumISBN").val(isbn);
    $("#addNumModal").modal("show");
}

$(function () {
    $("#add_BookNum").click(function () {
        if (!validAddBookNum()) {
            return;
        }
        var postData = "bookId=" + $.trim($("#addBookNumId").val()) + "&num=" + $.trim($("#addBookNum").val());
        $.post("/admin/bookManageController_addBookNum.action", postData, function (data) {
            if (data == 1) {
                //关闭之前的模态框
                $("#addNumModal").modal("hide");
                //弹出修改成功的模态框
                showInfo("修改成功");
            } else {
                $("#addNumModal").modal("hide");
                showInfo("修改失败");
            }
        });
    });
});
function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}
function validAddBookNum() {
    var flag = true;
    var num = $.trim($("#addBookNum").val());
    if (num == "") {
        $('#addBookNum').parent().addClass("has-error");
        $('#addBookNum').next().text("请输入新增图书数量");
        $("#addBookNum").next().show();
        flag = false;
    } else if (num <= 0 || num != parseInt(num)) {
        $('#addBookNum').parent().addClass("has-error");
        $('#addBookNum').next().text("图书数量必须为正整数");
        $("#addBookNum").next().show();
        flag = false;
    } else {
        $('#addBookNum').parent().removeClass("has-error");
        $('#addBookNum').next().text("");
        $("#addBookNum").next().hide();
    }
    return flag;
}
