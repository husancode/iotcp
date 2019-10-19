package cn.husan.tio.tcp.client;

import cn.husan.tio.tcp.common.CommandTypeEnum;
import cn.husan.tio.tcp.common.FireAlarmPacket;
import cn.husan.tio.tcp.common.FireAlarmPacketBuilder;
import cn.husan.tio.tcp.server.payload.DevicePayload;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @Auther: husan
 * @Date: 2019/10/17 09:26
 * @Description:
 */
public class TioTt {

	public static void main(String[] args) {
		FireAlarmPacket packet = FireAlarmPacketBuilder.buildSenderPacket(new DevicePayload("131213156", (short)1), CommandTypeEnum.SEND);
		ByteBuffer buffer = packet.encode();
		byte[] byteArray = buffer.array();
		System.out.println(Arrays.toString(byteArray));
		System.out.println(byteArray.length);
		byte[] byteArray1 = new byte[21];
		byte[] byteArray2 = new byte[byteArray.length-21];
		System.arraycopy(byteArray, 0, byteArray1, 0, 21);
		System.arraycopy(byteArray, 21, byteArray2, 0, byteArray.length-21);
		System.out.println(Arrays.toString(byteArray1));
		System.out.println(Arrays.toString(byteArray2));
		ByteBuffer buffer1 = ByteBuffer.wrap(byteArray1);
		ByteBuffer buffer2 = ByteBuffer.wrap(byteArray2);
		System.out.println("");
	}
}
