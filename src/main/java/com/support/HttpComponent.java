package com.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/3/28
 */
public class HttpComponent {

    private static final String PUT = "_PUT";
    private static final String GET = "_GET";
    private static final String DELETE = "_DELETE";
    private static final String POST = "_POST";

    private static final String SUCCESS = "OK";
    private static final String FAILED = "FAILED";



    private static RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setSocketTimeout(5000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .setStaleConnectionCheckEnabled(true).build();

    private static CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

    public static HttpResult rpc_post(String url,byte[] bytes) throws IOException {

        HttpResult httpResult = new HttpResult();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new ByteArrayEntity(bytes));
        CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpPost);
        if(closeableHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

            ObjectMapper mapper = new ObjectMapper();
            HashMap<String,Object> hashMap = mapper.readValue(EntityUtils.toString(closeableHttpResponse.getEntity()), HashMap.class);
            if(Boolean.valueOf(hashMap.get("success").toString())){
                httpResult.setSuccess(true);
                httpResult.setResult(hashMap.get("result").toString());
            }else{
                httpResult.setSuccess(false);
                httpResult.setMessage(hashMap.get("message").toString());
            }
        }else{
            httpResult.setSuccess(false);
            httpResult.setMessage("request failed:" + closeableHttpResponse.getStatusLine().getStatusCode());
        }
        return httpResult;
    }

    public static <T> byte[] registerKryo(T t){
        Kryo kryo = new Kryo();
        kryo.register(t.getClass(),new BeanSerializer(kryo,t.getClass()));
        Output output = new Output(1,1024);
        kryo.writeObject(output,t);
        output.close();
        return output.toBytes();
    }
}
