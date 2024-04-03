package com.example.rxjavademo.network

import com.example.rxjavademo.consts.DATA
import com.example.rxjavademo.models.DataModel
import io.reactivex.Observable
import retrofit2.http.GET


interface ApiInterface {

    @GET(DATA)
    fun apiData(): Observable<ArrayList<DataModel>>

}