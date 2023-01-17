package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import Server.Method;
import Server.Request;
import Server.RequestBuilder;
import org.junit.Test;

public class RequestBuilderTest {
    @Test
    public void testBuildRequest() throws IOException {
        String requestString = "GET /path?param1=value1&param2=value2 HTTP/1.1\r\n" +
                "Host: example.com\r\n" +
                "Content-Length: 11\r\n" +
                "\r\n" +
                "Hello World";
        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        RequestBuilder builder = new RequestBuilder();
        Request request = builder.buildRequest(reader);

        assertEquals(Method.GET, request.getMethod());
        assertEquals("/path", request.getPathname());
        assertEquals("param1=value1&param2=value2", request.getParams());
        assertEquals("example.com", request.getHeaderMap().get("Host"));
        assertEquals("11", request.getHeaderMap().get("Content-Length"));
        assertEquals("Hello World", request.getBody());
    }

    @Test
    public void testGetMethod() {
        RequestBuilder builder = new RequestBuilder();
        assertEquals(Method.GET, builder.getMethod("GET"));
        assertEquals(Method.POST, builder.getMethod("post"));
        assertEquals(Method.PUT, builder.getMethod("PUT"));
        assertEquals(Method.PATCH, builder.getMethod("patch"));
        assertEquals(Method.DELETE, builder.getMethod("delete"));

    }

    @Test
    public void testSetPathname() {
        RequestBuilder builder = new RequestBuilder();
        Request request = new Request();
        builder.setPathname(request, "/path");
        assertEquals("/path", request.getPathname());
        assertEquals(null, request.getParams());

        builder.setPathname(request, "/path?param=value");
        assertEquals("/path", request.getPathname());
        assertEquals("param=value", request.getParams());
    }

}
