package com.project.core.common.util;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5Util {

    private static final String SALT = "Attk123456";

    public static String encode(String password) {
        return encode(password, SALT);
    }

    public static String encode(String password, String salt) {
        password = password + salt;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.encode("123456", "admin"));
        DelegatingPasswordEncoder delegatingPasswordEncoder =
                (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        //String md5Password = md5.encodePassword(userid, password);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new MessageDigestPasswordEncoder("MD5"));
        String md5Password = delegatingPasswordEncoder.encode("123456");
        System.out.println(md5Password);
        String password = "123456";

        PasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder("MD5");

        String encodePassword = passwordEncoder.encode(password);

        System.out.println(encodePassword);

        System.out.println(new BCryptPasswordEncoder(4).encode("123456"));

        System.out.println("----------------------------------------------------");
        String encodingId = "bcrypt";

        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put(encodingId, new BCryptPasswordEncoder());

        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        // DelegatingPasswordEncoder的第一个参数表示新的加密方式，第二个参数表示支持兼容的加密方式
        PasswordEncoder passwordEncoder1 = new DelegatingPasswordEncoder(encodingId, encoders);
        String rawPassword = "123456";
        // 因为之前没有使用DelegatingPasswordEncoder，所以原md5加密没有前缀表示，所以需要手动加上，如果之前是用的DelegatingPasswordEncoder，会自动带上
        String md5EncodedPassword = "{MD5}{nt8XCm32M9dzeaetdtFACO0jiUMhqeayK7I4NJ+Exmw=}9469cad74c2e2cf18700b3d751d314c8";
        // 验证系统中原MD5加密方式的是否兼容
        boolean matched = passwordEncoder1.matches(rawPassword, md5EncodedPassword);
        System.out.println("是否兼容原MD5加密方式：" + matched);
        String bcryptEncodedPassword = passwordEncoder1.encode(rawPassword);
        System.out.println("采用bcrypt的加密密码：" + bcryptEncodedPassword);
    }
}
