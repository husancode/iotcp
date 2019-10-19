package cn.husan.tio.tcp.server;

import cn.husan.tio.tcp.common.Const;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;

/**
 * @Auther: husan
 * @Date: 2019/10/16 09:48
 * @Description:
 */
public class FireAlarmServerStarter {

	//handler, 包括编码、解码、消息处理
	private static ServerAioHandler aioHandler = new FireAlarmServerAioHandler();

	//事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
	private static ServerAioListener aioListener = null;

	//一组连接共用的上下文对象
	private static ServerTioConfig serverTioConfig = new ServerTioConfig("fireAlarm-tio-server", aioHandler, aioListener);

	//tioServer对象
	private static TioServer tioServer = new TioServer(serverTioConfig);

	//有时候需要绑定ip，不需要则null
	public static String serverIp = null;

	//监听的端口
	public static int serverPort = Const.PORT;

	public static void main(String[] args) throws IOException {
		serverTioConfig.setHeartbeatTimeout(Const.TIMEOUT);
		tioServer.start(serverIp, serverPort);
	}
}
