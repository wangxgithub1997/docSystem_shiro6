function getBookInfo(id) {
    ajax({
        method:"POST",
        url:"/admin/bookManageController_getBookInfo.action",
        params:"bookId="+id,
        type:"json",
        callback:function (data) {
            // alert(data);
            $("#findISBN").val(data.ISBN);
            $("#findBookName").val(data.bookName);
            $("#findBookType").val(data.bookType);
            $("#findAutho").val(data.autho);
            $("#findPress").val(data.press);
            $("#findPrice").val(data.price);
            $("#findDescription").val(data.description);
            $("#findNum").val(data.num);
            $("#findAdmin").val(data.adminName);
            $("#findCurrentNum").val(data.currentNum);
        }
    });
}