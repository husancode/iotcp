package cn.husan.tio.tcp.common;

import cn.husan.tio.tcp.server.handler.DeviceFireBusinessHandler;
import cn.husan.tio.tcp.server.handler.FireBusinessHandler;
import cn.husan.tio.tcp.server.handler.HeartbeatHandler;

/**
 * @Auther: husan
 * @Date: 2019/10/16 13:12
 * @Description:
 */
public enum CommandTypeEnum {

	SEND(2, new DeviceFireBusinessHandler()),HEARTBEAT(7, new HeartbeatHandler()),;

	private int commandType;

	private FireBusinessHandler handler;

	private CommandTypeEnum(int commandType, FireBusinessHandler handler){
		this.commandType = commandType;
		this.handler = handler;
	}

	public int getCommandType() {
		return commandType;
	}

	public static CommandTypeEnum of(int commandType){
		for(CommandTypeEnum commandTypeEnum : values()){
			if(commandTypeEnum.getCommandType() == commandType){
				return commandTypeEnum;
			}
		}
		return null;
	}

	public FireBusinessHandler getHandler() {
		return handler;
	}}
