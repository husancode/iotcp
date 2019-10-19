package cn.husan.tio.tcp.server.handler;

import cn.husan.tio.tcp.common.FireAlarmPacket;
import org.tio.core.ChannelContext;

/**
 * @Auther: husan
 * @Date: 2019/10/16 15:45
 * @Description:
 */
public class HeartbeatHandler implements FireBusinessHandler {
	@Override
	public void process(FireAlarmPacket packet, ChannelContext channelContext) {
		System.out.println("heat beart process");
	}
}
