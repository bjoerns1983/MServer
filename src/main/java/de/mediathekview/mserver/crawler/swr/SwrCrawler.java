package de.mediathekview.mserver.crawler.swr;

import de.mediathekview.mlib.daten.Film;
import de.mediathekview.mlib.daten.Sender;
import de.mediathekview.mlib.messages.listener.MessageListener;
import de.mediathekview.mserver.base.config.MServerConfigManager;
import de.mediathekview.mserver.base.messages.ServerMessages;
import de.mediathekview.mserver.crawler.basic.AbstractCrawler;
import de.mediathekview.mserver.crawler.basic.CrawlerUrlDTO;
import de.mediathekview.mserver.crawler.swr.tasks.SwrDayPageTask;
import de.mediathekview.mserver.crawler.swr.tasks.SwrFilmTask;
import de.mediathekview.mserver.crawler.swr.tasks.SwrTopicTask;
import de.mediathekview.mserver.crawler.swr.tasks.SwrTopicsOverviewTask;
import de.mediathekview.mserver.progress.listeners.SenderProgressListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SwrCrawler extends AbstractCrawler {

  private static final Logger LOG = LogManager.getLogger(SwrCrawler.class);

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd");

  public SwrCrawler(
      final ForkJoinPool aForkJoinPool,
      final Collection<MessageListener> aMessageListeners,
      final Collection<SenderProgressListener> aProgressListeners,
      final MServerConfigManager rootConfig) {
    super(aForkJoinPool, aMessageListeners, aProgressListeners, rootConfig);
  }

  @Override
  public Sender getSender() {
    return Sender.SWR;
  }

  @Override
  protected RecursiveTask<Set<Film>> createCrawlerTask() {
    final ConcurrentLinkedQueue<CrawlerUrlDTO> shows = new ConcurrentLinkedQueue<>();
    try {
      shows.addAll(getTopicEntries());
      printMessage(
          ServerMessages.DEBUG_ALL_SENDUNG_FOLGEN_COUNT, getSender().getName(), shows.size());

      getDaysEntries()
          .forEach(
              show -> {
                if (!shows.contains(show)) {
                  shows.add(show);
                }
              });

      printMessage(
          ServerMessages.DEBUG_ALL_SENDUNG_FOLGEN_COUNT, getSender().getName(), shows.size());
      getAndSetMaxCount(shows.size());

      return new SwrFilmTask(this, shows, SwrConstants.URL_BASE);
    } catch (final ExecutionException | InterruptedException ex) {
      LOG.fatal("Exception in SWR crawler.", ex);
    }
    return null;
  }

  private Set<CrawlerUrlDTO> getTopicEntries() throws ExecutionException, InterruptedException {
    final ConcurrentLinkedQueue<CrawlerUrlDTO> topicsUrl = new ConcurrentLinkedQueue<>();
    topicsUrl.add(new CrawlerUrlDTO(SwrConstants.URL_TOPICS));

    final SwrTopicsOverviewTask topicsTask = new SwrTopicsOverviewTask(this, topicsUrl);

    final ConcurrentLinkedQueue<CrawlerUrlDTO> topicUrl =
        new ConcurrentLinkedQueue<>(forkJoinPool.submit(topicsTask).get());

    final SwrTopicTask topicTask = new SwrTopicTask(this, topicUrl);

    return forkJoinPool.submit(topicTask).get();
  }

  private Set<CrawlerUrlDTO> getDaysEntries() throws ExecutionException, InterruptedException {
    final ConcurrentLinkedQueue<CrawlerUrlDTO> dayPageUrls = getDayPageUrls();

    final SwrDayPageTask dayPageTask = new SwrDayPageTask(this, dayPageUrls, SwrConstants.URL_BASE);
    final Set<CrawlerUrlDTO> shows = forkJoinPool.submit(dayPageTask).get();

    printMessage(
        ServerMessages.DEBUG_ALL_SENDUNG_FOLGEN_COUNT, getSender().getName(), shows.size());

    return shows;
  }

  private ConcurrentLinkedQueue<CrawlerUrlDTO> getDayPageUrls() {
    final ConcurrentLinkedQueue<CrawlerUrlDTO> urls = new ConcurrentLinkedQueue<>();

    final LocalDateTime today = LocalDateTime.now();

    for (int i = 0;
        i <= crawlerConfig.getMaximumDaysForSendungVerpasstSection()
            && i <= SwrConstants.MAX_DAYS_PAST;
        i++) {
      final LocalDateTime day = today.minusDays(i);
      final String url = SwrConstants.URL_DAY_PAGE + day.format(DATE_TIME_FORMATTER);

      urls.add(new CrawlerUrlDTO(url));
    }

    return urls;
  }
}
