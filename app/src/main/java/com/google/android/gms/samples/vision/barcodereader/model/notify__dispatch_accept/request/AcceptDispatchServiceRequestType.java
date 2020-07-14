package com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "AcceptDispatchServiceRequestType")

public class AcceptDispatchServiceRequestType {
    @Element(name = "DISPATCHNOTIFICATIONID")
    private String notificationId;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}
