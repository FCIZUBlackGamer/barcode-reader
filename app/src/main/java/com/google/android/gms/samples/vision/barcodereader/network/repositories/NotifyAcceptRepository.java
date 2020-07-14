package com.google.android.gms.samples.vision.barcodereader.network.repositories;

import android.util.Log;
import android.view.View;

import com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.request.AcceptDispatchRequestEnvlope;
import com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.response.AcceptDispatchResponseBody;
import com.google.android.gms.samples.vision.barcodereader.model.notify__dispatch_accept.response.AcceptDispatchResponseEnvelope;
import com.google.android.gms.samples.vision.barcodereader.network.retrofit_service.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyAcceptRepository {
    private final static String TAG = NotifyAcceptRepository.class.getSimpleName();

    public void notifyAccept(AcceptDispatchRequestEnvlope acceptDispatchRequestEnvlope) {

        Call<AcceptDispatchResponseEnvelope> call = RetrofitInstance.getService().notifyAccept(acceptDispatchRequestEnvlope);
        call.enqueue(new Callback<AcceptDispatchResponseEnvelope>() {
            @Override
            public void onResponse(Call<AcceptDispatchResponseEnvelope> call, Response<AcceptDispatchResponseEnvelope> response) {
                if (response.isSuccessful()) {
                    AcceptDispatchResponseEnvelope responseEnvelope = response.body();
                    Log.i(TAG, "notification id : " + responseEnvelope.body.getAcceptDispatchServiceResponse().getNotificationId());
                }
            }

            @Override
            public void onFailure(Call<AcceptDispatchResponseEnvelope> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

}
