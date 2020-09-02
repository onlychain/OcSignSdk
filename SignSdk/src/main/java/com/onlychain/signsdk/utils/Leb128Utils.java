package com.onlychain.signsdk.utils;

/**
 * @James
 * 对数值进行LEB128编码和解码
 */
public final class Leb128Utils {

    private Leb128Utils() {
        // This space intentionally left blank.
    }


    public static int unsignedLeb128Size(long value) {
        // TODO: This could be much cleverer.

        long remaining = value >>> 7;
        int count = 0;

        while (remaining != 0) {
            value = remaining;
            remaining >>>= 7;
            count++;
        }
        return count + 1;
    }


    public static int signedLeb128Size(int value) {
        int remaining = value >> 7;
        int count = 0;
        boolean hasMore = true;
        int end = ((value & Integer.MIN_VALUE) == 0) ? 0 : -1;

        while (hasMore) {
            hasMore = (remaining != end)
                    || ((remaining & 1) != ((value >> 6) & 1));

            value = remaining;
            remaining >>= 7;
            count++;
        }

        return count;
    }



    /**
     * 根据长整型获取无符号LEB128编码
     * @param value
     * @return
     */
    public static String encodeUleb128(long value) {
        byte[] buffer = new byte[unsignedLeb128Size(value)];
        int bufferIndex=0;
        long remaining = value >>> 7;
        while (remaining != 0) {
            buffer[bufferIndex] = (byte)((value & 0x7f) | 0x80);
            bufferIndex++;
            value = remaining;
            remaining >>>= 7;
        }
        buffer[bufferIndex] = (byte)(value & 0x7f);
        return OcMath.toHexStringNoPrefix(buffer);
    }

    /**
     * 根据字符串获取无符号LEB128编码
     * 输入值不超过long的最大值
     * @param value
     * @return
     */
    public static String encodeUleb128(String value) {
        return encodeUleb128(Long.valueOf(value));
    }


    /**
     *解码LEB128 支持64位
     * @param input
     * @return
     */
    public static long decodeUnsigned(byte[] input) {
        long result = 0;
        long shift = 0;
        int i = 0;
        while (true) {
            long byteTemp= Long.valueOf(input[i++] & 0xfF);
            result |= Long.valueOf((byteTemp & 0x7F) << shift);
            if ((byteTemp & 0x80) == 0)
                break;
            shift += 7;
        }
        return result;
    }

    /**
     * 校验是否到达LEB最末尾数
     * @param hex
     * @return true:已经到达leb末尾
     * @throws Exception
     */
    public static boolean isLebEnd(String hex)  {
        if (hex.length()==2){
            byte[] data= OcMath.hexStringToByteArray(hex);
            int resultSig=((data[0]& 0xfF)& 0x80);
            if(resultSig!=0)
                return false;
            else
                return true;
        }
        return true;
    }


    /**
     * 解码LEB128 支持64位
     * @param input
     * @return
     */
    public static long decodeUnsigned(String input) {
        return decodeUnsigned(OcMath.hexStringToByteArray(input));
    }
}