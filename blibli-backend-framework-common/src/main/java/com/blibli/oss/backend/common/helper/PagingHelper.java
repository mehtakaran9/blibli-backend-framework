package com.blibli.oss.backend.common.helper;

import com.blibli.oss.backend.common.model.request.PagingRequest;
import com.blibli.oss.backend.common.model.response.Paging;

public class PagingHelper {

  public static Paging toPaging(PagingRequest request, Integer totalPage, Integer totalItem) {
    return Paging.builder()
      .page(request.getPage())
      .totalItem(totalItem)
      .totalPage(totalPage)
      .itemPerPage(request.getItemPerPage())
      .sortBy(request.getSortBy())
      .build();
  }

}
