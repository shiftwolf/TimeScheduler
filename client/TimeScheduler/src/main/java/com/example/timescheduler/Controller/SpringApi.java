package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpringApi {

    @GET("users")
    Call<List<User>> getUsers(
            @Header("token") String tokenString,
            @Header("userID") Long userID
    );

    @POST("users")
    Call<User> createUser(@Body User user);

    @GET("users/{id}")
    Call<User> getUserById(
            @Header("token") String tokenString,
            @Header("userID") Long userID,
            @Path("id") int id
    );

    @DELETE("users/{id}")
    Call<Boolean> deleteUser(
            @Header("token") String tokenString,
            @Header("userID") Long userID,
            @Path("id") int id
    );

    @POST("login")
    Call<token> login(@Body User user);

}
