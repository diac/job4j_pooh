package ru.job4j.pooh;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        final String regex = "(\\w+)\\s+/([a-zA-Z0-9]+)/([a-zA-Z0-9]+)/?([a-zA-Z0-9]*).*\\s+(\\S+)\\s*$";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            return new Req(null, null, null, null);
        }
        String httpRequestType;
        String poohMode;
        String sourceName;
        String param = "";
        httpRequestType = matcher.group(1);
        poohMode = matcher.group(2);
        sourceName = matcher.group(3);
        if ("GET".equals(httpRequestType)) {
            param = matcher.group(4) != null ? matcher.group(4) : "";
        } else if ("POST".equals(httpRequestType)) {
            param = matcher.group(5) != null ? matcher.group(5) : "";
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}