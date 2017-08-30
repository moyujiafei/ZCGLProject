package org.lf.admin.service.wx;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lf.admin.db.dao.ChuWXDeptMapper;
import org.lf.admin.db.pojo.WxDept;
import org.lf.admin.service.WXResultCode;
import org.lf.admin.service.sys.WXDeptService;
import org.lf.admin.service.wx.vue.picker.PickerData;
import org.lf.admin.service.wx.vue.picker.PickerDataElement;
import org.lf.utils.AjaxResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WXChuDeptService {

	@Autowired
	private WXDeptService wxDeptService;
	
	@Autowired
	private ChuWXDeptMapper deptDao;
	
	/**
	 * 获取微信部门列表(包含全部)
	 * 
	 * @param appId
	 * @return
	 */
	public AjaxResultModel getWXDeptPickWithAll(Integer appId) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			List<WxDept> deptList = deptDao.getWxDeptsLevel(appId);

			PickerData pd = new PickerData();
			List<PickerDataElement> pickList = pd.getElementList();
			Set<String> lvOneMap = new HashSet<>();
			Set<String> lvTwoMap = new HashSet<>();
			Set<String> lvThreeMap = new HashSet<>();
			PickerDataElement firstPickers = new PickerDataElement();
			firstPickers.setName("");
			firstPickers.setParent("0");
			firstPickers.setValue("null1qq");

			PickerDataElement sencondPickers = new PickerDataElement();
			sencondPickers.setName("");
			sencondPickers.setParent("null1qq");
			sencondPickers.setValue("null2qq");

			PickerDataElement thirdPickers = new PickerDataElement();
			thirdPickers.setName("");
			thirdPickers.setParent("null2qq");
			thirdPickers.setValue("null3qq");

			pickList.add(firstPickers);
			pickList.add(sencondPickers);
			pickList.add(thirdPickers);
			boolean LvOne = false;
			boolean LvTwo = false;
			boolean LvThree = false;

			for (WxDept wxDept : deptList) {
				if (wxDept.getLvOneId() != null) {
					LvOne = true;
					if (lvOneMap.add(wxDept.getLvOneId()) == true) {
						PickerDataElement deptPickers = new PickerDataElement();
						deptPickers.setName(wxDept.getLvOne());
						deptPickers.setValue(wxDept.getLvOneId());
						deptPickers.setParent("0");
						PickerDataElement nullDept = new PickerDataElement();
						nullDept.setName("");
						nullDept.setValue("null"+wxDept.getLvOneId());
						nullDept.setParent(wxDept.getLvOneId());
						PickerDataElement nullDept2 = new PickerDataElement();
						nullDept2.setName("");
						nullDept2.setValue("null2"+wxDept.getLvOneId());
						nullDept2.setParent("null"+wxDept.getLvOneId());
						pickList.add(deptPickers);
						pickList.add(nullDept);
						pickList.add(nullDept2);
					}
				}
				if (wxDept.getLvTwoId() != null) {
					LvTwo = true;
					if (lvTwoMap.add(wxDept.getLvTwoId()) == true) {
						PickerDataElement deptPickers = new PickerDataElement();
						deptPickers.setName(wxDept.getLvTwo());
						deptPickers.setValue(wxDept.getLvTwoId());
						deptPickers.setParent(wxDept.getLvOneId());
						PickerDataElement nullDept = new PickerDataElement();
						nullDept.setName("");
						nullDept.setValue("null"+wxDept.getLvTwoId());
						nullDept.setParent(wxDept.getLvTwoId());
						pickList.add(deptPickers);
						pickList.add(nullDept);					
					}
				}
				if (wxDept.getLvThreeId() != null) {
					LvThree = true;
					if (lvThreeMap.add(wxDept.getLvThreeId()) == true) {
						PickerDataElement deptPickers = new PickerDataElement();
						deptPickers.setName(wxDept.getLvThree());
						deptPickers.setValue(wxDept.getLvThreeId());
						deptPickers.setParent(wxDept.getLvTwoId());
						pickList.add(deptPickers);
					}
				}
			}

			if (LvThree == true) {
				pd.setLevel(3);
			} else if (LvTwo == true) {
				pd.setLevel(2);
			} else if (LvOne == true) {
				pd.setLevel(1);
			}
			pd.setRes(pd.getLinkArray());
			result.setData(pd);
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取微信部门列表(不包含全部)
	 * 
	 * @param appId
	 * @return
	 */
	public AjaxResultModel getWXDeptPick(Integer appId) {
		AjaxResultModel result = new AjaxResultModel();
		try {
			List<WxDept> deptList = deptDao.getWxDeptsLevel(appId);

			PickerData pd = new PickerData();
			List<PickerDataElement> pickList = pd.getElementList();
			Set<String> lvOneMap = new HashSet<>();
			Set<String> lvTwoMap = new HashSet<>();
			Set<String> lvThreeMap = new HashSet<>();
			boolean LvOne = false;
			boolean LvTwo = false;
			boolean LvThree = false;

			for (WxDept wxDept : deptList) {
				if (wxDept.getLvOneId() != null) {
					LvOne = true;
					if (lvOneMap.add(wxDept.getLvOneId()) == true) {
						PickerDataElement deptPickers = new PickerDataElement();
						deptPickers.setName(wxDept.getLvOne());
						deptPickers.setValue(wxDept.getLvOneId());
						deptPickers.setParent("0");
						PickerDataElement nullDept = new PickerDataElement();
						nullDept.setName("");
						nullDept.setValue("null"+wxDept.getLvOneId());
						nullDept.setParent(wxDept.getLvOneId());
						PickerDataElement nullDept2 = new PickerDataElement();
						nullDept2.setName("");
						nullDept2.setValue("null2"+wxDept.getLvOneId());
						nullDept2.setParent("null"+wxDept.getLvOneId());
						pickList.add(deptPickers);
						pickList.add(nullDept);
						pickList.add(nullDept2);
					}
				}
				if (wxDept.getLvTwoId() != null) {
					LvTwo = true;
					if (lvTwoMap.add(wxDept.getLvTwoId()) == true) {
						PickerDataElement deptPickers = new PickerDataElement();
						deptPickers.setName(wxDept.getLvTwo());
						deptPickers.setValue(wxDept.getLvTwoId());
						deptPickers.setParent(wxDept.getLvOneId());
						PickerDataElement nullDept = new PickerDataElement();
						nullDept.setName("");
						nullDept.setValue("null"+wxDept.getLvTwoId());
						nullDept.setParent(wxDept.getLvTwoId());
						pickList.add(deptPickers);
						pickList.add(nullDept);					
					}
				}
				if (wxDept.getLvThreeId() != null) {
					LvThree = true;
					if (lvThreeMap.add(wxDept.getLvThreeId()) == true) {
						PickerDataElement deptPickers = new PickerDataElement();
						deptPickers.setName(wxDept.getLvThree());
						deptPickers.setValue(wxDept.getLvThreeId());
						deptPickers.setParent(wxDept.getLvTwoId());
						pickList.add(deptPickers);
					}
				}
			}

			if (LvThree == true) {
				pd.setLevel(3);
			} else if (LvTwo == true) {
				pd.setLevel(2);
			} else if (LvOne == true) {
				pd.setLevel(1);
			}
			pd.setRes(pd.getLinkArray());
			
			result.setData(pd);
			result.setCode(WXResultCode.SUCCESS.getCode());
			result.setMsg(WXResultCode.SUCCESS.getMsg());
		} catch (Exception e) {
			result.setCode(WXResultCode.ERROR.getCode());
			result.setMsg(e.getMessage());
		}
		return result;
	}
	
}
