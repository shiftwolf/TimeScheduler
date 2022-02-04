package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

/**
 * @author Hendrik
 * @version 1.0
 *
 * Manage Class contains basic functions of communicating with server.
 *
 */

public class Manage {

    // --- insert your the target IP here
    static String url = "http://192.168.178.172";
    //static String url = "http://192.168.178.28";

    public static void main(String[] args) {
        System.out.println("Hello");
        User u1 = new User((long)1, "hendrik.weichel@gmail.com","hendrikwe", "hendrik", "1234", null);
        User u2 = new User("hendrikweicsdhel", "helrsfsdfdjelsfjr", "fjklsfdsasdflfksdfjd", "kdjsassfdffösfdlj");
        token tokenu1 = new token((long)13, "6c0c79bd-ee5b-4810-815c-9641958e485c");
        postUser(u2);
        //login(u1, tokenu1);
    }

    /**
     *
     * @param user user that has to be inserted
     *
     * postUser posts a User to the database, i.e. it creates it on the db
     *
     */

    public static void postUser(User user){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url + ":8090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpringApi jsonPlaceHolderApi = retrofit.create(SpringApi.class);

        Call<User> call = jsonPlaceHolderApi.createUser(user);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                System.out.println("created");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    /**
     *
     */
    public static void printUsers(token token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url + ":8090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpringApi jsonPlaceHolderApi = retrofit.create(SpringApi.class);

        Call<List<User>> call = jsonPlaceHolderApi.getUsers(token.getTokenString(), token.getUserID());

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                List<User> users = response.body();

                String content = "";

                for (User user : users) {
                    //content += "ID: " + user.getId() + "\n";
                    content += "User ID: " + user.getUsername() + "\n";
                    content += "Name: " + user.getName() + "\n";
                    content += "Email: " + user.getEmail() + "\n\n";
                }

                System.out.println(content);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    /**
     * @return true if worked, false if didnt work
     */
    public static token login(User user, token mytoken){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url + ":8090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpringApi jsonPlaceHolderApi = retrofit.create(SpringApi.class);

        Call<token> call = jsonPlaceHolderApi.login(user);

        call.enqueue(new Callback<token>() {
            @Override
            public void onResponse(Call<token> call, Response<token> response) {

                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                assert response.body() != null;
                System.out.println(response.body().getUserID());
                System.out.println(response.body().getTokenString());

            }

            @Override
            public void onFailure(Call<token> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        return null;
    }

    public static void deleteUser(int id, token token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url + ":8090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpringApi jsonPlaceHolderApi = retrofit.create(SpringApi.class);

        Call<Boolean> call = jsonPlaceHolderApi.deleteUser(token.getTokenString(), token.getUserID(), id);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                Boolean deleted = (Boolean)response.body();

                if(deleted){
                    System.out.println("Nutzer " + id + " gelöscht.");
                    return;
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    public static void getUserById(int id, token token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url + ":8090/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SpringApi jsonPlaceHolderApi = retrofit.create(SpringApi.class);

        Call<User> call = jsonPlaceHolderApi.getUserById(token.getTokenString(), token.getUserID(), id);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                User user = (User) response.body();

                String content = "";

                content += "ID: " + user.getId() + "\n";
                content += "User ID: " + user.getUsername() + "\n";
                content += "Name: " + user.getName() + "\n";
                content += "Email: " + user.getEmail() + "\n\n";

                System.out.println(content);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}