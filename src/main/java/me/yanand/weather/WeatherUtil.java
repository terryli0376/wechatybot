package me.yanand.weather;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WeatherUtil {
    public static final String URL = "http://d1.weather.com.cn/dingzhi/";
    public static JSONObject city;
    static {
        System.out.println("----");
        BufferedReader reader = new BufferedReader(new InputStreamReader(WeatherUtil.class.getClassLoader().getResourceAsStream("city.json")));
        String temp = "";
        StringBuilder sb = new StringBuilder();
        while (true){
            try {
                if (!StringUtils.isNotBlank(temp=reader.readLine())) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(temp);
        }
        city = JSONUtil.parseObj(sb.toString());
    }

    public static JSONObject weather(String cityName){
        String id = city.getStr(cityName);
        Map<String,String> header = new HashMap<>();
        header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        header.put("Cookie","HttpOnly");
        header.put("Referer","http://www.weather.com.cn/weather1d/"+id+".shtml");
        String data = HttpRequest.get(URL+id+".html").addHeaders(header).execute().body();
        String d = data.split(";")[0].split("=")[1];
        return JSONUtil.parseObj(d);
    }

    public static void main(String[] args) {
        WeatherUtil.weather("北京");
    }
}
