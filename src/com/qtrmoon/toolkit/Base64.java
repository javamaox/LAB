package com.qtrmoon.toolkit;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

public class Base64 {
	/**
	 * base64码表
	 */
	private static final byte base[] = { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46,
			0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d, 0x4e, 0x4f, 0x50, 0x51,
			0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a, 0x61, 0x62,
			0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d,
			0x6e, 0x6f, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78,
			0x79, 0x7a, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38,
			0x39, 0x2b, 0x2f };

	/**
	 * 解码的方法 传进来的是编码后的base64字符的字节码 解析时是4个一组进行解析
	 * 
	 * @param b编码后的字符的字节码数组
	 * @return 返回原来的字符串
	 */
	public static byte[] decode(byte[] b) {
		byte[] sb = new byte[b.length*3/4];
		Vector<Byte> list = new Vector<Byte>();
		int real_len = b.length;
		int len = real_len - 2;
		int more_len = len & 3;
		int use_len = len - more_len;

		for (int i = 0; i < use_len; i += 4) {
			list.add(backFirst(baseIndex(b[i]), baseIndex(b[i + 1])));
			list.add(backSecond(baseIndex(b[i + 1]), baseIndex(b[i + 2])));
			list.add(backThird(baseIndex(b[i + 2]), baseIndex(b[i + 3])));
		}
		Enumeration e = list.elements();
		byte bytes[]=new byte[b.length*3/4];
		int k = -1;
		while (e.hasMoreElements()) {
			bytes[++k] = (Byte) e.nextElement();
		}

		if (more_len == 2) {
			bytes[bytes.length-1]=backLastOne(baseIndex(b[len - 2]), baseIndex(b[len - 1]),2, 6);
		}else if (more_len == 3) {
			byte b_2[] = new byte[2];
			b_2[0] = backFirst(baseIndex(b[len - 3]), baseIndex(b[len - 2]));
			b_2[1] = backLastOne(baseIndex(b[len - 2]), baseIndex(b[len - 1]),4, 4);
			bytes[bytes.length-2]=b_2[0];
			bytes[bytes.length-1]=b_2[1];
		}
		return bytes;
	}
	/**
	 * 编码 将传进来的字符编码为base64，返回一个base64的字符串 编码时3个字节一组进行编码，传进来的是要进行编码的字符串数组
	 * 
	 * @param b要进行编码的字符串数组
	 * @return 编码后的字符串
	 */
	public static String encode(byte[] b) {
		StringBuffer sb = new StringBuffer();
		int len = b.length;
		int more_len = len % 3;
		int use_len = len - more_len;
		byte[] bytes = new byte[4];
		for (int i = 0; i < use_len; i += 3) {
			bytes[0] = base[firstByte(b[i])];
			bytes[1] = base[secondByte(b[i], b[i + 1])];
			bytes[2] = base[thirdByte(b[i + 1], b[i + 2])];
			bytes[3] = base[fourthByte(b[i + 2])];
			sb.append(new String(bytes));
		}
		if (more_len == 1) {
			byte b_2[] = new byte[2];
			b_2[0] = base[firstByte(b[len - 1])];
			b_2[1] = base[lastOneByte(b[len - 1], 6)];
			sb.append(new String(b_2));
			return sb.append("==").toString();
		} else if (more_len == 2) {
			byte b_3[] = new byte[3];
			b_3[0] = base[firstByte(b[len - 2])];
			b_3[1] = base[secondByte(b[len - 2], b[len - 1])];
			b_3[2] = base[lastOneByte(b[len - 1], 4)];
			sb.append(new String(b_3));
			return sb.append("=").toString();
		}
		return sb.toString();
	}
	//--------------------------------------------
	/**
	 * 根据传进来的字符的字节码，查询base64码表的索引，并返回所查到的索引
	 * 
	 * @param b一个编码后的字节码
	 * @return 返回base64码表的索引
	 */
	private static byte baseIndex(byte b) {
		for (int i = 0; i < base.length; i++) {
			if (base[i] == b) {
				return (byte) i;
			}
		}
		return -1;
	}
	
	/**
	 * 解码 将4个字节码中的第1个的后6位（00xxxxxx）和第2个 字节的前4位的后2位（00yy0000） 还原为原来的字节码（xxxxxxyy）
	 * 
	 * @param first4个字节码中的第1个
	 * @param second4个字节码中的第2个
	 * @return 原来的字符的字节码
	 */
	private static byte backFirst(byte first, byte second) {
		int r_f = first & 0xff;
		int r_s = second & 0xff;
		r_f = r_f << 2;
		r_s = r_s >>> 4;
		return (byte) ((r_f | r_s) & 0xff);
	}

	/**
	 * 解码 将4个字节码中的第2个的后4位（0000xxxx）和第3个 字节的前6位的后4位（00yyyy00） 还原为原来的字节码（xxxxyyyy）
	 * 
	 * @param second4个字节码中的第2个
	 * @param third4个字节码中的第3个
	 * @return 原来的字符的字节码
	 */
	private static byte backSecond(byte second, byte third) {
		int r_s = second & 0xff;
		int r_t = third & 0xff;
		r_s = r_s << 4;
		r_t = r_t >>> 2;
		return (byte) ((r_s | r_t) & 0xff);
	}

