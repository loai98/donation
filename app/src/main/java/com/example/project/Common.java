package com.example.project;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {


    public static  final String USER_KEY="User";
    public static  final String PASSWORD_KEY="Password";

    public static  boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null)
            {
                for (int i=0 ; i<info.length;i++)
                {
                    if(info[i].getState()==NetworkInfo.State.CONNECTED)
                        return true ;

                }
            }
        }
        return false ;

    }
}
