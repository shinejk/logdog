package logdog.ErrorReport.DAO;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import logdog.Common.TimeUtil;
import logdog.ErrorReport.DTO.ClientReportData;
import logdog.ErrorReport.DTO.UserReportData;
import logdog.ErrorReport.DTO.UserSummaryInfo;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;



@PersistenceCapable ( identityType = IdentityType.APPLICATION)
public class ErrorReportInfo {
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY )
	private Key E_ReportCode;
	
	//�ܷ�Ű 
	@Persistent
	private Key E_ClassificationCode;

	@Persistent
	private BlobKey LogBolbKey;
	//�Ӽ�
	@Persistent
	private String CountryName;

	@Persistent
	private String AppVersion;
	
	@Persistent
	private String OSVersion;
	
	@Persistent 
	private String DeviceName;
	
	@Persistent
	private boolean GPSState;
	
	@Persistent
	private boolean WifiState;
	
	@Persistent
	private boolean ProviderNetworkState;
	
	@Persistent
	private int ScreanWidth;
	
	@Persistent
	private int ScreanHeight;
	
	@Persistent
	private int YearCode;
	
	@Persistent
	private int TimeCode;
	
	

	public ErrorReportInfo() {
		super();
	
	}
	
	/**
	 *
	 * @since 2012. 11. 2.오전 4:55:34
	 * TODO ClassificationCode는 Backend로 갱신하기 
	 * @author Karuana
	 * @param report
	 */
	public ErrorReportInfo(ClientReportData report)
	{
		super();
		CountryName = report.National;
		AppVersion =report.AppVersion;
		OSVersion = report.OSVersion;
		DeviceName = report.Model;
		GPSState =report.GPS;
		WifiState = report.WiFi;
		ProviderNetworkState = report.MobileNetwork;
		ScreanWidth = report.ScreenWidth;
		ScreanHeight = report.ScreenHeight;
		YearCode = TimeUtil.GetNowYear();
		TimeCode = TimeUtil.GetNowTimeCode();
	}

	public Key getE_ReportCode() {
		return E_ReportCode;
	}
	
	public void setE_ClassificationCode(Key e_ClassificationCode) {
		E_ClassificationCode = e_ClassificationCode;
	}

	public Key getE_ClassificationCode() {
		return E_ClassificationCode;
	}

	public void setLogBolbKey(BlobKey logBolbKey) {
		LogBolbKey = logBolbKey;
	}

	public BlobKey getLogBolbKey() {
		return LogBolbKey;
	}

	public UserReportData getUserData()
	{
		return new UserReportData(DeviceName,GPSState,WifiState, ProviderNetworkState,ScreanWidth,ScreanHeight);
	}
	
	public UserSummaryInfo getSummary()
	{
		return new UserSummaryInfo(CountryName,AppVersion,OSVersion, YearCode,TimeCode);
	}
	
}
