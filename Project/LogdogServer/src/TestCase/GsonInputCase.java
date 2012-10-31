package TestCase;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import logdog.Model.ErrorClassifiedInfo;
import logdog.Model.GsonRefer.ClientReportData;

import com.google.gson.Gson;


@Path("/score")
public class GsonInputCase {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("TestGet")
	public String addScore()
	{	   
		Gson gson = new Gson();
		ClientReportData nData = new ClientReportData(true);    

		//scoreService.AddScore(new Score(id, score));
		return gson.toJson(nData);
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("test")
	public String addScore2()
	{	   
		Gson gson = new Gson();
		ClientReportData nData = new ClientReportData(true);    

		//scoreService.AddScore(new Score(id, score));
		return gson.toJson(nData);
	}
	/**
	 * 일반 String으로 받아두 되며 객체로 받아도 된다.
	 * @since 2012. 10. 31.오전 3:34:55
	 * TODO
	 * @author Karuana
	 * @param track
	 * @return
	 */
	@POST
	@Path("/post")
	@Consumes("application/json")
	public Response createTrackInJSON(ClientReportData track) {
		Gson gson = new Gson();
		//ClientReportData nData = gson.fromJson(track, ClientReportData.class);
		ErrorClassifiedInfo Temp = new ErrorClassifiedInfo("aaa","bbb");
		String result = "Track saved : " + track.ErrorName+"    "+Temp.getE_ClassificationCode();
		return Response.status(201).entity(result).build();
 
	}
 
	
	/*public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {		

	    resp.getWriter().println(gson.toJson(nData)+" ");
	    
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {		
	    Gson gson = new Gson();
	    ClientReportData readData = gson.fromJson(req.getParameter("json"), ClientReportData.class);
	    
	    resp.setContentType("application/json");
	    resp.getWriter().println(gson.toJson(readData));
	    
	    
	}*/
	
}