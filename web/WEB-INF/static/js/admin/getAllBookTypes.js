//获取所有图书类型
window.onload = new function () {
    $.post("/admin/bookManageController_getAllBookTypes.action", function (data) {
        for(var index in data){
            var op = document.createElement("option");//创建指定名称的元素
            op.value = data[index].id;//设置元素的值
            var textNode = document.createTextNode(data[index].name);//创建文本节点
            op.appendChild(textNode);//将文本节点添加到元素中（显示的值）
            document.getElementById("bookTypeId").appendChild(op);
            //<option></option>
        }
    },"json");
}