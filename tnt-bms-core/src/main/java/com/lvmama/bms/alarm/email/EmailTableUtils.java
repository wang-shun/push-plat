package com.lvmama.bms.alarm.email;

import java.util.List;

public class EmailTableUtils {

    public static String createTable(String title, String[] headers, List<List<Object>> rows) {

        StringBuilder table = new StringBuilder();
        table.append("<table border=\"1\">");
        table.append("<caption>").append(title).append("</caption>");
        table.append(addHeader(headers));
        table.append(addRow(rows));
        table.append("</table>");
        return table.toString();

    }


    private static String addHeader(String[] headers) {

        StringBuilder row = new StringBuilder();
        row.append("<tr>");
        for(String header : headers) {
            row.append("<td>").append(header).append("</td>");
        }
        row.append("</tr>");

        return row.toString();
    }

    private static String addRow(List<List<Object>> rows) {

        StringBuilder body = new StringBuilder();

        for(List<Object> row : rows) {
            StringBuilder columns = new StringBuilder();
            columns.append("<tr>");
            for(Object column : row) {
                columns.append("<td>").append(column==null ? "" : column.toString()).append("</td>");
            }
            columns.append("</tr>");
            body.append(columns);
        }

        return body.toString();
    }


}
