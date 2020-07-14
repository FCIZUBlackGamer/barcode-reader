package com.google.android.gms.samples.vision.barcodereader.model.server_error;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ServiceError")
public class ServiceError {
    @Element(name = "FC")
    String FC ;

    public String getFC() {
        return FC;
    }

    public void setFC(String FC) {
        this.FC = FC;
    }

}
