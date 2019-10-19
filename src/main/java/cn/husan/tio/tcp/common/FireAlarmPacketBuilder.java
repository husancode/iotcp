package cn.husan.tio.tcp.common;

import cn.husan.tio.tcp.server.payload.DevicePayload;
import cn.husan.tio.tcp.server.payload.Payload;

import java.util.Date;

/**
 * @Auther: husan
 * @Date: 2019/10/16 13:09
 * @Description:
 */
public class FireAlarmPacketBuilder {

	private static final byte[] SOURCE_ADDR = new byte[6];

	private static final byte[] TARGET_ADDR = new byte[6];

	private static final int CRC_16 = 1121;

	private static final int VERSION = 3;

	public static FireAlarmPacket buildHeartbeatPacket(){
		FireAlarmPacket packet = new FireAlarmPacket();
		packet.setSequenceNumber(packet.getId().intValue());
		packet.setUserVersion(VERSION);
		packet.setTimeStamp(new Date());
		packet.setSourceAddress(SOURCE_ADDR);
		packet.setTargetAddress(TARGET_ADDR);
		packet.setDataLength(0);
		packet.setCommandType(CommandTypeEnum.HEARTBEAT.getCommandType());
		packet.setCrc16(CRC_16);
		return packet;
	}

	public static FireAlarmPacket buildSenderPacket(byte[] data, CommandTypeEnum commandTypeEnum){
		FireAlarmPacket packet = new FireAlarmPacket();
		packet.setSequenceNumber(packet.getId().intValue());
		packet.setUserVersion(VERSION);
		packet.setTimeStamp(new Date());
		packet.setSourceAddress(SOURCE_ADDR);
		packet.setTargetAddress(TARGET_ADDR);
		packet.setBody(data);
		packet.setDataLength(data != null ? data.length: 0);
		packet.setCommandType(commandTypeEnum.getCommandType());
		packet.setCrc16(CRC_16);
		return packet;
	}

	public static FireAlarmPacket buildSenderPacket(Payload playLoad, CommandTypeEnum commandTypeEnum){
		byte[] body = playLoad.encode();
		return buildSenderPacket(body, commandTypeEnum);
	}

	public static void main(String[] args) {
		FireAlarmPacket packet = FireAlarmPacketBuilder.buildSenderPacket(new DevicePayload("131213156", (short)1), CommandTypeEnum.SEND);
		System.out.println(packet.getId());
		FireAlarmPacket packet2 = FireAlarmPacketBuilder.buildSenderPacket(new DevicePayload("131213156", (short)1), CommandTypeEnum.SEND);
		System.out.println(packet2.getId());
	}
}
