package com.cennavi.modules.loginrsa.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {
    public final String CHARSET = "UTF-8";
    /**
     * 加密算法RSA
     */
    public static final String RSA_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * 初始化KeyPairGenerator对象,密钥长度
     */
    public static final int KEY_SIZE = 3072;

    private static  String PRIVATE_KEY="";

    private static  String PUBLIC_KEY="";
    static {
        Resource resource = new ClassPathResource("ppencry.enc");
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                PRIVATE_KEY = line;
            }
            if(br != null){
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Resource resource_p = new ClassPathResource("publickey.enc");
        BufferedReader br_p;
        try {
            br_p = new BufferedReader(new InputStreamReader(resource_p.getInputStream()));
            String line = "";
            while ((line = br_p.readLine()) != null) {
                PUBLIC_KEY = line;
            }
            if(br_p != null){
                br_p.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成公私钥
     *
     * @return
     */
    public static Map<String, String> createKeys() {
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }

        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(KEY_SIZE);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();

        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 签名（经过Base64编码）
     *
     * @param priKey
     * @param src
     * @return
     */
    public String sign(String priKey, String src) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKey));
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
            Signature sigEng = Signature.getInstance(SIGNATURE_ALGORITHM);
            sigEng.initSign(privateKey);
            sigEng.update(src.getBytes("UTF-8"));
            byte[] signature = sigEng.sign();
            return Base64.encodeBase64String(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证签名
     *
     * @param pubKey
     * @param sign
     * @param src
     * @return
     */
    public boolean verify(String pubKey, String sign, String src) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKey));
            KeyFactory fac = KeyFactory.getInstance("RSA");
            RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);
            Signature sigEng = Signature.getInstance(SIGNATURE_ALGORITHM);
            sigEng.initVerify(rsaPubKey);
            sigEng.update(src.getBytes("UTF-8"));
            return sigEng.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, String publicKey) {
        try {
            //通过X509编码的Key指令获得公钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //return Base64.encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), key.getModulus().bitLength()));
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes("UTF-8"), key.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }
    /**
     * 公钥加密
     *
     * @param data
     * @param
     * @return
     */
    public static String publicEncrypt(String data) {
        try {
            //通过X509编码的Key指令获得公钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(PUBLIC_KEY));
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //return Base64.encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), key.getModulus().bitLength()));
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes("UTF-8"), key.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data
     * @return
     */
    public static String privateDecrypt(String data) {
        try {
            //通过PKCS#8编码的Key指令获得私钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(PRIVATE_KEY));
            RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            //return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decode(data), key.getModulus().bitLength()), CHARSET);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), key.getModulus().bitLength()),"UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     */
    public String privateEncrypt(String data, String privateKey) {
        try {
            //通过PKCS#8编码的Key指令获得私钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //return Base64.encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), key.getModulus().bitLength()));
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes("UTF-8"), key.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data
     * @return
     */
    public static String privateEncrypt(String data) {
        try {
            //通过PKCS#8编码的Key指令获得私钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(PRIVATE_KEY));
            RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //return Base64.encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), key.getModulus().bitLength()));
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes("UTF-8"), key.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public String publicDecrypt(String data, String publicKey) {
        try {
            //通过X509编码的Key指令获得公钥对象
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            //return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decode(data), key.getModulus().bitLength()), CHARSET);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), key.getModulus().bitLength()),"UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        byte[] resultDatas = null;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
                resultDatas = out.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultDatas;
    }


    public static void main(String[] args) throws Exception {
//        Map<String, String> keys = createKeys();//生成公钥私钥
//        System.out.println(keys.get("publicKey"));
//        System.out.println(keys.get("privateKey"));
        String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7qdEg98tYYRtSkOk9AxEETZIKksEPoc9pPy0SWYP3hFJL2LwNNLH+mX+8dbCFtedH09gMw3gLDuwQePrQU59070rMmGK7nkarOYjTZF2IYdHK4hQRMlEQQIrUK6MQCZrlXbi5SNA5cmxidhPmduT2QFsrfdbmtKjPvXa/1lku8wIDAQAB";
        String privateKey="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALup0SD3y1hhG1KQ6T0DEQRNkgqSwQ+hz2k/LRJZg/eEUkvYvA00sf6Zf7x1sIW150fT2AzDeAsO7BB4+tBTn3TvSsyYYrueRqs5iNNkXYhh0criFBEyURBAitQroxAJmuVduLlI0DlybGJ2E+Z25PZAWyt91ua0qM+9dr/WWS7zAgMBAAECgYAArbuI3/yHREqiAM4nH1S3bERj72XtVYt3ePgDwfkdMwhNJ+p+LbubDRkOKGrMAP1mIBj+6tILUDGAWb+mczb3ZrN0TRzxwb4uxz3N4bgx7IFvix77Otm7d5Zd+b59DhYew5fSRZVB5ys09GpqO0+vDcvms+r6zmKCHA6USfOQ6QJBAOnGaeNzCm0w6fbdIaYNXyd5HOIHmS+q3SupOo71/7Tvhgt1kNuSkqQkRaSpWY5aruhM3qfmBs9V722nWxPy6c8CQQDNgSSGOUjA0A+nk8wukVs9MtIf4wsYSx9LEZl97hdFW5n10M5V/FEDF18dtXaFQYNQiw92JhnUyBShKKyOtUWdAkApa7p/Tnbeefg6gVvg7CWb/N2dPvNpesDNZ6K0ienQyU/a3+3WudW5t6OPVWJE0tSC3HvnC97RBczyhOCiXDwJAkAl0deD8DTobyICaBvSkiOlbp3nCmS3UtPuf82stE2KESKTb5sZjfbmx71UfVnTikv9Xao5xydH1o6dXhvro4atAkEAzy5UDqxZ6Y57N6WqwY09FHaTvpwdNZ40dROqGzfdOd+8OwBMYsGFMzX2gIILybsGLCyO1hlcT0Dd/qL/L4omug==";
        String data="123456";
//        String encrypt = publicEncrypt(data, publicKey);//公钥加密数据
        String encrypt = "R8wyGgmQjsL/uhVsOyEH82PyV6y371Rd8nK9gNY0Zm8tfqxAYPcPZsco8O+ypWl9ym9rB20DmkNuVr7FB/tyIaCYca+dwyEbcDxChoOlSnaIXjXy3kfFrbCvXCdWLyMNtlHpfRwsnj6iAP8WG15Ctn/LTyLGJuRDU95/IT/jdyM=";
//        System.out.println(encrypt);
        System.out.println("jm==="+publicEncrypt(data,publicKey));

        System.out.println(privateDecrypt(publicEncrypt(data,publicKey)));//privateDecrypt私钥解密数据
        System.out.println(privateDecrypt("TI3zSbE1Ub7LVbHHP+/YzviApltX67kHA3HCgjBLHCz7jbvEX3CU3M83zx0+J8oo8arzP2BPi2xTEgNxU5vyujYqI8goO9jI2Z/9Httws4JysnoXBTcwBRuw4bXAnpxQTPN/+GrXClycNcRDe36755JRxOCNQdRNsr6+7W7ALC4="));

        String username = "XpjGcyaS9Iyo4elTy3OXcx/dMkaCAR/zxVhkVsQv12SiMpmSJTPJyIMgr5wmqdA2Hedn4lnrQ9yH4pHNUVTvwO4In3KgLfKw46t3AG6BTSQ2P4pXI7n5SmJPdb2i90cPHoUKRQcEjoQ4FuUFXaLfp7xc4xuv6kz4x+vVniwUld0=";
        String password = "MKHjod/HSXeDeDn1sZiETS15ZJeRDGpen8mnJxYJzpUCs3QrhlNiMpenLiPO5HDAMfj7BSSYIp1owH07+hFLb7EZOfUVlx8WbGDWDpNK/8U8VcIa5VJvBBuXfAv5boQuWu7ENKFQmoJZ0q2TmPxVMGCQztGhWKyL2jw8fKk0Awo=";
//        String decodeusername = URLDecoder.decode(privateDecrypt(username,privateKey), "UTF-8");
        System.out.println("username:"+privateDecrypt(username));
        System.out.println("password:"+privateDecrypt(password));

    }
}