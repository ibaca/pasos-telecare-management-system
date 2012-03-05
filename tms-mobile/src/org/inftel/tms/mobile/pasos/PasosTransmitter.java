
package org.inftel.tms.mobile.pasos;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public interface PasosTransmitter {

    /**
     * Envia un mensaje pasos con el contenido del mensaje pasado como
     * argumento.
     */
    public void sendPasosMessage(PasosMessage message) throws URISyntaxException,
            ClientProtocolException, IOException;

    /**
     * Envia un mensaje vacio al servidor. Usado para recibir informacion
     * inicial.
     */
    public void sendEmptyMessage() throws URISyntaxException,
            IOException;
}
