package org.lf.wx.message.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.lf.wx.WXProperties;
import org.lf.wx.utils.WXUtils;

/**
 * 提供接收和推送给公众平台消息的加解密接口(UTF8编码的字符串).
 * <ol>
 * 	<li>第三方回复加密消息给公众平台</li>
 * 	<li>第三方收到公众平台发送的消息，验证消息的安全性，并对消息进行解密。</li>
 * </ol>
 * 说明：异常java.security.InvalidKeyException:illegal Key Size的解决方案
 * <ol>
 * 	<li>在官方网站下载JCE无限制权限策略文件（JDK7的下载地址：
 *      http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html</li>
 * 	<li>下载后解压，可以看到local_policy.jar和US_export_policy.jar以及readme.txt</li>
 * 	<li>如果安装了JRE，将两个jar文件放到%JRE_HOME%\lib\security目录下覆盖原来的文件</li>
 * 	<li>如果安装了JDK，将两个jar文件放到%JDK_HOME%\jre\lib\security目录下覆盖原来文件</li>
 * </ol>
 */
public class WXEncryptUtils {
	private static Charset CHARSET = Charset.forName("utf-8");

	// 生成4个字节的网络字节序
	private static byte[] getNetworkBytesOrder(int sourceNumber) {
		byte[] orderBytes = new byte[4];
		orderBytes[3] = (byte) (sourceNumber & 0xFF);
		orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
		orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
		orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
		return orderBytes;
	}

	// 还原4个字节的网络字节序
	private static int recoverNetworkBytesOrder(byte[] orderBytes) {
		int sourceNumber = 0;
		for (int i = 0; i < 4; i++) {
			sourceNumber <<= 8;
			sourceNumber |= orderBytes[i] & 0xff;
		}
		return sourceNumber;
	}

