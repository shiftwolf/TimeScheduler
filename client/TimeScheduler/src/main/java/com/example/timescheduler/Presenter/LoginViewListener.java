package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;

import java.io.IOException;

public interface LoginViewListener {

    token onLogin(String username, String password) throws IOException, InterruptedException;
}
