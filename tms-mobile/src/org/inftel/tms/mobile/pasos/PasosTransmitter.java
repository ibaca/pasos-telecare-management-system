
package org.inftel.tms.mobile.pasos;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

public interface PasosTransmitter {

    public HttpResponse sendPasosMessage() throws URISyntaxException,
            ClientProtocolException, IOException;

    public HttpResponse sendEmptyMessage() throws URISyntaxException,
            IOException;
}
