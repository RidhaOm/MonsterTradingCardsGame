package Server;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private Method method;
    private String pathname;
    private String params;
    private Map<String, String> headerMap = new HashMap<>();
    private String body;

    private int contentLength = 0;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }



    public String addHeader(String key, String value) {
        if (key != null && key.equals("Content-Length"))
        {
            this.contentLength = Integer.parseInt(value);
        }
        return headerMap.put(key, value);
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
