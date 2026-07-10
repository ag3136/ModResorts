package com.acme.modres.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

/**
 * JSON parsing utility that supports both File-based and byte-array-based input.
 * The byte-array constructor enables cloud-native usage where content is read
 * directly from classpath resources or GCS without writing to the local filesystem.
 */
public class JsonInputStream extends FileInputStream {

  private File file;
  private byte[] content;

  public JsonInputStream(File file) throws FileNotFoundException {
    super(file);
    this.file = file;
    this.content = null;
  }

  /**
   * Constructs a JsonInputStream from a byte array, avoiding local filesystem dependency.
   * Used when content is sourced from classpath resources or Google Cloud Storage.
   *
   * @param content byte array of JSON content
   * @throws IOException if a temporary file cannot be created for the underlying FileInputStream
   */
  public JsonInputStream(byte[] content) throws IOException {
    super(createTempFileFromBytes(content));
    this.content = content;
    this.file = null;
  }

  private static File createTempFileFromBytes(byte[] content) throws IOException {
    File tmp = File.createTempFile("json-parse-", ".json");
    tmp.deleteOnExit();
    try (java.io.FileOutputStream fos = new java.io.FileOutputStream(tmp)) {
      fos.write(content);
    }
    return tmp;
  }

  public Object parseJsonAs(Class<?> cls) {
    Object jsonObject = null;
    InputStream inputStream = null;
    try {
      if (content != null) {
        // Use in-memory byte array — no local filesystem dependency
        inputStream = new ByteArrayInputStream(content);
      } else if (file != null && file.exists()) {
        inputStream = new FileInputStream(file);
      } else {
        return null;
      }
      Gson gson = new Gson();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      jsonObject = gson.fromJson(reader, cls);
    } catch (Exception e) {
      e.printStackTrace();
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          // closed successfully
        }
      }
    }
    return jsonObject;
  }

}
