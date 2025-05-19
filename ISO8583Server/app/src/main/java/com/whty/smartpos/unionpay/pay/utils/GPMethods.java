package com.whty.smartpos.unionpay.pay.utils;

/**
 * Created by IntelliJ IDEA.
 * User: fibiger
 * Date: 2009-05-05
 * Time: 10:18:48
 * To change this template use File | Settings | File Templates.
 */

import android.Manifest;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.whty.smartpos.unionpay.pay.model.BaseStationInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class GPMethods {

    private static final String TAG = GPMethods.class.getSimpleName();

    /**
     * ECB模式中的DES/3DES/TDES算法(数据不需要填充),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与 先用前8个字节密钥DES解密
     * 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
     *
     * @param key  加解密密钥(8字节:DES算法 16字节:3DES/TDES算法 24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
     * @param data 待加/解密数据(长度必须是8的倍数)
     * @param mode 0-加密，1-解密
     * @return 加解密后的数据 为null表示操作失败
     */
    public static String desecb(String key, String data, int mode) {
        try {

            // 判断加密还是解密
            int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE
                    : Cipher.DECRYPT_MODE;

            SecretKey keySpec = null;

            Cipher enc = null;

            // 判断密钥长度
            if (key.length() == 16) {
                // 生成安全密钥
                keySpec = new SecretKeySpec(str2bytes(key), "DES");// key

                // 生成算法
                enc = Cipher.getInstance("DES/ECB/NoPadding");
            } else if (key.length() == 32) {
                // 计算加解密密钥，即将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同,并生成安全密钥
                keySpec = new SecretKeySpec(str2bytes(key
                        + key.substring(0, 16)), "DESede");// 将key前8个字节复制到keyecb的最后8个字节

                // 生成算法
                enc = Cipher.getInstance("DESede/ECB/NoPadding");
            } else if (key.length() == 48) {
                // 生成安全密钥
                keySpec = new SecretKeySpec(str2bytes(key), "DESede");// key

                // 生成算法
                enc = Cipher.getInstance("DESede/ECB/NoPadding");
            } else {
                return null;
            }

            // 初始化
            enc.init(opmode, keySpec);

            // 返回加解密结果
            return (bytesToHexString(enc.doFinal(str2bytes(data))))
                    .toUpperCase(Locale.getDefault());// 开始计算
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 将16进制组成的字符串转换成byte数组 例如 hex2Byte("0710BE8716FB"); 将返回一个byte数组
     * b[0]=0x07;b[1]=0x10;...b[5]=0xFB;
     *
     * @param src 待转换的16进制字符串
     * @return
     */
    public static byte[] str2bytes(String src) {
        if (src == null || src.length() == 0 || src.length() % 2 != 0) {
            return null;
        }
        src = src.toUpperCase();
        int nSrcLen = src.length();
        byte byteArrayResult[] = new byte[nSrcLen / 2];
        StringBuffer strBufTemp = new StringBuffer(src);
        String strTemp;
        int i = 0;
        while (i < strBufTemp.length() - 1) {
            strTemp = src.substring(i, i + 2);
            byteArrayResult[i / 2] = (byte) Integer.parseInt(strTemp, 16);
            i += 2;
        }
        return byteArrayResult;
    }

    /**
     * 将byte数组转换成16进制组成的字符串 例如 一个byte数组 b[0]=0x07;b[1]=0x10;...b[5]=0xFB; byte2hex(b);
     * 将返回一个字符串"0710BE8716FB"
     *
     * @param bytes 待转换的byte数组
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer buff = new StringBuffer();
        int len = bytes.length;
        for (int j = 0; j < len; j++) {
            if ((bytes[j] & 0xff) < 16) {
                buff.append('0');
            }
            buff.append(Integer.toHexString(bytes[j] & 0xff));
        }
        return buff.toString();
    }

    /**
     * 将byte数组转换成16进制组成的字符串 例如 一个byte数组 b[0]=0x07;b[1]=0x10;...b[5]=0xFB; byte2hex(b);
     * 将返回一个字符串"0710BE8716FB"
     *
     * @param bytes 待转换的byte数组
     * @return
     */
    public static String bytesToHexString(byte[] bytes, int len) {
        if (bytes == null) {
            return "";
        }
        StringBuffer buff = new StringBuffer();
        for (int j = 0; j < len; j++) {
            if ((bytes[j] & 0xff) < 16) {
                buff.append('0');
            }
            buff.append(Integer.toHexString(bytes[j] & 0xff));
        }
        return buff.toString();
    }

    /**
     * 将二进制字符串转换成16进制字符串 "00110110" ==> "36"
     *
     * @param str 传入字符串长度必须是16的倍数
     * @return
     */
    public static String binaryStrToHexString(String str) {
        StringBuffer sb = new StringBuffer();
        if (str.length() % 16 != 0) {
            System.out.println("输入参数的长度不是16的整数倍");
            return null;
        } else {
            for (int i = 0; i < str.length(); i += 8) {
                String part = "";
                part = Integer.toHexString(Integer.parseInt(str.substring(i, i + 8), 2));
                while (part.length() < 2) {
                    part = '0' + part;
                }
                sb.append(part);
            }
            return sb.toString();
        }
    }

    /**
     * 将字符串转化成二进制表示的字符串 "36" ==> "00110110"
     *
     * @param str 长度必须为偶数
     * @return
     */
    public static String stringToBinaryStr(String str) {
        if (str.length() % 2 != 0) {
            System.out.println("传入的字符串长度非偶数");
            return null;
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < str.length(); i += 2) {
                String string = str.substring(i, i + 2);
                String part = Integer.toBinaryString(Integer.parseInt(string, 16));
                while (part.length() < 8) {
                    part = '0' + part;
                }
                sb.append(part);
            }


            System.out.println("setelah di konversi maka BIT yang Hidup adalah : " + sb.toString());
          //  System.out.println("setelah di RENOVASI maka BIT yang Hidup adalah : " + str2.toString());
            return sb.toString().toUpperCase();
        }
    }

    public static String reverseStringBit(String src) {
        byte[] bits = str2bytes(src);
        for (int i = 0; i < bits.length; i++) {
            bits[i] = (byte) (0xff - bits[i]);
        }
        return bytesToHexString(bits);
    }

    /**
     * 生成len个字节的十六进制随机数字符串 例如:len=8 则可能会产生 DF15F0BDFADE5FAF 这样的字符串
     *
     * @param len 待产生的字节数
     * @return String
     */
    public static String yieldHexRand(int len) {
        StringBuffer strBufHexRand = new StringBuffer();
        Random rand = new Random(47);
        int index;
        // 随机数字符
        char charArrayHexNum[] = {
                '1', '2', '3', '4', '5', '6', '7', '8', '9',
                '0', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        for (int i = 0; i < len * 2; i++) {
            index = Math.abs(rand.nextInt()) % 16;
            if (i == 0) {
                while (charArrayHexNum[index] == '0') {
                    index = Math.abs(rand.nextInt()) % 16;
                }
            }
            strBufHexRand.append(charArrayHexNum[index]);
        }
        return strBufHexRand.toString();
    }

    /**
     * 将整数转换为规定长度的字符串
     *
     * @param i   整数
     * @param len 装换字符串的长度
     * @return
     */
    public static String int2String(int i, int len) {
        String ret = String.valueOf(i);
        while (ret.length() < len) {
            ret = "0" + ret;
        }
        return ret;
    }

    /**
     * 按位异或byte数组 (两个byte数组的长度必须一样)
     *
     * @param b1
     * @param b2
     * @return
     */
    public static String doXOR(String b1, String b2) {
        if (b1.length() != b2.length()) {
            return null;
        }

        byte[] byte1 = str2bytes(b1);
        byte[] byte2 = str2bytes(b2);

        byte[] result = new byte[byte1.length];
        for (int i = 0; i < byte1.length; i++) {
            int temp = (byte1[i] ^ byte2[i]) & 0xff;
            result[i] = (byte) temp;
        }
        return bytesToHexString(result).toUpperCase(Locale.getDefault());
    }

    /**
     * 例：将000000000001转化为0.01
     *
     * @param amount
     * @return
     */
    public static String toAmount(String amount) {
        String balanceStr = "";
        int len = amount.length();
        int j = 0;
        for (int i = 0; i < len; i++) {
            if (amount.charAt(i) != '0') {
                j = i;
                break;
            }
            j = 12;
        }
        if (j <= 9) {
            balanceStr = amount.substring(j, 10) + "." + amount.substring(10);
        } else {
            balanceStr = "0." + amount.substring(10);
        }
        return balanceStr;

    }

    /**
     * 得到TLV格式长度
     *
     * @return
     */
    public static String getLengthTLV(int n) {
        n = n / 2;
        String hex = "";
        if (n < 128) {
            hex = Int2HexStr(n, 2);
        } else if (n >= 128 && n < 256) {
            hex = "81" + Int2HexStr(n, 2);
        } else if (n >= 256) {
            hex = "82" + Int2HexStr(n, 4);
        }
        return hex;
    }

    /**
     * 将整数转为16进行数后并以指定长度返回（当实际长度大于指定长度时只返回从末位开始指定长度的值）
     *
     * @param val int 待转换整数
     * @param len int 指定长度
     * @return String
     */
    public static String Int2HexStr(int val, int len) {
        String result = Integer.toHexString(val).toUpperCase(
                Locale.getDefault());
        int r_len = result.length();
        if (r_len > len) {
            return result.substring(r_len - len, r_len);
        }
        if (r_len == len) {
            return result;
        }
        StringBuffer strBuff = new StringBuffer(result);
        for (int i = 0; i < len - r_len; i++) {
            strBuff.insert(0, '0');
        }
        return strBuff.toString();
    }

    /**
     * 将字符串转化成内存中存放的16进制表示 eg. "31323334" ==> "1234"
     *
     * @param src
     * @return
     */
    public static String hexByteStringToStr(String src) {
        String tag = "hexByteStringToStr";
        if (src == null || src.length() == 0) {
            return null;
        }
        if (src.length() % 2 != 0) {
            Log.e(tag, "输入的字符串长度有误！");
            return null;
        }
        byte[] tempData = str2bytes(src);
        String finalStr = "";
        for (int i = 0; i < tempData.length; i++) {
            char tempChar = (char) tempData[i];
            finalStr += tempChar;
        }
        return finalStr;
    }

    /**
     * 按位异或byte数组 (两个byte数组的长度必须一样)
     *
     * @param b1     (byte数组1)
     * @param b2     (byte数组2)
     * @param length byte数组的长度 (b1.length = b2.length)
     * @return
     */
    public static byte[] doXOR(byte[] b1, byte[] b2, int length) {
        if (b1 == null || b2 == null || b1.length != length
                || b2.length != length) {
            return null;
        }
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            int temp = b1[i] ^ b2[i];
            result[i] = (byte) temp;
        }
        return result;
    }

    /**
     * 按位求反byte数组
     *
     * @param b
     * @return
     */
    public static String doReverse(String b) {
        byte[] byte1 = str2bytes(b);
        for (int i = 0; i < byte1.length; i++) {
            byte1[i] = (byte) (~byte1[i] & 0xff);
        }
        return bytesToHexString(byte1).toUpperCase(Locale.getDefault());
    }

    /**
     * 将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同
     *
     * @param key 待转换密钥(16个字节 由2个8字节密钥组成)
     * @return
     */
    public static String key16To24(String key) {
        // 计算加解密密钥，即将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同
        if (key.length() == 32) {
            return key + key.substring(0, 16); // 将key前8个字节复制到keyresult的最后8个字节
        } else {
            return null;
        }
    }

    /**
     * 填充05数据，如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x05到数据块的右边，直到数据块的长度是8的倍数。
     *
     * @param data 待填充05的数据
     * @return
     */
    public static String padding05(String data) {
        int padlen = 8 - (data.length() / 2) % 8;
        if (padlen != 8) {
            String padstr = "";
            for (int i = 0; i < padlen; i++)
                padstr += "05";
            data += padstr;
            return data;
        } else {
            return data;
        }
    }

    /**
     * 填充00数据，如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x00到数据块的右边，直到数据块的长度是8的倍数。
     *
     * @param data 待填充00的数据
     * @return
     */
    public static String padding00(String data) {
        int padlen = 8 - (data.length() / 2) % 8;
        if (padlen != 8) {
            String padstr = "";
            for (int i = 0; i < padlen; i++)
                padstr += "00";
            data += padstr;
            return data;
        } else {
            return data;
        }
    }

    /**
     * 左填充0数据,字节数
     *
     * @param data
     * @return
     */
    public static String paddingLeft0(String data, int len) {
        int padlen = len * 2 - data.length();
        String padstr = "";
        for (int i = 0; i < padlen; i++)
            padstr += "0";
        data = padstr + data;
        return data;
    }

    /**
     * 左填充0数据,字符数
     *
     * @param data
     * @return
     */
    public static String paddingLeft0forChar(String data, int len) {
        int padlen = len - data.length();
        String padstr = "";
        for (int i = 0; i < padlen; i++)
            padstr += "0";
        data = padstr + data;
        return data;
    }

    /**
     * 右填充0数据
     *
     * @param data
     * @return
     */
    public static String paddingRight0(String data, int len) {
        int padlen = len * 2 - data.length();
        String padstr = "";
        for (int i = 0; i < padlen; i++)
            padstr += "0";
        data = data + padstr;
        return data;
    }

    /**
     * 右填充空格
     *
     * @param data
     * @return
     */
    public static String paddingRightSpace(String data, int len) {
        int padlen = len * 2 - data.length();
        String padstr = "";
        for (int i = 0; i < padlen; i++)
            padstr += " ";
        data = data + padstr;
        return data;
    }

    /**
     * 填充20数据，如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x20到数据块的右边，直到数据块的长度是8的倍数。
     *
     * @param data 待填充20的数据
     * @return
     */
    public static String padding20(String data) {
        int padlen = 8 - (data.length() / 2) % 8;
        if (padlen != 8) {
            String padstr = "";
            for (int i = 0; i < padlen; i++)
                padstr += "20";
            data += padstr;
            return data;
        } else {
            return data;
        }
    }

    /**
     * 填充80数据，首先在数据块的右边追加一个 '80',如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x00到数据块的右边，直到数据块的长度是8的倍数。
     *
     * @param data 待填充80的数据
     * @return
     */
    public static String padding80(String data) {
        int padlen = 8 - (data.length() / 2) % 8;
        String padstr = "";
        for (int i = 0; i < padlen - 1; i++)
            padstr += "00";
        data = data + "80" + padstr;
        return data;
    }

    /**
     * 填充F数据
     *
     * @param data
     * @return
     */
    public static String paddingF(String data, int len) {
        int padlen = len - data.length();
        String padstr = "";
        for (int i = 0; i < padlen; i++)
            padstr += "F";
        data = data + padstr;
        return data;
    }

    /**
     * CBC模式中的DES/3DES/TDES算法(数据不需要填充),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与 先用前8个字节密钥DES解密
     * 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
     *
     * @param key  加解密密钥(8字节:DES算法 16字节:3DES/TDES算法 24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
     * @param data 待加/解密数据(长度必须是8的倍数)
     * @param icv  初始链值(8个字节) 一般为8字节的0x00 icv="0000000000000000"
     * @param mode 0-加密，1-解密
     * @return 加解密后的数据 为null表示操作失败
     */
    public static String descbc(String key, String data, String icv, int mode) {
        try {
            // 判断加密还是解密
            int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE
                    : Cipher.DECRYPT_MODE;

            SecretKey keySpec = null;

            Cipher enc = null;

            // 判断密钥长度
            if (key.length() == 16) {
                // 生成安全密钥
                keySpec = new SecretKeySpec(str2bytes(key), "DES");// key

                // 生成算法
                enc = Cipher.getInstance("DES/CBC/NoPadding");
            } else if (key.length() == 32) {
                // 计算加解密密钥，即将16个字节的密钥转换成24个字节的密钥，24个字节的密钥的前8个密钥和后8个密钥相同,并生成安全密钥
                keySpec = new SecretKeySpec(str2bytes(key
                        + key.substring(0, 16)), "DESede");// 将key前8个字节复制到keycbc的最后8个字节

                // 生成算法
                enc = Cipher.getInstance("DESede/CBC/NoPadding");
            } else if (key.length() == 48) {
                // 生成安全密钥
                keySpec = new SecretKeySpec(str2bytes(key), "DESede");// key

                // 生成算法
                enc = Cipher.getInstance("DESede/CBC/NoPadding");
            } else {
                System.out.println("Key length is error");
                return null;
            }

            // 生成ICV
            IvParameterSpec ivSpec = new IvParameterSpec(str2bytes(icv));// icv

            // 初始化
            enc.init(opmode, keySpec, ivSpec);

            // 返回加解密结果
            return (bytesToHexString(enc.doFinal(str2bytes(data))))
                    .toUpperCase(Locale.getDefault());// 开始计算
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * CBC模式中的DES/3DES/TDES算法(数据需要填充80),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与 先用前8个字节密钥DES解密
     * 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
     *
     * @param key  加解密密钥(8字节:DES算法 16字节:3DES/TDES算法 24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
     * @param data 待加/解密数据
     * @param icv  初始链值(8个字节) 一般为8字节的0x00 icv="0000000000000000"
     * @param mode 0-加密，1-解密
     * @return 加解密后的数据 为null表示操作失败
     */
    public static String descbcNeedPadding80(String key, String data,
                                             String icv, int mode) {
        return descbc(key, padding80(data), icv, mode);
    }

    /**
     * ECB模式中的DES/3DES/TDES算法(数据需要填充80),支持8、16、24字节的密钥 说明：3DES/TDES解密算法 等同与 先用前8个字节密钥DES解密
     * 再用中间8个字节密钥DES加密 最后用后8个字节密钥DES解密 一般前8个字节密钥和后8个字节密钥相同
     *
     * @param key  加解密密钥(8字节:DES算法 16字节:3DES/TDES算法 24个字节:3DES/TDES算法,一般前8个字节密钥和后8个字节密钥相同)
     * @param data 待加/解密数据
     * @param mode 0-加密，1-解密
     * @return 加解密后的数据 为null表示操作失败
     */
    public static String desecbNeedPadding80(String key, String data, int mode) {
        return desecb(key, padding80(data), mode);
    }

    /**
     * ECB模式中的DES算法(数据不需要填充)
     *
     * @param key  加解密密钥(8个字节)
     * @param data 输入:待加/解密数据(长度必须是8的倍数) 输出:加/解密后的数据
     * @param mode 0-加密，1-解密
     * @return
     */
    public static void des(byte[] key, byte[] data, int mode) {
        try {
            if (key.length == 8) {
                // 判断加密还是解密
                int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE
                        : Cipher.DECRYPT_MODE;

                // 生成安全密钥
                SecretKey keySpec = new SecretKeySpec(key, "DES");// key

                // 生成算法
                Cipher enc = Cipher.getInstance("DES/ECB/NoPadding");

                // 初始化
                enc.init(opmode, keySpec);

                // 加解密结果
                byte[] temp = enc.doFinal(data); // 开始计算

                System.arraycopy(temp, 0, data, 0, 8); // 将加解密结果复制一份到data
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 产生MAC算法3,即Single DES加上最终的TDES MAC,支持8、16字节的密钥 这也叫Retail
     * MAC,它是在[ISO9797-1]中定义的MAC算法3，带有输出变换3、没有截断，并且用DES代替块加密
     *
     * @param key       密钥(8字节:CBC/DES算法 16字节:先CBC/DES，再完成3DES/TDES算法)
     * @param data      要计算MAC的数据
     * @param icv       初始链向量 (8个字节) 一般为8字节的0x00 icv="0000000000000000"
     * @param padding   0：填充0x00 (PIM专用) 1：填充0x20 (SIM卡专用 必须输出8个字节) 2：填充0x80 (GP卡用)3:填充0x05（卡门户应用）
     * @param outlength 返回的MAC长度 1：4个字节 2：8个字节
     * @return
     */
    public static String generateMAC(String key, String data, String icv,
                                     int padding, int outlength) {
        try {
            if (key.length() == 32 || key.length() == 16) {
                byte[] leftKey = new byte[8];
                byte[] rightKey = new byte[8];
                System.arraycopy(str2bytes(key), 0, leftKey, 0, 8); // 将key复制一份到leftKey

                byte[] icvTemp = str2bytes(icv); // 将icv复制一份到icvTemp

                // 实际参与计算的数据
                byte[] dataTemp = null;

                if (padding == 0) {
                    dataTemp = str2bytes(padding00(data)); // 填充0x00
                } else if (padding == 1) {
                    dataTemp = str2bytes(padding20(data)); // 填充0x20
                } else if (padding == 2) {
                    dataTemp = str2bytes(padding80(data)); // 填充0x80
                } else if (padding == 3) {
                    dataTemp = str2bytes(padding05(data));
                }// 填充0x05

                int nCount = dataTemp.length / 8;
                for (int i = 0; i < nCount; i++) {
                    for (int j = 0; j < 8; j++)
                        // 初始链值与输入数据异或
                        icvTemp[j] ^= dataTemp[i * 8 + j];
                    des(leftKey, icvTemp, 0); // DES加密
                }
                if (key.length() == 32) // 如果key的长度是16个字节
                {
                    System.arraycopy(str2bytes(key), 8, rightKey, 0, 8); // 将key复制一份到rightKey
                    des(rightKey, icvTemp, 1); // DES解密
                    des(leftKey, icvTemp, 0); // DES加密
                }
                String mac = (bytesToHexString(icvTemp)).toUpperCase(Locale
                        .getDefault());
                return (outlength == 1 && padding != 1) ? mac.substring(0, 8)
                        : mac;// 返回结果
            } else {
                System.out.println("Key length is error");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 产生MAC算法1,即Full TDES MAC,支持16、24字节的密钥 这也叫完整的TDES
     * MAC,它是在[ISO9797-1]中定义的MAC算法1，带有输出变换1，没有截断，并且用TDES代替块加密。
     *
     * @param key       密钥(16字节:3DES/TDES算法 24字节:3DES/TDES算法)
     * @param data      要计算MAC的数据
     * @param icv       初始链向量 (8个字节) 一般为8字节的0x00 icv="0000000000000000"
     * @param padding   0：填充0x00 (PIM专用) 1：填充0x20 (SIM卡专用 必须输出8个字节) 2：填充0x80 (GP卡用)
     * @param outlength 返回的MAC长度 1：4个字节 2：8个字节
     * @return
     */
    public static String generateMACAlg1(String key, String data, String icv,
                                         int padding, int outlength) {
        try {
            if (key.length() == 32 || key.length() == 48) {
                byte[] icvTemp = str2bytes(icv); // 将icv复制一份到icvTemp

                // 实际参与计算的数据
                byte[] dataTemp = null;

                if (padding == 0) {
                    dataTemp = str2bytes(padding00(data)); // 填充0x00
                } else if (padding == 1) {
                    dataTemp = str2bytes(padding20(data)); // 填充0x20
                } else {
                    dataTemp = str2bytes(padding80(data)); // 填充0x80
                }

                int nCount = dataTemp.length / 8;
                for (int i = 0; i < nCount; i++) {
                    for (int j = 0; j < 8; j++)
                        // 初始链值与输入数据异或
                        icvTemp[j] ^= dataTemp[i * 8 + j];
                    String resulticv = desecb(key, bytesToHexString(icvTemp), 0); // 3DES/TDES加密
                    System.arraycopy(str2bytes(resulticv), 0, icvTemp, 0, 8); // 将icv复制一份到icvTemp
                }
                String mac = (bytesToHexString(icvTemp)).toUpperCase(Locale
                        .getDefault());
                return (outlength == 1 && padding != 1) ? mac.substring(0, 8)
                        : mac;// 返回结果
            } else {
                System.out.println("Key length is error");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 产生MAC算法2,即RIPEMD-MAC,支持8、16字节的密钥 这也叫RIPEMD-MAC(RIPEMD-MAC [RIPE 93] + EMAC (DMAC)
     * [Petrank-Rackoff 98]), 它是在[ISO9797-1]中定义的MAC算法2，带有输出变换2、没有截断，并且用DES代替块加密
     *
     * @param key       密钥(8字节:CBC/DES算法 16字节:先CBC/DES，再完成3DES/TDES算法)
     * @param data      要计算MAC的数据
     * @param icv       初始链向量 (8个字节) 一般为8字节的0x00 icv="0000000000000000"
     * @param padding   0：填充0x00 (PIM专用) 1：填充0x20 (SIM卡专用 必须输出8个字节) 2：填充0x80 (GP卡用)
     * @param outlength 返回的MAC长度 1：4个字节 2：8个字节
     * @return
     */
    public static String generateMACAlg2(String key, String data, String icv,
                                         int padding, int outlength) {
        try {
            if (key.length() == 32 || key.length() == 16) {
                byte[] leftKey = new byte[8];
                byte[] rightKey = new byte[8];
                System.arraycopy(str2bytes(key), 0, leftKey, 0, 8); // 将key复制一份到leftKey

                byte[] icvTemp = str2bytes(icv); // 将icv复制一份到icvTemp

                // 实际参与计算的数据
                byte[] dataTemp = null;

                if (padding == 0) {
                    dataTemp = str2bytes(padding00(data)); // 填充0x00
                } else if (padding == 1) {
                    dataTemp = str2bytes(padding20(data)); // 填充0x20
                } else {
                    dataTemp = str2bytes(padding80(data)); // 填充0x80
                }

                int nCount = dataTemp.length / 8;
                for (int i = 0; i < nCount; i++) {
                    for (int j = 0; j < 8; j++)
                        // 初始链值与输入数据异或
                        icvTemp[j] ^= dataTemp[i * 8 + j];
                    des(leftKey, icvTemp, 0); // DES加密
                }
                if (key.length() == 32) // 如果key的长度是16个字节
                {
                    System.arraycopy(str2bytes(key), 8, rightKey, 0, 8); // 将key复制一份到rightKey
                    des(rightKey, icvTemp, 0); // DES加密
                }
                String mac = (bytesToHexString(icvTemp)).toUpperCase(Locale
                        .getDefault());
                return (outlength == 1 && padding != 1) ? mac.substring(0, 8)
                        : mac;// 返回结果
            } else {
                System.out.println("Key length is error");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 产生MAC算法4,支持16、24字节的密钥 这也叫Mac-DES,它是在[ISO9797-1]中定义的MAC算法4，带有输出变换4，没有截断，并且用DES代替块加密。
     *
     * @param key       密钥(16字节:3DES/TDES算法 24字节:3DES/TDES算法)
     * @param data      要计算MAC的数据
     * @param icv       初始链向量 (8个字节) 一般为8字节的0x00 icv="0000000000000000"
     * @param padding   0：填充0x00 (PIM专用) 1：填充0x20 (SIM卡专用 必须输出8个字节) 2：填充0x80 (GP卡用)
     * @param outlength 返回的MAC长度 1：4个字节 2：8个字节
     * @return
     */
    public static String generateMACAlg4(String key, String data, String icv,
                                         int padding, int outlength) {
        try {
            byte[] leftKey = new byte[8];
            byte[] middleKey = new byte[8];
            byte[] rightKey = new byte[8];

            if (key.length() == 48) {
                System.arraycopy(str2bytes(key), 16, rightKey, 0, 8); // 将key的最右边8个字节复制一份到rightKey
            } else if (key.length() == 32) {
                System.arraycopy(str2bytes(key), 8, rightKey, 0, 8); // 将key的最右边8个字节复制一份到rightKey
            } else {
                System.out.println("Key length is error");
                return null;
            }

            System.arraycopy(str2bytes(key), 0, leftKey, 0, 8); // 将key的最左边8个字节复制一份到leftKey
            System.arraycopy(str2bytes(key), 8, middleKey, 0, 8); // 将key的中间8个字节复制一份到middleKey

            byte[] icvTemp = str2bytes(icv); // 将icv复制一份到icvTemp

            // 实际参与计算的数据
            byte[] dataTemp = null;

            if (padding == 0) {
                dataTemp = str2bytes(padding00(data)); // 填充0x00
            } else if (padding == 1) {
                dataTemp = str2bytes(padding20(data)); // 填充0x20
            } else {
                dataTemp = str2bytes(padding80(data)); // 填充0x80
            }

            int nCount = dataTemp.length / 8;
            for (int i = 0; i < nCount; i++) {
                for (int j = 0; j < 8; j++)
                    // 初始链值与输入数据异或
                    icvTemp[j] ^= dataTemp[i * 8 + j];
                des(leftKey, icvTemp, 0); // DES加密
                if (i == 0)
                    des(rightKey, icvTemp, 0); // DES加密
                if (i == nCount - 1)
                    des(middleKey, icvTemp, 0); // DES加密
            }
            String mac = (bytesToHexString(icvTemp)).toUpperCase(Locale
                    .getDefault());
            return (outlength == 1 && padding != 1) ? mac.substring(0, 8) : mac;// 返回结果
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 生成CRC32
     *
     * @param lOldCRC the crc32 value
     * @param ByteVal the new data byte
     * @return
     */
    public static long lGenCRC32(long lOldCRC, byte ByteVal) {
        long TabVal;
        int j;
        TabVal = ((lOldCRC) ^ ByteVal) & 0xff;
        for (j = 8; j > 0; j--) {
            if ((TabVal & 1) == 1) {
                TabVal = (TabVal >> 1) ^ 0xEDB88320L;
            } else {
                TabVal >>= 1;
            }
        }
        return TabVal ^ (((lOldCRC) >> 8) & 0x00FFFFFFL);
    }

    /**
     * SHA-1摘要
     *
     * @param data 要计算SHA-1摘要的数据
     * @return 16进制字符串形式的SHA-1消息摘要，一般为20字节 为null表示操作失败
     */
    public static String generateSHA1(String data) {
        try {
            // 使用getInstance("算法")来获得消息摘要,这里使用SHA-1的160位算法
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

            // 开始使用算法
            messageDigest.update(str2bytes(data));

            // 输出算法运算结果
            byte[] hashValue = messageDigest.digest(); // 20位字节

            return (bytesToHexString(hashValue)).toUpperCase(Locale
                    .getDefault());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * SHA-1摘要
     *
     * @param data 要计算SHA-1摘要的数据
     * @return 16进制字符串形式的SHA-1消息摘要，一般为20字节 为null表示操作失败
     */
    public static byte[] generateMD5(byte[] data) {
        try {
            // 使用getInstance("算法")来获得消息摘要,这里使用SHA-1的160位算法
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            // 开始使用算法
            messageDigest.update(data);

            // 输出算法运算结果
            byte[] hashValue = messageDigest.digest(); // 20位字节

            return hashValue;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * RSA签名
     *
     * @param N    RSA的模modulus
     * @param E    RSA公钥的指数exponent
     * @param D    RSA私钥的指数exponent
     * @param data 输入数据
     * @param mode 0-加密，1-解密
     * @param type 0-私钥加密，公钥解密 1-公钥加密，私钥解密
     * @return 签名后的数据 为null表示操作失败
     */
    public static String generateRSA(String N, String E, String D, String data,
                                     int mode, int type) {
        try {
            // 判断加密还是解密
            int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE
                    : Cipher.DECRYPT_MODE;

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            BigInteger big_N = new BigInteger(N, 16);
            Key key = null;

            if (mode != type) { // 生成公钥
                BigInteger big_E = new BigInteger(E, 16);
                KeySpec keySpec = new RSAPublicKeySpec(big_N, big_E);
                key = keyFactory.generatePublic(keySpec);
            } else { // 生成私钥
                BigInteger big_D = new BigInteger(D, 16);
                KeySpec keySpec = new RSAPrivateKeySpec(big_N, big_D);
                key = keyFactory.generatePrivate(keySpec);
            }

            // 获得一个RSA的Cipher类，使用私钥加密
            Cipher cipher = Cipher.getInstance("RSA"); // RSA/ECB/PKCS1Padding

            // 初始化
            cipher.init(opmode, key);

            // 返回加解密结果
            return (bytesToHexString(cipher.doFinal(str2bytes(data))))
                    .toUpperCase(Locale.getDefault());// 开始计算
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * RSA签名
     *
     * @param key  RSA的密钥 公钥用X.509编码；私钥用PKCS#8编码
     * @param data 输入数据
     * @param mode 0-加密，1-解密
     * @param type 0-私钥加密，公钥解密 1-公钥加密，私钥解密
     * @return 签名后的数据 为null表示操作失败
     */
    public static String generateRSA(String key, String data, int mode, int type) {
        try {
            // 判断加密还是解密
            int opmode = (mode == 0) ? Cipher.ENCRYPT_MODE
                    : Cipher.DECRYPT_MODE;

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            Key strkey = null;
            if (mode != type) { // 生成公钥
                KeySpec keySpec = new X509EncodedKeySpec(
                        GPMethods.str2bytes(key)); // X.509编码
                strkey = keyFactory.generatePublic(keySpec);
            } else { // 生成私钥
                KeySpec keySpec = new PKCS8EncodedKeySpec(
                        GPMethods.str2bytes(key)); // PKCS#8编码
                strkey = keyFactory.generatePrivate(keySpec);
            }

            // 获得一个RSA的Cipher类，使用私钥加密
            Cipher cipher = Cipher.getInstance("RSA"); // RSA/ECB/PKCS1Padding

            // 初始化
            cipher.init(opmode, strkey);

            // 返回加解密结果
            return (bytesToHexString(cipher.doFinal(str2bytes(data))))
                    .toUpperCase(Locale.getDefault());// 开始计算
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 生成带SHA-1摘要的RSA签名
     *
     * @param N    RSA的模modulus
     * @param D    RSA私钥的指数exponent
     * @param data 输入数据
     * @return 签名后的数据 为null表示操作失败
     */
    public static String generateSHA1withRSA(String N, String D, String data) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            BigInteger big_N = new BigInteger(N);
            BigInteger big_D = new BigInteger(D);
            KeySpec keySpec = new RSAPrivateKeySpec(big_N, big_D);
            PrivateKey key = keyFactory.generatePrivate(keySpec);

            // 使用私钥签名
            Signature sig = Signature.getInstance("SHA1WithRSA"); // SHA1WithRSA
            sig.initSign(key);
            sig.update(str2bytes(data));

            // 返回加密结果
            return (bytesToHexString(sig.sign())).toUpperCase(Locale
                    .getDefault());// 开始计算
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 验证带SHA-1摘要的RSA签名
     *
     * @param N         RSA的模modulus
     * @param E         RSA公钥的指数exponent
     * @param plaindata 原始数据
     * @param signdata  签名数据
     * @return 验证结果 true 验证成功 false 验证失败
     */
    public static boolean verifySHA1withRSA(String N, String E,
                                            String plaindata, String signdata) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            BigInteger big_N = new BigInteger(N);
            BigInteger big_E = new BigInteger(E);
            KeySpec keySpec = new RSAPublicKeySpec(big_N, big_E);
            PublicKey key = keyFactory.generatePublic(keySpec);

            // 使用公钥验证
            Signature sig = Signature.getInstance("SHA1WithRSA"); // SHA1WithRSA
            sig.initVerify(key);
            sig.update(str2bytes(plaindata));

            // 返回验证结果
            return sig.verify(str2bytes(signdata));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static String hexByteToString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            char ch = (char) bytes[i];
            sb.append(ch);
        }
        if (sb.length() != 0)
            return sb.toString();
        return null;
    }

    /*
     * 16进制数字字符集
     */
    private static String hexString = "0123456789ABCDEF";

    /*
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     */
    public static String str2HexStr(String str) {
        // 根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        // 将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }

    /*
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String hexStr2Str(String bytes) {
        if (bytes == null || bytes.length() % 2 != 0) {
            System.out.println("hexStr2Str 参数错误！！！");
            return "";
        }
        bytes = bytes.toUpperCase();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                    .indexOf(bytes.charAt(i + 1))));
        return new String(baos.toByteArray());
    }

    public static String padding0_32(String data) {
        int padlen = 32 - (data.length()) % 32;
        if (padlen != 32) {
            String padstr = "";
            for (int i = 0; i < padlen; i++)
                padstr += "0";
            data += padstr;
            return data;
        } else {
            return data;
        }
    }

    /**
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
     *
     * @param strSrc  要加密的字符串
     * @param encName 加密类型
     * @return
     */
    public static String Encrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = str2bytes(strSrc);
        try {
            if (encName == null || encName.equals("")) {
                encName = "SHA-256";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytesToHexString(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String parse2GBK(String s) {
        if (s.equals("") || s == null) {
            return "";
        }

        String s1 = "";
        try {
            s1 = new String(GPMethods.str2bytes(s), "gbk");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return s1;
    }

    /**
     * 将金额“000000000100”转成“1.00”的形式
     *
     * @param s
     * @return
     */
    public static String formatAmount(String s) {
        DecimalFormat df = new DecimalFormat("0.00");
        double i2 = Double.parseDouble(s);
        return df.format(i2 / 100);
    }

    /**
     * 日期格式转换
     *
     * @param date       如 20160617
     * @param oldPattern 如 yyyyMMdd
     * @param newPattern 如 yyyy-MM-dd
     * @return 如 2016-06-17
     */
    public static final String dateStringPattern(String date, String oldPattern,
                                                 String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);
        Date d = null;
        try {
            d = sdf1.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdf2.format(d);
    }

    // public static String read(File file) {
    // if (!file.exists() || !file.isFile()) {
    // return null;
    // }
    // FileReader fr = null;
    // BufferedReader br = null;
    // StringBuffer sb = new StringBuffer();
    // try {
    // fr = new FileReader(file);
    // br = new BufferedReader(fr);
    // String str;
    // while ((str = br.readLine()) != null) {
    // sb.append(str);
    // }
    // return sb.toString();
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // br.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // return null;
    // }

    public static String read(File file) {
        String data = null;
        if (!file.isFile() || !file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int lenght = fis.available();
            byte[] buffer = new byte[lenght];
            fis.read(buffer);
            data = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {

        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    // public static boolean write(File file, Object content, boolean append) {
    // FileWriter fw = null;
    // BufferedWriter bw = null;
    // try {
    // if (!file.exists()) {
    // file.createNewFile();
    // }
    // file.setReadable(true, false);
    // fw = new FileWriter(file, append);
    // bw = new BufferedWriter(fw);
    // if (content instanceof String) {
    // bw.write((String) content);
    // }
    // if (content instanceof byte[]) {
    // bw.write(GPMethods.bytesToHexString((byte[]) content));
    // }
    // bw.flush();
    // return true;
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // bw.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // return false;
    // }

    public static boolean write(File file, String params, boolean append) {
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            file.setReadable(true, false);
            fos = new FileOutputStream(file, append);
            byte[] data = params.getBytes();
            fos.write(data);
            fos.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static boolean write(File file, byte[] content) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            file.setReadable(true, false);
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(new String(content));
            bw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取系统属性
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, "unknown"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    /**
     * 设置系统属性
     *
     * @param key
     * @param value
     */
    public static void setProperty(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue) {
        float scale =
                context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dp(Context context, float pxValue) {
        float scale =
                context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /*
     * 杀死扫码进程
     */
    public static void killTopActivity(Context context) {
        ActivityManager manager = (ActivityManager)
                context.getSystemService(
                        context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null) {
            RunningTaskInfo rti = runningTaskInfos.get(0);
            ComponentName cn = rti.topActivity;
            if (!"com.whty.sycodescan".equals(cn.getPackageName())
                    && !"com.google.zxing.client.android".equals(cn.getPackageName())) {
                return;
            }
            try {
                Method method = Class.forName("android.app.ActivityManager").getMethod(
                        "forceStopPackage", String.class);
                method.invoke(manager, cn.getPackageName());
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                Throwable t = e.getTargetException();// 获取目标异常
                t.printStackTrace();
            }
        }
    }

    //获取基站信息
    public static List<BaseStationInfo> getTowerInfo(Context context) {
        int mcc = -1;
        int mnc = -1;
        int lac = -1;
        int cellId = -1;
        int rssi = 1;
        TelephonyManager tm =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(operator))
            mcc = Integer.parseInt(operator.substring(0, 3));
        List<BaseStationInfo> list = new ArrayList<BaseStationInfo>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getTowerInfo: 没有权限");
            return null;
        }
        List<CellInfo> infos = tm.getAllCellInfo();
        for (CellInfo info : infos) {
            if (info instanceof CellInfoCdma) {
                CellInfoCdma cellInfoCdma = (CellInfoCdma) info;
                CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
                mnc = cellIdentityCdma.getSystemId();
                lac = cellIdentityCdma.getNetworkId();
                cellId = cellIdentityCdma.getBasestationId();
                CellSignalStrengthCdma cellSignalStrengthCdma =
                        cellInfoCdma.getCellSignalStrength();
                rssi = (cellSignalStrengthCdma.getCdmaDbm() + 113) / 2;
            } else if (info instanceof CellInfoGsm) {
                CellInfoGsm cellInfoGsm = (CellInfoGsm) info;
                CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                mnc = cellIdentityGsm.getMnc();
                lac = cellIdentityGsm.getLac();
                cellId = cellIdentityGsm.getCid();
                CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();
                rssi = (cellSignalStrengthGsm.getDbm() + 113) / 2;
            } else if (info instanceof CellInfoLte) {
                CellInfoLte cellInfoLte = (CellInfoLte) info;
                CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                mnc = cellIdentityLte.getMnc();
                lac = cellIdentityLte.getTac();
                cellId = cellIdentityLte.getCi();
                CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                rssi = (cellSignalStrengthLte.getDbm() + 113) / 2;
            } else if (info instanceof CellInfoWcdma) {
                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) info;
                CellIdentityWcdma cellIdentityWcdma = null;
                CellSignalStrengthWcdma cellSignalStrengthWcdma = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                    mnc = cellIdentityWcdma.getMnc();
                    lac = cellIdentityWcdma.getLac();
                    cellId = cellIdentityWcdma.getCid();
                    cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                    rssi = (cellSignalStrengthWcdma.getDbm() + 113) / 2;
                }
            } else {
                Log.e("GPMethods", "get CellInfo error");
                return null;
            }
            BaseStationInfo baseStationInfo = new BaseStationInfo();
            baseStationInfo.setMcc(mcc);
            baseStationInfo.setMnc(mnc);
            baseStationInfo.setLac(lac);
            baseStationInfo.setCellId(cellId);
            if (rssi < 0) {
                rssi = 1;
            }
            baseStationInfo.setRssi(rssi);
//            String tower = String.valueOf(mcc) + "|" + String.valueOf(mnc) + "|" + String
//            .valueOf(lac)
//                    + "|" + String.valueOf(cellId) + "|" + String.valueOf(rssi);
            list.add(baseStationInfo);
        }
        if (list.size() > 3) {
            list = list.subList(0, 3);
        }
        return list;
    }

    /**
     * 读取系统文件的值
     *
     * @param path
     * @return
     */
    public static String readSysFile(String path) {
        String str = null;
        try {
            FileReader localFileReader = new FileReader(path);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str = localBufferedReader.readLine();//获取到的文件中的值

            localBufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 将分为单位的转换为元
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) {
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

}
