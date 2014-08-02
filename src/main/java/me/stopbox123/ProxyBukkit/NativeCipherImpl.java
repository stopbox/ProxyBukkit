package me.stopbox123.ProxyBukkit;

class NativeCipherImpl {
  native long init(byte[] paramArrayOfByte);

  native void free(long paramLong);

  native void cipher(boolean paramBoolean, long paramLong1, byte[] paramArrayOfByte, long paramLong2, long paramLong3, int paramInt);
}