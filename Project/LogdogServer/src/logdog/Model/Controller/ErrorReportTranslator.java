package logdog.Model.Controller;

import java.util.List;

import javax.jdo.PersistenceManager;

import logdog.Model.ErrorClassifiedInfo;
import logdog.Model.ErrorReportInfo;
import logdog.Util.GAE.PMF;

import com.google.appengine.api.datastore.Key;

public class ErrorReportTranslator {
	
	private PersistenceManager jdoConnector;
	
	private ErrorClassifier EClassifier;
	
	public ErrorReportTranslator() {
		super();
		jdoConnector=null;
		EClassifier=new ErrorClassifier();
	}
	
	public Key insertErrorReport()
	{
		jdoConnector = PMF.getPMF().getPersistenceManager();
		
		ErrorClassifiedInfo eType;
		
		try{
			
			
			//	jdoConnector.makePersistent(eType);
		}
		catch(Exception e){
				
			return null;
				
		}
		finally{
		
			jdoConnector.close();
			
		}
	//	return eType.getE_ClassificationCode();
		return null;
	}
	
	public List<ErrorReportInfo> getErrorReport(Key ErrorType)
	{
		return null;
	}
	
	public boolean deleteErrorReport(Key reportKey)
	{
		return true;
	}	
	
}
