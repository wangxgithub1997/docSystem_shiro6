$(function () {
    $('#batchAdd').click(function () {
        if (!validBatchAddReader()) {
            return;
        }
        var postdata = "fileName=" + $.trim($("#excel").val());
        ajax({
            method: 'POST',
            url: '/admin/readerManageController_batchAddReader.action',
            type: "json",
            params: postdata,
            callback: function (data) {
                if (data.code == 1) {
                    showInfo2(data.msg);
                } else if (data.code == -4) {
                    $("#batchAddModal").modal("hide");
                    showInfo2(data.msg + "，未成功的数据<a href=/fileDownloadController_fileDownload.action?fileType=3&fileName="+data.filePath+">点击下载</a>")
                } else {
                    showInfo2(data.msg);
                }
            }
        });
    });
    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });
});

function validBatchAddReader() {
    var flag = true;
    var upload = $.trim($("#upload").val());
    if (upload == "") {
        alert("请选择excel文件");
        flag = false;
    }
    return flag;
}

function checkFileExt(filename) {
    var flag = false; //状态
    var arr = ["xls"];
    //取出上传文件的扩展名
    var index = filename.lastIndexOf(".");
    var ext = filename.substr(index + 1);
    //循环比较
    for (var i = 0; i < arr.length; i++) {
        if (ext == arr[i]) {
            flag = true; //一旦找到合适的，立即退出循环
            break;
        }
    }
    return flag;
}

function showInfo2(msg) {
    $("#div_info").html(msg);
    $("#modal_info").modal('show');
}