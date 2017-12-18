package zw.org.mohcc.sadombo;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 * @author Charles Chigoriwa
 */
public class OkHttpClientsample {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.google.com")
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body != null) {
            System.out.println(body.string());
        }
    }

}
