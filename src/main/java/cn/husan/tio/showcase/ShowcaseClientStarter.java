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

import cn.husan.tio.showcase.common.Const;
import cn.husan.tio.showcase.common.ShowcasePacket;
import cn.husan.tio.showcase.common.Type;
import cn.husan.tio.showcase.common.packets.GroupMsgReqBody;
import cn.husan.tio.showcase.common.packets.JoinGroupReqBody;
import cn.husan.tio.showcase.common.packets.LoginReqBody;
import cn.husan.tio.showcase.common.packets.P2PReqBody;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientTioConfig;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.utils.hutool.StrUtil;
import org.tio.utils.json.Json;

/**
 *
 * @author tanyaowu
 */
public class ShowcaseClientStarter {
	static String serverIp = "127.0.0.1";
	static int serverPort = Const.PORT;

	private static Node serverNode = new Node(serverIp, serverPort);

	//用来自动连接的，不想自动连接请设为null
	private static ReconnConf reconnConf = new ReconnConf(5000L);

	private static ClientAioHandler tioClientHandler = new ShowcaseClientAioHandler();
	private static ClientAioListener aioListener = new ShowcaseClientAioListener();
	private static ClientTioConfig clientTioConfig = new ClientTioConfig(tioClientHandler, aioListener, reconnConf);

	private static TioClient tioClient = null;

	static ClientChannelContext clientChannelContext;

	public static void command() throws Exception {
		@SuppressWarnings("resource")
		java.util.Scanner sc = new java.util.Scanner(System.in);
		int i = 1;
		StringBuilder sb = new StringBuilder();
		sb.append("使用指南:\r\n");
		sb.append(i++ + "、需要帮助，输入 '?'.\r\n");
		sb.append(i++ + "、登录，输入 'login loginname password'.\r\n");
		sb.append(i++ + "、进入群组，输入 'join group1'.\r\n");
		sb.append(i++ + "、群聊，输入 'groupMsg group1 text'.\r\n");
		sb.append(i++ + "、点对点聊天，输入 'p2pMsg loginname text'.\r\n");

		sb.append(i++ + "、退出程序，输入 'exit'.\r\n");

		System.out.println(sb);

		String line = sc.nextLine(); // 这个就是用户输入的数据
		while (true) {
			if ("exit".equalsIgnoreCase(line)) {
				System.out.println("Thanks for using! bye bye.");
				break;
			} else if ("?".equals(line)) {
				System.out.println(sb);
			}

			processCommand(line);

			line = sc.nextLine(); // 这个就是用户输入的数据
		}

		tioClient.stop();
		System.exit(0);
	}

	public static void main(String[] args) throws Exception {
		tioClient = new TioClient(clientTioConfig);
		clientChannelContext = tioClient.connect(serverNode);
		command();
	}

	public static void processCommand(String line) throws Exception {
		if (StrUtil.isBlank(line)) {
			return;
		}

		String[] args = line.split(" ");//StrUtil.split(line, " ");
		String command = args[0];

		if ("login".equalsIgnoreCase(command)) {
			String loginname = args[1];
			String password = args[2];

			LoginReqBody loginReqBody = new LoginReqBody();
			loginReqBody.setLoginname(loginname);
			loginReqBody.setPassword(password);

			ShowcasePacket reqPacket = new ShowcasePacket();
			reqPacket.setType(Type.LOGIN_REQ);
			reqPacket.setBody(Json.toJson(loginReqBody).getBytes(ShowcasePacket.CHARSET));

			Tio.send(clientChannelContext, reqPacket);

		} else if ("join".equals(command)) {
			String group = args[1];

			JoinGroupReqBody joinGroupReqBody = new JoinGroupReqBody();
			joinGroupReqBody.setGroup(group);

			ShowcasePacket reqPacket = new ShowcasePacket();
			reqPacket.setType(Type.JOIN_GROUP_REQ);
			reqPacket.setBody(Json.toJson(joinGroupReqBody).getBytes(ShowcasePacket.CHARSET));

			Tio.send(clientChannelContext, reqPacket);
		} else if ("groupMsg".equals(command)) {
			String group = args[1];
			String text = args[2];

			GroupMsgReqBody groupMsgReqBody = new GroupMsgReqBody();
			groupMsgReqBody.setToGroup(group);
			groupMsgReqBody.setText(text);

			ShowcasePacket reqPacket = new ShowcasePacket();
			reqPacket.setType(Type.GROUP_MSG_REQ);
			reqPacket.setBody(Json.toJson(groupMsgReqBody).getBytes(ShowcasePacket.CHARSET));

			Tio.send(clientChannelContext, reqPacket);
		} else if ("p2pMsg".equals(command)) {
			String toUserid = args[1];
			String text = args[2];

			P2PReqBody p2pReqBody = new P2PReqBody();
			p2pReqBody.setToUserid(toUserid);
			p2pReqBody.setText(text);

			ShowcasePacket reqPacket = new ShowcasePacket();
			reqPacket.setType(Type.P2P_REQ);
			reqPacket.setBody(Json.toJson(p2pReqBody).getBytes(ShowcasePacket.CHARSET));

			Tio.send(clientChannelContext, reqPacket);
		}

	}
}
