package com.cwebber.webblock.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.http.HttpRequest;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * Copyright 2019 Chris Webber
 *
 */
public class HttpRequestHandler implements Runnable
{
    private final static String CRLF = "\r\n";
    
    private Socket socket;
    
    
    public HttpRequestHandler(Socket socket) 
    {
        this.socket = socket;
    }
    
    public void run()
    {
        try 
        {
            processRequest();
        }
        catch (Exception e) 
        {
            System.out.println("Error processing request: " + e);
            e.printStackTrace();
        }
    }
    
    private void processRequest() throws IOException
    {
        InputStream is = socket.getInputStream();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        
        InputStreamReader ir = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(ir);
        
        String requestLine = br.readLine();
        
        System.out.println("REQUEST==================================================");
        System.out.println(requestLine);
        
        ArrayList<String> requestLines = new ArrayList<String>();
        requestLines.add(requestLine);
        String line = null;
        while ((line = br.readLine()) != null) 
        {
            System.out.println(line);
            requestLines.add(line);
        }
        
        System.out.println("END REQUEST==============================================");
        
        //TODO: Break this all into separate methods to handle the block and
        //allowed.
        boolean resourceAccessible = true;
        //TODO: check request URI against blocked hosts.
        if (requestLine.toLowerCase().contains("google.com"))
        {
            resourceAccessible = false;
        }
        
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if (resourceAccessible)
        {
            allowRequest(requestLines);
        } 
        else 
        {
            statusLine = "HTTP/1.1 403 Forbidden" + CRLF;
            contentTypeLine = "text/html" + CRLF;
            entityBody = "<HTML>" + 
                "<HEAD><TITLE>Forbidden</TITLE></HEAD>" +
                "<BODY>403 Forbidden</BODY></HTML>";
       
            try
            {
                os.writeBytes(statusLine);
                os.writeBytes(contentTypeLine);
                os.writeBytes(CRLF);
                
                os.writeBytes(entityBody);
            }
            catch (SocketException e)
            {
                //java.net.SocketException: Software caused connection abort: socket write error
                System.out.println("Error sending request: [" + requestLine + "]: " + e);
            }
        }
        
        try
        {
            os.close();
        }
        catch (Exception e)
        {}
        
        try
        {
            br.close();
        }
        catch (Exception e)
        {}
        
        try
        {
            socket.close();
        }
        catch (Exception e)
        {}
    }
    
    private void allowRequest(List<String> requestLines, DataOutputStream outputStream)
    {
        
    }

    private HttpRequest generateHttpRequest(List<String> requestLines)
    {
        
    }
}
