$(function () {
   $("#batchAdd").click(function () {
       //需要传递文件名
       //获取隐藏域中的文件名
       var postData = "fileName="+$.trim($("#excel").val());
       $.post("/admin/bookManageController_batchAddBook.action",postData,function (data) {
           if (data.code == 1) {
           } else if (data.code == -4) {
               $("#batchAddModal").modal("hide");
               showInfo(data.msg + "，未成功的数据<a href=/fileDownloadController_fileDownload.action?fileType=3&fileName="+data.filePath+">点击下载</a>")
           }
           else {
               showInfo(data.msg);
           }
       },"json");
   });
});


function showInfo(msg) {
    $("#div_info").html(msg);
    $("#modal_info").modal('show');
}
