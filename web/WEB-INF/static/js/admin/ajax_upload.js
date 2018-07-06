$(function () {
    $(document).on('change',"#upload",function () {
        //提交表单（模拟一个点击上传的操作）
        $("#uploadForm").ajaxSubmit({
            dataType:'json',
            success:function (data) {
                if (data.code == 1) {
                    // alert(data.fileName);
                    //上传成功，设置文件名到隐藏域中
                    $("#excel").val(data.fileName);
                }else{
                    showInfo(data.msg)
                }
            }
        });

    });
});

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}
