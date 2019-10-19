package cn.husan.tio.tcp.server.handler;

import cn.husan.tio.tcp.common.FireAlarmPacket;
import org.tio.core.ChannelContext;

/**
 * @Auther: husan
 * @Date: 2019/10/16 14:30
 * @Description:
 */
public interface FireBusinessHandler {

	void process(FireAlarmPacket packet, ChannelContext channelContext);
}
