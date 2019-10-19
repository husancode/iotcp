/*
 * 	一、服务条款
	1. 被授权者务必尊重知识产权，严格保证不恶意传播产品源码、不得直接对授权的产品本身进行二次转售或倒卖、不得对授权的产品进行简单包装后声称为自己的产品等。否则我们有权利收回产品授权，并根据事态轻重追究相应法律责任。
	2. 被授权者可将授权后的产品用于任意符合国家法律法规的应用平台，但一个授权使用不得超过5个域名和5个项目。
	3. 授权 t-io 官方的付费产品（如：t-io官方文档阅读权限、tio-study等），不支持退款。
	4. 我们有义务为被授权者提供有效期内的产品下载、更新和维护，一旦过期，被授权者无法享有相应权限。终身授权则不受限制。
	t-io
	5. t-io 官方的付费产品中的”tio-study”并不保证源代码中绝对不出现BUG，用户遇到BUG时希望可以及时反馈给t-io，相信在大众的努力下，”tio-study”这个产品会越来越成熟，越来越普及
	6. 本条款主要针对恶意用户，在实施过程中会保持灵活度，毕竟谁也不想找麻烦，所以请良心用户可以放心使用！
	7. 本站有停止运营的风险，如果本站主动暂停或停止运营，本站有义务提前30天告知所有用户，以便用户作好相应准备；如果本站受不可抗拒因素暂停或停止运营（包括但不限于遭受DDOS攻击、SSL临时过期、手续升级等），本站不承担任何责任
	
	二、免责声明
	服务条款第2条已经说得很清楚，被授权者只能将产品应用于符合国家法律法规的应用平台，如果被授权者违背该条，由此产生的法律后果由被授权者独自承担，与t-io及t-io作者无关
 */


package cn.husan.tio.showcase.common;

import java.nio.ByteBuffer;

import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.AioHandler;
import org.tio.core.intf.Packet;

/**
 *
 * @author tanyaowu
 * 2017年3月27日 上午12:14:12
 */
public abstract class ShowcaseAbsAioHandler implements AioHandler {
	/**
	 * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
	 * 消息头：type + bodyLength
	 * 消息体：byte[]
	 */
	@Override
	public ShowcasePacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
		//可读数据，小于头部的固定长度，直接返回null，这样tio框架会自动把本次收到的数据暂存起来，并和下次收到的数据组合起来
		if (readableLength < ShowcasePacket.HEADER_LENGTH) {
			return null;
		}

		//position的值不一定是0，但是
		//消息类型
		byte type = buffer.get();

		int bodyLength = buffer.getInt();

		if (bodyLength < 0) {
			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
		}

		int neededLength = ShowcasePacket.HEADER_LENGTH + bodyLength;
//		int test = readableLength - neededLength;
		if (readableLength < neededLength) // 不够消息体长度(剩下的buffe组不了消息体)
		{
			return null;
		} else {
			ShowcasePacket imPacket = new ShowcasePacket();
			imPacket.setType(type);
			if (bodyLength > 0) {
				byte[] dst = new byte[bodyLength];
				buffer.get(dst);
				imPacket.setBody(dst);
			}
			return imPacket;
		}
	}

	/**
	 * 编码：把业务消息包编码为可以发送的ByteBuffer
	 * 消息头：type + bodyLength
	 * 消息体：byte[]
	 */
	@Override
	public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
		ShowcasePacket showcasePacket = (ShowcasePacket) packet;
		byte[] body = showcasePacket.getBody();
		int bodyLen = 0;
		if (body != null) {
			bodyLen = body.length;
		}

		//总长度是消息头的长度+消息体的长度
		int allLen = ShowcasePacket.HEADER_LENGTH + bodyLen;

		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		buffer.order(tioConfig.getByteOrder());

		//写入消息类型
		buffer.put(showcasePacket.getType());
		//写入消息体长度
		buffer.putInt(bodyLen);

		//写入消息体
		if (body != null) {
			buffer.put(body);
		}
		return buffer;
	}
}
