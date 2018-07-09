$(function () {
    $("#addBook").click(function () {
        if (!validAddBook()) {
            return;
        }
        var postData = "name=" + $.trim($("#addBookName").val()) +
            "&autho=" + $.trim($("#addAutho").val()) + "&press=" + $.trim($("#addPress").val())
            + "&num=" + $.trim($("#addNum").val()) + "&price=" + $.trim($("#addPrice").val())
            + "&description=" + $.trim($("#addDescription").val()) + "&bookTypeId="
            + $.trim($("#addBookType").val()) + "&ISBN=" + $.trim($("#addISBN").val());
        $.post("/admin/bookManageController_addBook.action", postData, function (data) {
            if (data.code == 1) {
                $("#addModal").modal("hide");//关闭模糊框
                showInfo("添加成功");
            } else if (data.code == -2) {
                $("#addModal").modal("hide");//关闭模糊框
                showInfo("图书isbn重复了");
            }
            else {
                $("#addModal").modal("hide");//关闭模糊框
                showInfo("添加失败");
            }
        });
    });
    $("#btn_add").click(function () {
        var $addBookType = $("#addBookType");
        $addBookType.find("option").not(":first").remove();
        $.post("/admin/bookManageController_getAllBookTypes.action", function (data) {
            for (var index in data) {
                $addBookType.append("<option value=" + data[index].id + ">" + data[index].name + "</option>");
            }
            $("#addModal").modal("show");
        }, "json");
    });
});
function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}
function validAddBook() {
    var flag = true;
    var ISBN = $.trim($("#addISBN").val());
    if (ISBN == "") {
        $('#addISBN').parent().addClass("has-error");
        $('#addISBN').next().text("请输入图书ISBN码");
        $("#addISBN").next().show();
        flag = false;
    } else {
        $('#addISBN').parent().removeClass("has-error");
        $('#addISBN').next().text("");
        $("#addISBN").next().hide();
    }

    var bookName = $.trim($("#addBookName").val());
    if (bookName == "") {
        $('#addBookName').parent().addClass("has-error");
        $('#addBookName').next().text("请输入图书名称");
        $("#addBookName").next().show();
        flag = false;
    } else {
        $('#addBookName').parent().removeClass("has-error");
        $('#addBookName').next().text("");
        $("#addBookName").next().hide();
    }


    var bookType = $.trim($("#addBookType").val());
    if (bookType == -1) {
        $('#addBookType').parent().addClass("has-error");
        $('#addBookType').next().text("请选择图书分类");
        $("#addBookType").next().show();
        flag = false;
    } else {
        $('#addBookType').parent().removeClass("has-error");
        $('#addBookType').next().text("");
        $("#addBookType").next().hide();
    }

    var autho = $.trim($("#addAutho").val());
    if (autho == "") {
        $('#addAutho').parent().addClass("has-error");
        $('#addAutho').next().text("请输入作者名称");
        $("#addAutho").next().show();
        flag = false;
    } else {
        $('#addAutho').parent().removeClass("has-error");
        $('#addAutho').next().text("");
        $("#addAutho").next().hide();
    }


    var press = $.trim($("#addPress").val());
    if (press == "") {
        $('#addPress').parent().addClass("has-error");
        $('#addPress').next().text("请输入出版社名称");
        $("#addPress").next().show();
        flag = false;
    } else {
        $('#addPress').parent().removeClass("has-error");
        $('#addPress').next().text("");
        $("#addPress").next().hide();
    }

    var num = $.trim($("#addNum").val());
    if (num == "") {
        $('#addNum').parent().addClass("has-error");
        $('#addNum').next().text("请输入总数量");
        $("#addNum").next().show();
        flag = false;
    } else if (num <= 0 || num != parseInt(num)) {
        $('#addNum').parent().addClass("has-error");
        $('#addNum').next().text("数量必须为正整数");
        $("#addNum").next().show();
        flag = false;
    } else {
        $('#addNum').parent().removeClass("has-error");
        $('#addNum').next().text("");
        $("#addNum").next().hide();
    }


    var price = $.trim($("#addPrice").val());
    if (price == "") {
        $('#addPrice').parent().addClass("has-error");
        $('#addPrice').next().text("请输入总数量");
        $("#addPrice").next().show();
        flag = false;
    } else if (price <= 0 || price != parseInt(price)) {
        $('#addPrice').parent().addClass("has-error");
        $('#addPrice').next().text("数量必须为正整数");
        $("#addPrice").next().show();
        flag = false;
    } else {
        $('#addPrice').parent().removeClass("has-error");
        $('#addPrice').next().text("");
        $("#addPrice").next().hide();
    }
    return flag;
}


