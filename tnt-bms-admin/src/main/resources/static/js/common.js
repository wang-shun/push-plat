//导航选择器
$(document).ready(function() {
	$("#side-menu ul li").click(function(){
        var url = window.location.href;
        if (url.indexOf("main") > 0) {

        } else if (url.indexOf("news") > 0) {
            $("#news").addClass("active");
            $("#newsTypeManage").addClass("active");
        } else if (url.indexOf("chart") > 0) {
            $("#chart").addClass("active");
        } else if (url.indexOf("setting") > 0) {
            $("#chart").addClass("active");
        }
	});


});

/**
 * 函数：检查上传文件大小
 * 输入：input的js对象
 * 输出：
 **/
function checkSize(input) {
    var name=$(input).val();
    var fileName = name.substring(name.lastIndexOf(".")+1).toLowerCase();
    if (fileName != 'jar'){
        alert("仅支持jar文件！");
        return false;
    }

    var Sys = {};
    var flag;
    var fileSize = 0;
    //判断浏览器种类
    if (navigator.userAgent.indexOf("MSIE") > 0) {
        Sys.ie=true;
    }
    if (navigator.userAgent.indexOf("Firefox")>0) {
        Sys.firefox=true;
    }

    //获取文件大小
    if (Sys.firefox) {
        fileSize = input.files[0].size;
    } else if (Sys.ie){
        var fileobject = new ActiveXObject ("Scripting.FileSystemObject");//获取上传文件的对象
        var file = fileobject.GetFile (input.value);//获取上传的文件
        fileSize = file.Size;//文件大小
    } else {
        fileSize = input.files[0].size;
	}

    //判断是否符合要求
    if (fileSize / (1024 * 1024) < 4 ) {
        flag = true;
    } else {
        alert("附件过大，建议不要超过4M！");
        flag = false;
    }
    return flag;
}

function compare(num1, num2) {
    return num1 > num2;
}

// 提示条配置
/*
toastr.options = {
	"closeButton" : true,
	"debug" : false,
	"progressBar" : true,
	"preventDuplicates" : false,
	"positionClass" : "toast-top-right",
	"onclick" : null,
	"showDuration" : "400",
	"hideDuration" : "1000",
	"timeOut" : "7000",
	"extendedTimeOut" : "1000",
	"showEasing" : "swing",
	"hideEasing" : "linear",
	"showMethod" : "fadeIn",
	"hideMethod" : "fadeOut"
}*/
