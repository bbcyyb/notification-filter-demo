package com.dellemc.katalist.notificationfilter.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

public class HttpRequest {
    private HttpURLConnection URLConnection;
    private URL myURL;
    private String url;
    private Properties props;
    private Logger logger = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public HttpRequest(String URL, Properties props) {
        try {
            this.url = URL;
            this.props = props;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public Tuple2<String, Integer> put(String json) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPut httpput = new HttpPut(this.url);
            httpput.setEntity(new StringEntity(json));
            props.forEach((k, v) -> httpput.addHeader(k.toString(), v.toString()));

            HttpResponse response = client.execute(httpput);
            String content = EntityUtils.toString(response.getEntity());
            int httpStatus = response.getStatusLine().getStatusCode();

            return new Tuple2<>(content, httpStatus);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            return new Tuple2<>(ex.getMessage(), 500);
        }
    }
}