package com.google.android.gms.samples.vision.barcodereader.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

@Root(name = "soapenv:Envelope")
@NamespaceList({
        @Namespace(reference = "http://dtts.sfda.gov.sa/AcceptService", prefix = "tns"),
        @Namespace(reference = "http://dtts.sfda.gov.sa/AcceptDispatchService", prefix = "xsd"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/encoding/", prefix = "enc"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soapenv")
})
public class RequestEnvelope {
    @Element(name = "soapenv:Body", required = false)
    public NotifyAcceptRequestBody body;

}
