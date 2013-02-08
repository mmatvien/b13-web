/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */
package util

import java.io.{File, FileWriter, PrintWriter}

object FileUtil {

  def using[A <: {def close()}, B](param: A)(f: A => B): B =
    try {
      f(param)
    } finally {
      param.close()
    }

  def writeToFile(fileName: String, data: String): Unit =
    using(new FileWriter(fileName)) {
      fileWriter => fileWriter.write(data)
    }

  def appendToFile(fileName: String, textData: String): Unit =
    using(new FileWriter(fileName, true)) {
      fileWriter =>
        using(new PrintWriter(fileWriter))(printWriter => printWriter.println(textData))
    }


  def removeFile(fileName: String) {
    new File(fileName).delete()
  }


}