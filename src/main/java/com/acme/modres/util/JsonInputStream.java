package com.acme.modres.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class JsonInputStream extends InputStream {

  private final InputStream delegate;

  public JsonInputStream(InputStream inputStream) {
    this.delegate = inputStream;
  }

  @Override
  public int read() throws IOException {
    return delegate.read();
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return delegate.read(b, off, len);
  }

  @Override
  public void close() throws IOException {
    delegate.close();
  }

  public Object parseJsonAs(Class<?> cls) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(delegate, StandardCharsets.UTF_8))) {
      Gson gson = new Gson();
      return gson.fromJson(reader, cls);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
