package com.example.travelsearchsuggest.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestItem implements Serializable {

  private String name;

  private String url;

  @Override
  public String toString() {
    return "SuggestItem{" +
        "name='" + name + '\'' +
        ", url='" + url + '\'' +
        '}';
  }
}
