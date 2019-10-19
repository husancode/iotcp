package cn.husan.tio.tcp.client;

import cn.husan.tio.tcp.common.FireAlarmAioHandler;
import cn.husan.tio.tcp.common.FireAlarmPacketBuilder;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * @Auther: husan
 * @Date: 2019/10/16 09:41
 * @Description:
 */
public class FireAlarmClientAioHandler extends FireAlarmAioHandler implements ClientAioHandler {

	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		System.out.println(Thread.currentThread() + " recevier packet:" + packet);
	}

	/**
	 * 返回null不发心跳
	 * @param channelContext
	 * @return
	 */
	@Override
	public Packet heartbeatPacket(ChannelContext channelContext) {
		//return FireAlarmPacketBuilder.buildHeartbeatPacket();
		return null;
	}
}
