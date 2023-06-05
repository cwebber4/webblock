package com.cwebber.webblock.server;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

import org.apache.commons.collections4.trie.PatriciaTrie;

/**
 * 
 * Copyright 2019 Chris Webber
 *
 */
public final class WebServer
{
    private ServerSocket connectionSocket;
    private int port;
    
    public WebServer(int inPort)
    {
        port = inPort;
    }
    
    public void run() throws IOException
    {
        try
        {
            connectionSocket = new ServerSocket(port);
        }
        catch(IOException e)
        {
            System.out.println("Unable to open port " + port + "\n" + e);
            throw e;
        }
        
        while (true) 
        {
            try
            {
                Socket requestSocket = connectionSocket.accept();
                HttpRequestHandler request = new HttpRequestHandler(requestSocket);
                
                Thread thread = new Thread(request);
                thread.start();
            }
            catch (IOException e)
            {
                System.out.println("Error handling client connection: " + e);
                throw e;
            }

        }
    }
}
