<%@ page import="com.percussion.services.utils.jspel.PSRoleUtilities, org.jsoup.Jsoup, org.jsoup.safety.Whitelist, org.owasp.encoder.Encode"
import="javax.naming.Context"
import="javax.naming.InitialContext"
import="javax.naming.NamingException"
import="javax.sql.DataSource"
import="java.sql.*"
%>

<%--
  ~     Percussion CMS
  ~     Copyright (C) 1999-2020 Percussion Software, Inc.
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU Affero General Public License for more details.
  ~
  ~     Mailing Address:
  ~
  ~      Percussion Software, Inc.
  ~      PO Box 767
  ~      Burlington, MA 01803, USA
  ~      +01-781-438-9900
  ~      support@percussion.com
  ~      https://www.percusssion.com
  ~
  ~     You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <https://www.gnu.org/licenses/>
  --%>

<%!
private String sanitizeForHtml(String input){
	   String ret = null;
	   if(input != null){
		   ret = Jsoup.clean(input, Whitelist.none());
	   }
	   return ret;
} 


%>


<!-- 	Import all neccesary packages to send and recieve sql queries and updates.
		Import PSO Admin Security-->
<%
String fullrolestr = PSRoleUtilities.getUserRoles();

if (fullrolestr.contains("Admin") == false)
   	response.sendRedirect(response.encodeRedirectURL(request.getContextPath()
      	+ "/ui/RxNotAuthorized.jsp"));

%>
<html>
<head>

<title>
SQL Execution
</title>
<!-- Link to bootstrap CSS and JS preprocessor. -->
<link href="/rx_resources/css/bootstrap/3.3.4/bootstrap.min.css" rel="stylesheet">


</head>
<body>

<style type="text/css">
	textarea.form-control
	{   
		overflow-y: scroll; 
	}
  #DefaultJNDIReplacer{
    position: relative;
    top: -25px;
    float:right;
    border-top: none;
    border-top-right-radius: 0px;
    border-top-left-radius: 0px;
    

  }

</style>

<!-- Main container div for the entire page. This pads both the left and right sides of the page. -->
<div class="container">
  <nav class="navbar navbar-inverse">
    <a class="navbar-brand" href="#">Support Tools</a>
    <ul class="nav navbar-nav">
      <li class="active">
        <a href="#">SQL Editor</a>
      </li>
      <li>
        <a href="/test/logs.jsp">Server Log</a>
      </li>
      <li>
        <a href="/test/search.jsp">Test JSR-170 searches</a>
      </li>
      <li>
        <a href="/test/countFolderItem.jsp">Count Items in Folder</a>
      </li>
      <li>
        <a href="/test/velocitylog.jsp">Velocity Log</a>
      </li>
    </ul>
  </nav>

<%

String dburl="java:jdbc/RhythmyxData";
if(request.getParameter("dburl") != null){
	dburl = request.getParameter("dburl");
}

String dbquery = "";
if(request.getParameter("dbquery")!= null){
	dbquery = sanitizeForHtml(request.getParameter("dbquery"));
}

%>
<!-- THIS FORM CANNOT REDIRECT, thats why its action is a pound. We will be posting to the server and getting back the sql.  -->
<form action="" method="POST" role="form">
	<legend>DB Access</legend>
<div class="well well-lg">
      <button type="button" id="DefaultJNDIReplacer" class="btn btn-default">Default</button>


  <div class="form-group">
    <div class="row">

		<label for="dburl">JNDI Name</label>
		<!-- Do not change the id or name here or the Java Server Page will break on submission of this form. -->

		<input type="text" class="form-control col-xs-8" id="dburl" name="dburl" placeholder="java:jdbc/RhythmyxData" title="JNDI URL" value="<%= sanitizeForHtml(dburl) %>" />
			
		</div>
		<div class="row">
		<label for="dbquery">Sql Query</label>
			<!-- Do not change the id or name here or the Java Server Page will break on submission of this form. We used a textarea because these queries can get a little complicated. -->
			<textarea style="height:100px; max-width:1120px;" name="dbquery" id="dbquery" class="form-control" rows="3" required="required" placeholder="Select * from ContentStatus" title="Database Query"><%= dbquery %></textarea>
		</div>
	</div>

	
	<!-- The submission button. -->
	<button type="submit" href="" class="btn btn-primary">Submit</button>
  </div>
</form>
</div>

<!-- Another container to keep the page looking good -->



<%
if(request.getMethod().equals("POST")){												//when the post is fired at the server...
InitialContext ctx;
  DataSource ds=null;
  Connection conn=null;
  Statement stmt=null;
  ResultSet rs = null;

  try {																				// Attempt to
    ctx = new InitialContext();
    Context envCtx = (Context) ctx.lookup("java:comp/env");							// Get the initial context
    ds = (DataSource) ctx.lookup(request.getParameter("dburl") );					// Get the Context provided by the user (JNDI Name)
    //ds = (DataSource) ctx.lookup("jdbc/MySQLDataSource");
    conn = ds.getConnection();
    stmt = conn.createStatement();													// Finally allow us to send information.

    if (request.getParameter("dbquery").toLowerCase().startsWith("select "))		// If we are querying...
    {
    rs= stmt.executeQuery(request.getParameter("dbquery"));							// Run the query, build a table. (SEE LINES 72 to EOF)
    }
    else
    {
    int i = stmt.executeUpdate(request.getParameter("dbquery"));					// But if we are updating.
    %>
    <h1>Rows Updated: <%=i %></h1>													<!-- Show how many Rows we updated! -->
    </div>
    <%
    return;
    }

    ResultSetMetaData rsmd = rs.getMetaData();										// Get metadata so that we can get the column count.
int columnCount = rsmd.getColumnCount();


%>
<div class="container-fluid">
<table class="table table-hover">
<!-- This is the result table for the query. -->
	<thead>
		<tr>
	
<%		
// The column count starts from 1
for (int i = 1; i < columnCount + 1; i++ ) {
  String name = rsmd.getColumnName(i);												//Create a html column for each column in sql we asked for.
  if(name!=null){
	  name = Encode.forHtml(name);
  }else{name="null";}

  %>
    <th><%= name %></th>
<%  

}
%> </tr><%

    while(rs.next()) {
%>
    <tr>
<%  
	String colData="";
    for (int i = 1; i < columnCount + 1; i++ ) {
    	if(rs.getObject(i)!=null){
    		colData = Encode.forHtml(rs.getObject(i).toString());
    	}else{
    		colData = "null";
    	}
%>
    <td><%= colData %></td>

<%    
    }

%>
    </tr>
<%  

    }
 %>
</table>

<%
}
  catch (SQLException se) {															//Properly handle SQLException. Display SQLException.
%>
    <%= se.getMessage() %>
<%      
  }
  catch (NamingException ne) {														//Properly handle NamingException. Display NamingException.
%>  
    <%= ne.getMessage() %>
<%
  }finally{
 	try{ rs.close();}catch(Exception e){}
 	try{ stmt.close();}catch(Exception e){}
  	try{ conn.close();}catch(Exception e){}

}

}
%>
</body>
<script src="/cm/cui/components/jquery/jquery.min.js"></script>
<script src="/cm/cui/components/jquery-migrate/jquery-migrate.min.js"></script>
<script src="/rx_resources/js/bootstrap/3.3.4/bootstrap.min.js"></script>
<script>
$(document).ready(function(){

  $("#DefaultJNDIReplacer").on("click", function(e) {
    e.preventDefault();
    $("#dburl").val($("#dburl").attr("placeholder"));
     $("#dbquery").val($("#dbquery").attr("placeholder"));
  })

})
</script>
</html>
