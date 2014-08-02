package me.stopbox123.ProxyBukkit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;

public abstract interface ProxyBukkitCipher
{
  public abstract void init(boolean paramBoolean, SecretKey paramSecretKey)
    throws GeneralSecurityException;

  public abstract void free();

  public abstract void cipher(ByteBuf paramByteBuf1, ByteBuf paramByteBuf2)
    throws GeneralSecurityException;

  public abstract ByteBuf cipher(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf)
    throws GeneralSecurityException;
}