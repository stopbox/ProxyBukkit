package me.stopbox123.ProxyBukkit.log;

import java.beans.ConstructorProperties;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingOutputStream extends ByteArrayOutputStream
{
  private static final String separator = System.getProperty("line.separator");
  private final Logger logger;
  private final Level level;

  public void flush()
    throws IOException
  {
    String contents = toString();
    super.reset();
    if ((!contents.isEmpty()) && (!contents.equals(separator)))
    {
      this.logger.logp(this.level, "", "", contents);
    }
  }

  @ConstructorProperties({"logger", "level"})
  public LoggingOutputStream(Logger logger, Level level)
  {
    this.logger = logger; this.level = level;
  }
}