var groupData = null, groupToken = null;
$(document).ready(function () {
    $("#pushType").change(function () {
        var pusher = $("#pushType").val();
        if (pusher == 3) {
            $("#pusherChoose").removeClass("hidden");
        } else {
            $("#pusherChoose").addClass("hidden");
            $("#puserID").val("");
        }
    });

   /* $("#converterType").change(function () {
        var pusher = $("#converterType").val();
        if (pusher == 2) {
            $("#convertChoose").removeClass("hidden");
        } else {
            $("#convertChoose").addClass("hidden");
            $("#convertID").val("");
        }
    });*/

    $("#groupToken").change(function(){
        groupToken = $("#groupToken").val();
        if(!isEmpty(groupToken)) {
            bms.requestAjaxGet("findGroup/"+groupToken,function(resp){
                if (resp.result != null) {
                    groupData = resp.result;
                    groupToken = groupData.token;
                }else {
                    sweetAlert("查无此分组", "", "error");
                }
            },function (resp) {
                sweetAlert("查询分组异常", "", "error");
            });
        }
    });

    //提交
    $("#editSubmitButton").click(function(){
        var mode = $("#mode").val();
        if(!checkForm(mode)){
            return;
        }
        var newsAccessDO = {
            id : $("#id").val(),
            groupId : $("#groupId").val(),
            token : $("#token").val(),
            name : $("#name").val(),
            receiveType : $("#receiveType").val(),
            pushType : $("#pushType").val(),
            pushUrl : $("#pushUrl").val(),
            opened : $("#opened").val(),
            converterType : $("#converterType").val(),
            priority : $("#priority").val(),
            threshold : $("#threshold").val(),
            connectTimeOut : $("#connectTimeOut").val(),
            readTimeOut : $("#readTimeOut").val(),
            pushID : $("#pushID").val(),
            convertID : $("#convertID").val(),
            version : $("#version").val(),
            encryptKey: $("#encryptKey").val(),
            encryptMethod: $("#encryptMethod").val()
        };
        bms.requestAjaxPost("../../editN",JSON.stringify(newsAccessDO),
            function(response){
                window.location.href = "../../list";
            },function(response){
                toastr.error(response.resMsg,'提示');
            });
    });

    //提交
    $("#submitButton").click(function(){
        var mode = $("#mode").val();
        if(!checkForm(mode)){
            return;
        }
        var newsAccessDO = {
            groupToken : groupToken,
            name : $("#name").val(),
            receiveType : $("#receiveType").val(),
            pushType : $("#pushType").val(),
            pushUrl : $("#pushUrl").val(),
            opened : $("#opened").val(),
            converterType : $("#converterType").val(),
            priority : $("#priority").val(),
            threshold : $("#threshold").val(),
            connectTimeOut : $("#connectTimeOut").val(),
            readTimeOut : $("#readTimeOut").val(),
            mode : mode,
            pushID : $("#pushID").val(),
            convertID : $("#convertID").val(),
            encryptKey: $("#encryptKey").val(),
            encryptMethod: $("#encryptMethod").val()
        };
        bms.requestAjaxPost("addN",JSON.stringify(newsAccessDO),
            function(response){
                window.location.href = "list";
            },function(response){
                sweetAlert(response.resMsg, "", "error");
            });
    });

});
function checkForm(mode) {
    if($("#name").val() == ""){
        if (mode == "access"){
            sweetAlert("请输入接收方名称", "", "error");
        } else {
            sweetAlert("请输入分组名称", "", "error");
        }
        return false;
    }
    if (mode == "access") {
        if(isEmpty($("#pushUrl").val())){
             if (3 != $("#pushType").val()){
                sweetAlert("请填写推送接口地址", "", "error");
                 return false;
             }
        }

        // if (isEmpty($("#encryptKey").val())){
        //     sweetAlert("请输入密匙", "", "error");
        //     return false;
        // }
        //
        // if (isEmpty($("#encryptMethod").val())){
        //     sweetAlert("请输入加密方式", "", "error");
        //     return false;
        // }

        //默认开启
        if(isEmpty($("#opened").val())){
            if (groupData == null || isEmpty(groupData.opened)){
                sweetAlert("请选择消息开启状态", "", "error");
                return false;
            }
        }
        //默认5
        if(isEmpty($("#priority").val())){
            if (groupData == null || isEmpty(groupData.priority)){
                sweetAlert("请选择消息优先级", "", "error");
                return false;
            }
        }

        /*if(isEmpty($("#timeOut").val())){
            if (groupData == null || isEmpty(groupData.timeOut)){
                sweetAlert("请填写超时时间", "", "error");
                return;
            }
        }*/

        if(isEmpty($("#receiveType").val())){
            if (groupData == null || isEmpty(groupData.receiveType)){
                sweetAlert("请选择接收类型", "", "error");
                return false;
            }
        }
        var pushType = $("#pushType").val();
        if(isEmpty(pushType)){
            if (groupData == null || isEmpty(groupData.pushType)){
                sweetAlert("请选择推送类型", "", "error");
                return false;
            }
        } else if (3 == pushType) {
            var pushID = $("#pushID").val();
            if (isEmpty(pushID)){
                sweetAlert("请选择推送器", "", "error");
                return false;
            }
        }

        var converterType = $("#converterType").val();
        if(isEmpty(converterType)){
            if (groupData == null || isEmpty(groupData.converterType)){
                sweetAlert("请选择转换器", "", "error");
                return false;
            }
        } else if (2 == converterType) {
            if (isEmpty($("#convertID").val())){
                sweetAlert("请选择转换映射器", "", "error");
                return false;
            }
        }
        if(isEmpty($("#threshold").val())){
            if (groupData == null || isEmpty(groupData.threshold)){
                sweetAlert("请填写推送频率", "", "error");
                return false;
            }
        }
    }
    return true;
}