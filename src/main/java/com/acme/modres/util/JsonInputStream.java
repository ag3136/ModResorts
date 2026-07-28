package com.acme.modres.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

/**
 * Cloud-native JSON input stream that works with InputStream instead of File.
 * This eliminates file system dependencies and works with classpath resources.
 */
public class JsonInputStream extends InputStream {

  private InputStream inputStream;

  public JsonInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  @Override
  public int read() throws IOException {
    return inputStream.read();
  }

  @Override
  public int read(byte[] b) throws IOException {
    return inputStream.read(b);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return inputStream.read(b, off, len);
  }

  @Override
  public void close() throws IOException {
    if (inputStream != null) {
      inputStream.close();
    }
  }

  /**
   * Parse JSON from input stream to specified class.
   * Uses try-with-resources for proper resource management.
   * 
   * @param cls Target class for JSON deserialization
   * @return Parsed object or null if error occurs
   */
  public Object parseJsonAs(Class<?> cls) {
    Object jsonObject = null;
    try {
      Gson gson = new Gson();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      jsonObject = gson.fromJson(reader, cls);
    } catch (Exception e) {
      e.printStackTrace();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return jsonObject;
  }

}
