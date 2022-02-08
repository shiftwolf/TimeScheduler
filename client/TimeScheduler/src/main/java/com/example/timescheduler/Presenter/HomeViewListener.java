package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.User;

import java.io.IOException;
import java.util.List;

public interface HomeViewListener {

    List<User> getUsers(token token) throws IOException, InterruptedException;

}
