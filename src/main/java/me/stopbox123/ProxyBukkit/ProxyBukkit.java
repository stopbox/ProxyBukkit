package me.stopbox123.ProxyBukkit;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.stopbox123.ProxyBukkit.log.LoggingOutputStream;
import me.stopbox123.ProxyBukkit.log.ProxyBukkitLogger;

import org.fusesource.jansi.AnsiConsole;

import com.google.common.io.ByteStreams;

import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;
import jline.internal.Log;

public class ProxyBukkit extends ProxyServer {

	public volatile boolean isRunning;
	public ResourceBundle bundle;
	private static ProxyServer proxy;
	private static ConsoleReader consoleReader;
	private Logger logger;
	private ReadWriteLock connectionLock = new ReentrantReadWriteLock();
	
	public static ProxyServer getInstance() {
		return proxy;
	}
	
	public ProxyBukkit() {
		
	}
	
	public static ConsoleReader getConsoleReader() {
		return consoleReader;
	}

	public void start() throws IOException {
		proxy = this;
	    try {
	//      this.bundle = ResourceBundle.getBundle("messages");
	    }
	    catch (MissingResourceException ex) {
//	      this.bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
	    }
	    
	    Log.setOutput(new PrintStream(ByteStreams.nullOutputStream()));
	    AnsiConsole.systemInstall();
	    consoleReader = new ConsoleReader();
	    
	    this.logger = new ProxyBukkitLogger(this);
	    System.setErr(new PrintStream(new LoggingOutputStream(this.logger, Level.SEVERE), true));
	    System.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true));
	    
	    if ((consoleReader.getTerminal() instanceof UnsupportedTerminal))
	    {
	      this.logger.info("Unable to initialize fancy terminal. To fix this on Windows, install the correct Microsoft Visual C++ 2008 Runtime");
	      this.logger.info("NOTE: This error is non crucial, and BungeeCord will still function correctly! Do not bug the author about it unless you are still unable to get it working");
	    }
	    
	    if (NativeCipher.load()) {
	      this.logger.info("Using OpenSSL based native cipher.");
	    } else {
	      this.logger.info("Using standard Java JCE cipher. To enable the OpenSSL based native cipher, please make sure you are using 64 bit Ubuntu or Debian with libssl installed.");
	}
	}
}
