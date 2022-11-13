package Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Utils {
    //读取文件内容，返回一个set集合
    public Set<String> readfile(String filePath){
//        filePath = java.net.URLDecoder.decode(String.valueOf(this.getClass().getResource("/"+filePath)).substring(6));
        Set<String> set = new HashSet<>();
        String str;
        try {
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            while ((str = in.readLine())!=null){
                set.add(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return set;
    }

    public static void main(String[] args) {
        Utils o = new Utils();
        System.out.println(o.readfile("username.txt"));
        System.out.println(o.readfile("password.txt"));
    }
}
