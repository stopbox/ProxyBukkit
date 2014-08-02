package me.stopbox123.ProxyBukkit.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import me.stopbox123.ProxyBukkit.ProxyBukkit;

public class ProxyBukkitLogger extends Logger {
	 private final Formatter formatter = new ConciseFormatter();
	 private final LogDispatcher dispatcher = new LogDispatcher(this);

	
	  public ProxyBukkitLogger(ProxyBukkit proxy) {
		// TODO Auto-generated constructor stub
	    super("ProxyBukkit", null);
	    try
	    {
	      FileHandler fileHandler = new FileHandler("proxy.log", 16777216, 8, true);
	      fileHandler.setFormatter(this.formatter);
	      addHandler(fileHandler);

	      ColouredWriter consoleHandler = new ColouredWriter(ProxyBukkit.getConsoleReader());
	      consoleHandler.setFormatter(this.formatter);
	      addHandler(consoleHandler);
	    }
	    catch (IOException ex) {
	      System.err.println("Could not register logger!");
	      ex.printStackTrace();
	    }
	    this.dispatcher.start();
	  }

	  public void log(LogRecord record)
	  {
	    this.dispatcher.queue(record);
	  }

	  void doLog(LogRecord record)
	  {
	    super.log(record);
	  }
}
