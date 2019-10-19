package cn.husan.tio.tcp.server.handler;

import cn.husan.tio.tcp.common.FireAlarmPacket;
import cn.husan.tio.tcp.server.payload.DevicePayload;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.core.utils.ByteBufferUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @Auther: husan
 * @Date: 2019/10/16 14:33
 * @Description:
 */
public class DeviceFireBusinessHandler implements FireBusinessHandler {

	public static DevicePayload decode(byte[] body) throws UnsupportedEncodingException {
		if(body == null || body.length > DevicePayload.MAX_SIZE){
			return null;
		}
		DevicePayload devicePayload = new DevicePayload();
		ByteBuffer buffer = ByteBuffer.wrap(body);
		devicePayload.setSubSerial(ByteBufferUtils.readString(buffer, 9 , null));
		devicePayload.setStatus(buffer.getShort());
		return devicePayload;
	}
	@Override
	public void process(FireAlarmPacket packet, ChannelContext channelContext) {
		FireAlarmPacket fireAlarmPacket = (FireAlarmPacket) packet;
		try {
			DevicePayload playload = decode(fireAlarmPacket.getBody());
			System.out.println(playload);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
}
