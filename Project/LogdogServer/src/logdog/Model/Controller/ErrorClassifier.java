package logdog.Model.Controller;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import logdog.Model.ErrorClassifiedInfo;
import logdog.Util.GAE.PMF;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;

public class ErrorClassifier {

	private PersistenceManager jdoConnector;
	
	
	
	public ErrorClassifier() {
		super();
		jdoConnector = null;
	}

	public boolean IsErrorType(String name, String classname)
	{
		//���� ����, ���� ����, 
		jdoConnector = PMF.getPMF().getPersistenceManager();
		ErrorClassifiedInfo SearchError=null;
		
		Query SearchQuery = jdoConnector.newQuery(ErrorClassifiedInfo.class);
		List<ErrorClassifiedInfo> ErrorTypeResults=null;
		boolean isError=false;

		try{
		
			
			SearchQuery.setFilter("ErrorName == errorId && OccurrenceClass == ClassName");
			SearchQuery.declareParameters("String errorId,String ClassName");
	
			ErrorTypeResults = (List<ErrorClassifiedInfo>) SearchQuery.execute(name, classname);
			isError = (ErrorTypeResults.size()==0) ? false: true; 
				
		}
		catch(Exception e){
				
		
		}
		finally{
			SearchQuery.closeAll();
			jdoConnector.close();
			
		}
		return isError;
	}
	
	public Key InsertErrorType(String name, String classname)
	{
		jdoConnector = PMF.getPMF().getPersistenceManager();
		
		ErrorClassifiedInfo eType;
	
		try{
				eType = new ErrorClassifiedInfo(name,classname);
				jdoConnector.makePersistent(eType);
		}
		catch(Exception e){
				
			return null;
				
		}
		finally{
		
			jdoConnector.close();
			
		}
		return eType.getE_ClassificationCode();
	}
	
	public boolean UpdateErrorType(String name, String classname)
	{
		jdoConnector = PMF.getPMF().getPersistenceManager();
		
		ErrorClassifiedInfo eType = getErrorTypeInfo(name,classname);
		
		if(eType == null)
		{
			return false;
		}

		try{
			eType.updateError();
		}
		catch(Exception e){
		
			return false;
		
		}
		finally{
		
			jdoConnector.close();
		
		}
		
		return true;
	}
	
	public ErrorClassifiedInfo getErrorTypeInfo(String name, String classname)
	{	
		jdoConnector = PMF.getPMF().getPersistenceManager();
		
		ErrorClassifiedInfo SearchError=null;
		
		Query SearchQuery = jdoConnector.newQuery(ErrorClassifiedInfo.class);
		List<ErrorClassifiedInfo> ErrorTypeResults=null;


		try{
		
			
			SearchQuery.setFilter("ErrorName == errorId && OccurrenceClass == ClassName");
			SearchQuery.declareParameters("String errorId,String ClassName");
	
			ErrorTypeResults = (List<ErrorClassifiedInfo>) SearchQuery.execute(name, classname);

				
		}
		catch(Exception e){
				
		
		}
		finally{
			SearchQuery.closeAll();
			jdoConnector.close();
			
		}
		
		
		return (ErrorTypeResults.size()==0) ? null: ErrorTypeResults.get(0);
		
	}
	
	public ErrorClassifiedInfo getErrorTypeInfo(Key ErrorKey)
	{
	
		jdoConnector = PMF.getPMF().getPersistenceManager();
			
		ErrorClassifiedInfo SearchError=null;
			
		Query SearchQuery = jdoConnector.newQuery(ErrorClassifiedInfo.class);
		List<ErrorClassifiedInfo> ErrorTypeResults=null;


		try{
			
				
			SearchQuery.setFilter("E_ClassificationCode == ErrorKey");
			SearchQuery.declareParameters("Key ErrorKey");
		
			ErrorTypeResults = (List<ErrorClassifiedInfo>) SearchQuery.execute(ErrorKey);
			}
		catch(Exception e){
					
			
		}
		finally{
			SearchQuery.closeAll();
			jdoConnector.close();
				
		}
			
			
		return (ErrorTypeResults.size()==0) ? null: ErrorTypeResults.get(0);
			
	}
	
	public Key getErrorTypeKey(String name, String classname)
	{
		jdoConnector = PMF.getPMF().getPersistenceManager();
		
		ErrorClassifiedInfo SearchError=null;
			
		Query SearchQuery = jdoConnector.newQuery(ErrorClassifiedInfo.class);
		List<ErrorClassifiedInfo> ErrorTypeResults=null;
		Key ErrorKey=null;

		try{		
			SearchQuery.setFilter("E_ClassificationCode == ErrorKey");
			SearchQuery.declareParameters("Key ErrorKey");
		
			ErrorTypeResults = (List<ErrorClassifiedInfo>) SearchQuery.execute(ErrorKey);
			if(ErrorTypeResults.size()>0)
			{
				ErrorKey = ErrorTypeResults.get(0).getE_ClassificationCode();
			}
			
		}
		catch(Exception e){
					
			
		}
		finally{
			SearchQuery.closeAll();
			jdoConnector.close();
				
		}
			
			
		return ErrorKey;
	}

	public void LinkedCallStack(String name, String classname,BlobKey callstackKey)
	{
		ErrorClassifiedInfo LinkData = getErrorTypeInfo(name,classname);
		LinkData.setCallstackBlobKey(callstackKey);

	}
}