package cn.husan.tio.tcp.common;

import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.AioHandler;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * @Auther: husan
 * @Date: 2019/10/15 16:24
 * @Description:
 */
public class FireAlarmAioHandler implements AioHandler {

	@Override
	public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
		System.out.println(Thread.currentThread() +" decode buffer：" + buffer);
		return FireAlarmPacket.decode(buffer);
	}

	@Override
	public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
		System.out.println(Thread.currentThread() +" encode packet：" + packet);
		FireAlarmPacket fireAlarmPacket = (FireAlarmPacket) packet;
		return fireAlarmPacket.encode();
	}

	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {

	}
}
