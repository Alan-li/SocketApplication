package com.example.socket_lib.utils;

import java.io.UnsupportedEncodingException;

/**
 * 128位MD5加密
 */
public class MD5Code {
	/*
	 * 下面这些S11-S44实际上是一个4*4的矩阵,在四轮循环运算中用到
	 * 在原始的C实现中是用#define定义的，这里定义为static final，表示只读
	 */
	static final int S11 = 7;
	static final int S12 = 12;
	static final int S13 = 17;
	static final int S14 = 22;

	static final int S21 = 5;
	static final int S22 = 9;
	static final int S23 = 14;
	static final int S24 = 20;

	static final int S31 = 4;
	static final int S32 = 11;
	static final int S33 = 16;
	static final int S34 = 23;

	static final int S41 = 6;
	static final int S42 = 10;
	static final int S43 = 15;
	static final int S44 = 21;

	static final byte[] PADDING = { -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0 };

	/*
	 * 下面的三个成员是MD5计算过程中用到的3个核心数据，在原始的C实现中 被定义到MD5_CTX结构中
	 */
	private long[] state = new long[4];// state = {A, B, C, D}，存储hash结果，共4×32=128位，初始化值为（幻数的级联）

	private long[] count = new long[2];// number of bits, modulo 2^64 

	private byte[] buffer = new byte[64]; // 输入缓存数组，64个字节

	private byte[] digest = new byte[16];  // 最新一次计算结果的2进制内部表示，表示128bit的MD5值（8bit × 16 = 128bit）

	public String digestHexStr; // 最新一次计算结果的16进制的32位MD5值（每一位16进制数可以代替4位二进制数:128bit / 4 = 32）


	/*
	 * F, G, H ,I是4个基本的MD5函数,每次操作中用到的四个非线性函数（每轮一个）
	 * 如果X、Y和Z的对应位是独立和均匀的，那么结果的每一位也应是独立和均匀的
	 * F是一个逐位运算的函数。即，如果X，那么Y，否则Z。函数H是逐位奇偶操作符
	 */
	private long F(long x, long y, long z) {
		return (x & y) | ((~x) & z);
	}

	private long G(long x, long y, long z) {
		return (x & z) | (y & (~z));
	}

	private long H(long x, long y, long z) {
		return x ^ y ^ z;
	}

	private long I(long x, long y, long z) {
		return y ^ (x | (~z));
	}


