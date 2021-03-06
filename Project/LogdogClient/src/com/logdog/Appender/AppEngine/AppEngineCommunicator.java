package com.logdog.Appender.AppEngine;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import android.util.Log;

import com.logdog.ErrorReport.ClientReportData;
import com.logdog.common.File.FileControler;
import com.logdog.common.Network.AbstractCommunicator;
import com.logdog.common.Parser.LogDogJsonParser;

/**
 * 구글 AppEngine과의 소통을 위한 커뮤니케이터(네트워크를 담당한다)
 * @since 2012. 11. 11.오후 11:39:47
 * TODO
 * @author JeongSeungsu
 */
@Root
public class AppEngineCommunicator extends AbstractCommunicator {
	
		
	/**
	 * AppEngine URL
	 */
	@Element
	private String URL;
	
	/**
	 * 보낼 파일들이 저장된 폴더
	 */
	String SaveDir;
	/**
	 * 보낼 에러 리포트 파일 이름
	 */
	String ErrorReportFileName;
	
	Map<String,String>		m_SendData;
	
	private final String ErrorCheckUrl = "ErrorType";
	
	private final String SendUserInfoUrl = "UserInfo";
	
	private final String LogSettingUrl = "LogSetting";
	

	/**
	 *
	 * @since 2012. 11. 15.오전 5:53:06
	 * TODO
	 * @author JeongSeungsu
	 */
	public AppEngineCommunicator() {
		// TODO Auto-generated constructor stub
		super();
		m_SendData  	 = new HashMap<String, String>();
	}
	/**
	 *
	 * @since 2012. 11. 11.오후 11:40:45
	 * TODO
	 * @author JeongSeungsu
	 * @param CommunicatorName 커뮤니케이터 등록
	 * @param url URL 등록
	 */
	public AppEngineCommunicator(String CommunicatorName,String url) {
		// TODO Auto-generated constructor stub
		super(CommunicatorName);
		m_SendData  	 = new HashMap<String, String>();
		URL				 = url;
	}
	/**
	 * 세이브된 디렉토리 이름 설정
	 * @since 2012. 11. 15.오전 5:54:14
	 * TODO
	 * @author JeongSeungsu
	 * @param savedir
	 */
	public void SetSavedDir(String savedir){
		SaveDir = savedir;
	}
	/**
	 * 세이브된 에러리포트이름 설정 
	 * @since 2012. 11. 15.오전 5:54:27
	 * TODO
	 * @author JeongSeungsu
	 * @param errorreportfilename
	 */
	public void SetSavedErrorReportFileName(String errorreportfilename){
		ErrorReportFileName = errorreportfilename;
	}
	/**
	 * URL 설정
	 * @since 2012. 11. 11.오후 11:41:03
	 * TODO
	 * @author JeongSeungsu
	 * @param url
	 */
	public void SetURL(String url){
		URL = url;
	}
	public synchronized boolean SendData() {
		
		// TODO Auto-generated method stub
		File[] SendFileList = FileControler.ExternalStorageDirectoryFileList(SaveDir);
		
		//보낼 파일이 없다면 그냥 성공
		if(SendFileList.length == 0)
			return true;
		
		//get해와야함..
		
		//post로 각각을 보내기 일단 먼저 에러리포트 전송
		for (File file : SendFileList) {
			
			if(file.getName().matches("(?i).*"+ErrorReportFileName+".*")){
				String Content 		  = FileControler.FiletoString(file);
				ClientReportData Data = (ClientReportData) LogDogJsonParser.fromJson(Content, ClientReportData.class);
				OneSendData(Data);
			}
		}
		return true;
	}
	/**
	 * 하나의 데이터만 보낸다.
	 * @since 2012. 11. 15.오전 5:55:47
	 * TODO
	 * @author JeongSeungsu
	 * @param data 보낼 에러리포트
	 */
	public void OneSendData(ClientReportData data) {
		
		String Content 		  = LogDogJsonParser.toJson(data);
		File callstackfile 	  = FileControler.GetExternalStorageFile(SaveDir, 
																	 data.CallStackFileName);
		File LogFile 		  = FileControler.GetExternalStorageFile(SaveDir, 
																	  data.LogFileName);
		
		AllDeleteSendData();
		AddSendData("JSon/ErrorReport", Content);
		AddSendData("CallStack", FileControler.FiletoString(callstackfile));
		AddSendData("Log", FileControler.FiletoString(LogFile));
		AddSendData("ErrorName", data.ErrorName);
		AddSendData("ErrorClassName", data.ErrorClassName);
		AddSendData("ErrorLine", String.valueOf(data.line));

		SendMessage(GetSendData());
		
	}

	
	/**
	 * 앱이 사망할시 전송할 데이터만 보낸다.
	 * @since 2012. 11. 15.오전 5:56:07
	 * TODO
	 * @author JeongSeungsu
	 * @param data 보낼 에러리포트
	 * @throws InterruptedException
	 */
	public void EmergencySendData(ClientReportData data) throws InterruptedException {
		
		String Content 		  = LogDogJsonParser.toJson(data);
		File callstackfile 	  = FileControler.GetExternalStorageFile(SaveDir, 
																	 data.CallStackFileName);
		File LogFile 		  = FileControler.GetExternalStorageFile(SaveDir, 
																	  data.LogFileName);
		
		AllDeleteSendData();
		AddSendData("JSon/ErrorReport", Content);
		AddSendData("CallStack", FileControler.FiletoString(callstackfile));
		AddSendData("Log", FileControler.FiletoString(LogFile));
		AddSendData("ErrorName", data.ErrorName);
		AddSendData("ErrorClassName", data.ErrorClassName);
		AddSendData("ErrorLine", String.valueOf(data.line));

		HTTPSender Sender = new HTTPSender(GetSendData());
		Sender.start();
		Sender.join();
		
	}
	
