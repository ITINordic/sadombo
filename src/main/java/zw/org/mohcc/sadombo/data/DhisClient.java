package zw.org.mohcc.sadombo.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.getBasicAuthorization;

/**
 *
 * @author Charles Chigoriwa
 */
public class DhisClient {

    public static void main(String[] args) throws IOException {
        System.out.println(getCategoryComboById("Yqri7Qy4PhY"));

    }

    public static DataSet getDataSetByCode(String code) throws IOException {
        String username = "cchigoriwa";
        String password = "Test1234";

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://zim.dhis2.org/develop/api/dataSets").newBuilder();
        urlBuilder.addQueryParameter("filter", "code:eq:" + code);
        urlBuilder.addQueryParameter("fields", "id,code,periodType,categoryCombo,dataSetElements[categoryCombo,dataElement[id,name,code,categoryCombo]],organisationUnits[id,code,name]");
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
        if (response.isSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            DataSetWrapper dataSetWrapper = mapper.readValue(bodyString, DataSetWrapper.class);
            DataSet dataSet = null;
            List<DataSet> dataSets = dataSetWrapper.getDataSets();
            if (dataSets != null && !dataSets.isEmpty()) {
                dataSet = dataSets.get(0);
            }
            return dataSet;
        } else {
            throw new RuntimeException(bodyString);
        }
    }

    public static CategoryCombo getCategoryComboById(String id) throws IOException {

        String username = "cchigoriwa";
        String password = "Test1234";

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://zim.dhis2.org/develop/api/categoryCombos/" + id).newBuilder();
        urlBuilder.addQueryParameter("fields", "id,code,name,categories[id,code,name,categoryOptions[id,code,name]]");
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
        if (response.isSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(bodyString, CategoryCombo.class);

        } else {
            throw new RuntimeException(bodyString);
        }
    }

}
