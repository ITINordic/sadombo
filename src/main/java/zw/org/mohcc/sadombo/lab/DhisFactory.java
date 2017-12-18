package zw.org.mohcc.sadombo.lab;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *
 * @author Charles Chigoriwa
 */
public class DhisFactory {

    public static void main(String[] args) throws IOException {
        String username = "cchigoriwa";
        String password = "Test1234";

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://zim.dhis2.org/develop/api/dataSets").newBuilder();
        urlBuilder.addQueryParameter("filter", "code:eq:ATB_005");
        urlBuilder.addQueryParameter("fields", "id,code,periodType,categoryCombo,dataSetElements[categoryCombo,dataElement[categoryCombo]]");
        urlBuilder.addQueryParameter("paging", "false");
        String url = urlBuilder.build().toString();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization", getBasicAuthorization(username, password))
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = body.string();
        System.out.println(bodyString);
        if (response.isSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            DataSetWrapper dataSetWrapper = mapper.readValue(bodyString, DataSetWrapper.class);
            dataSetWrapper.getDataSets().forEach((dataSet) -> {
                System.out.println(dataSet.toString());
            });
        }
    }

    public static String getBasicAuthorization(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

}
