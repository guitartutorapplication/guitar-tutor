package com.example.jlo19.guitartutor.services;

import android.content.Context;

import com.example.jlo19.guitartutor.R;
import com.example.jlo19.guitartutor.services.interfaces.DatabaseApi;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creates database API using retrofit
 */
public class DatabaseService {

    private static final String BASE_URL = "https://ec2-34-251-98-53.eu-west-1.compute.amazonaws.com/";
    private static Retrofit retrofit = null;

    public static DatabaseApi getApi(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getHttpClient(context))
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(DatabaseApi.class);
    }

    private static OkHttpClient getHttpClient(Context context){
        // allows use of self signed certificate when connecting to API via HTTPS
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            // get and generate self signed certificate
            InputStream caInput = context.getResources().openRawResource(R.raw.certificate);
            Certificate ca = certificateFactory.generateCertificate(caInput);

            // create keystore containing trusted certificate
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // TrustManager that trusts the certificate in key store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());
            tmf.init(keyStore);

            // creates SSLContext that uses TrustManager
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, tmf.getTrustManagers(), null);

            // creates client that uses the SSLContext
            final OkHttpClient client;
            client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslcontext.getSocketFactory(), (X509TrustManager)
                            tmf.getTrustManagers()[0])
                    .build();
            caInput.close();

            return client;
        } catch (Exception e) {
            return null;
        }
    }
}
