package com.example.travelsearchsuggest.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.travelsearchsuggest.domain.SuggestItem;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class LYService implements SuggestService {

  @Override
  public String getSupplier() {
    return "ly";
  }

  @Override
  public List<SuggestItem> execute(String keyword) {
    String url = "https://www.ly.com/commonajax/SearchBoxAjaxHandler/AutoCompleteSearch";
    HttpRequest httpRequest = HttpRequest
        .get(url, true, "keyword", keyword, "fchannel", "", "fpage", "", "_dAjax", "callback", "selectCity", 91)
        .userAgent(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        .accept("text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*; q=0.01")
        .referer("https://www.ly.com/")
        .header("sec-ch-ua-platform", "macOS")
        .header("Sec-Fetch-Dest", "script")
        .header("Sec-Fetch-Site", "same-origin")
        .header("Pragma", "no-cache")
        .header("sec-ch-ua-mobile", "?0")
        .header("Host", "www.ly.com")
        .header("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
        .header("Cache-Control", "no-cache")
        .header("Cookie",
            "NewProvinceId=6; NCid=91; NewProvinceName=%E5%B9%BF%E4%B8%9C; NCName=%E6%B7%B1%E5%9C%B3; 17uCNRefId=RefId=14211860&SEFrom=google&SEKeyWords=; TicketSEInfo=RefId=14211860&SEFrom=google&SEKeyWords=; CNSEInfo=RefId=14211860&tcbdkeyid=&SEFrom=google&SEKeyWords=&RefUrl=https%3A%2F%2Fwww.google.com.hk%2F; qdid=35294|1|14211860|be6ca5; Hm_lvt_64941895c0a12a3bdeb5b07863a52466=1703994703; __tctmc=144323752.205791637; __tctmu=144323752.0.0; __tctmz=144323752.1703994688560.1.1.utmccn=(referral)|utmcsr=google.com.hk|utmcct=|utmcmd=referral; longKey=1703994688915992; __tctrack=0; __tctmd=144323752.737325; route=664ddc368a43cb118b8eb59f40e95e2b; Hm_lpvt_64941895c0a12a3bdeb5b07863a52466=1704114991; __tctma=144323752.1703994688915992.1703994688560.1704011071514.1704114990135.3; __tctmb=144323752.2824957202975196.1704114990135.1704114990135.1; __tccgd=144323752.0");
    String result = httpRequest.body();
    JSONObject res = JSONObject.parseObject(result);
    List<SuggestItem> output = new ArrayList<>();
    if (res.containsKey("ReturnValue")) {
      JSONObject returnValueObj = res.getJSONObject("ReturnValue");
      JSONArray objArr = returnValueObj.getJSONArray("records");
      for (Object obj : objArr) {
        JSONObject jsonObject = (JSONObject) obj;
        String name = jsonObject.getString("title");
        String pcUrl = jsonObject.getString("redirectUrl");
        String subTitle = jsonObject.getString("subTitle");
        if (StringUtils.isNotBlank(subTitle)) {
          name += "<span class='gray'>(" + subTitle + ")</span>";
        }
        SuggestItem suggestItem = SuggestItem.builder().name(name).url(pcUrl).build();
        output.add(suggestItem);
      }
    }
    return output;
  }
}
