package com.example.travelsearchsuggest.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.travelsearchsuggest.domain.SuggestItem;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MaFengWoService implements SuggestService {

  @Override
  public String getSupplier() {
    return "mafengwo";
  }

  @Override
  public List<SuggestItem> execute(String keyword) {
    String url = "https://www.mafengwo.cn/search/ss.php";
    HttpRequest httpRequest = HttpRequest.get(url, true, "key", keyword)
        .userAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        .accept("text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01")
        .referer("https://www.mafengwo.cn/")
        .header("sec-ch-ua-platform", "macOS");

    String result = StringEscapeUtils.unescapeJava(httpRequest.body());
    JSONObject res = JSONObject.parseObject(result);
    List<SuggestItem> output = new ArrayList<>();
    if (res.containsKey("mdd_info")) {
      JSONObject mddObj = res.getJSONObject("mdd_info");
      if (mddObj.containsKey("result")) {
        JSONArray resultArrObj = mddObj.getJSONArray("result");
        for (Object obj : resultArrObj) {
          JSONObject r1 = (JSONObject) obj;
          String name = r1.getString("name");
          String info = r1.getString("infoname");
          String parent = r1.getString("parent");
          String deepLink = r1.getString("url");
          name += "-" + info;
          if (StringUtils.isNotBlank(parent)) {
            name += "<span class='gray'>(" + parent + ")</span>";
          }

          SuggestItem suggestItem = SuggestItem.builder().name(name).url(deepLink).build();
          output.add(suggestItem);
        }
      }
    }

    if (res.containsKey("hotel_info")) {
      JSONObject hotelInfo = res.getJSONObject("hotel_info");
      if (hotelInfo.containsKey("result")) {
        JSONArray resultArrObj = hotelInfo.getJSONArray("result");
        for (Object obj : resultArrObj) {
          JSONObject r1 = (JSONObject) obj;
          String name = r1.getString("title");
          String typename = r1.getString("typename");
          String deepLink = r1.getString("url");
          if (StringUtils.isNotBlank(typename)) {
            name += "<span class='gray'>(" + typename + ")</span>";
          }
          SuggestItem suggestItem = SuggestItem.builder().name(name).url(deepLink).build();
          output.add(suggestItem);
        }
      }
    }
    if (res.containsKey("route_info")) {
      JSONObject routeInfo = res.getJSONObject("route_info");
      if (routeInfo.containsKey("result")) {
        JSONArray resultArrObj = routeInfo.getJSONArray("result");
        for (Object obj : resultArrObj) {
          JSONObject r1 = (JSONObject) obj;
          String name = r1.getString("title");
          String typename = r1.getString("typename");
          String deepLink = r1.getString("url");
          if (StringUtils.isNotBlank(typename)) {
            name += "<span class='gray'>(" + typename + ")</span>";
          }
          SuggestItem suggestItem = SuggestItem.builder().name(name).url(deepLink).build();
          output.add(suggestItem);
        }
      }
    }
    if (res.containsKey("poi_info")) {
      JSONObject poiInfo = res.getJSONObject("poi_info");
      if (poiInfo.containsKey("result")) {
        JSONArray resultArrObj = poiInfo.getJSONArray("result");
        for (Object obj : resultArrObj) {
          JSONObject r1 = (JSONObject) obj;
          String name = r1.getString("name");
          String typename = r1.getString("typename");
          String deepLink = r1.getString("url");
          if (StringUtils.isNotBlank(typename)) {
            name += "<span class='gray'>(" + typename + ")</span>";
          }
          SuggestItem suggestItem = SuggestItem.builder().name(name).url(deepLink).build();
          output.add(suggestItem);
        }
      }
    }
    return output;
  }
}
