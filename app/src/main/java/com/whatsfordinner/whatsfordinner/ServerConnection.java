package com.whatsfordinner.whatsfordinner;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Michael on 3/25/2017.
 */

public class ServerConnection extends AsyncTask<Void, Void, String>
{
    public static String response;
    public static Socket socket;
    public static String serverIP = "192.168.0.25";
    public static int serverPort = 8080;
    private static String username;
    private static String password;
    public static Activity activity;
    public static NavigationView navView;

    public ServerConnection(Activity _activity, NavigationView _navView)
    {
        activity = _activity;
        navView = _navView;
    }

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

    private static void enterPassword() {
        System.out.println("Enter password");
        password = System.console().readLine();
        writeToSocket(password);

        while (!response.equals("Authenticated")) {
            System.out.println("Password not accepted. Try again");
            password = System.console().readLine();
            writeToSocket(password);
        }
        System.out.println("Password accepted");
    }

    protected String doInBackground(Void...params)
    {
//        while (true)
//        {
            try {
                System.out.println("Enter a username");
//                username = System.console().readLine();
//                password = System.console().readLine();
                username = "username";
                password = "password";


                System.out.println("Adding user");

                connectSocket();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                writeToSocket("addUser");
                writeToSocket(username);
                writeToSocket(password);
                //socket.shutdownOutput();


                response = input.readLine();
                System.out.println(response);

                if (response.equals("Success")) {
                    System.out.println("User added successfully");
                } else {
                    System.out.println("Failed to add user");
                }

                writeToSocket("getUsers");
                response = input.readLine();
                String userList = response;
                System.out.println("Users added: " + response);


//                writeToSocket("userLogin");
//                writeToSocket("CyanBlob");
//                writeToSocket("Password");

                socket.close();
            /*connectSocket();

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writeToSocket("addUser");
            writeToSocket("CyanBlob");
            socket.shutdownOutput();

            while ((response = input.readLine()) != null)
            {
                System.out.println(response);
            }

            socket.close();*/
                return userList;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
//        }
        return null;

    }
    protected void onPostExecute(String result)
    {

        TextView text = (TextView) activity.findViewById(R.id.testID);
        text.setText(result);

//        TextView hamTitle = (TextView) activity.findViewById(R.id.hamburgerTitle);
//        hamTitle.setText(result);

        View headerView = navView.getHeaderView(0);
        TextView hamTitle = (TextView)headerView.findViewById(R.id.hamburgerTitle);
        hamTitle.setText(result );

    }
}











