package org.lf.admin.action.console.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lf.admin.action.console.BaseController;
import org.lf.admin.service.sys.EWMPrintService;
import org.lf.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/console/sys/ewm/")
public class EWMPrintController extends BaseController {
	private final String ROOT = "/console/sys/ewm";
	
	@Autowired
	private EWMPrintService ewmService;
	
	@RequestMapping("printEWMUI.do")
	public String printEWMUI() {
		return ROOT + "/printEWMUI";
	}
	
	@RequestMapping("pdfEWM.do")
	@ResponseBody
	public String pdfEWM(HttpSession session, String zclxId, Integer deptNo, String cfdd, String syr, String glr,
			HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isEmpty(zclxId)){
			zclxId=null;
		}
		if(StringUtils.isEmpty(cfdd)){
			cfdd=null;
		}
		if(StringUtils.isEmpty(syr)){
			syr=null;
		}
		if(StringUtils.isEmpty(glr)){
			glr=null;
		}
		try {
			ewmService.getEwmPdf(getAppId(session), zclxId, deptNo, cfdd, syr, glr, request, response);
		} catch (Exception e) {
			return e.getMessage();
		}
		return SUCCESS;
	}
}
