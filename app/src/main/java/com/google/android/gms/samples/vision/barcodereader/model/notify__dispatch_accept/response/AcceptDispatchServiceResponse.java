package com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.response;

import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

@Root (name = "AcceptDispatchServiceResponse")
public class AcceptDispatchServiceResponse {
    @Element(name = "NOTIFICATIONID")
    String notificationId;
    @Element(name = "PRODUCTLIST", required =false  )
    private List<Product> productList;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

}
