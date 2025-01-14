<%@page import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://rhythmyx.percussion.com/components"
	prefix="rxcomp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	


<%
/* This servlet is a generalized site/folder chooser. Originally it was 
just used with the Action Panel. Therefore, you will still see some action
panel language, such as I18N keys that refer to that heritage. */

String path = request.getContextPath();
String basePath = request.getScheme() + "://"
          + request.getServerName() + ":" + request.getServerPort() + path
          + "/";
%>

<html>
<head>
	<base href="<%=basePath%>ui/actionpage">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<link href="../sys_resources/css/actionpagefonts.css" rel="stylesheet" type="text/css"/>
	<link href="../rx_resources/css/actionpagefonts.css" rel="stylesheet" type="text/css"/>
	<link href="../sys_resources/css/actionpagelayout.css" rel="stylesheet" type="text/css"/>
	<title>${rxcomp:i18ntext('jsp_actionpage@Choose a Folder for',
		param.sys_lang)}&#160;${title}</title>
	<%@include file="../header.jsp"%>
</head>
<body>
	<div id="RhythmyxBanner" style="border-bottom:none;">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="action-panel-header">&#160;</td>
				<td align="right"  valign="bottom"><%@ include file="../userstatus-inner.jsp" %></td>
			</tr>
		</table>
	</div>
	<div style="margin: 10pt">
	<p style="font-style: oblique; font-weight: bold; font-size: larger">${rxcomp:i18ntext('jsp_actionpage@Choose item location', param.sys_lang)}</p>
	<ul>
		<c:set var="contentid" value="${param.sys_contentid}" />
		<c:set var="folderlabel" value="${rxcomp:i18ntext('jsp_actionpage@folder', param.sys_lang)}" />
		<c:forEach var="site" items="${siteinfo}" >
			<c:set var="siteid" value="${rxcomp:getSiteIdFromName(site.key)}"/>
			<c:set var="folders" value="${site.value}" />
			<%
				List folders = (List) pageContext.getAttribute("folders");
				int size = folders.size();
				pageContext.setAttribute("size", new Integer(size));
			%>
			<c:choose>
				<c:when test="${size < 2}">
					<c:forEach var="folder" items="${folders}">
						<c:set var="folderid" value="${rxcomp:getFolderIdFromPath(folder)}" />
						<li><a href="${rxcomp:getPanelUrl(contentid,siteid,folderid)}">${site.key}</a></li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<c:forEach var="folder" items="${folders}">
						<c:set var="folderid" value="${rxcomp:getFolderIdFromPath(folder)}" />
						<c:choose>
							<c:when test="${site.key != '*'}">
								<li><a href="${rxcomp:getPanelUrl(contentid,siteid,folderid)}">${site.key}, ${folderlabel} ${folder}</a></li>
							</c:when>
							<c:otherwise>
								<li><a href="${rxcomp:getPanelUrl(contentid,siteid,folderid)}">${folderlabel} ${folder}</a></li>							
							</c:otherwise>
						</c:choose>
					</c:forEach>				
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</ul>
	</div>
</body>
</html>	
