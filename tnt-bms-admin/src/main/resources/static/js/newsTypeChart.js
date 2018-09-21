var M_DATA_CHART_CONFIGS = [
    {"field": "reachCount", "title": "到达量"},
    {"field": "receiveCount", "title": "接收量"},
    {"field": "failCount", "title": "失败量"},
    {"field": "sendCount", "title": "发送量"}
]

$(document).ready(function () {
    $(document).on("focus", ".datepicker", function () {
        WdatePicker({
            el: $(this).attr("id"),
            dateFmt: $(this).attr("date-format"),
            lang: 'zh-cn',
            qsEnabled: false
        })
    });

    $("#searchBtn").on("click", function () {
        var startTime = $("input[name='startTime']").val();
        var endTime = $("input[name='endTime']").val();
        if (!startTime) {
            swal("请选择开始时间", "开始时间不能为空，格式:yyyy-MM-dd HH:mm:ss", "warning");
            return;
        }
        if (!endTime) {
            swal("请选择结束时间", "结束时间不能为空，格式:yyyy-MM-dd HH:mm:ss", "warning");
            return;
        }

        var requestVo = {
            msgType : $("#msgType").val(),
            startTime: startTime,
            endTime: endTime
        };

        bms.requestAjaxPost('getData',
            JSON.stringify(requestVo),
            function (response) {
                Highcharts.setOptions({
                    global: {
                        useUTC: false //关闭UTC
                    }
                });
                showJobDataChart(response);
            }, function (response) {
                swal(response.resMsg);
            });
    });

    $("#searchBtn").trigger("click");

    function showJobDataChart(json) {
        if (json.result == null || json.result.length == 0) {
            $("#highcharts").html("暂无数据，请重新选择条件。");
            return;
        }
        $("#highcharts").html("");

        var seriesMap = {};
        for (var i = 0; i < M_DATA_CHART_CONFIGS.length; i++) {
            var chartConfig = M_DATA_CHART_CONFIGS[i];
            seriesMap[chartConfig['field']] = {
                name: chartConfig["title"],
                data: []
            };
        }

        var rows = json.result;
        for (var i = 0; i < rows.length; i++) {
            var row = rows[i];
            var timestamp = row['timestamp'];
            for (var seriesKey in seriesMap) {
                seriesMap[seriesKey]['data'].push([timestamp, row[seriesKey]]);
            }
        }

        var series = [];
        for (var seriesKey in seriesMap) {
            series.push(seriesMap[seriesKey]);
        }
        showLineChart("#highcharts", "全局推送统计", "个数", series, null);
    }
});

function showLineChart(chartId, title, yTitle, series, colors, valueSuffix) {
    if (!colors) {
        colors = ['#FCAF64', '#009944', '#f9b700', '#eb6100'];
    }

    $(chartId).highcharts({
        chart: {
            zoomType: 'x',
            type: 'spline'
        },
        colors: colors,
        title: {
            text: title
        },
        xAxis: {
            type: 'datetime'
        },
        yAxis: {
            title: {
                text: yTitle
            },
            min: 0,
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: valueSuffix || '',
            dateTimeLabelFormats: {
                minute: "%Y-%m-%d %H:%M"
            },
            crosshairs: true,
            shared: true
        },
        plotOptions: {
            series: {
                fillOpacity: 0.1,
                shadow: false,
                marker: {
                    enabled: false,
                    radius: 4,
                    fillColor: null,
                    lineWidth: 2,
                    lineColor: '#FFFFFF',
                    states: {hover: {enabled: true}}
                }
            }
        },
        credits: {
            enabled: false
        },
        series: series
    });
}