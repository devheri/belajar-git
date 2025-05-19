package com.example.iso8583server.api;

import com.example.iso8583server.ResponseParse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Isoparsing {
    @GET
    Call<ResponseParse> ParseResult(@Url String url);

}
