package cn.sjj.util;

/**
 * @author 宋疆疆
 * @date 2015/4/21.
 */
public class IntegerUtil {

    /**
     * @param i
     * @param bits 只返回指定的字节数，最多为4，优先返回低位的字节
     * @return
     */
    public static byte[] intToByteArray(int i, int bits) {
        byte[] bytes = new byte[bits];
        byte[] bytes1 = intToByteArray(i);
        for (int j = 0; j < bits; j++) {
            int i1 = bytes1.length - bits + j;
            bytes[j] = bytes1[i1];
        }
        return bytes;
    }

    /**
     * int到byte[]
     *
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        return intToByteArray(i, true);
    }

    /**
     * int到byte[]
     *
     * @param i
     * @param bigEndian 是否使用大端字节序
     * @return
     */
    public static byte[] intToByteArray(int i, boolean bigEndian) {
        byte[] result = new byte[4];
        if (bigEndian) {
            //由高位到低位
            result[0] = (byte) ((i >> 24) & 0xFF);
            result[1] = (byte) ((i >> 16) & 0xFF);
            result[2] = (byte) ((i >> 8) & 0xFF);
            result[3] = (byte) (i & 0xFF);
        } else {
            //由低位到高位
            result[3] = (byte) ((i >> 24) & 0xFF);
            result[2] = (byte) ((i >> 16) & 0xFF);
            result[1] = (byte) ((i >> 8) & 0xFF);
            result[0] = (byte) (i & 0xFF);
        }
        return result;
    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        return byteArrayToInt(bytes, 4);
    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @param len
     * @return
     */
    public static int byteArrayToInt(byte[] bytes, int len) {
        return byteArrayToInt(bytes, len, true);
    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @param len
     * @param bigEndian
     * @return
     */
    public static int byteArrayToInt(byte[] bytes, int len, boolean bigEndian) {
        int value = 0;
        if (bigEndian) {
            //由高位到低位  0x04  0x01
            for (int i = len - 1; i >= 0; i--) {
                int shift = (len - 1 - i) * 8;
                value += (bytes[i] & 0x000000FF) << shift;//往高位游
            }
        } else {
            //0x01 0x00
            for (int i = 0; i < 4; i++) {
                if (i >= len) {
                    break;
                }
                int shift = i * 8;
                value += (bytes[i] & 0x000000FF) << shift;//往高位游
            }
        }
        return value;
    }
}
