package com.logdog.Handler;


import com.logdog.Worker.Worker;



/**
 * 사용자가 차마 잡지 못한 Exception을 Catch해서 처리한다. 
 * @since 2012. 11. 12.오전 12:17:53
 * TODO
 * @author JeongSeungsu
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler{

	private Worker Process; 
	Thread.UncaughtExceptionHandler m_DefaultExceptionHandler;
	
	
	/**
	 * 워커를 가지고 워커에 정의된 메서드를 실행하기 위한 생성자
	 * @since 2012. 11. 15.오전 6:02:42
	 * TODO
	 * @author JeongSeungsu
	 * @param process
	 */
	public ExceptionHandler(Worker process) {
		// TODO Auto-generated constructor stub
		m_DefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        Process = process;
        
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
	
		//Process.CreateErrorReport(ex);
		try {
			Process.EmergencySendErrorRerport(ex);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_DefaultExceptionHandler.uncaughtException(thread, ex);
		
		android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

}
