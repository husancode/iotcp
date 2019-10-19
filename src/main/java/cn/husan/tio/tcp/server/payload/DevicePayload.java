package cn.husan.tio.tcp.server.payload;

import java.nio.ByteBuffer;

/**
 * @Auther: husan
 * @Date: 2019/10/16 14:55
 * @Description:
 * 设备序列号
 */
public class DevicePayload implements Payload {

	public static final int MAX_SIZE = 64;

	private String subSerial;

	private short status;

	public DevicePayload(){}

	public DevicePayload(String subSerial, short status){
		this.subSerial = subSerial;
		this.status = status;
	}

	@Override
	public byte[] encode() {
		byte[] subSrialBytes = subSerial.getBytes();
		ByteBuffer byteBuffer = ByteBuffer.allocate(subSrialBytes.length + 2);
		byteBuffer.put(subSrialBytes);
		byteBuffer.putShort(status);
		return byteBuffer.array();
	}

	@Override
	public String toString() {
		return "DevicePayload{" +
				"subSerial='" + subSerial + '\'' +
				", status=" + status +
				'}';
	}

	public String getSubSerial() {
		return subSerial;
	}

	public void setSubSerial(String subSerial) {
		this.subSerial = subSerial;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}
}
