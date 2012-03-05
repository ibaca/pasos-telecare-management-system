
package org.inftel.tms.mobile.pasos;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class PasosHTTPTransmitter implements PasosTransmitter {
    private String url = "http://localhost:8080/tms-web/connector";
    protected String senderNumber;
    private PasosMessage message;

    public PasosMessage getMessage() {
        return message;
    }

    public void setMessage(PasosMessage message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public PasosHTTPTransmitter() {
        super();
    }

    public PasosHTTPTransmitter(String url, String senderNumber, PasosMessage message) {
        super();
        this.url = url;
        this.senderNumber = senderNumber;
        this.message = message;
    }

    /**
     * Manda un mensaje vacío, para pedir los remote parameters.Por si lo
     * usamos.
     * 
     * @return HttpResponse
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResponse sendEmptyMessage() throws URISyntaxException,
            IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();
        post.setHeader("senderNumber", this.senderNumber);
        URI uri = new URI(this.url);
        post.setURI(uri);
        return client.execute(post);
    }

    /**
     * Send PASOS message
     * 
     * @return HttpResponse
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public HttpResponse sendPasosMessage() throws URISyntaxException,
            ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();
        post.setHeader("sender-mobile-number", this.senderNumber);
        URI uri = new URI(this.url);
        post.setURI(uri);
        post.setEntity(new StringEntity(message.toString()));
        return client.execute(post);
    }
}
