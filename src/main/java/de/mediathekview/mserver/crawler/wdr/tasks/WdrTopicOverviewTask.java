package de.mediathekview.mserver.crawler.wdr.tasks;

import de.mediathekview.mserver.base.webaccess.JsoupConnection;
import de.mediathekview.mserver.crawler.basic.AbstractCrawler;
import de.mediathekview.mserver.crawler.basic.AbstractDocumentTask;
import de.mediathekview.mserver.crawler.basic.AbstractUrlTask;
import de.mediathekview.mserver.crawler.basic.TopicUrlDTO;
import de.mediathekview.mserver.crawler.wdr.WdrTopicUrlDto;
import de.mediathekview.mserver.crawler.wdr.parser.WdrTopicOverviewDeserializer;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jsoup.nodes.Document;

public class WdrTopicOverviewTask extends AbstractDocumentTask<TopicUrlDTO, WdrTopicUrlDto> {

  private final int recursiveCount;

  public WdrTopicOverviewTask(
          AbstractCrawler aCrawler,
          ConcurrentLinkedQueue<WdrTopicUrlDto> aUrlToCrawlDtos,
          final JsoupConnection jsoupConnection,
          int aRecursiveCount) {
    super(aCrawler, aUrlToCrawlDtos, jsoupConnection);

    recursiveCount = aRecursiveCount;
  }

  @Override
  protected void processDocument(WdrTopicUrlDto aUrlDto, Document aDocument) {
    if (aUrlDto.isFileUrl()) {
      taskResults.add(new TopicUrlDTO(aUrlDto.getTopic(), aUrlDto.getUrl()));
      return;
    }

    final ConcurrentLinkedQueue<WdrTopicUrlDto> subpages = new ConcurrentLinkedQueue<>();

    WdrTopicOverviewDeserializer deserializer = new WdrTopicOverviewDeserializer();
    List<WdrTopicUrlDto> dtos = deserializer.deserialize(aUrlDto.getTopic(), aDocument);
    dtos.forEach(
            dto -> {
              if (dto.isFileUrl()) {
                taskResults.add(new TopicUrlDTO(dto.getTopic(), dto.getUrl()));
              } else if (!dto.getUrl().contains("/fernsehen/")) {
                subpages.add(dto);
              }
            });

    if (crawler.getCrawlerConfig().getMaximumSubpages() > recursiveCount) {
      taskResults.addAll(createNewOwnInstance(subpages).invoke());
    }
  }

  @Override
  protected AbstractUrlTask<TopicUrlDTO, WdrTopicUrlDto> createNewOwnInstance(
          ConcurrentLinkedQueue<WdrTopicUrlDto> aUrlsToCrawl) {
    return new WdrTopicOverviewTask(crawler, aUrlsToCrawl, getJsoupConnection(),  recursiveCount + 1);
  }
}
