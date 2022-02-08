package com.example.timescheduler.Presenter;

import java.io.IOException;

public interface LoginViewListener {

    public void onLogin(String username, String password) throws IOException, InterruptedException;
}
