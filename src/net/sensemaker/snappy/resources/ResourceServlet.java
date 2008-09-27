package net.sensemaker.snappy.resources;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.io.InputStream;

import java.util.logging.Logger;

/**
 * Copyright 2008 Sensemaker Software Inc.
 * User: rob
 * Date: Jul 20, 2008
 * Time: 12:09:11 PM
 */
public class ResourceServlet extends HttpServlet {


    /**
	 * 
	 */
	private static final long serialVersionUID = 7441466065746824386L;
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(ResourceServlet.class.getName());
	
	private static final String PATH = "/snappyresources/";
    public static String getResourcePath(Object o /*Don't bother with servlet api in other layers */){
        return ((HttpServletRequest)o).getContextPath() + PATH;
    }


    public void service(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        this.init();
        String uri = request.getRequestURI();
        int lastSlash = uri.lastIndexOf(PATH);
        lastSlash += PATH.length();
        String resource = uri.substring(lastSlash);
        String path =  "net/sensemaker/snappy/resources/" + resource;
        System.err.println("[" + path + "]");
        InputStream stream =
                   this.getClass().getClassLoader().getResourceAsStream(path);
               response.setContentType("text");

        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = stream.read(buf)) != -1) {
            response.getOutputStream().write(buf, 0, i);
        }
        response.flushBuffer();
    }
}

