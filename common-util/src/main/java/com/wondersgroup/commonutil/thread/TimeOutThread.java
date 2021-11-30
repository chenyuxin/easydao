package com.wondersgroup.commonutil.thread;

import com.wondersgroup.commonutil.MyObj;

/**
 * 超时守护线程控制
 * 对象超时
 *
 */
public class TimeOutThread extends Thread {

	/**
	 * 超时时间
	 */
	private long timeOut;
	
	/**
	 * 超时守护的对象
	 */
	private Object object;
	
	/**
	 * 对象的关闭的方法名
	 */
	private String methodName;
	
	/**
	 * 超时监听线程或客户端等，超时时停止。
	 * @param timeOut 超时时间
	 * @param object 某线程或客户端
	 * @param methodName 停止(或超时后要执行的对象)的方法名，方法没有参数
	 */
	public TimeOutThread(long timeOut, Object object,String methodName) {
		super();
		this.timeOut = timeOut;
		this.object = object;
		this.methodName = methodName;
	}
	
	/**
	 * 结束计时
	 * 关闭计时线程
	 */
	public void end() {
		if (this.isAlive()) {
			this.interrupt();
		}
	}
	
	/**
	 * 超时守护线程执行对象的关闭或终止方法。
	 * @throws Exception
	 */
	public void doClose() throws Exception {
		MyObj.invokeMethod(this.object, this.methodName, new Object[0]);
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(timeOut);
			System.out.println(this.object.getClass().getName().concat(": 执行超时"));
			this.doClose();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
