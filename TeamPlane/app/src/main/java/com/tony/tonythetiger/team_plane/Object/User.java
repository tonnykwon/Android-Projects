package com.tony.tonythetiger.team_plane.Object;

import android.content.SharedPreferences;

import com.tony.tonythetiger.team_plane.ServerConnection.GetData;
import com.tony.tonythetiger.team_plane.ServerConnection.OnPostListener;
import com.tony.tonythetiger.team_plane.ServerConnection.SetData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tony on 2017-12-04.
 */

public class User {
    public String ServerURL = "http://52.78.119.132/";
    private String logInPHP = "login.php";
    public String matchingPHP = "matching.php";
    private OnPostListener onPostListener;

    public User() {
    }

    public void SignIn(HashMap<String,String> parameters, OnPostListener onPostListener){
        this.onPostListener = onPostListener;

    }

    public void LogIn(HashMap<String,String> parameters, OnPostListener onPostListener){
        this.onPostListener = onPostListener;
        SetData logIn = new SetData(onPostListener);
        logIn.setParameters(parameters);
        String[] strings = {"keyy"};
        logIn.setJsonParameter(strings);
        logIn.execute(ServerURL+logInPHP);
    }

    public void LogOut(){
        SetData logOut = new SetData(onPostListener);
        logOut.execute("Server/log_out.php");
    }

    public void DropOut(){
        SetData dropOut = new SetData(onPostListener);
        dropOut.execute("Server/drop_out.php");
    }

    public void request(){
        SetData req = new SetData(onPostListener);
        req.execute();
    }

    public void matching(HashMap<String, String> parameters, OnPostListener onPostListener){
        SetData matching = new SetData(onPostListener);
        matching.setParameters(parameters);
        String[] strings = {"keyy"};
        matching.setJsonParameter(strings);
        matching.execute("Server/login.php");
        this.onPostListener = onPostListener;
    }

}
