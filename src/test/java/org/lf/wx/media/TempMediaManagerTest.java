package org.lf.wx.media;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.lf.wx.media.TempMediaManager;
import org.lf.wx.utils.WXException;
import org.lf.wx.utils.WXUtils;

public class TempMediaManagerTest {

	@Test
	public void testTempMedia() throws WXException {
		File uploadFile = new File("D:/学习/微信/image/02.gif");
		File downloadFile = new File("D:/学习/微信/image/02_d.gif");
		
		if (downloadFile.exists()) {
			downloadFile.delete();
		}
		
//		update media
//		TempMedia tmj = new TempMedia(TempMediaManager.uploadMedia(uploadFile, MediaType.image));
//		System.out.println(tmj);
		
		//download media
		TempMediaManager.downloadMedia(WXUtils.getLocalToken(), downloadFile, "1eYXGMuZq_q2tiQe5blWZVTdJQtbB3iM071CqoOEnrzl0lfbw-H6Hvs2Wz0RVKtqOkMloLO5ourc-KPyVA9-bVQ");
		assertTrue(downloadFile != null);
	}

}