	// 随机生成16位字符串
	private static String getRandomStr() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 对密文进行解密，返回一个明文的xml字符串。目前主要用于解密微信服务器推送过来的信息。
	 * @see org#lf#wx#message#Message
	 * 
	 * @param text 需要解密的密文
	 * @return 解密得到的明文
	 * @throws AESException aes解密失败
	 */
	public static String decrypt(String text) throws AESException {
		byte[] original;
		String appId = WXProperties.APP_ID;
		
		try {
			
			byte[] aesKey = Base64.decodeBase64(WXProperties.WX_AES_KEY);
			
			// 设置解密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
			cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

			// 使用BASE64对密文进行解码
			byte[] encrypted = Base64.decodeBase64(text);

			// 解密
			original = cipher.doFinal(encrypted);
		} catch (Exception e) {
			throw new AESException(AESException.DecryptAESError);
		}

		String xmlContent, from_appid;
		try {
			// 去除补位字符
			byte[] bytes = PKCS7Encoder.decode(original);

			// 分离16位随机字符串,网络字节序和AppId
			byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

			int xmlLength = recoverNetworkBytesOrder(networkOrder);

			xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), CHARSET);
			from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), CHARSET);
		} catch (Exception e) {
			throw new AESException(AESException.IllegalBuffer);
		}

		// appid不相同的情况
		if (!from_appid.equals(appId)) {
			throw new AESException(AESException.ValidateAppidError);
		}
		
		return xmlContent;

	}
	
	/**
	 * 用SHA1算法生成安全签名
	 * @param token 票据
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @param encrypt 密文
	 * @return 安全签名
	 * @throws AesException 
	 */
	private static String sha1(String token, String timestamp, String nonce, String encrypt) throws AESException {
		try {
			String[] array = new String[] { token, timestamp, nonce, encrypt };
			StringBuffer sb = new StringBuffer();
			// 字符串排序
			Arrays.sort(array);
			for (int i = 0; i < 4; i++) {
				sb.append(array[i]);
			}
			String str = sb.toString();
			// SHA1签名生成
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			
			// 转16进制方式输出
			byte[] digest = md.digest();
			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			throw new AESException(AESException.ComputeSignatureError);
		}
	}
	
	/**
	 * 将公众平台被动回复的消息进行加密打包.
	 * @see org#lf#wx#message#SendMessage
	 * 
	 * <ol>
	 * 	<li>对要发送的消息进行AES-CBC加密</li>
	 * 	<li>生成安全签名</li>
	 * 	<li>将消息密文和安全签名打包成xml格式</li>
	 * </ol>
	 * 
	 * @param text 需要加密的字符串
	 * 
	 * @return 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce, encrypt的xml格式的字符串
	 * 加密后的内容如下
<xml>
    <Encrypt><![CDATA[pE6gp6qvVBMHwCXwnM7illFBrh9LmvlKFlPUDuyQo9EKNunqbUFMd2KjiYoz+3K1B+93JbMWHt+19TI8awdRdyopRS4oUNg5M2jwpwXTmc6TtafkKNjvqlvPXIWmutw0tuMXke1hDgsqz0SC8h/QjNLxECuwnczrfCMJlt+APHnX2yMMaq/aYUNcndOH387loQvl2suCGucXpglnbxf7frTCz9NQVgKiYrvKOhk6KFiVMnzuxy6WWmoe3GBiUCPTtYf5b1CxzN2IHViEBm28ilV9wWdNOM9TPG7BSSAcpgY4pcwdIG5+4KhgYmnVU3bc/ZJkk42TIdidigOfFpJwET4UWVrLB/ldUud4aPexp3aPCR3Fe53S2HHcl3tTxh4iRvDftUKP3svYPctt1MlYuYv/BZ4JyzUQV03H+0XrVyDY2tyVjimgCrA2c1mZMgHttOHTQ6VTnxrMq0GWlRlH0KPQKqtjUpNQzuOH4upQ8boPsEtuY3wDA2RaXQPJrXon]]></Encrypt>
    <MsgSignature><![CDATA[6c46904dc1f58b2ddf2dd0399f1c6cf41f33ecb9]]></MsgSignature>
    <TimeStamp>1414243733</TimeStamp>
    <Nonce><![CDATA[1792106704]]></Nonce>
</xml> 
	 * @throws AESException 执行失败，请查看该异常的错误码和具体的错误信息
	 */
	public static String encrypt(String text) throws AESException {
		// 使用AES-CBC进行加密
		String encrypt = "";
		ByteGroup byteCollector = new ByteGroup();
		byte[] randomStrBytes = getRandomStr().getBytes(CHARSET);
		byte[] textBytes = text.getBytes(CHARSET);
		byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
		byte[] appidBytes = WXProperties.APP_ID.getBytes(CHARSET);
		byte[] aesKey = Base64.decodeBase64(WXProperties.WX_AES_KEY);

		// randomStr + networkBytesOrder + text + appid
		byteCollector.addBytes(randomStrBytes);
		byteCollector.addBytes(networkBytesOrder);
		byteCollector.addBytes(textBytes);
		byteCollector.addBytes(appidBytes);

		// ... + pad: 使用自定义的填充方式对明文进行补位填充
		byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
		byteCollector.addBytes(padBytes);

		// 获得最终的字节流, 未加密
		byte[] unencrypted = byteCollector.toBytes();

		try {
			// 设置加密模式为AES的CBC模式
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

			// 加密
			byte[] encrypted = cipher.doFinal(unencrypted);

			// 使用BASE64对加密后的字符串进行编码
			encrypt = new Base64().encodeToString(encrypted);
		} catch (Exception e) {
			throw new AESException(AESException.EncryptAESError);
		}
		
		// 时间戳
		String timeStamp = WXUtils.getTime();
		// 随机字符串
		String nonce = UUID.randomUUID().toString(); 
		// 生成安全签名
		String signature = sha1(WXProperties.TOKEN, timeStamp, nonce, encrypt);

		// System.out.println("发送给平台的签名是: " + signature[1].toString());
		// 生成发送的xml
		String format = "<xml>" 
		        + "<Encrypt><![CDATA[%1$s]]></Encrypt>"
				+ "<MsgSignature><![CDATA[%2$s]]></MsgSignature>"
				+ "<TimeStamp>%3$s</TimeStamp>" 
				+ "<Nonce><![CDATA[%4$s]]></Nonce>" 
				+ "</xml>";
		return String.format(format, encrypt, signature, timeStamp, nonce);
	}
	
}
