package me.stopbox123.ProxyBukkit;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;

public class NativeCipher implements ProxyBukkitCipher
{
  private final NativeCipherImpl nativeCipher = new NativeCipherImpl();
  private boolean forEncryption;
  private byte[] iv;
  private static boolean loaded;
  private long pointer;

  public static boolean isSupported()
  {
    return ("Linux".equals(System.getProperty("os.name"))) && ("amd64".equals(System.getProperty("os.arch")));
  }

  public static boolean load()
  {
    if ((!loaded) && (isSupported()))
      try {
        InputStream lib = ProxyBukkitCipher.class.getClassLoader().getResourceAsStream("native-cipher.so"); Throwable localThrowable3 = null;
        try
        {
          File temp = File.createTempFile("bungeecord-native-cipher", ".so");
          temp.deleteOnExit();

          OutputStream outputStream = new FileOutputStream(temp); Throwable localThrowable4 = null;
          try {
            ByteStreams.copy(lib, outputStream);
            System.load(temp.getPath());
          }
          catch (Throwable localThrowable1)
          {
            localThrowable4 = localThrowable1; throw localThrowable1;
          }
          finally
          {
          }
          loaded = true;
        }
        catch (Throwable localThrowable2)
        {
          localThrowable3 = localThrowable2; throw localThrowable2;
        }
        finally
        {
          if (lib != null) if (localThrowable3 != null) try { lib.close(); } catch (Throwable x2) { localThrowable3.addSuppressed(x2); } else lib.close(); 
        }
      }
      catch (Throwable t)
      {
      }
    return loaded;
  }

  public static boolean isLoaded()
  {
    return loaded;
  }

  public void init(boolean forEncryption, SecretKey key)
    throws GeneralSecurityException
  {
    Preconditions.checkArgument(key.getEncoded().length == 16, "Invalid key size");
    if (this.pointer != 0L)
    {
      this.nativeCipher.free(this.pointer);
    }
    this.forEncryption = forEncryption;
    this.iv = key.getEncoded();
    this.pointer = this.nativeCipher.init(key.getEncoded());
  }

  public void free()
  {
    if (this.pointer != 0L)
    {
      this.nativeCipher.free(this.pointer);
      this.pointer = 0L;
    }
  }

  public void cipher(ByteBuf in, ByteBuf out)
    throws GeneralSecurityException
  {
    in.memoryAddress();
    out.memoryAddress();
    Preconditions.checkState(this.pointer != 0L, "Invalid pointer to AES key!");
    Preconditions.checkState(this.iv != null, "Invalid IV!");

    int length = in.readableBytes();

    out.ensureWritable(length);

    this.nativeCipher.cipher(this.forEncryption, this.pointer, this.iv, in.memoryAddress() + in.readerIndex(), out.memoryAddress() + out.writerIndex(), length);

    in.readerIndex(in.writerIndex());

    out.writerIndex(out.writerIndex() + length);
  }

  public ByteBuf cipher(ChannelHandlerContext ctx, ByteBuf in)
    throws GeneralSecurityException
  {
    int readableBytes = in.readableBytes();
    ByteBuf heapOut = ctx.alloc().directBuffer(readableBytes);
    cipher(in, heapOut);

    return heapOut;
  }

  public NativeCipherImpl getNativeCipher()
  {
    return this.nativeCipher;
  }
}