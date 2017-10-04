package bd.com.zerogravity.disablessl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//=============================================================

Disable_Certificate_Validation_Java_SSL_Connections();
//============================================================
        HashMap data = new HashMap();
        data.put("page", "2");
        data.put("page_size", "5");
        String url = "https://www.nakov.com:2083/";
        postData(url, data);
    }

    public void Disable_Certificate_Validation_Java_SSL_Connections() {

// Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            Log.d("test_disable", "OK========");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    public void postData(String url, HashMap data) {
        RequestQueue requstQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test_disable_sess", response.toString());
                        Toast.makeText(MainActivity.this, "This is my " + response.toString(),
                                Toast.LENGTH_LONG).show();

//                        if(mResultCallback != null){
//                            mResultCallback.notifySuccess(response);
//                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_disable_err", error.toString());
                        Toast.makeText(MainActivity.this, "This is my " + error.toString(),
                                Toast.LENGTH_LONG).show();

//                        if(mResultCallback != null){
//                            mResultCallback.notifyError(error);
//                        }
                    }
                }
        ) {
            //here I want to post data to sever
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonobj.setRetryPolicy(policy);
        requstQueue.add(jsonobj);

    }
}
