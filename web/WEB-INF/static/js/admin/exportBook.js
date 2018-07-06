function exportBook() {

    $.get("/admin/bookManageController_exportBook.action",function (data) {
        var basePath = $("#basePath").val();
        showInfo("数据已导出：<a href="+basePath+"fileDownloadController_fileDownload.action?fileName="+data+"&fileType=1>点击下载</a>");
    });

}

function showInfo(msg) {
    $("#div_info").html(msg);
    $("#modal_info").modal('show');
}
