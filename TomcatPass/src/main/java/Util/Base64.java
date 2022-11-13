package Util;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

public class Base64 {

    public Base64(){

    }

//    public Set<String> base64_encode(Set<String> connect){
//        Set<String> s = new  HashSet<>();
//        for (String con:connect) {
//            s.add(java.util.Base64.getEncoder().encodeToString(con.getBytes()));
//        }
//        return s;
//    }

    public String base64_decode(String str){
        String res = null;
        try {
            res = new String(java.util.Base64.getDecoder().decode(str),"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public Set<String> file_base64(String usernameFileName, String passwdFileName) {
        Utils u = new Utils();
        Set<String> res = new HashSet<>();
        Set<String> s = new HashSet<>();
        for (String i:u.readfile(usernameFileName)){
            for (String j:u.readfile(passwdFileName)){
                res.add(i+":"+j);
            }
        }
        for (String content:res){
            s.add(java.util.Base64.getEncoder().encodeToString(content.getBytes()));
        }
        return s;
    }
}
