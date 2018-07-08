$(function () {
    $("#updateBook").click(function () {
        if (!validUpdateBook()) {
            return;
        }
        var postData = "bookId=" + $.trim($("#updateBookId").val())
            + "&bookName=" + $.trim($("#updateBookName").val())
            + "&bookTypeId=" + $.trim($("#updateBookType").val())
            + "&autho=" + $.trim($("#updateAutho").val())
            + "&press=" + $.trim($("#updatePress").val())
            + "&price=" + $.trim($("#updatePrice").val())
            + "&description=" + $.trim($("#updateDescription").val())
            + "&ISBN=" + $.trim($("#updateISBN").val());
        $.post("/admin/bookManageController_updateBook.action", postData, function (data) {
            if (data.code == 1) {
                //关闭之前的模态框
                $("#updateModal").modal("hide");
                //弹出修改成功的模态框
                showInfo("修改成功");
            } else {
                $("#updateModal").modal("hide");
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
    $("#modal_info").modal("show");
}

function updateBook(id) {
    $("#updateBookType").find("option").not(":first").remove();
    $.post("/admin/bookManageController_getBookInfo.action", "bookId=" + id, function (data) {
        $("#updateBookId").val(data.id);
        $("#updateISBN").val(data.ISBN);
        $("#updateBookName").val(data.bookName);
        //$("#updateBookType").val(data.bookType);
        $("#updateAutho").val(data.autho);
        $("#updatePress").val(data.press);
        $("#updatePrice").val(data.price);
        $("#updateDescription").val(data.description);
        $.post("/admin/bookManageController_getAllBookTypes.action", function (types) {
            for (var index in types) {
                if (data.bookType == types[index].name) {
                    $("#updateBookType").append("<option value=" + types[index].id + " selected >" + types[index].name + "</option>");
                } else {
                    $("#updateBookType").append("<option value=" + types[index].id + ">" + types[index].name + "</option>");
                }
            }
        }, "json");
    }, "json");
}

function validUpdateBook() {
    var flag = true;

    var ISBN = $.trim($("#updateISBN").val());
    if (ISBN == "") {
        $('#updateISBN').parent().addClass("has-error");
        $('#updateISBN').next().text("请输入图书ISBN码");
        $("#updateISBN").next().show();
        flag = false;
    } else {
        $('#updateISBN').parent().removeClass("has-error");
        $('#updateISBN').next().text("");
        $("#updateISBN").next().hide();
    }

    var bookName = $.trim($("#updateBookName").val());
    if (bookName == "") {
        $('#updateBookName').parent().addClass("has-error");
        $('#updateBookName').next().text("请输入图书名称");
        $("#updateBookName").next().show();
        flag = false;
    } else {
        $('#updateBookName').parent().removeClass("has-error");
        $('#updateBookName').next().text("");
        $("#updateBookName").next().hide();
    }


    var bookType = $.trim($("#updateBookType").val());
    if (bookType == -1) {
        $('#updateBookType').parent().addClass("has-error");
        $('#updateBookType').next().text("请选择图书分类");
        $("#updateBookType").next().show();
        flag = false;
    } else {
        $('#updateBookType').parent().removeClass("has-error");
        $('#updateBookType').next().text("");
        $("#updateBookType").next().hide();
    }

    var autho = $.trim($("#updateAutho").val());
    if (autho == "") {
        $('#updateAutho').parent().addClass("has-error");
        $('#updateAutho').next().text("请输入作者名称");
        $("#updateAutho").next().show();
        flag = false;
    } else {
        $('#updateAutho').parent().removeClass("has-error");
        $('#updateAutho').next().text("");
        $("#updateAutho").next().hide();
    }


    var press = $.trim($("#updatePress").val());
    if (press == "") {
        $('#updatePress').parent().addClass("has-error");
        $('#updatePress').next().text("请输入出版社名称");
        $("#updatePress").next().show();
        flag = false;
    } else {
        $('#updatePress').parent().removeClass("has-error");
        $('#updatePress').next().text("");
        $("#updatePress").next().hide();
    }
    var price = $.trim($("#updatePrice").val());
    if (price == "") {
        $('#updatePrice').parent().addClass("has-error");
        $('#updatePrice').next().text("请输入总数量");
        $("#updatePrice").next().show();
        flag = false;
    } else if (price <= 0 || price != parseInt(price)) {
        $('#updatePrice').parent().addClass("has-error");
        $('#updatePrice').next().text("数量必须为正整数");
        $("#updatePrice").next().show();
        flag = false;
    } else {
        $('#updatePrice').parent().removeClass("has-error");
        $('#updatePrice').next().text("");
        $("#updatePrice").next().hide();
    }
    return flag;
}