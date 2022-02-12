package com.example.timescheduler.Presenter;

import com.example.timescheduler.Model.User;

public interface RegistrationViewListener {

    User createUser(String name, String email, String username, String password);
}
