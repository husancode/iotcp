/*
 * 	一、服务条款
	1. 被授权者务必尊重知识产权，严格保证不恶意传播产品源码、不得直接对授权的产品本身进行二次转售或倒卖、不得对授权的产品进行简单包装后声称为自己的产品等。否则我们有权利收回产品授权，并根据事态轻重追究相应法律责任。
	2. 被授权者可将授权后的产品用于任意符合国家法律法规的应用平台，但一个授权使用不得超过5个域名和5个项目。
	3. 授权 t-io 官方的付费产品（如：t-io官方文档阅读权限、tio-study等），不支持退款。
	4. 我们有义务为被授权者提供有效期内的产品下载、更新和维护，一旦过期，被授权者无法享有相应权限。终身授权则不受限制。
	t-io
	5. t-io 官方的付费产品中的”tio-study”并不保证源代码中绝对不出现BUG，用户遇到BUG时希望可以及时反馈给t-io，相信在大众的努力下，”tio-study”这个产品会越来越成熟，越来越普及
	6. 本条款主要针对恶意用户，在实施过程中会保持灵活度，毕竟谁也不想找麻烦，所以请良心用户可以放心使用！
	7. 本站有停止运营的风险，如果本站主动暂停或停止运营，本站有义务提前30天告知所有用户，以便用户作好相应准备；如果本站受不可抗拒因素暂停或停止运营（包括但不限于遭受DDOS攻击、SSL临时过期、手续升级等），本站不承担任何责任
	
	二、免责声明
	服务条款第2条已经说得很清楚，被授权者只能将产品应用于符合国家法律法规的应用平台，如果被授权者违背该条，由此产生的法律后果由被授权者独自承担，与t-io及t-io作者无关
 */


package cn.husan.tio.showcase;

import java.util.HashMap;
import java.util.Map;

import cn.husan.tio.showcase.common.ShowcaseAbsAioHandler;
import cn.husan.tio.showcase.common.ShowcasePacket;
import cn.husan.tio.showcase.common.Type;
import cn.husan.tio.showcase.common.intf.AbsShowcaseBsHandler;
import cn.husan.tio.showcase.handler.GroupMsgRespHandler;
import cn.husan.tio.showcase.handler.JoinGroupRespHandler;
import cn.husan.tio.showcase.handler.LoginRespHandler;
import cn.husan.tio.showcase.handler.P2PRespHandler;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;


/**
 *
 * @author tanyaowu
 * 2017年3月27日 上午12:18:11
 */
public class ShowcaseClientAioHandler extends ShowcaseAbsAioHandler implements ClientAioHandler {

	private static Map<Byte, AbsShowcaseBsHandler<?>> handlerMap = new HashMap<>();
	static {
		handlerMap.put(Type.GROUP_MSG_RESP, new GroupMsgRespHandler());
		handlerMap.put(Type.JOIN_GROUP_RESP, new JoinGroupRespHandler());
		handlerMap.put(Type.LOGIN_RESP, new LoginRespHandler());
		handlerMap.put(Type.P2P_RESP, new P2PRespHandler());
	}

	private static ShowcasePacket heartbeatPacket = new ShowcasePacket(Type.HEART_BEAT_REQ, null);

	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		ShowcasePacket showcasePacket = (ShowcasePacket) packet;
		Byte type = showcasePacket.getType();
		AbsShowcaseBsHandler<?> showcaseBsHandler = handlerMap.get(type);
		showcaseBsHandler.handler(showcasePacket, channelContext);
		return;
	}

	/**
	 * 此方法如果返回null，框架层面则不会发心跳；如果返回非null，框架层面会定时发本方法返回的消息包
	 */
	@Override
	public ShowcasePacket heartbeatPacket(ChannelContext channelContext) {
		return heartbeatPacket;
	}
}
