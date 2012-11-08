<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<html>
  <head>
    <title>LogDog -The only choice for your development -</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="/assets/css/bootstrap.css" rel="stylesheet">
    <link href="/assets/css/bootstrap-responsive.css" rel="stylesheet">
    <link href="/assets/css/docs.css" rel="stylesheet">
    <link href="/assets/js/google-code-prettify/prettify.css" rel="stylesheet">
 

    
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <!-- Fav and touch icons -->
    <link rel="shortcut icon" href="/assets/ico/favicon.ico">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="/assets/ico/apple-touch-icon-57-precomposed.png">
    
    <script type='text/javascript' src='https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js'></script>

	<script>
	var Request = function() {
	    this.getParameter = function( name ) {
	        var rtnval = '';
	        var nowAddress = unescape(location.href);
	        var parameters = (nowAddress.slice(nowAddress.indexOf('?')+1,nowAddress.length)).split('&');
	        for(var i = 0 ; i < parameters.length ; i++)
	        {
	            var varName = parameters[i].split('=')[0];
	            if(varName.toUpperCase() == name.toUpperCase())
	            {
	                rtnval = parameters[i].split('=')[1];
	               	rtnval = rtnval.split('#')[0];
	                break;
	            }
	        }
	        return rtnval;
	    };
	};
	</script>
     <%
     UserService userService = UserServiceFactory.getUserService();
     if (!userService.isUserLoggedIn()) {
  
	   response.sendRedirect(userService.createLoginURL("../Setting/UserSetting/LogIn"));
    } 
   %>
  </head>
  
  <body data-spy="scroll" data-target=".bs-docs-sidebar">
	<div class="navbar navbar-inverse navbar-fixed-top">
    	<div class="navbar-inner">
        	<div class="container-fluid">
        		<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            		<span class="icon-bar"></span>
         		   	<span class="icon-bar"></span>
            		<span class="icon-bar"></span>
          		</a>
          		<a class="brand" href="#">Logdog</a>
          		
          		<div class="nav-collapse collapse">
            			<p class="navbar-text pull-right">
             			 <a href="<%=userService.createLogoutURL("../index.jsp")%>" class="navbar-link">logout</a></p>
     	       			<ul class="nav">
        	    		 <li class="active"><a href="#">Intro</a></li>
           	   			</ul>
          		</div><!--/.nav-collapse -->
        	</div>
      	</div>
    </div>
    	
	<header class="jumbotron subhead" id="overview">
 		 <div class="container">
    		<h1>DashBoard!!</h1>
    		<p class="lead">LogDog Information and Setting for Starter</p>
 		 </div>
	</header>
	
	<div class="container">
    <!-- Docs nav
    ================================================== -->
    <div class="row">


	</div>
	</div>
    <!-- Footer
    ================================================== -->
    <footer class="footer">
      <div class="container">
        <p class="pull-right"><a href="#">Back to top</a></p>
        <p>Designed and built with all the love in the world by <a href="http://twitter.com/mdo" target="_blank">@mdo</a> and <a href="http://twitter.com/fat" target="_blank">@fat</a>.</p>
        <p>Code licensed under <a href="http://www.apache.org/licenses/LICENSE-2.0" target="_blank">Apache License v2.0</a>, documentation under <a href="http://creativecommons.org/licenses/by/3.0/">CC BY 3.0</a>.</p>
        <p><a href="http://glyphicons.com">Glyphicons Free</a> licensed under <a href="http://creativecommons.org/licenses/by/3.0/">CC BY 3.0</a>.</p>
        <ul class="footer-links">
          <li><a href="http://blog.getbootstrap.com">Blog</a></li>
          <li class="muted">&middot;</li>
          <li><a href="https://github.com/twitter/bootstrap/issues?state=open">Issues</a></li>
          <li class="muted">&middot;</li>
          <li><a href="https://github.com/twitter/bootstrap/wiki">Roadmap and changelog</a></li>
        </ul>
      </div>
    </footer>



    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/assets/js/jquery.js"></script>
    <script src="/assets/js/bootstrap-transition.js"></script>
    <script src="/assets/js/bootstrap-alert.js"></script>
    <script src="/assets/js/bootstrap-modal.js"></script>
    <script src="/assets/js/bootstrap-dropdown.js"></script>
    <script src="/assets/js/bootstrap-scrollspy.js"></script>
    <script src="/assets/js/bootstrap-tab.js"></script>
    <script src="/assets/js/bootstrap-tooltip.js"></script>
    <script src="/assets/js/bootstrap-popover.js"></script>
    <script src="/assets/js/bootstrap-button.js"></script>
    <script src="/assets/js/bootstrap-collapse.js"></script>
    <script src="/assets/js/bootstrap-carousel.js"></script>
    <script src="/assets/js/bootstrap-typeahead.js"></script>
	<script src="http://code.highcharts.com/highcharts.js"></script>
	<script src="http://code.highcharts.com/modules/exporting.js"></script>
  </body>
  
</html>
