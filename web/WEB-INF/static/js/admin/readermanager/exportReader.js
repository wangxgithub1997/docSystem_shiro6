function exportReader() {
    $.get("/admin/readerManageController_exportReader.action", function (data) {
        showInfo("数据已导出：<a href='"+ data + "'>点击下载</a>");
    });
}
function showInfo(msg) {
    $("#div_info").html(msg);
    $("#modal_info").modal('show');
}
