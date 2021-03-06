package logdog.DashBoard.Controller;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import logdog.Common.TimeUtil;
import logdog.Common.DataStore.PMF;
import logdog.DashBoard.DTO.Json.Highcharts.ClassReportRate;
import logdog.DashBoard.DTO.Json.Highcharts.DayReport;
import logdog.DashBoard.DTO.Json.Highcharts.MonthReport;
import logdog.DashBoard.DTO.Json.Highcharts.VersionReportRate;
import logdog.DashBoard.DTO.Json.Highcharts.WeekReport;
import logdog.ErrorReport.DAO.AppVesionInfo;
import logdog.ErrorReport.DAO.Summary.ClassErrorInfo;
import logdog.ErrorReport.DAO.Summary.DayReportInfo;
import logdog.ErrorReport.DAO.Summary.MonthReportInfo;
import logdog.ErrorReport.DAO.Summary.VersionReportInfo;
import logdog.ErrorReport.DAO.Summary.WeekReportInfo;

import com.google.gson.Gson;

/**
 *  요청받은 그래프를 그리기 위한 정보를 DataStore를 수집하여 Json 객체로 만들어주는 Controller이다.
 * @since 2012. 11. 15.오전 6:22:27
 * TODO
 * @author Karuana
 */
public class SummaryGetter {

	/**
	 *	하이차트에 사용하는 데이터 형태로 최근 접소된 날짜별 에러 정보를 리턴한다.
	 *	리턴 데이터는 Json이며 최대 7일 기준으로 리턴된다.
	 * @since 2012. 11. 10.오전 2:01:45
	 * TODO
	 * @author Karuana
	 * @return Json
	 */
	public String getDayErrorRate(int Interval)
	{
		PersistenceManager jdoConnector = PMF.getPMF().getPersistenceManager();

		Query SearchQuery = jdoConnector.newQuery(DayReportInfo.class);
		List<DayReportInfo> ErrorTypeResults=null;


		try{
			//최대 30개만 가져온다.
			SearchQuery.setFilter("TotalCode <= Timecode");
			SearchQuery.declareParameters("int Timecode");
			SearchQuery.setRange(0,Interval);
			SearchQuery.setOrdering("TotalCode descending");	
			
			int YearCode = TimeUtil.GetNowYear();
			int TimeCode = TimeUtil.GetNowTimeCode();
			ErrorTypeResults = (List<DayReportInfo>) 
								SearchQuery.execute(YearCode*10000+TimeCode);


			Iterator<DayReportInfo> iterator = ErrorTypeResults.iterator();
			
			DayReport report = new DayReport();  

			DayReportInfo prevData=null;
			int size= 0;
			while ( iterator.hasNext() ){
				DayReportInfo info = iterator.next();
				
				int NonData= (prevData==null) ? TimeCode - info.getMDay() : prevData.getMDay() - info.getMDay()-1;//1일이상 차이나는지 체크
				int code = (prevData==null) ? TimeCode+1: prevData.getMDay();
				YearCode = (prevData==null) ? YearCode: prevData.getYear();
				NonData= NonData+(YearCode-info.getYear())*Interval;//년도가 넘치면 Max만
				for(int i=1;i<=NonData;i++)
				{
					report.AddDay(TimeUtil.minTimCode(YearCode, code , -1*i));	//현재 TimeCode 연산을 단순 덧셈으로 하기 때문에 나중에 변경할 필요가 있다.
				
					report.AddReportRate(0);
					if(++size ==Interval)
						break;
				}
				if(size==Interval)
					break;
				
				report.AddDay(info.getMDay());
				report.AddReportRate(info.getTotalOccurrences());
				prevData = info;
				
				if(++size==Interval)
					break;
			  }
			int StartDate = (prevData==null) ? TimeCode:prevData.getMDay()-1;	//잠재적 에러 요소 1월 1일이면?
			int j=1;
			YearCode = (prevData==null) ? YearCode: prevData.getYear();
			for(int i=report.getSize();i<Interval;i++)
			{
				
				report.AddDay(StartDate);
				report.AddReportRate(0);
				StartDate=TimeUtil.minTimCode(YearCode,StartDate,-1);
			}
			
			Gson gson = new Gson();
			return gson.toJson(report);
		}
		catch(Exception e){
					
			e.printStackTrace();
			return null;
		}
		finally{
			SearchQuery.closeAll();
			jdoConnector.close();
				
		}
	}
	/**
	 *	Week별 그래프를 그려주기 위한 Json을 리턴한다.
	 * @since 2012. 11. 18.오후 8:31:09
	 * TODO
	 * @author Karuana
	 * @param Interval
	 * @return
	 */
	public String getWeekDayErrorRate(int Interval)
	{
		PersistenceManager jdoConnector = PMF.getPMF().getPersistenceManager();

		Query SearchQuery = jdoConnector.newQuery(WeekReportInfo.class);
		List<WeekReportInfo> ErrorTypeResults=null;


		try{
			//최대 30개만 가져온다.
			SearchQuery.setFilter("TotalCode <= Timecode");
			SearchQuery.declareParameters("int Timecode");
			SearchQuery.setRange(0,Interval);
			SearchQuery.setOrdering("TotalCode descending");	
			
			int YearCode = TimeUtil.GetNowYear();
			int WeekCode = TimeUtil.GetWeek();
			ErrorTypeResults = (List<WeekReportInfo>) 
								SearchQuery.execute(YearCode*100+ WeekCode);


			Iterator<WeekReportInfo> iterator = ErrorTypeResults.iterator();
			
			WeekReport report = new WeekReport();  

			WeekReportInfo prevData=null;
			int size=0;
			while ( iterator.hasNext() ){
				WeekReportInfo info = iterator.next();
				
				int NonData= (prevData==null) ? WeekCode - info.getWeek() : prevData.getWeek() - info.getWeek()-1;//
				int code = (prevData==null) ? WeekCode+1: prevData.getWeek();
				YearCode = (prevData==null) ? YearCode: prevData.getYear();
			
				if(YearCode-info.getYear()!=0)
					NonData+=TimeUtil.MaxWeekCount(YearCode-1);//하나만 더해도 50개가 넘어간다 즉 처리할 필요 없을듯 ㅇ
					
				for(int i=0;i<NonData;i++)
				{
					if(code == 1 )
					{
						YearCode = YearCode-1;
						code =TimeUtil.MaxWeekCount(YearCode)+1;
					}
					report.AddWeek(YearCode, --code);	//현재 TimeCode 연산을 단순 덧셈으로 하기 때문에 나중에 변경할 필요가 있다.
					report.AddReportRate(0);
					if(++size==Interval)
						break;
				}
				if(size==Interval)
					break;
				report.AddWeek(info.getYear(),info.getWeek());
				report.AddReportRate(info.getTotalOccurrences());
				prevData = info;
				if(++size==Interval)
					break;
			  }
			int StartDate = (prevData==null) ? WeekCode:prevData.getWeek()-1;
			int j=1;
			YearCode = (prevData==null) ? YearCode: prevData.getYear();
			for(int i=report.getSize();i<Interval;i++)
			{
				if(StartDate == 0 )
				{
					YearCode = YearCode-1;
					StartDate =TimeUtil.MaxWeekCount(YearCode);
				}
				report.AddWeek(YearCode,StartDate--);
				report.AddReportRate(0);
			}
			
			Gson gson = new Gson();
			return gson.toJson(report);
		}
		catch(Exception e){
					
			e.printStackTrace();
			return null;
		}
		finally{
			SearchQuery.closeAll();
			jdoConnector.close();
				
		}
	}
	
