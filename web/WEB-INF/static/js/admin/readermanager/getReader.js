function getReaderInfo(id) {

    $.post("/admin/readerManageController_getReaderInfo.action","id=" + id, function (data) {
        $("#findPaperNO").val(data.paperNO);
        $("#findReaderName").val(data.readerName);
        $("#findEmail").val(data.email);
        $("#findPhone").val(data.phone);
        $("#findReaderType").val(data.readerType);
        $("#findAdmin").val(data.adminName);
        $("#findModal").modal("show");
    },"json");



}