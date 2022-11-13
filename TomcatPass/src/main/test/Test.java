public class Test {
    public static void main(String[] args) {
        String url1 = "http://122.3.1.45";
        String url2 = "http://122.3.1.45/321321";
        String url3 = "https://122.3.1.45/";
        Test t = new Test();
        t.testUrl(url1);
        t.testUrl(url2);
        t.testUrl(url3);
    }
    public void testUrl(String url){
//        System.out.println(url.substring(url.length()-1));
        if (!url.endsWith("/") && !url.substring(8).contains("/")){
            System.out.println(url);
            return;
        }
        System.out.println(url.substring(0,url.indexOf("/",8)));
    }
}