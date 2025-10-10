/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author CÔNG HOÀNG
 */
public class RandomPassword {
    public static String RandomPassword() {
        SecureRandom random = new SecureRandom();
        
//  Định nghĩa các nhóm ký tự
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        
        List<Character> passwordChars = new ArrayList<>();
        
//  Đảm bảo có ít nhất 1 ký tự từ mỗi nhóm (3 ký tự)
        passwordChars.add(uppercase.charAt(random.nextInt(uppercase.length())));
        passwordChars.add(lowercase.charAt(random.nextInt(lowercase.length())));
        passwordChars.add(numbers.charAt(random.nextInt(numbers.length())));
        
//  Tạo 5 ký tự còn lại từ tất cả các nhóm
        String allChars = uppercase + lowercase + numbers;
        for (int i = 0; i < 5; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }
       
//  Xáo trộn để tránh pattern cố định
        Collections.shuffle(passwordChars, random);
        
//  Chuyển List<Character> thành String
        StringBuilder password = new StringBuilder();
        for (Character c : passwordChars) {
            password.append(c);
        }   
        
        return password.toString();
    }
}
