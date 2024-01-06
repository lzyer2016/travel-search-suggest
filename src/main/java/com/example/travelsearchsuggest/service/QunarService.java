package com.example.travelsearchsuggest.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.travelsearchsuggest.domain.SuggestItem;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class QunarService implements SuggestService {

  @Override
  public String getSupplier() {
    return "qunar";
  }

  @Override
  public List<SuggestItem> execute(String keyword) {
    String url = "https://www.qunar.com/suggestion/pc";
    HttpRequest httpRequest = HttpRequest.post(url)
        .userAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        .accept("text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01")
        .referer("https://www.qunar.com/")
        .header("sec-ch-ua-platform", "macOS")
        .header("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
        .header("authority", "www.qunar.com")
        .header("content-type", "application/x-www-form-urlencoded")
        .form("input", keyword);
    String result = httpRequest.body();
    JSONObject res = JSONObject.parseObject(result);
    List<SuggestItem> output = new ArrayList<>();
    if (res.containsKey("data")) {
      JSONObject dataObj = res.getJSONObject("data");
      JSONArray objArr = dataObj.getJSONArray("suggestions");
      for (Object obj : objArr) {
        JSONObject jsonObject = (JSONObject) obj;
        String name = jsonObject.getString("text");
        String pcUrl = jsonObject.getString("schemeUrl");
        SuggestItem suggestItem = SuggestItem.builder().name(name).url(pcUrl).build();
        output.add(suggestItem);
      }
    }
    return output;
  }
}
