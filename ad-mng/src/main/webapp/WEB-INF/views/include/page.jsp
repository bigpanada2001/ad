<%@ page import="com.duowan.adcenter.util.page.PageQuery"%>
<%@ page import="com.duowan.adcenter.util.page.PageResult"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    PageResult p_result = (PageResult) request.getAttribute("pageResult");
    PageQuery p_query = p_result.getQuery();
    request.setAttribute("p_result", p_result);
    request.setAttribute("p_query", p_query);
%>
<div class="row">
    <div class="col-lg-6">
        <div class="dataTables_info">
            总记录数为<strong><%=p_result.allRecordCount()%></strong>&nbsp;&nbsp;
            当前显示<strong><%=p_result.curRecordBegin()%></strong>至<strong><%=p_result.curRecordEnd()%></strong>
        </div>
    </div>
    <div class="col-lg-6" style="padding-right:15px">
        <div class="dataTables_info pull-right" id="dataTables-example_info" role="alert" aria-live="polite" aria-relevant="all">
            <ul class="pagination" style="margin:0 0;">
                <c:choose>
                    <c:when test="${p_query.getPageNumber() > 1}">
                        <li class="paginate_button previous" aria-controls="dataTables-example" tabindex="0">
                            <a href="<%=p_query.getPreviousUrl()%>">上一页</a>
                        </li>
                    </c:when><c:otherwise>
                        <li class="paginate_button previous disabled" aria-controls="dataTables-example" tabindex="0">
                            <a href="javascript:void(0);">上一页</a>
                        </li>
                    </c:otherwise>
                </c:choose>

                <%
                    for (int p_number = 1 ; p_number <= p_result.allPageCount() ; p_number++) {
                        if (Math.abs(p_number - p_query.getPageNumber()) <= 4) {
                            request.setAttribute("p_number", p_number);
                %>
                <li class="paginate_button <c:if test="${p_number == p_query.pageNumber}">active</c:if>" aria-controls="dataTables-example" tabindex="0">
                    <a href="<%=p_query.getSpecifyUrl(p_result.allPageCount(), p_number)%>">${p_number}</a>
                </li>
                <%
                        }
                    }
                %>

                <c:choose>
                    <c:when test="${p_query.getPageNumber() < p_result.allPageCount()}">
                        <li class="paginate_button next" aria-controls="dataTables-example" tabindex="0">
                            <a href="<%=p_query.getNextUrl(p_result.allPageCount())%>">下一页</a>
                        </li>
                    </c:when><c:otherwise>
                        <li class="paginate_button next disabled" aria-controls="dataTables-example" tabindex="0">
                            <a href="javascript:void(0);">下一页</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</div>
