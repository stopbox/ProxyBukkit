package me.stopbox123.ProxyBukkit.log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

public class LogDispatcher extends Thread
{
  private final ProxyBukkitLogger logger;
  @SuppressWarnings({ "unchecked", "rawtypes" })
private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue();

  public LogDispatcher(ProxyBukkitLogger logger)
  {
    super("ProxyBukkit Logger Thread");
    this.logger = logger;
  }

  public void run()
  {
    while (!isInterrupted())
    {
      LogRecord record = null;
      try
      {
        record = (LogRecord)this.queue.take();
      } catch (InterruptedException ex) {
      }
      this.logger.doLog(record);
      continue;

    }
    for (LogRecord record : this.queue)
    {
      this.logger.doLog(record);
    }
  }

  public void queue(LogRecord record)
  {
    if (!isInterrupted())
    {
      this.queue.add(record);
    }
  }
}