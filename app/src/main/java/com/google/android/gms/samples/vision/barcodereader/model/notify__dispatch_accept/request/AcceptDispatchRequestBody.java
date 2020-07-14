package com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "soapenv:Body", strict = false)
public class AcceptDispatchRequestBody {
    @Element(name = "AcceptDispatchServiceRequest")
    private AcceptDispatchServiceRequest acceptDispatchServiceRequest;

    public AcceptDispatchServiceRequest getAcceptDispatchServiceRequest() {
        return acceptDispatchServiceRequest;
    }

    public void setAcceptDispatchServiceRequest(AcceptDispatchServiceRequest acceptDispatchServiceRequest) {
        this.acceptDispatchServiceRequest = acceptDispatchServiceRequest;
    }
}
