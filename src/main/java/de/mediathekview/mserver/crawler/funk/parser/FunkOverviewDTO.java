package de.mediathekview.mserver.crawler.funk.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import de.mediathekview.mserver.crawler.basic.CrawlerUrlDTO;

public class FunkOverviewDTO<T extends CrawlerUrlDTO> {
  private final List<T> urls;
  private Optional<Integer> nextPageId;

  public FunkOverviewDTO() {
    super();
    urls = new ArrayList<>();
    nextPageId = Optional.empty();
  }

  public boolean addAllUrls(final Collection<? extends T> aUrls) {
    return urls.addAll(aUrls);
  }

  public boolean addUrl(final T aUrl) {
    return urls.add(aUrl);
  }


  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final FunkOverviewDTO<?> other = (FunkOverviewDTO<?>) obj;
    if (nextPageId.isPresent()) {
      if (!other.nextPageId.isPresent()) {
        return false;
      }
    } else if (!nextPageId.equals(other.nextPageId)) {
      return false;
    }
    if (urls == null) {
      if (other.urls != null) {
        return false;
      }
    } else if (!urls.equals(other.urls)) {
      return false;
    }
    return true;
  }

  public Optional<Integer> getNextPageId() {
    return nextPageId;
  }

  public List<T> getUrls() {
    return urls;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (nextPageId.isPresent() ? 0 : nextPageId.hashCode());
    result = prime * result + (urls == null ? 0 : urls.hashCode());
    return result;
  }

  public void setNextPageId(final Optional<Integer> aNextPageId) {
    nextPageId = aNextPageId;
  }

}