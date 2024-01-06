package com.example.travelsearchsuggest.service;

import com.example.travelsearchsuggest.domain.SuggestItem;
import java.util.List;

public interface SuggestService {

  String getSupplier();

  List<SuggestItem> execute(String keyword);

}
