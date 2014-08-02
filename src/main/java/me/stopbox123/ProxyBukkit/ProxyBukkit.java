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
	      this.bundle = ResourceBundle.getBundle("messages");
	    }
	    catch (MissingResourceException ex) {
	      this.bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
	    }
	    
	    Log.setOutput(new PrintStream(ByteStreams.nullOutputStream()));
	    AnsiConsole.systemInstall();
	    consoleReader = new ConsoleReader();
	    
	    this.logger = new ProxyBukkitLogger(this);
	    System.setErr(new PrintStream(new LoggingOutputStream(this.logger, Level.SEVERE), true));
	    System.setOut(new PrintStream(new LoggingOutputStream(this.logger, Level.INFO), true));
	}
}
