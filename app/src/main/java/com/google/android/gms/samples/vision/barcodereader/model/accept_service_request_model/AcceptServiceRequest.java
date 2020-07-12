package com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "AcceptServiceRequest")
public class AcceptServiceRequest {
    @Element(name = "PRODUCTLIST")
    private List<Product> productList;

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}