	/**
	 *	월별 그래프를 그려주기위한 Json을 반환한다.
	 * @since 2012. 11. 18.오후 8:31:47
	 * TODO
	 * @author Karuana
	 * @param Interval
	 * @return
	 */
	public String getMonthErrorDate(int Interval){
		PersistenceManager jdoConnector = PMF.getPMF().getPersistenceManager();

		Query SearchQuery = jdoConnector.newQuery(MonthReportInfo.class);
		List<MonthReportInfo> ErrorTypeResults=null;


		try{
			//최대 30개만 가져온다.
			SearchQuery.setFilter("TotalCode <= Timecode");
			SearchQuery.declareParameters("int Timecode");
			SearchQuery.setRange(0,Interval);
			SearchQuery.setOrdering("TotalCode descending");	
			
			int YearCode = TimeUtil.GetNowYear();
			int MonthCode = TimeUtil.GetNowTimeCode()/100;
			ErrorTypeResults = (List<MonthReportInfo>) 
								SearchQuery.execute(YearCode*100+ MonthCode);


			Iterator<MonthReportInfo> iterator = ErrorTypeResults.iterator();
			
			MonthReport report = new MonthReport();  

			MonthReportInfo prevData=null; 
			int size=0;
			while ( iterator.hasNext() ){
				MonthReportInfo info = iterator.next();
				
				int NonData= (prevData==null) ? MonthCode - info.getMonth(): prevData.getMonth() - info.getMonth()-1;
			
				int code = (prevData==null) ? MonthCode+1: prevData.getMonth();
				YearCode = (prevData==null) ? YearCode: prevData.getYear();
				NonData= NonData+(YearCode-info.getYear())*12;
				System.out.println(NonData);
				for(int i=0;i<NonData;i++)
				{
					if(code == 1 )
					{
						YearCode = YearCode-1;
						code =13;//12월 달로 컴백
					}
					report.AddMonth(YearCode, --code);	//현재 TimeCode 연산을 단순 덧셈으로 하기 때문에 나중에 변경할 필요가 있다.
					report.AddReportRate(0);
					if(++size==Interval) 
						break;
				}
				if(size==Interval)
					break;
				report.AddMonth(info.getYear(),info.getMonth());
				report.AddReportRate(info.getTotalOccurrences());
				prevData = info;
				if(++size==Interval)
					break;
			  }
			int StartDate = (prevData==null) ? MonthCode:prevData.getMonth()-1;
			int j=1;
			YearCode = (prevData==null) ? YearCode: prevData.getYear();
			
			for(int i=report.getSize();i<Interval;i++)
			{
				if(StartDate == 0 )
				{
					YearCode = YearCode-1;
					StartDate =12;//12월 달로 컴백
				}
				report.AddMonth(YearCode,StartDate--);
				report.AddReportRate(0);
			}
			
			Gson gson = new Gson();
			return gson.toJson(report);
		}
		catch(Exception e){
					
			e.printStackTrace();
			return null;
		}
		finally{
			SearchQuery.closeAll();
			jdoConnector.close();
				
		}
	}
	/**
	 *	class별 에러량을 조사하여 해당 그래프를 그리기위한 데이터를 Json으로 리턴한다. 
	 * 
	 * @since 2012. 11. 11.오전 9:14:06
	 * TODO
	 * @author Karuana
	 * @return Json
	 */
	public String getClassErrorRate()
	{
		PersistenceManager jdoConnector = PMF.getPMF().getPersistenceManager();

		Query SearchQuery = jdoConnector.newQuery(ClassErrorInfo.class);
		List<ClassErrorInfo> ErrorTypeResults=null;
		

		try{
			
				
			SearchQuery.setOrdering("TotalOccurrences descending");	
			
			ErrorTypeResults = (List<ClassErrorInfo>) 
								SearchQuery.execute();
			
			Iterator<ClassErrorInfo> iterator = ErrorTypeResults.iterator();
			ClassReportRate report = new ClassReportRate(); 

			while ( iterator.hasNext() ){
				ClassErrorInfo info = iterator.next();
				report.addClassRate(info.getOccurrenceClass(), info.getTotalOccurrences());
			  }
		 
			Gson gson = new Gson();
			return gson.toJson(report);
		}
		catch(Exception e){
					
			return null;
		}
		finally{
			SearchQuery.closeAll();
			jdoConnector.close();
				
		}
	}
	
