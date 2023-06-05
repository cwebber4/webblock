package com.cwebber.webblock;

import com.cwebber.webblock.server.WebServer;


/**
 * 
 * Copyright 2019 Chris Webber
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        int port = 6146;
        
        try
        {
            WebServer webServer = new WebServer(port);
            webServer.run();
        }
        catch(Exception e)
        {
            System.out.println("Error in WebServer: " + e);
        }
    }
}
