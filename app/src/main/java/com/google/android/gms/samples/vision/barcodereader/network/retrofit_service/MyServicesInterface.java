package com.google.android.gms.samples.vision.barcodereader.network.retrofit_service;

import com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.request.AcceptDispatchRequestEnvlope;
import com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.response.AcceptDispatchResponseEnvelope;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MyServicesInterface {
    //TODO: add apis services abstract methods
    @Headers({"Content-Type: text/xml;charset=UTF-8"})
    @POST("/AcceptDispatchService/AcceptDispatchService")
    Call<AcceptDispatchResponseEnvelope> notifyAccept(@Body AcceptDispatchRequestEnvlope envelope);

}
