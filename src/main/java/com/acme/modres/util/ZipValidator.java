package com.acme.modres.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class ZipValidator {

  private final byte[] zipContent;

  public ZipValidator(byte[] zipContent) throws ZipException, IOException {
    this.zipContent = zipContent == null ? new byte[0] : zipContent.clone();
  }

  public boolean isValid() throws IOException {
    try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipContent))) {
      ZipEntry entry = zipInputStream.getNextEntry();
      return entry != null;
    }
  }
}
