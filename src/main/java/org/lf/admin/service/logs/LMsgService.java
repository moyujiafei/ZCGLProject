package org.lf.admin.service.logs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.lf.admin.db.dao.LMsgMapper;
import org.lf.admin.db.pojo.ChuTag;
import org.lf.admin.db.pojo.ChuWXDept;
import org.lf.admin.db.pojo.LMsg;
import org.lf.admin.db.pojo.WXUser;
import org.lf.admin.service.OperException;
import org.lf.admin.service.sys.WXUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("lmsgService")
public class LMsgService {

	public static final String TO_USER = "toUser";
	public static final String TO_PARTY = "toParty"; // 部门
	public static final String TO_TAG = "toTag";
	public static final String TO_ALL = "@all";

	@Autowired
	private LMsgMapper lmsgDao;

	@Autowired
	private WXUserService wxUserService;

	/**
	 * 查找发送给指定用户的消息列表，并对消息内容进行过滤。结果集按照时间进行降序排列
	 * 
	 * JSR使用的是JSON字符串方式进行存放。包括三种方式：
	 * 	TO_USER方式：将消息发送给指定用户（userid）。{"toUser":["XuQingWei"]}
	 * 	TO_ALL方式：将消息发送给所有用户。{"toUser":"@all"}
	 * 	TO_PARTY方式：将消息发送给指定部门中的用户{"toParty":"3"}
	 * 	TO_TAG方式：将消息发送给指定标签的用户{"toTag":"2"}
	 * 	如果我要查看许庆炜的消息，他的部门号为3，标签号为2的话，那么上述4种方式发生的消息均将查找出来。

	 * 
	 * 内容过滤原则：
	 * 	去除“点击”之后的内容
	 * 	去除<a href 开头，</a>结尾的超链接内容
	 * 
	 * @param appId
	 * @param msgLx
	 * @param userid
	 * @return
	 * @throws OperException 
	 */
	public SortedSet<LMsg> getMsgList(Integer appId, Integer msgLx, String userid) throws OperException {
		WXUser user = wxUserService.getWXUser(appId, userid);
		String jsr = "";
		// 查找发送方式为TO_USER的消息列表
		LMsg param = new LMsg();
		param.setLx(msgLx);
		param.setAppId(appId);
		// 利用getMsgListByJsr sql语句中的 jsr REGEXP '.+toUser.+userid'进行正则匹配查找
		jsr = ".+" + TO_USER + ".+" + userid;
		param.setJsr(jsr);
		List<LMsg> list1 = lmsgDao.getMsgListByJsr(param);
		// 查找发送方式为TO_ALL的消息列表
		jsr = ".+" + TO_USER + ".+" + TO_ALL;
		param.setJsr(jsr);
		List<LMsg> list2 = lmsgDao.getMsgListByJsr(param);
		// 查找发送方式为TO_PARTY的消息列表
		HashMap<Integer, List<LMsg>> map1 = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		sb.append(".+" + TO_PARTY + ".+");
		int i = 0;
		List<LMsg> tempList;
		for (ChuWXDept dept : user.getDeptList()) {
			sb.append(dept.getDeptNo());
			jsr = sb.toString();
			param.setJsr(jsr);
			tempList = lmsgDao.getMsgListByJsr(param);
			map1.put(i++, tempList);
			sb.delete(11, sb.length());
		}
		// 查找发送方式为TO_TAG的消息列表
		HashMap<Integer, List<LMsg>> map2 = new HashMap<>();
		sb.delete(0, sb.length());
		sb.append(".+" + TO_TAG + ".+");
		i = 0;
		for (ChuTag tag : user.getTagList()) {
			sb.append(tag.getTagNo());
			jsr = sb.toString();
			param.setJsr(jsr);
			tempList = lmsgDao.getMsgListByJsr(param);
			map2.put(i++, tempList);
			sb.delete(9, sb.length());
		}
		// 将list1,list2,map1,map2中的LMsg消息加入sortedSet集合中排序，然后作为返回值返回
		SortedSet<LMsg> sortedSet = new TreeSet<>(new Comparator<LMsg>() {

			@Override
			public int compare(LMsg o1, LMsg o2) {
				return -o1.getFssj().compareTo(o2.getFssj());
			}

		});
		for (LMsg msg : list1) {
			sortedSet.add(msg);
		}
		for (LMsg msg : list2) {
			sortedSet.add(msg);
		}
		for (List<LMsg> list : map1.values()) {
			for (LMsg msg : list) {
				sortedSet.add(msg);
			}
		}
		for (List<LMsg> list : map2.values()) {
			for (LMsg msg : list) {
				sortedSet.add(msg);
			}
		}
		
		/**
		 * 进行内容过滤：
		 * 	去除“点击”之后的内容
		 * 	去除<a href 开头，</a>结尾的超链接内容
		 */
		String nr = "";
		for (LMsg msg : sortedSet) {
			nr = msg.getNr();
			nr = nr.replaceAll("<a href=.+?>|</a>|点击<a href=.+?>.+</a>", "");
			msg.setNr(nr);
		}
		return sortedSet;
	}

}
