package com.logdog.Appender;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import com.logdog.ErrorReport.ClientReportData;
import com.logdog.Formatter.IFormatter;
import com.logdog.common.Network.Network;


/**
 *  각 어펜더의 추상 인터페이스
 *  어펜더 생성에 관한 부분에 대해 일괄적으로 처리 하도록 랩퍼 해준 것임.
 * @since 2012. 10. 13.오후 10:09:34
 * TODO 
 * @author JeongSeungsu
 */
@Root(name = "Appender")
public abstract class AbstractAppender 
{

	/**
	 * 어펜더 이름
	 * @see GetAppenderName()
	 */
	@Attribute
	String AppenderName;
	
	/**
	 *
	 * @since 2012. 11. 15.오전 5:58:14
	 * TODO
	 * @author JeongSeungsu
	 */
	public AbstractAppender(){
		AppenderName = new String();
	}
	/**
	 * 어펜더 이름
	 * @since 2012. 11. 15.오전 5:58:00
	 * TODO
	 * @author JeongSeungsu
	 * @param appendername
	 */
	public AbstractAppender(String appendername){
		AppenderName = appendername;
	}
	
	/**
	 * 어펜더 이름 설정
	 * @since 2012. 11. 15.오전 5:58:16
	 * TODO
	 * @author JeongSeungsu
	 * @param name
	 */
	public void SetAppenderName(String name){
		AppenderName = name;
	}
	/**
	 * 어펜더 이름을 가져온다.
	 * @since 2012. 11. 15.오전 5:58:23
	 * TODO
	 * @author JeongSeungsu
	 * @return
	 */
	public String GetAppenderName(){
		return AppenderName;
	}
	
	/**
	 * 에러 리포트 처리 인터페이스
	 * 아무것도 하지 않는다면 그냥 true값 리턴
	 * @since 2012. 11. 11.오후 11:51:56
	 * TODO
	 * @author JeongSeungsu
	 * @param Data 처리할 ClientReportData
	 * @return
	 */
	public boolean ErrorReportProcess(ClientReportData Data){
		return true;
	}
	
	/**
	 * 어펜더 초기화 
	 * @since 2012. 11. 11.오후 11:52:32
	 * TODO
	 * @author JeongSeungsu
	 * @param network
	 */
	public abstract void InitAppender(Network network);
	
	/**
	 * Microlog4android의 랩퍼 이기 때문에 Log에 등록하기 위해서는 다음과 같은 형의 어펜더가 필요
	 * @since 2012. 11. 11.오후 11:52:39
	 * TODO
	 * @author JeongSeungsu
	 * @return Microlog4android의 어펜더 타입
	 */
	public com.google.code.microlog4android.appender.Appender GetLog4Appender(){
		return null;
	}
	
}
