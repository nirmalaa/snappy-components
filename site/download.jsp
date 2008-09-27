<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %><%
    String email = request.getParameter("email");
    boolean showEmail = true;
    if(email != null){
         Class.forName("com.mysql.jdbc.Driver");
         //url = "jdbc:mysql://localhost/" + ClipConfig.DB_NAME;
         String url = "jdbc:mysql://localhost/snappy";
         Connection con = DriverManager.getConnection(url, "root", "");
        String sql = "Insert into Newsletter values (?)";
        PreparedStatement prepStmt = con.prepareStatement(sql);
        prepStmt.setString(1, email);
        prepStmt.executeUpdate();
        prepStmt.close();
        showEmail = false;
    }

%>
<html>
<head>
    <link rel="stylesheet" type="text/css" media="screen, projection" href="style.css"/>
</head>
<body>
<div id="title">
	<div id="logo">Snappy JSF Components</div>
	<div id="logoSub">Developer Preview</div>
</div>
	<div id="banner">
		<span><a href="snappy">Demo</a></span>
		<span><a href="wiki">Wiki</a></span>
		<span><a href="download.jsp">Download</a></span></div>

	<div id="body">
		<div class="infoBox">
			<div class="head">Sign-up</div>
			<div class="content">
                <%
                    if(showEmail){
                %>
                        <div id="email">
                            <form method="post">
                                <p>Sign up for Snappy Table updates.</p>
                                <table><tr><td>E-mail:</td><td><input type="text" name="email" size="80"/> </td></tr>
                                <tr><td colspan="2" align="right"><input type="submit" value="Sign up"/></td></tr></table>
                            </form>
                        </div>
                <%
                    }else{
                %>
                      <div id="email">
                            <form method="post">
                                <h2>Thank you!</h2>
                                <p>Your e-mail has been added to our newsletter.</p>
                            </form>
                        </div>
                <%
                    }
                %>

			</div>
		</div>

		<div class="infoBox">
			<div class="head">Download</div>
			<div class="content">
				Download Developer Preview<br/>

                Sept 13, 2008 - <a href="snappy-rc1.zip">Download - Release Candidate - 1</a>
		 	</div>
		</div>


	</div>



<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
var pageTracker = _gat._getTracker("UA-100555-20");
pageTracker._initData();
pageTracker._trackPageview();
</script>
</body>
</html>
