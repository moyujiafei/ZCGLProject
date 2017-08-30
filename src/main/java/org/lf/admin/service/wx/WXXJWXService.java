package org.lf.admin.service.wx;

import java.util.HashMap;
import java.util.Map;

import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.catalog.FJService;
import org.lf.admin.service.catalog.JZWService;
import org.lf.admin.service.catalog.XQService;
import org.lf.admin.service.catalog.ZCGLService;
import org.lf.admin.service.sys.WXAppService;
import org.lf.admin.service.zcgl.GZWXService;
import org.lf.admin.service.zcgl.RCXJService;
import org.lf.admin.service.zcgl.ZCDJService;
import org.lf.admin.service.zcgl.ZCFPService;
import org.lf.utils.AjaxResultModel;
import org.lf.wx.media.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WXXJWXService {
	@Autowired
	private ZCFPService zcfpService;

	@Autowired
	private WXAppService wxAppService;

	@Autowired
	private ZCGLService zcglService;

	@Autowired
	private ZCDJService zcdjService;

	@Autowired
	private XQService xqService;

	@Autowired
	private JZWService jzwService;

	@Autowired
	private FJService fjService;

	@Autowired
	private GZWXService gzwxService;

	@Autowired
	private RCXJService rcxjService;

	/**
	 * 完成故障维修 维修组
	 * 
	 * @param request
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @return
	 */
	public AjaxResultModel finishGZWX(Integer appId, String wxr, String imgId, String voiceId, Integer zcId) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			gzwxService.finishGZWX(appId, zcId, mediaList, wxr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 重新提交维修申请 维修组
	 * 
	 * @param request
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @param sqRemark
	 * @return
	 */
	public AjaxResultModel resubmitWXSQ(Integer appId, String wxr, String imgId, String voiceId, Integer zcId, String sqRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {

			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			gzwxService.resubmitWXSQ(appId, zcId, mediaList, sqRemark, wxr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 提交闲置申请 维修组
	 * 
	 * @param request
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @param sqRemark
	 * @return
	 */
	public AjaxResultModel submitXZSQ(Integer appId, String wxr, String imgId, String voiceId, Integer zcId, String sqRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {

			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			gzwxService.submitXZSQ(appId, zcId, mediaList, sqRemark, wxr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 提交报废申请 维修组
	 * 
	 * @param request
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @param sqRemark
	 * @return
	 */
	public AjaxResultModel submitBFSQ(Integer appId, String wxr, String imgId, String voiceId, Integer zcId, String sqRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			gzwxService.submitBFSQ(appId, zcId, mediaList, sqRemark, wxr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 完成日常巡检 巡检组
	 * 
	 * @param appId
	 * @param xjr
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @return
	 */
	public AjaxResultModel finishRCXJ(Integer appId, String xjr, String imgId, String voiceId, Integer zcId) {
		AjaxResultModel result = new AjaxResultModel();

		try {
			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			rcxjService.finishRCXJ(appId, zcId, mediaList, xjr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;

	}

	/**
	 * 申请维修资产 巡检组
	 * 
	 * @param appId
	 * @param wxr
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @param sqRemark
	 * @return
	 */
	public AjaxResultModel submitXJWXSQ(Integer appId, String wxr, String imgId, String voiceId, Integer zcId, String sqRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {

			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			rcxjService.submitWXSQ(appId, zcId, mediaList, sqRemark, wxr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 申请闲置资产 巡检组
	 * 
	 * @param appId
	 * @param wxr
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @param sqRemark
	 * @return
	 */
	public AjaxResultModel submitXJXZSQ(Integer appId, String wxr, String imgId, String voiceId, Integer zcId, String sqRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {

			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			rcxjService.submitXZSQ(appId, zcId, mediaList, sqRemark, wxr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * 申请报废资产 巡检组
	 * 
	 * @param appId
	 * @param wxr
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @param sqRemark
	 * @return
	 */
	public AjaxResultModel submitXJBFSQ(Integer appId, String wxr, String imgId, String voiceId, Integer zcId, String sqRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			gzwxService.submitBFSQ(appId, zcId, mediaList, sqRemark, wxr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}
	/**
	 * 申请维修资产 使用人组
	 * 
	 * @param appId
	 * @param syr
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @param sqRemark
	 * @return
	 */
	public AjaxResultModel submitSyrWXSQ(Integer appId, String syr, String imgId, String voiceId, Integer zcId, String sqRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {

			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}
			rcxjService.submitWXSQ(appId, zcId, mediaList, sqRemark, syr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 申请闲置资产 使用人组
	 * 
	 * @param appId
	 * @param syr
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @param sqRemark
	 * @return
	 */
	public AjaxResultModel submitSyrXZSQ(Integer appId, String syr, String imgId, String voiceId, Integer zcId, String sqRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {

			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			rcxjService.submitXZSQ(appId, zcId, mediaList, sqRemark, syr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 申请报废资产 使用人组
	 * 
	 * @param appId
	 * @param syr
	 * @param imgId
	 * @param voiceId
	 * @param zcId
	 * @param sqRemark
	 * @return
	 */
	public AjaxResultModel submitSyrBFSQ(Integer appId, String syr, String imgId, String voiceId, Integer zcId, String sqRemark) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			Map<String, MediaType> mediaList = new HashMap<>();
			String[] ids = imgId.split("###");
			for (String imgString : ids) {
				if (imgString != null && imgString.length() != 0) {
					mediaList.put(imgString, MediaType.image);
				}
			}
			if (voiceId != null && voiceId.length() > 0) {
				mediaList.put(voiceId, MediaType.voice);
			}

			gzwxService.submitBFSQ(appId, zcId, mediaList, sqRemark, syr);
			result.setData("success");
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}

}
