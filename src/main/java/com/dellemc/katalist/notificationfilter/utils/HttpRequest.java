package com.dellemc.katalist.notificationfilter.utils;

import scala.Tuple2;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class HttpRequest {
    private HttpURLConnection URLConnection;
    private URL myURL;
    private Properties props;

    public HttpRequest(String URL, Properties props) {
        try {
            this.props = props;
            this.myURL = new URL(URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tuple2<String, Integer> get() {
        try {
            //Set Request Information
            URLConnection = (HttpURLConnection) myURL.openConnection();
            URLConnection.setRequestMethod("GET");
            URLConnection.setAllowUserInteraction(false);
            URLConnection.setDoOutput(false);
            URLConnection.setInstanceFollowRedirects(false);

            props.forEach((k, v) -> {
                URLConnection.setRequestProperty(k.toString(), v.toString());
            });

            //Connection
            URLConnection.connect();
            return new Tuple2<>(URLConnection.getResponseMessage(), URLConnection.getResponseCode());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Tuple2<>(ex.getMessage(), 500);
        } finally {
            if (URLConnection != null) {
                URLConnection.disconnect();
            }
        }
    }

    public Tuple2<String, Integer> post(String parameters) {
        try {
            //Set Request Information
            URLConnection = (HttpURLConnection) myURL.openConnection();
            URLConnection.setRequestMethod("POST");
            URLConnection.setAllowUserInteraction(false);
            URLConnection.setDoOutput(true);
            URLConnection.setInstanceFollowRedirects(false);

            //Write Data
            OutputStreamWriter writer = new OutputStreamWriter(URLConnection.getOutputStream());
            writer.write(parameters);
            writer.flush();
            writer.close();

            return new Tuple2<>(URLConnection.getResponseMessage(), URLConnection.getResponseCode());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Tuple2<>(ex.getMessage(), 500);
        } finally {
            if (this.URLConnection != null) {
                this.URLConnection.disconnect();
            }
        }
    }
}