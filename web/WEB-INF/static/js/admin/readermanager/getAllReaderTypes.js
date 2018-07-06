/**
 * 读者管理（获取读者类型）
 */
window.onload = new function () {
    $.post("/admin/readerTypeManageController_getAllReaderTypes.action",function (data) {
        // 循环遍历每个读者分类，每个名称生成一个option对象，添加到<select>中
        for (var index in data) {
            var op = document.createElement("option");//创建一个指名名称元素
            op.value = data[index].id;//设置op的实际值为当前的读者分类编号
            var textNode = document.createTextNode(data[index].name);//创建文本节点
            op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
            if (readerType == data[index].id) {
                op.selected = true;
            }
            document.getElementById("readerType").appendChild(op);
        }
    },"json");
};