	/**
	 * 解码 将4个字节码中的第3个的后2位（000000xx）和第4个 字节的后6位（00yyyyyy） 还原为原来的字节码（xxyyyyyy）
	 * 
	 * @param third传进来的第3个字符
	 * @param fourth传进来的第4个字符
	 * @return 原来的字符的字节码
	 */
	private static byte backThird(byte third, byte fourth) {
		int r_t = third & 0xff;
		int r_f = fourth & 0xff;
		r_t = r_t << 6;
		return (byte) ((r_t | r_f) & 0xff);
	}
	
	/**
	 * 解码 将编码后的字符串数组的最后2个字节码还原为原来的字节码 假如数组末尾剩下2个字节： 将倒数第2个字节的前后6位(00xxxxxx)
	 * 和倒数第一个字节的后2位(000000yy) 还原为原来的编码（xxxxxxyy） 假如数组末尾剩下3个字节：
	 * 将倒数第2个字节的前后4位(0000xxxx) 和倒数第一个字节的后4位(0000yyyy) 还原为原来的编码（xxxxyyyy）
	 * 
	 * @param last_b倒数第2个字节
	 * @param next_b倒数第1个字节
	 * @param move_l倒数第2个字节移动位数的参数
	 * @param move_b倒数第1个字节移动位数的参数
	 * @return 原来的字符的字节码
	 */
	private static byte backLastOne(byte last_b, byte next_b, int move_l, int move_b) {
		int r_l = last_b & 0xff;
		int r_n = next_b & 0xff;
		r_l = r_l << move_l;
		r_n = r_n << move_b;
		r_n = r_n >>> move_b;
		return (byte) ((r_l | r_n) & 0xff);
	}
	
//----------------------------------------------	
	/**
	 * 编码 假如b=xxxxyyyy 将第1个字节的前6位编码为base64 将3个字节中的第1个子节码转为（00xxxxyy）
	 * 
	 * @paramb 3个字节中的第1个字节
	 * @return 编码后的字节码
	 */
	private static byte firstByte(byte b) {
		// 00000000000000000000000001010011
		// 01010011
		int r_f = b & 0xff;
		r_f = r_f >>> 2;
		return (byte) (r_f & 0x3f);
	}
	
	/**
	 * 编码 假如last_b=xxxxyyyynext_b=kkkkffff 将3个字节中的第1个字节的最后2位（000000yy）
	 * 和第2个字节的前4位（kkkk0000）编码为（00yykkkk）
	 * 
	 * @param last_b3个字节中的第1个字节
	 * @param next_b3个字节中的第2个字节
	 * @return 编码后的字节码
	 */
	private static byte secondByte(byte last_b, byte next_b) {
		int r_l = last_b & 0xff;
		int r_n = next_b & 0xff;
		r_l = r_l << 6;
		r_l = r_l >>> 2;
		r_n = r_n >>> 4;
		return (byte) ((r_l | r_n) & 0x3f);
	}
	
	/**
	 * 编码 假如last_b=xxxxyyyynext_b=kkkkffff 将3个字节中的第2个字节的最后4位（0000yyyy）
	 * 和第4个字节的前2位（kk000000）编码为（00yyyykk）
	 * 
	 * 
	 * @param last_b3个字节中的第2个字节
	 * @param next_b3个字节中的第3个字节
	 * @return 编码后的字节码
	 */
	private static byte thirdByte(byte last_b, byte next_b) {
		int r_l = last_b & 0xff;
		int r_n = next_b & 0xff;
		r_l = r_l << 4;
		r_l = r_l >>> 2;
		r_n = r_n >>> 6;
		return (byte) ((r_l | r_n) & 0x3f);
	}
	
	/**
	 * 编码 假如b=xxxxyyyy 将3个字节中的第3个字节的最后6位（00xxyyyy） 转码为（00xxyyyy）
	 * 
	 * @param b3个字节中的第3个字节
	 * @return 编码后的字节码
	 */
	private static byte fourthByte(byte b) {
		int r_b = b & 0xff;
		r_b = r_b << 2;
		r_b = r_b >>> 2;
		return (byte) (r_b & 0x3f);
	}
	
	/**
	 * 假如字符长度%3！=0，使用此方法编码末尾字符 假如b=xxxxyyyy 假如末尾字节个数等于1：
	 * 将这个字节的前6位作为一个字节(00xxxxyy) 将这个字节的后6位作为一个字节(00xxyyyy) 假如末尾字节个数等于2：
	 * 将这个字节的后6位作为一个字节(00xxyyyy)
	 * 
	 * @param b末尾的字符的字节码
	 * @param move末尾的字符的字节码要移动的位数的参数
	 * @return 编码后的字节码
	 */
	private static byte lastOneByte(byte b, int move) {
		int r_b = b & 0xff;
		r_b = r_b << move;
		r_b = r_b >>> 2;
		return (byte) (r_b & 0x3f);
	}
}
