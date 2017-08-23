package org.lf.utils.wx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lf.utils.ThreadUtils;

public class WXAppHandler {
	private Map<Integer, WXAppListener> repository = new HashMap<>();
	
	public void addListener(WXAppListener listener) {
		repository.put(listener.getEvent().getAppId(), listener);
		fireListener();
	}
	
	public List<Integer> getAppThreadList(){
		List<Integer> appThreadList = new ArrayList<Integer>();
		for (Integer appId : repository.keySet()) {
			appThreadList.add(appId);
		}
		return appThreadList;
	}
	
	public boolean threadIsAlive(Integer appId){
		return repository.containsKey(appId);
	}
	
	public void removeListener(Integer appId) {
		// 停止微信应用的线程
		WXAppListener listener = repository.get(appId);
		if (listener != null) {
			WXAppEvent event = listener.getEvent();
			Thread thread = ThreadUtils.getThread(event.getThreadId());
			
			thread.interrupt();
			
			event.setThreadId(null);
			
			repository.remove(appId);
			fireListener();
		}
	}
	
	public WXAppListener getListener(Integer appId) {
		return repository.get(appId);
	}
	
	/**
	 * 通知所有的监听器进行处理
	 */
	public void fireListener() {
		WXAppListener listener;
		for (Integer appId : repository.keySet()) {
			listener = repository.get(appId);
			listener.handleEvent();
		}
	}

}
