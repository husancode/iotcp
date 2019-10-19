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


package cn.husan.tio.tcp.client;

import cn.husan.tio.tcp.common.CommandTypeEnum;
import cn.husan.tio.tcp.common.Const;
import cn.husan.tio.tcp.common.FireAlarmPacket;
import cn.husan.tio.tcp.common.FireAlarmPacketBuilder;
import cn.husan.tio.tcp.server.payload.DevicePayload;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientTioConfig;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author tanyaowu
 *
 */
public class FireAlarmClientPartPacketStarter {
	//服务器节点
	public static Node serverNode = new Node(Const.SERVER, Const.PORT);

	//handler, 包括编码、解码、消息处理
	public static ClientAioHandler tioClientHandler = new FireAlarmClientAioHandler();

	//事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
	public static ClientAioListener aioListener = null;

	//断链后自动连接的，不想自动连接请设为null
	private static ReconnConf reconnConf = new ReconnConf(5000L);

	//一组连接共用的上下文对象
	public static ClientTioConfig clientTioConfig = new ClientTioConfig(tioClientHandler, aioListener, reconnConf);

	public static TioClient tioClient = null;

	public static ClientChannelContext clientChannelContext = null;

	/**
	 * 启动程序入口
	 */
	public static void main(String[] args) throws Exception {
		clientTioConfig.setHeartbeatTimeout(Const.TIMEOUT);
		tioClient = new TioClient(clientTioConfig);
		clientChannelContext = tioClient.connect(serverNode);
		for(int i=0; i <10; i++){
			send();
			TimeUnit.SECONDS.sleep(10);
		}

	}

	private static void send() throws Exception {

		FireAlarmPacket packet = FireAlarmPacketBuilder.buildSenderPacket(new DevicePayload("131213156", (short)1), CommandTypeEnum.SEND);
		ByteBuffer buffer = packet.encode();
		byte[] byteArray = buffer.array();
		System.out.println(byteArray);
		System.out.println(byteArray.length);
		byte[] byteArray1 = new byte[21];
		byte[] byteArray2 = new byte[byteArray.length-21];
		System.arraycopy(byteArray, 0, byteArray1, 0, 21);
		System.arraycopy(byteArray, 21, byteArray2, 0, byteArray.length-21);
		System.out.println(byteArray1);
		System.out.println(byteArray2);
		ByteBuffer buffer1 = ByteBuffer.wrap(byteArray1);
		ByteBuffer buffer2 = ByteBuffer.wrap(byteArray2);
		clientChannelContext.sendRunnable.sendByteBuffer(buffer1, packet);
		TimeUnit.SECONDS.sleep(5);
		buffer.limit(buffer.capacity());
		clientChannelContext.sendRunnable.sendByteBuffer(buffer2, packet);
	}


}
