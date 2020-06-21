package com.situn.imagessubredditviewer.app.services

import com.situn.pcfapp.model.HomeResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {
    @GET("repositories")
    fun getHomeData() : Call<List<HomeResponseModel>>
}