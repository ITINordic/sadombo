/* 
 * Copyright (c) 2018, ITINordic
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package zw.org.mohcc.sadombo.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import zw.org.mohcc.sadombo.EnhancedChannels;
import zw.org.mohcc.sadombo.SadomboException;
import zw.org.mohcc.sadombo.utils.ConfigUtility;
import static zw.org.mohcc.sadombo.utils.GeneralUtility.getBasicAuthorization;

/**
 *
 * @author Charles Chigoriwa
 */
public class DhisClient {

    public static void main(String[] args) throws IOException {
        EnhancedChannels channels = ConfigUtility.loadChannels(args, null);
        System.out.println(getCategoryComboById("Yqri7Qy4PhY", channels));

    }

    public static DataSet getDataSetByCode(String code, EnhancedChannels channels) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(channels.getDhisBaseUrl() + "/api/dataSets").newBuilder();
        urlBuilder.addQueryParameter("filter", "code:eq:" + code);
        urlBuilder.addQueryParameter("fields", "id,code,periodType,categoryCombo,dataSetElements[categoryCombo,dataElement[id,name,code,categoryCombo]],organisationUnits[id,code,name]");
        urlBuilder.addQueryParameter("paging", "false");
        String url = urlBuilder.build().toString();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization", getBasicAuthorization("", ""))
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
            throw new SadomboException(bodyString);
        }
    }

    public static CategoryCombo getCategoryComboById(String id, EnhancedChannels channels) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(channels.getDhisBaseUrl() + "/api/categoryCombos/" + id).newBuilder();
        urlBuilder.addQueryParameter("fields", "id,code,name,categories[id,code,name,categoryOptions[id,code,name]]");
        urlBuilder.addQueryParameter("paging", "false");
        String url = urlBuilder.build().toString();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("Authorization", getBasicAuthorization("", ""))
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = body.string();
        if (response.isSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(bodyString, CategoryCombo.class);

        } else {
            throw new SadomboException(bodyString);
        }
    }

}
