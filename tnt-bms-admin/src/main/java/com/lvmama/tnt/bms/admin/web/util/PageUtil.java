package com.lvmama.tnt.bms.admin.web.util;

import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import org.apache.ibatis.session.RowBounds;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class PageUtil {

	public static long calculateTotalPage(long totalCount, int pageSize) {
		return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
	}

	public static RowBounds getRowBounds(int pageNo, int pageSize) {
		return new RowBounds((pageNo - 1) * pageSize, pageSize);
	}

	public static <T> String getPageContent(HttpServletRequest request, PageResultVO<T> resultVo) {
		long totalCount = resultVo.getTotalCount();
		long totalPage = resultVo.getTotalPage();
		long pageNo = resultVo.getPageNo();
		StringBuffer sb = new StringBuffer();
		sb.append("<form method=\"post\" action=\"").append(request.getRequestURI()).append("\" name=\"qPagerForm\">\r\n");
		sb.append("<span>共 ").append("<span id='totalCount'>"+totalCount+"</span>").append(" 条记录，每页展示 ").append(resultVo.getPageSize()).append(" 条</span>");
		sb.append("<ul id=\"pageHTML\" class=\"pagination pull-right\">");
		sb.append("<input type=\"hidden\" name=\"").append("pageNo").append("\" id=\"").append("pageNo").append("\" value=\"").append(pageNo).append("\"/>\r\n");

		Enumeration<String> enumeration = request.getParameterNames();
		String name = null; // 参数名
		String value = null; // 参数值
		// 把请求中的所有参数当作隐藏表单域 如 <input type="hidden" name="pageNo" value="1"/>
		while (enumeration.hasMoreElements()) {
			name = enumeration.nextElement();
			value = request.getParameter(name);
			// 去除页号
			if (name.equals("pageNo")) {
				if (null != value && !"".equals(value)) {
					pageNo = Integer.parseInt(value);
				}
				continue;
			}
			sb.append("<input type=\"hidden\" name=\"").append(name)
					.append("\" value=\"").append(value).append("\"/>\r\n");
		}
		// 上一页处理
		if (pageNo == 1) {
			sb.append("<li class=\"footable-page-arrow disabled").append("\"><a href=\"javascript:turnOverPage(").append(1).append(")\">«</a></li>");
			sb.append("<li class=\"footable-page-arrow disabled").append("\"><a href=\"javascript:turnOverPage(").append(1).append(")\">‹</a></li>");
		} else {
			sb.append("<li class=\"footable-page-arrow ").append("\"><a href=\"javascript:turnOverPage(").append(1).append(")\">«</a></li>");
			sb.append("<li class=\"footable-page-arrow ").append("\"><a href=\"javascript:turnOverPage(").append(pageNo - 1).append(")\">‹</a></li>");
		}

		// 中间页码处理 start:当前页的前一页
		long start = 1;
		if (pageNo > 5) {
			// 如果前面页数过多,第1,2显示,跟着显示"..."
			start = pageNo - 2;
			sb.append("<li class=\"footable-page").append((1 == pageNo)?" active":"").append("\"><a href=\"javascript:turnOverPage(1)\">1</a></li>");
			sb.append("<li class=\"footable-page").append((2 == pageNo)?" active":"").append("\"><a href=\"javascript:turnOverPage(2)\">2</a></li>");
			sb.append("<li class=\"footable-page").append((3 == pageNo)?" active":"").append("\"><a href=\"javascript:turnOverPage(3)\">3</a></li>");
			sb.append("<li class=\"footable-page").append("\"><a href=\"#\">&hellip;</a></li>");
		}
		// 中间页码处理 start:当前页的后一页
		long end = pageNo + 1;
		if (end > totalPage) {
			// 当前页码的下一页码>总页码是，显示总页码
			end = totalPage;
		}
		// 显示当前页附近的页
		for (long i = start; i <= end; i++) {
			if (pageNo == i) { // 当前页号不需要超链接
				sb.append("<li class=\"footable-page active").append("\"><a href=\"#\">").append(i).append("</a></li>");
			} else {
				sb.append("<li class=\"footable-page").append("\"><a href=\"javascript:turnOverPage(").append(i).append(")\">").append(i).append("</a></li>");
			}
		}
		// 如果后面页数过多,显示"..."
		if (end < totalPage - 2) {
			sb.append("<li class=\"footable-page").append("\"><a href=\"#\">&hellip;</a></li>");
		}

		// 显示最后两页页码
		if (end < totalPage - 1) {
			sb.append("<li class=\"footable-page").append("\"><a href=\"javascript:turnOverPage(").append(totalPage-1).append(")\">").append(totalPage-1).append("</a></li>");
		}
		if (end < totalPage) {
			sb.append("<li class=\"footable-page").append("\"><a href=\"javascript:turnOverPage(").append(totalPage).append(")\">").append(totalPage).append("</a></li>");
		}

		// 下一页处理
		if (pageNo == totalPage) {
			sb.append("<li class=\"footable-page-arrow disabled").append("\"><a href=\"javascript:turnOverPage(").append(totalPage).append(")\">›</a></li>");
			sb.append("<li class=\"footable-page-arrow disabled").append("\"><a href=\"javascript:turnOverPage(").append(totalPage).append(")\">»</a></li>");
		} else {
			sb.append("<li class=\"footable-page-arrow").append("\"><a href=\"javascript:turnOverPage(").append(pageNo+1).append(")\">›</a></li>");
			sb.append("<li class=\"footable-page-arrow").append("\"><a href=\"javascript:turnOverPage(").append(totalPage).append(")\">»</a></li>");
		}

		sb.append("</ul>").append("</form>\r\n");

		// 生成提交表单的JS
		sb.append("<script language=\"javascript\">\r\n");
		sb.append("  function turnOverPage(no){\r\n");
		sb.append("if(!isNaN(no)){\r\n");
		sb.append("    if(no>").append(totalPage).append("){");
		sb.append("      no=").append(totalPage).append(";}\r\n");
		sb.append("    if(no<1){no=1;}\r\n");
		sb.append("    document.getElementById(\"pageNo\").value=no;\r\n");
		sb.append("    document.qPagerForm.submit();\r\n");
		sb.append("  }\r\n");
		sb.append("  }\r\n");
		sb.append("</script>\r\n");
		return sb.toString();
	}
	
	public static String getPageContent(String url, int pageCurrent, int pageSize, int pageCount){
		if (pageCount == 0) {
			return "";
		}
		String urlNew = url.replace("{pageSize}", pageSize+"").replace("{pageCount}", pageCount+"");
		
		String first = urlNew.replace("{pageCurrent}", 1+"");
		String prev = urlNew.replace("{pageCurrent}", (pageCurrent - 1)+"");
		String next = urlNew.replace("{pageCurrent}", (pageCurrent + 1)+"");
		String last = urlNew.replace("{pageCurrent}", pageCount+"");
		
		StringBuffer html = new StringBuffer();
		html.append("<li class=\"footable-page-arrow"+(pageCurrent<=1?" disabled":"")+"\"><a href=\""+(pageCurrent<=1?"#":first)+"\">«</a></li>");
		html.append("<li class=\"footable-page-arrow"+(pageCurrent<=1?" disabled":"")+"\"><a href=\""+(pageCurrent<=1?"#":prev)+"\">‹</a></li>");
		for(int i = 0 ;i < pageCount; i++){
			String urlItem = urlNew.replace("{pageCurrent}", (i+1)+"");
			html.append("<li class=\"footable-page"+(((i+1) == pageCurrent)?" active":"")+"\"><a href=\""+urlItem+"\">"+(i+1)+"</a></li>");
		}
		html.append("<li class=\"footable-page-arrow"+(pageCurrent==pageCount?" disabled":"")+"\"><a href=\""+(pageCurrent==pageCount?"#":next)+"\">›</a></li>");
		html.append("<li class=\"footable-page-arrow"+(pageCurrent==pageCount?" disabled":"")+"\"><a href=\""+(pageCurrent==pageCount?"#":last)+"\">»</a></li>");
		
		return html.toString().replaceAll("null", "");
	}
	
}