	/**
	 *	Version별 에러량을 체크하여 그래프를 그리기 위한 Json데이터로 만들어 리턴한다.
	 * @since 2012. 11. 11.오전 9:47:42
	 * TODO
	 * @author Karuana
	 * @return Json
	 */
	public String getVersionRate()
	{
		PersistenceManager jdoConnector = PMF.getPMF().getPersistenceManager();

		Query SearchQuery = jdoConnector.newQuery(AppVesionInfo.class);
		Query OSVSearch = jdoConnector.newQuery(VersionReportInfo.class);
		 
		List<AppVesionInfo> Versions=null;


		try{
			Versions = (List<AppVesionInfo>) 
								SearchQuery.execute();	//우선 서버에 저장된 App version 리스트를 가져온다.
			
			Iterator<AppVesionInfo> iterator = Versions.iterator();

			VersionReportRate Data = new VersionReportRate();
			Data.addAppVersion(Versions);
			int AppCount=0;
			while ( iterator.hasNext() ){
				AppVesionInfo info = iterator.next();
				List<VersionReportInfo> OSversion=null;
				OSVSearch.setFilter("AppVersion == ver");	// 각 App 버젼에 맞는 OS version에 에러량을 가져온다.
				OSVSearch.declareParameters("String ver");
			
				OSversion = (List<VersionReportInfo>) 
						OSVSearch.execute(info.getVersion());
				
				for(int i=0;i<OSversion.size();i++)
				{
					VersionReportInfo vinfo = OSversion.get(i);
					Data.addOSerror(vinfo.getOSVersion(), AppCount, vinfo.getTotalOccurrences());
				}
				
				AppCount++;
				
			  }
		 
			Gson gson = new Gson();
			return gson.toJson(Data);
		}
		catch(Exception e){
					e.printStackTrace();
			return null;
		}
		finally{
			SearchQuery.closeAll();
			jdoConnector.close();
				
		}
	}
	
}