	/**
	 * Map 형태로 보낼 데이터를 넣어준다.
	 * @since 2012. 11. 15.오전 5:56:34
	 * TODO
	 * @author JeongSeungsu
	 * @param Key 키 
	 * @param Data 데이터
	 */
	private void AddSendData(String Key,String Data){
		m_SendData.put(Key, Data);
	}
	/**
	 * 모든 보낼 데이터를 삭제한다.
	 * @since 2012. 11. 15.오전 5:56:55
	 * TODO
	 * @author JeongSeungsu
	 */
	private void AllDeleteSendData(){
		m_SendData.clear();
	}
	/**
	 * 보낼 데이터를 얻는다.
	 * @since 2012. 11. 15.오전 5:57:04
	 * TODO
	 * @author JeongSeungsu
	 * @return 
	 */
	private Map<String,String> GetSendData(){
		return m_SendData;
	}
	
	
	/**
	 * 여기서 앱엔진과의 통신을 한다.
	 * @since 2012. 11. 11.오후 11:41:25
	 * TODO
	 * @author JeongSeungsu
	 * @param SendData 보낼데이터들
	 * @return
	 */
	public boolean SendMessage(Map<String, String> SendData) {

		HTTPSender Sender = new HTTPSender(SendData);
		Sender.start();
		return true;
		
	}

	/**
	 * HTTPSenderThread 쓰레드를 이용해서 보낸다!
	 * @since 2012. 11. 12.오전 5:31:53
	 * TODO
	 * @author JeongSeungsu
	 */
	public class HTTPSender extends Thread {

		private Map<String, String> SendData;

