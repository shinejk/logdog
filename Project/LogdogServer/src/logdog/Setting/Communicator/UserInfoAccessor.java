package logdog.Setting.Communicator;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import logdog.Setting.Controller.DeveloperChecker;
import logdog.Setting.Controller.LogdogSetter;
import logdog.Setting.DTO.LoginStateInfo;

@Path("/UserSetting")
public class UserInfoAccessor {

	@Context UriInfo uriInfo;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/LogIn")
	public Response DeveloperLogin()
	{	
		DeveloperChecker dChecker = new DeveloperChecker();
		UriBuilder RedirectPath = uriInfo.getBaseUriBuilder();
		try{   
		LoginStateInfo loginState = dChecker.DevleoprLogin();
		switch(loginState.getLoginstate())
		{
			case FIRST_LOGIN:
				RedirectPath.path("UserSignUp.html").queryParam("user", loginState.getUserNmae());
				break;
			case AUTHORIZED_DEVELOPER:
				RedirectPath.path("DashboardRedireter.html");
				break;
			default:
				RedirectPath.path("NotPermission.html");
				break;
		}
		
			
		}
		catch(Exception e)
		{
			throw new WebApplicationException(500);
		}
		return Response.seeOther(RedirectPath.build()).build();
	}
	
	  @POST
	  @Path("/StarterSet")
	  @Produces(MediaType.TEXT_HTML)
	  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	  public Response newTodo(
	      @FormParam("LogSetting") String Log,
	      @Context HttpServletResponse servletResponse) throws IOException {

		  UriBuilder RedirectPath = uriInfo.getBaseUriBuilder();
		  try{
			  System.out.print(Log);
		  boolean setting = new Boolean(Log);
		  DeveloperChecker dveChecker = new DeveloperChecker();
		  LogdogSetter setter = new LogdogSetter();
		  
		  UserService userService = UserServiceFactory.getUserService();
		  User user= userService.getCurrentUser();
		  
		  dveChecker.insertDeveloper(user.getEmail());
		  
		  setter.InitLogdogSettingInfo(setting);
		  RedirectPath.path("DashboardRedireter.html");
		  }
		  catch(Exception e)
		  {
			  RedirectPath.path("NotPermission.html");
			  e.printStackTrace();
			  System.out.println(e.getClass() + "  " + e.getCause());
		  }
		  return Response.seeOther(RedirectPath.build()).build();

	  }
	
}
