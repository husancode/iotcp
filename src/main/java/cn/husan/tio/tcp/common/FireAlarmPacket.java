package cn.husan.tio.tcp.common;

import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.core.utils.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

/**
 * @Auther: husan
 * @Date: 2019/10/15 15:52
 * @Description:
 */
public class FireAlarmPacket extends Packet {

	private static final long serialVersionUID = 1L;

	private static final byte START_FLAG = 64;

	private static final byte STOP_FLAG = 31;

	private static final int PACKET_HEAD_LENTH = 31;
	/**
	 * 主版本号
	 */
	private static final int MAIN_VERSION_NUMBER = 2;

	private int sequenceNumber;
	/**
	 * 用户版本号0-255
	 */
	private int userVersion;
	/**
	 * 时间： 6个字节
	 *
	 */
	private Date timeStamp;
	/**
	 * 源地址： 6个字节
	 */
	private byte[] sourceAddress;
	/**
	 * 目的地址： 6个字节
	 */
	private byte[] targetAddress;
	/**
	 * 数据长度:2个字节，不大于990
	 */
	private int dataLength;
	/**
	 * 命令字节
	 */
	private int commandType;

	private byte[] body;

	private int crc16;

	private static void checkStartFlag(ByteBuffer buffer) throws AioDecodeException {
		if(START_FLAG != buffer.get() || START_FLAG != buffer.get()){
			System.out.println("start flag decode error!");
			throw new AioDecodeException("start flag decode error!");
		}

	}

	private static void checkEndFlag(ByteBuffer buffer) throws AioDecodeException{
		if(STOP_FLAG != buffer.get() || STOP_FLAG != buffer.get()){
			throw new AioDecodeException("end flag decode error!");
		}
	}


	public static FireAlarmPacket decode(ByteBuffer buffer) throws AioDecodeException {
		checkStartFlag(buffer);
		FireAlarmPacket packet = new FireAlarmPacket();
		System.out.println(packet.getId());
		packet.setSequenceNumber(ByteBufferUtils.readUB2(buffer));
		if(buffer.get() != MAIN_VERSION_NUMBER){
			throw new AioDecodeException("main version decode error!");
		}
		packet.setUserVersion(ByteBufferUtils.readUB1(buffer));
		packet.setTimeStamp(convert(buffer));
		packet.setSourceAddress(ByteBufferUtils.readBytes(buffer, 6));
		packet.setTargetAddress(ByteBufferUtils.readBytes(buffer, 6));
		int bodyLength = ByteBufferUtils.readUB2(buffer);
		if(bodyLength > 990){
			throw new AioDecodeException(String.format("body lenth(%s) greter than 990!", bodyLength));
		}
		packet.setDataLength(bodyLength);
		int commandType = buffer.get();
		if(commandType < 0 || commandType > 127){
			throw new AioDecodeException(String.format("commandType(%s)  exceed 0-127"));
		}
		packet.setCommandType(commandType);
		packet.setBody(ByteBufferUtils.readBytes(buffer, bodyLength));
		//不校验crc16
		packet.setCrc16(ByteBufferUtils.readUB2(buffer));
		checkEndFlag(buffer);
		return packet;
	}

	public ByteBuffer encode(){
		ByteBuffer buffer = ByteBuffer.allocate(PACKET_HEAD_LENTH+dataLength);
		buffer.put(START_FLAG);
		buffer.put(START_FLAG);
		ByteBufferUtils.writeUB2(buffer, sequenceNumber);
		buffer.put((byte)MAIN_VERSION_NUMBER);
		buffer.put((byte)userVersion);
		encodeDate(buffer);
		buffer.put(sourceAddress);
		buffer.put(targetAddress);
		ByteBufferUtils.writeUB2(buffer, dataLength);
		buffer.put((byte)commandType);
		if(dataLength > 0){
			buffer.put(body);
		}
		ByteBufferUtils.writeUB2(buffer, crc16);
		buffer.put(STOP_FLAG);
		buffer.put(STOP_FLAG);
		buffer.rewind();
		return buffer;
	}

	private static Date convert(ByteBuffer buffer){
		byte second = buffer.get();
		byte minute = buffer.get();
		byte hour = buffer.get();
		byte day = buffer.get();
		byte month = buffer.get();
		byte year = buffer.get();
		Date date = new Date();
		date.setYear(year);
		date.setMonth(month-1);
		date.setDate(day);
		date.setHours(hour);
		date.setMinutes(minute);
		date.setSeconds(second);
		return date;
	}

	private void encodeDate(ByteBuffer buffer){
		buffer.put((byte)timeStamp.getSeconds());
		buffer.put((byte)timeStamp.getMinutes());
		buffer.put((byte)timeStamp.getHours());
		buffer.put((byte)timeStamp.getDay());
		buffer.put((byte)(timeStamp.getMonth()+1));
		buffer.put((byte) timeStamp.getYear());
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getUserVersion() {
		return userVersion;
	}

	public void setUserVersion(int userVersion) {
		this.userVersion = userVersion;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public byte[] getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(byte[] sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public byte[] getTargetAddress() {
		return targetAddress;
	}

	public void setTargetAddress(byte[] targetAddress) {
		this.targetAddress = targetAddress;
	}

	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public int getCommandType() {
		return commandType;
	}

	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public int getCrc16() {
		return crc16;
	}

	public void setCrc16(int crc16) {
		this.crc16 = crc16;
	}

	@Override
	public String toString() {
		return "FireAlarmPacket{" +
				"sequenceNumber=" + sequenceNumber +
				", userVersion=" + userVersion +
				", timeStamp=" + timeStamp +
				", sourceAddress=" + Arrays.toString(sourceAddress) +
				", targetAddress=" + Arrays.toString(targetAddress) +
				", dataLength=" + dataLength +
				", commandType=" + commandType +
				", body=" + Arrays.toString(body) +
				", crc16=" + crc16 +
				'}';
	}
}
