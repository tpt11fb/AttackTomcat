package Util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class Mycert {
    //静态代码块，实例化类时候直接执行。忽略ssl证书
    static {
        try {
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier
                    (
                            (urlHostName, session) -> true
                    );
        } catch (Exception e) {
        }
    }

    private static void trustAllHttpsCertificates()
            throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = new TrustAllManager();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(
                sc.getSocketFactory());
    }

    private static class TrustAllManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] certs,
                                       String authType) {
        }

        public void checkClientTrusted(X509Certificate[] certs,
                                       String authType) {
        }
    }
}
