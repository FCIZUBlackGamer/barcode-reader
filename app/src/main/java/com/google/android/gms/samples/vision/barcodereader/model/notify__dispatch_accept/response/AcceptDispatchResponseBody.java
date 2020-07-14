package com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.response;

import com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.request.AcceptDispatchServiceRequest;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "soapenv:Body", strict = false)
public class AcceptDispatchResponseBody {
    @Element(name = "AcceptDispatchServiceResponse")
    private AcceptDispatchServiceResponse acceptDispatchServiceResponse;

    public AcceptDispatchServiceResponse getAcceptDispatchServiceResponse() {
        return acceptDispatchServiceResponse;
    }

    public void setAcceptDispatchServiceResponse(AcceptDispatchServiceResponse acceptDispatchServiceResponse) {
        this.acceptDispatchServiceResponse = acceptDispatchServiceResponse;
    }
}
