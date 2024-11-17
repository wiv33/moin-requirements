package com.moin.demomoin.adapter.out.persistence.entity;

import java.util.List;

public class MoinTransferQuoteCollection {
  private final List<MoinTransferQuote> todayQuoteList;

  public MoinTransferQuoteCollection(List<MoinTransferQuote> todayQuoteList) {
    this.todayQuoteList = todayQuoteList;
  }
}
