package com.whatsfordinner.whatsfordinner;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;



/**
 * Created by Michael on 3/26/2017.
 */

public class loginButton extends AsyncTask<Object, Object, Void> {
    public CharSequence username;
    public CharSequence password;
    public CharSequence groupName;
    public CharSequence suggestion;
    public static Socket socket;
    public String response;
    BufferedReader input;

    //TODO: save in config file
    public static String serverIP = "192.168.0.25";
    public static int serverPort = 8080;

    public loginButton(CharSequence _username, CharSequence _password, CharSequence _groupName, CharSequence _suggestion)
    {
        username = _username;
        password = _password;
        groupName = _groupName;
        suggestion = _suggestion;
    }

    protected Void doInBackground(Object... params) {
        connectSocket();
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeToSocket("addUser");
        writeToSocket(username.toString());
        writeToSocket(password.toString());

        try {
            response = input.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println("RESPONSE " + response);

        writeToSocket("userLogin");
        writeToSocket(username.toString());
        writeToSocket(password.toString());

        try {
            response = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("LOGIN RESPONSE " + response);

        writeToSocket("addGroup");
        writeToSocket(groupName.toString());

        try {
            response = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("RESPONSE " + response);

        writeToSocket("addUserToGroup");
        writeToSocket(username.toString());
        writeToSocket(groupName.toString());

        writeToSocket("addSuggestionToGroup");
        writeToSocket(suggestion.toString());
        writeToSocket(groupName.toString());
        writeToSocket(username.toString());

        writeToSocket("getSuggestionsFromGroup");
        writeToSocket(groupName.toString());

        try {
            response = input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("RESPONSE " + response);

        return null;
    }
    //TODO: Move helper functions to external file
    static void writeToSocket(String message)
    {
        try
        {
            OutputStream os = socket.getOutputStream();
            os.write(message.concat("\r\n").getBytes("UTF-8"));
            os.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    static void connectSocket()
    {
        try
        {
            socket = new Socket(serverIP, serverPort);
        }
        catch (Exception e) {
            System.out.println("CAUGHT");
            e.printStackTrace();
        }
    }
}

