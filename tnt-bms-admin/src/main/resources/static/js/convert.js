var headers,indexs = new Array();
var deleteHeader = function(obj) {
    var index = $(obj).attr("index");
    $(obj).parent().parent().remove();
    indexs.push(index);
}

$(document).ready(function () {
    //update

    $("#update_btn").click(function () {
        var httpHeader = $("#httpHeader").val();
        if (isEmpty(httpHeader)){
            return;
        }
        headers = httpHeader.split("&");
        var content = "";

        for(var i = 0,len = headers.length; i < len; i++) {
            if (!isEmpty(headers[i])){
                var temp = headers[i];
                content += '<tr><td>'+temp.entityify()+'</td><td><a index='+i+' onclick="javascript:deleteHeader(this);" title="删除"><i class="fa fa-trash-o text-navy"></i></a></td></tr>';
            }
        }
        $("#headerList").html(content);
    });

    $("#u_save_btn").click(function () {
        var values = "";
        var isContain = false;
        for(var j = 0; j < headers.length; j++) {
            for (var i = 0; i < indexs.length; i++) {
                if (j == indexs[i]){
                    isContain = true;
                    break;
                }
            }
            if (!isContain && !isEmpty(headers[j])) {
                values += headers[j] + "&";
            }
        }
        $("#httpHeader").val(values);
        $("#u_close_btn").click();
    });

    //save
    $("#add_btn").click(function () {
        $("#httpKey").val("");
        $("#httpValue").val("");
    });
    $("#httpKey").blur(function () {
        console.log("blur1")
        if (isEmpty($("#httpKey").val())){
            sweetAlert("http头名称不能为空", "", "error");
        }
    });
    $("#httpValue").blur(function () {
        if (isEmpty($("#httpValue").val())){
            sweetAlert("http头值不能为空", "", "error");
        }
    });
    $("#save_btn").click(function () {
        var httpHeader = $("#httpHeader").val();
        var httpKey = $("#httpKey").val();
        var httpValue = $("#httpValue").val();
        if (!isEmpty(httpKey) && !isEmpty(httpValue)) {
            if (!isEmpty(httpHeader) && httpHeader.indexOf(httpKey) > 0) {
                sweetAlert("http头名称已存在", "", "error");
                return;
            }
            $("#httpHeader").val(httpHeader+httpKey+"="+httpValue+"&");
        }

       $("#close_btn").click();
    });
    //提交
    var check = false;
    var oldName = $("#name").val();
    $("#submitButton").click(function(){
        if($("#name").val() == ""){
            sweetAlert("请输入映射器名称", "", "error");
            return;
        }
        if($("#generation").val() <= 0){
            sweetAlert("请选择版本", "", "error");
            return;
        }
        if($("#requestMap").val() == ""){
            sweetAlert("请输入映射内容", "", "error");
            return;
        }
        if (!check){
            if (oldName != $("#name").val()) {
                sweetAlert("该映射器名称已存在！","","warning");
                return;
            }
        }
        $("#formTag").submit();

    });

    $("#name").blur(function () {
        var name = $("#name").val();
        if (name != oldName){
            bms.requestAjaxGet('check/'+name,
                function (response) {
                    check = true;
                }, function (response) {
                    sweetAlert(response.resMsg,"","warning");
                });
        }
    });
});
