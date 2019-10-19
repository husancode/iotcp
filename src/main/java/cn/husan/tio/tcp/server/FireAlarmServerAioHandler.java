package cn.husan.tio.tcp.server;

import cn.husan.tio.tcp.common.CommandTypeEnum;
import cn.husan.tio.tcp.common.FireAlarmAioHandler;
import cn.husan.tio.tcp.common.FireAlarmPacket;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

/**
 * @Auther: husan
 * @Date: 2019/10/16 09:37
 * @Description:
 */
public class FireAlarmServerAioHandler extends FireAlarmAioHandler implements ServerAioHandler {

	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		System.out.println(Thread.currentThread() + "handler packet:" + packet);
		FireAlarmPacket alarmPacket = (FireAlarmPacket) packet;
		CommandTypeEnum typeEnum = CommandTypeEnum.of(alarmPacket.getCommandType());
		if(typeEnum == null){
			System.out.println("commandType undefined!");
			return;
		}
		typeEnum.getHandler().process(alarmPacket, channelContext);
		Tio.send(channelContext, packet);
	}

}
