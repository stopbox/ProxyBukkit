package me.stopbox123.ProxyBukkit;

import com.google.common.base.Preconditions;

public abstract class ProxyServer {

	private static ProxyServer instance;
	
	public static void setInstance(ProxyServer proxy) {
		Preconditions.checkNotNull(instance, "Proxy is already setup!");
	    Preconditions.checkArgument(instance == null, "Instance already set");
	    instance = proxy;
	}
	
}
