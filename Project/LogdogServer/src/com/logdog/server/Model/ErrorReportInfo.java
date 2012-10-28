package com.logdog.server.Model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable ( identityType = IdentityType.APPLICATION)
public class ErrorReportInfo {
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY )
	private Key E_ReportCode;
	
	//외래키 
	@Persistent
	private Key E_ClassificationCode;
	
	@Persistent
	private BlobKey CallstackBlobKey;
	
	@Persistent
	private BlobKey LogBolbKey;
	
	//속성
	@Persistent
	private String CountryName;

	@Persistent
	private float AppVersion;
	
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

	public ErrorReportInfo(Key e_ClassificationCode, BlobKey callstackBlobKey,
			BlobKey logBolbKey, String countryName, float appVersion,
			String oSVersion, String deviceName, boolean gPSState,
			boolean wifiState, boolean providerNetworkState, int screanWidth,
			int screanHeight) {
		super();
		E_ClassificationCode = e_ClassificationCode;
		CallstackBlobKey = callstackBlobKey;
		LogBolbKey = logBolbKey;
		CountryName = countryName;
		AppVersion = appVersion;
		OSVersion = oSVersion;
		DeviceName = deviceName;
		GPSState = gPSState;
		WifiState = wifiState;
		ProviderNetworkState = providerNetworkState;
		ScreanWidth = screanWidth;
		ScreanHeight = screanHeight;
	}

	public Key getE_ReportCode() {
		return E_ReportCode;
	}

	public Key getE_ClassificationCode() {
		return E_ClassificationCode;
	}

	public BlobKey getCallstackBlobKey() {
		return CallstackBlobKey;
	}

	public BlobKey getLogBolbKey() {
		return LogBolbKey;
	}

	public String getCountryName() {
		return CountryName;
	}

	public float getAppVersion() {
		return AppVersion;
	}

	public String getOSVersion() {
		return OSVersion;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public boolean isGPSState() {
		return GPSState;
	}

	public boolean isWifiState() {
		return WifiState;
	}

	public boolean isProviderNetworkState() {
		return ProviderNetworkState;
	}

	public int getScreanWidth() {
		return ScreanWidth;
	}

	public int getScreanHeight() {
		return ScreanHeight;
	}
	
	
}
