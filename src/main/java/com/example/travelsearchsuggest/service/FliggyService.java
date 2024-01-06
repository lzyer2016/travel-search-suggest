package com.example.travelsearchsuggest.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.travelsearchsuggest.domain.SuggestItem;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FliggyService implements SuggestService {

  @Override
  public String getSupplier() {
    return "fliggy";
  }

  @Override
  public List<SuggestItem> execute(String keyword) {
    String url = "https://travelsearch.fliggy.com/async/queryItemSuggest.do";
    HttpRequest httpRequest = HttpRequest.get(url, true, "keyword", keyword)
        .userAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        .accept("text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01")
        .referer("https://travelsearch.fliggy.com/")
        .header("sec-ch-ua-platform", "macOS")
        .header("accept-language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
        .header("authority", "travelsearch.fliggy.com");
    String result = httpRequest.body();
    JSONObject res = JSONObject.parseObject(result);
    List<SuggestItem> output = new ArrayList<>();
    if (res.containsKey("data")) {
      JSONArray objArr = res.getJSONArray("data");
      for (Object obj : objArr) {
        JSONObject jsonObject = (JSONObject) obj;
        String name = jsonObject.getString("type_text");
        String pcUrl = jsonObject.getString("pc_url");
        SuggestItem suggestItem = SuggestItem.builder().name(name).url(pcUrl).build();
        output.add(suggestItem);
      }
    }
    return output;
  }
}