	/*
	 * FF, GG, HH, II将调用F, G, H, I进行进一步变换
	 */
	private long FF(long a, long b, long c, long d, long x, long s, long ac) {
		a += F(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;

		// a = b + ((a + (F(b,c,d) + x + ac) <<< s)
		return a;
	}

	private long GG(long a, long b, long c, long d, long x, long s, long ac) {
		a += G(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;

		// a = b + ((a + (G(b,c,d) + x + ac) <<< s)
		return a;
	}

	private long HH(long a, long b, long c, long d, long x, long s, long ac) {
		a += H(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;

		// a = b + ((a + (H(b,c,d) + x + ac) <<< s)
		return a;
	}

	private long II(long a, long b, long c, long d, long x, long s, long ac) {
		a += I(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;

		// a = b + ((a + (I(b,c,d) + x + ac) <<< s)
		return a;
	}

	/*
	 * 四轮循环运算(共64步)
	 * MD5核心变换程序，由md5Update调用，block是分块的原始字节
	 */
	private void md5Transform(byte block[]) {
		long a = state[0], b = state[1], c = state[2], d = state[3];
		long[] x = new long[16];
		Decode(x, block, 64);

		//Mj表示消息的第j个子分组（0-15），s表示循环左移的位数，
		//ti是4294967296*abs(sin(i))的整数部分(4294967296 = 2^32) ，i表示运算步数（单位是弧度）
		//比如：4294967296*abs(sin(1 rad)) = 3614090360 ，即0xd76aa478

		//第一轮：a = FF(a, b, c, d, Mj, s, ti)，即a = b + ((a + (F(b,c,d) + Mj + ti) <<< s)
		a = FF(a, b, c, d, x[0], S11, 0xd76aa478L); /* 1 */
		d = FF(d, a, b, c, x[1], S12, 0xe8c7b756L); /* 2 */
		c = FF(c, d, a, b, x[2], S13, 0x242070dbL); /* 3 */
		b = FF(b, c, d, a, x[3], S14, 0xc1bdceeeL); /* 4 */
		a = FF(a, b, c, d, x[4], S11, 0xf57c0fafL); /* 5 */
		d = FF(d, a, b, c, x[5], S12, 0x4787c62aL); /* 6 */
		c = FF(c, d, a, b, x[6], S13, 0xa8304613L); /* 7 */
		b = FF(b, c, d, a, x[7], S14, 0xfd469501L); /* 8 */
		a = FF(a, b, c, d, x[8], S11, 0x698098d8L); /* 9 */
		d = FF(d, a, b, c, x[9], S12, 0x8b44f7afL); /* 10 */
		c = FF(c, d, a, b, x[10], S13, 0xffff5bb1L); /* 11 */
		b = FF(b, c, d, a, x[11], S14, 0x895cd7beL); /* 12 */
		a = FF(a, b, c, d, x[12], S11, 0x6b901122L); /* 13 */
		d = FF(d, a, b, c, x[13], S12, 0xfd987193L); /* 14 */
		c = FF(c, d, a, b, x[14], S13, 0xa679438eL); /* 15 */
		b = FF(b, c, d, a, x[15], S14, 0x49b40821L); /* 16 */

		//第二轮：a = GG(a, b, c, d, Mj, s, ti)，即a = b + ((a + (G(b,c,d) + Mj + ti) <<< s)
		a = GG(a, b, c, d, x[1], S21, 0xf61e2562L); /* 17 */
		d = GG(d, a, b, c, x[6], S22, 0xc040b340L); /* 18 */
		c = GG(c, d, a, b, x[11], S23, 0x265e5a51L); /* 19 */
		b = GG(b, c, d, a, x[0], S24, 0xe9b6c7aaL); /* 20 */
		a = GG(a, b, c, d, x[5], S21, 0xd62f105dL); /* 21 */
		d = GG(d, a, b, c, x[10], S22, 0x2441453L); /* 22 */
		c = GG(c, d, a, b, x[15], S23, 0xd8a1e681L); /* 23 */
		b = GG(b, c, d, a, x[4], S24, 0xe7d3fbc8L); /* 24 */
		a = GG(a, b, c, d, x[9], S21, 0x21e1cde6L); /* 25 */
		d = GG(d, a, b, c, x[14], S22, 0xc33707d6L); /* 26 */
		c = GG(c, d, a, b, x[3], S23, 0xf4d50d87L); /* 27 */
		b = GG(b, c, d, a, x[8], S24, 0x455a14edL); /* 28 */
		a = GG(a, b, c, d, x[13], S21, 0xa9e3e905L); /* 29 */
		d = GG(d, a, b, c, x[2], S22, 0xfcefa3f8L); /* 30 */
		c = GG(c, d, a, b, x[7], S23, 0x676f02d9L); /* 31 */
		b = GG(b, c, d, a, x[12], S24, 0x8d2a4c8aL); /* 32 */

		//第三轮：a = HH(a, b, c, d, Mj, s, ti)，即a = b + ((a + (H(b,c,d) + Mj + ti) <<< s)
		a = HH(a, b, c, d, x[5], S31, 0xfffa3942L); /* 33 */
		d = HH(d, a, b, c, x[8], S32, 0x8771f681L); /* 34 */
		c = HH(c, d, a, b, x[11], S33, 0x6d9d6122L); /* 35 */
		b = HH(b, c, d, a, x[14], S34, 0xfde5380cL); /* 36 */
		a = HH(a, b, c, d, x[1], S31, 0xa4beea44L); /* 37 */
		d = HH(d, a, b, c, x[4], S32, 0x4bdecfa9L); /* 38 */
		c = HH(c, d, a, b, x[7], S33, 0xf6bb4b60L); /* 39 */
		b = HH(b, c, d, a, x[10], S34, 0xbebfbc70L); /* 40 */
		a = HH(a, b, c, d, x[13], S31, 0x289b7ec6L); /* 41 */
		d = HH(d, a, b, c, x[0], S32, 0xeaa127faL); /* 42 */
		c = HH(c, d, a, b, x[3], S33, 0xd4ef3085L); /* 43 */
		b = HH(b, c, d, a, x[6], S34, 0x4881d05L); /* 44 */
		a = HH(a, b, c, d, x[9], S31, 0xd9d4d039L); /* 45 */
		d = HH(d, a, b, c, x[12], S32, 0xe6db99e5L); /* 46 */
		c = HH(c, d, a, b, x[15], S33, 0x1fa27cf8L); /* 47 */
		b = HH(b, c, d, a, x[2], S34, 0xc4ac5665L); /* 48 */

		//第四轮：a = II(a, b, c, d, Mj, s, ti)，即a = b + ((a + (I(b,c,d) + Mj + ti) <<< s)
		a = II(a, b, c, d, x[0], S41, 0xf4292244L); /* 49 */
		d = II(d, a, b, c, x[7], S42, 0x432aff97L); /* 50 */
		c = II(c, d, a, b, x[14], S43, 0xab9423a7L); /* 51 */
		b = II(b, c, d, a, x[5], S44, 0xfc93a039L); /* 52 */
		a = II(a, b, c, d, x[12], S41, 0x655b59c3L); /* 53 */
		d = II(d, a, b, c, x[3], S42, 0x8f0ccc92L); /* 54 */
		c = II(c, d, a, b, x[10], S43, 0xffeff47dL); /* 55 */
		b = II(b, c, d, a, x[1], S44, 0x85845dd1L); /* 56 */
		a = II(a, b, c, d, x[8], S41, 0x6fa87e4fL); /* 57 */
		d = II(d, a, b, c, x[15], S42, 0xfe2ce6e0L); /* 58 */
		c = II(c, d, a, b, x[6], S43, 0xa3014314L); /* 59 */
		b = II(b, c, d, a, x[13], S44, 0x4e0811a1L); /* 60 */
		a = II(a, b, c, d, x[4], S41, 0xf7537e82L); /* 61 */
		d = II(d, a, b, c, x[11], S42, 0xbd3af235L); /* 62 */
		c = II(c, d, a, b, x[2], S43, 0x2ad7d2bbL); /* 63 */
		b = II(b, c, d, a, x[9], S44, 0xeb86d391L); /* 64 */

		// 链接变量的级联操作
		state[0] += a;
		state[1] += b;
		state[2] += c;
		state[3] += d;
	}

	// 带参数的构造函数
	public MD5Code(long pseudoRandomNumber) {
		md5Init(pseudoRandomNumber);
	}


	/*
	 * getMD5ofStr是类MD5最主要的公共方法，入口参数是你想要进行MD5变换的字符串
	 * 返回的是变换后的结果，这个结果是从公共成员digestHexStr取得的．
	 */
	public String getMD5ofStr(String inbuf, long pseudoRandomNumber) {

		try {
			md5Init(pseudoRandomNumber);
			md5Update(inbuf.getBytes("UTF-8"), inbuf.length());
			md5Final();

			digestHexStr = "";
			for (int i = 0; i < 16; i++) {
				digestHexStr += byteHEX(digest[i]);
			}

			return digestHexStr;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}


	//md5Init是一个初始化函数，初始化核心变量
	private void md5Init(long pseudoRandomNumber) {
		count[0] = 0L;
		count[1] = 0L;

   	   /*
	    * 装入非标准幻数（标准幻数 + 伪随机数），实现MD5差异性加密的关键
	    * 标准幻数（链接变量）是:A=(01234567)16，B=(89abcdef)16，C=(fedcba98)16，D=(76543210)16 （物理顺序）
 	    * 如果在程序中定义应该是:A=0x67452301l，B=0xefcdab89l，C=0x98badcfel，D=0x10325476l（内存的存储数据是低位在左，高位在右）
    	* 伪随机数可以自定义(通过传入的种子来确定，伪随机数不同，生成的MD5值也不同，所以需要记住设置的伪随机数)
    	*/
		state[0] = 0x67452301L + (pseudoRandomNumber * 11);
		state[1] = 0xefcdab89L + (pseudoRandomNumber * 71);
		state[2] = 0x98badcfeL + (pseudoRandomNumber * 37);
		state[3] = 0x10325476L + (pseudoRandomNumber * 97);
	}

	/*
	 * md5Update是MD5的主计算过程，inbuf是要变换的字节串，inputlen是长度
	 * 这个函数由getMD5ofStr调用，调用之前需要调用md5init
	 */
	private void md5Update(byte[] inbuf, int inputLen) {
		int i, index, partLen;
		byte[] block = new byte[64];
		index = (int) (count[0] >>> 3) & 0x3f;

		// /* Update number of bits */
		if ((count[0] += (inputLen << 3)) < (inputLen << 3))
			count[1]++;
		count[1] += (inputLen >>> 29);
		partLen = 64 - index;

		// Transform as many times as possible.
		if (inputLen >= partLen) {
			md5Memcpy(buffer, inbuf, index, 0, partLen);
			md5Transform(buffer);
			for (i = partLen; i + 63 < inputLen; i += 64) {
				md5Memcpy(block, inbuf, 0, i, 64);
				md5Transform(block);
			}
			index = 0;
		} else
			i = 0;

		// /* Buffer remaining input */
		md5Memcpy(buffer, inbuf, index, i, inputLen - i);
	}

	/*
	 * md5Final整理和填写输出结果
	 */
	private void md5Final() {
		byte[] bits = new byte[8];
		int index, padLen;

		// /* Save number of bits */
		Encode(bits, count, 8);

		//填充信息长度
		//在MD5算法中，需要对信息进行填充，使其比特位长度减去448后能被512整除(位长度对512求余的结果等于448)
		//信息的位长度将被扩展至N*512+448（Bits），即N*64+56个字节（Bytes），N为一个非负整数
		//填充的方法如下，在信息的后面填充一个1和无数个0，直到满足上面的条件时才停止用0对信息的填充
		//然后，再在这个结果后面附加一个以64位二进制表示的填充前信息长度
		//经过这两步的处理，现在的信息字节长度=N*512+448+64=(N+1)*512，即长度恰好是512的整数倍
		//这样做的原因是为满足后面处理中对信息长度的要求
		index = (int) (count[0] >>> 3) & 0x3f;
		padLen = (index < 56) ? (56 - index) : (120 - index);
		md5Update(PADDING, padLen);

		// /* Append length (before padding) */
		md5Update(bits, 8);

		// /* Store state in digest */
		Encode(digest, state, 16);
	}

	/*
	 * md5Memcpy是一个内部使用的byte数组的块拷贝函数
	 * 从input的inpos开始把len长度的字节拷贝到output的outpos位置开始
	 */
	private void md5Memcpy(byte[] output, byte[] input, int outpos, int inpos,
						   int len) {
		int i;
		for (i = 0; i < len; i++)
			output[outpos + i] = input[inpos + i];
	}



	/*
	 * Encode把long数组按顺序拆成byte数组，因为java的long类型是64bit的，只拆低32bit，以适应原始C实现的用途
	 */
	private void Encode(byte[] output, long[] input, int len) {
		int i, j;
		for (i = 0, j = 0; j < len; i++, j += 4) {
			output[j] = (byte) (input[i] & 0xff);
			output[j + 1] = (byte) ((input[i] >>> 8) & 0xff);
			output[j + 2] = (byte) ((input[i] >>> 16) & 0xff);
			output[j + 3] = (byte) ((input[i] >>> 24) & 0xff);
		}
	}

	/*
	 * Decode把byte数组按顺序合成成long数组，因为java的long类型是64bit的，
	 * 只合成低32bit，高32bit清零，以适应原始C实现的用途
	 */
	private void Decode(long[] output, byte[] input, int len) {
		int i, j;
		for (i = 0, j = 0; j < len; i++, j += 4)
			output[i] = b2iu(input[j]) | (b2iu(input[j + 1]) << 8)
					| (b2iu(input[j + 2]) << 16) | (b2iu(input[j + 3]) << 24);
	}

	/*
	 * 把byte按照不考虑正负号的原则的＂升位＂程序，因为java没有unsigned运算
	 */
	public static long b2iu(byte b) {
		return b < 0 ? b & 0x7f + 128 : b;
	}

	/*
	 * byteHEX()，用来把一个byte类型的数转换成十六进制的ASCII表示，
	 * 因为java中的byte的toString无法实现这一点，java没有类似C语言中的sprintf(outbuf,"%02X",ib)方法
	 */
	public static String byteHEX(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
				'b', 'c', 'd', 'e', 'f' };

		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0x0f];
		ob[1] = Digit[ib & 0x0f];

		String s = new String(ob);
		return s;
	}

}
