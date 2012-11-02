package logdog.ErrorReport.Communicator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import logdog.Common.Json.BooleanResult;
import logdog.ErrorReport.Controller.ErrorTypeClassifier;
import logdog.ErrorReport.DTO.ClientReportData;
import logdog.ErrorReport.DTO.ErrorUniqueID;

import com.google.gson.Gson;

@Path("/Report")
public class ErrorReport {

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/ErrorType/{errName}/{className}")
	public String IsErrorType(
			@PathParam("errName") final String errName,
			@PathParam("className") final String ClassName
			)
	{	   
		Gson gson = new Gson();
		ErrorUniqueID errType = new ErrorUniqueID(errName,ClassName);
		
		ErrorTypeClassifier eTypeClassifier = new ErrorTypeClassifier();
		BooleanResult isType = new BooleanResult(eTypeClassifier.IsErrorType(errType));
	
		return gson.toJson(isType);
	}
	
	@POST
	@Path("/ErrorInfo")
	@Consumes("application/json")
	public Response createTrackInJSON(ClientReportData track) {
		Gson gson = new Gson();
		//ClientReportData nData = gson.fromJson(track, ClientReportData.class);
		//ErrorTypeInfo Temp = new ErrorTypeInfo("aaa","bbb");
		String result = "Track saved : " + track.ErrorName+"    ";//+Temp.getE_ClassificationCode();
		return Response.status(201).entity(result).build();
 
	}

	
	

	/**
	 *
	 * @since 2012. 11. 2.오전 3:08:37
	 * TODO 요청받은 데이터 기록 작업을 Backend로 진행한다.
	 * @author Karuana
	 * @param userReport
	 * @return Response 처리에 대한 응답
	 */
	@POST
	@Path("/DataRegist")
	@Consumes("application/json")
	public Response ErrorRegister(ClientReportData userReport) {
		Gson gson = new Gson();
	//	ErrorTypeInfo Temp = new ErrorTypeInfo("aaa","bbb");
		String result = "Track saved : ";
		return Response.status(201).entity(result).build();
 
	}
	
	
}
