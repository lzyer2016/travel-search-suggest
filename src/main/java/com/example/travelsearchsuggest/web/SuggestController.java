package com.example.travelsearchsuggest.web;

import com.example.travelsearchsuggest.domain.SuggestItem;
import com.example.travelsearchsuggest.service.SuggestService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/suggest")
@Slf4j
public class SuggestController {

  @Autowired
  private List<SuggestService> suggestServices;

  @GetMapping("/search")
  @ResponseBody
  public Map<String, List<SuggestItem>> search(@RequestParam("keyword") String keyword, HttpServletResponse response) {
    response.setHeader("Access-Control-Allow-Origin", "*");
    try {
      Map<String, List<SuggestItem>> resultMap = new HashMap<>();
      List<CompletableFuture<Void>> futures = new ArrayList<>();
      for (SuggestService suggestService : suggestServices) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
          List<SuggestItem> suggestItems = suggestService.execute(keyword);
          resultMap.put(suggestService.getSupplier(), suggestItems);
        });
        futures.add(future);
      }
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
      return resultMap;
    } catch (Exception e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return new HashMap<>();
  }

}
