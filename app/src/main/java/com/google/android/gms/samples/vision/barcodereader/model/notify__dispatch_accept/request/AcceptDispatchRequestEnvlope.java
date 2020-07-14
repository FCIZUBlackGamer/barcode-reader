package com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.request;

import com.google.android.gms.samples.vision.barcodereader.model.NotifyAcceptRequestBody;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

@Root(name = "soapenv:Envelope")
@NamespaceList({
        @Namespace(reference = "http://dtts.sfda.gov.sa/AcceptDispatchService", prefix = "tns"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/encoding/", prefix = "enc"),
        @Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soapenv")})
public class AcceptDispatchRequestEnvlope {
    @Element(name = "soapenv:Body", required = false)
    public AcceptDispatchRequestBody body;


}
