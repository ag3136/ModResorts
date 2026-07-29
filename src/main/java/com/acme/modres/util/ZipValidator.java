package com.acme.modres.util;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * ZipValidator uses composition instead of inheritance to validate ZIP files.
 * This follows best practices and implements AutoCloseable for proper resource management.
 */
public class ZipValidator implements AutoCloseable {

  private File file;
  private ZipFile zipFile;

  public ZipValidator(File file) throws ZipException, IOException {
    this.file = file;
    this.zipFile = new ZipFile(file);
  }

  public boolean isValid() throws Throwable {
    if (file.exists() && zipFile != null) {
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      if (!entries.hasMoreElements()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void close() throws IOException {
    if (zipFile != null) {
      zipFile.close();
    }
  }
}
