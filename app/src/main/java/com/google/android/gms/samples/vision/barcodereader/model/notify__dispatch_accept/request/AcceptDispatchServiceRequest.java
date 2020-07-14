package com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.request;

import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "AcceptDispatchServiceRequest")
public class AcceptDispatchServiceRequest {
    @Element(name = "DISPATCHNOTIFICATIONID")
    private String notificationId;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}