		public HTTPSender(Map<String, String> senddata) {
			// TODO Auto-generated constructor stub
			SendData = senddata;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			boolean fail = true;

			String errorname = SendData.get("ErrorName");
			String errorclassname = SendData.get("ErrorClassName");
			int errorline = Integer.parseInt(SendData.get("ErrorLine"));
			String CallStackString = SendData.get("CallStack");
			String ClientReportJson = SendData.get("JSon/ErrorReport");
			String LogData = SendData.get("Log");
			
			

			// URL에 관한 문자셋으로 변환
			String URLerrorname = null;
			String URLerrorclassname = null;
			try {
				URLerrorname = URLEncoder.encode(errorname, "UTF-8");
				URLerrorclassname = URLEncoder.encode(errorclassname, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 1 현재 에러가 서버에 존재하는지 체크
			BooleanResult result = HttpGetExistErrorCheck(URLerrorname,
					URLerrorclassname, errorline);

			if (result == null)
				fail = false;

			// 2 성공시 콜스택과 에러정보를 보낸다.
			if (!result.isResult()) {
				if (!HttpPostSendNewError(errorname, errorclassname, errorline,
						CallStackString))
					fail = false;
			}

			// 2.1 유져 정보를 보낸다. 여기서 키값을 가져옴
			String LogKey = HttpPostSendUserInfo(ClientReportJson);

			if (LogKey == null)
				fail = false;

			// 3 서버에 로그를 전송할지 안할지 서버 셋팅에 따른 체크
			BooleanResult Logresult = HttpGetLogSendCheck();

			if (Logresult == null)
				fail = false;

			// 3.1 만약 서버에서 로그 받는걸 허용한다면 로그 전송
			if (Logresult.isResult()) {
				if (!HttpPutSendLog(LogData, LogKey))
					fail = false;
			}

			if(fail){
				//ErrorReportData, CallStackFile, LogFile 삭제..
				ClientReportData data = (ClientReportData) LogDogJsonParser.fromJson(ClientReportJson, ClientReportData.class);
				FileControler.DeleteFile(SaveDir, data.ReportTime+ErrorReportFileName);
				FileControler.DeleteFile(SaveDir, data.CallStackFileName);
				FileControler.DeleteFile(SaveDir, data.LogFileName);
			}
		}

		/**
		 * 서버에 이 에러가 있나 없나 체크
		 * 
		 * @since 2012. 11. 12.오전 1:19:08 TODO
		 * @author JeongSeungsu
		 * @param ErrorName
		 * @param ClassName
		 * @param errorline
		 * @return null값을 리턴하면 전혀 통신이 안된것이고 True,False는 데이터가 존재하는지 안하는지 리턴..
		 */
		private BooleanResult HttpGetExistErrorCheck(String ErrorName,
				String ClassName, int errorline) {
			return HttpGetSend(URL + ErrorCheckUrl + "/" + ErrorName + "/"
					+ ClassName + "/" + String.valueOf(errorline));
		}

		/**
		 * 로그값을 받냐 안받냐
		 * 
		 * @since 2012. 11. 5.오전 3:46:06 TODO
		 * @author JeongSeungsu
		 * @return null값을 리턴하면 전혀 통신이 안된것이고 True,False는 데이터가 존재하는지 안하는지 리턴..
		 */
		private BooleanResult HttpGetLogSendCheck() {
			return HttpGetSend(URL + LogSettingUrl);
		}

		/**
		 * 새로운 에러면 서버로 전송
		 * 
		 * @since 2012. 11. 12.오전 1:19:33 TODO
		 * @author JeongSeungsu
		 * @param ErrorName
		 * @param ClassName
		 * @param errorline
		 * @param CallStack
		 * @return 성공하면 True 실패면 False;
		 */
		private boolean HttpPostSendNewError(String ErrorName,
				String ClassName, int errorline, String CallStack) {

			ArrayList<String> callstackarray = new ArrayList<String>();

			String[] StrArray;
			StrArray = CallStack.split("\n");

			for (String s : StrArray) {
				callstackarray.add(s);
			}
			CallStackInfo info = new CallStackInfo(ErrorName, ClassName,
					errorline, callstackarray);

			String CallStackInfoJson = LogDogJsonParser.toJson(info);
			String SendUrl = URL + ErrorCheckUrl;

			String Response = HttpPostSendJson(SendUrl, CallStackInfoJson);

			if (Response == null)
				return false;

			return true;
		}

		/**
		 * ClientErrorReport전송
		 * 
		 * @since 2012. 11. 5.오전 3:34:51 TODO
		 * @author JeongSeungsu
		 * @param ClientReportDataJson
		 * @return 실패시 null값 리턴 성공시 키값 가져옴
		 */
		private String HttpPostSendUserInfo(String ClientReportDataJson) {
			String SendUrl = URL + SendUserInfoUrl;
			String Response = HttpPostSendJson(SendUrl, ClientReportDataJson);

			if (Response == null)
				return null;

			return Response;
		}

		/**
		 * 로그 전송
		 * 
		 * @since 2012. 11. 5.오전 4:01:17 TODO
		 * @author JeongSeungsu
		 * @param LogData
		 * @param Key
		 * @return
		 */
		private boolean HttpPutSendLog(String LogData, String Key) {

			try {
				HttpClient client = new DefaultHttpClient();
				HttpPut Put = new HttpPut(URL + SendUserInfoUrl + "/Key=" + Key);

				StringEntity input = new StringEntity(LogData);
				input.setContentType("text/plain");

				Put.setEntity(input);

				HttpResponse responsePut = client.execute(Put);

				if (!HttpSuccessResponsCode(responsePut))
					return false;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		/**
		 * 서버에 Get해서 URL에따라 셋팅값을 가져옴
		 * 
		 * @since 2012. 11. 5.오전 3:41:35 TODO
		 * @author JeongSeungsu
		 * @param Url
		 *            가져올 URL
		 * @return BooleanResult => 값에 따라 설정, null = 완전 실패
		 */
		private BooleanResult HttpGetSend(String Url) {
			try {
				HttpClient client = new DefaultHttpClient();

				HttpGet get = new HttpGet(Url);
				HttpResponse responseGet = client.execute(get);
				HttpEntity resEntityGet = responseGet.getEntity();

				if (resEntityGet != null) {
					// 결과를 처리
					String Result = EntityUtils.toString(resEntityGet);
					BooleanResult isExist = (BooleanResult) LogDogJsonParser
							.fromJson(Result, BooleanResult.class);
					return isExist;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * Post방식으로 JsonData를 보낸다.
		 * 
		 * @since 2012. 11. 5.오전 3:20:12 TODO
		 * @author JeongSeungsu
		 * @param Url
		 * @param JsonData
		 * @return ResponseString이 온다. 완전 실패시 Null
		 */
		private String HttpPostSendJson(String Url, String JsonData) {
			String Response = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(Url);

				StringEntity input = new StringEntity(JsonData);
				input.setContentType("application/json");

				post.setEntity(input);
				HttpResponse responsePOST = client.execute(post);
				HttpEntity resEntity = responsePOST.getEntity();

				if (!HttpSuccessResponsCode(responsePOST))
					return null;

				Response = EntityUtils.toString(resEntity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Response;
		}

		/**
		 * 202코드 == Reponse => 성공
		 * 
		 * @since 2012. 11. 5.오후 10:09:03 TODO
		 * @author JeongSeungsu
		 * @param response
		 * @return 리스폰코드가 202면 성공
		 */
		private boolean HttpSuccessResponsCode(HttpResponse response) {
			int Code = 202;
			int getcode = response.getStatusLine().getStatusCode();
			if (getcode == Code)
				return true;
			else
				return false;
		}

	}

}
