package com.gu.zuora.creditor

import com.gu.zuora.creditor.Types.{RawCSVText, SerialisedJson}
import org.scalatest.FlatSpec

class ZuoraExportDownloadServiceTest extends FlatSpec {

  val exportId = "foo"
  val unknownExportId = "bar"
  val invalidExportId = "baz"
  val fileId = "bim"
  val expectedCSV: RawCSVText =
    """lyric,lyric,lyric,lyric
      |abc,123,abc,123""".stripMargin

  implicit val zuoraRestClient = new TestRestClient {

    override def downloadFile(path: String): RawCSVText = {
      if (path ==  s"file/$fileId") {
        expectedCSV
      } else {
        ""
      }
    }
    override def makeRestGET(path: String): SerialisedJson = {
      if (path == s"object/export/$exportId") {
        s"""{"FileId":"$fileId"}"""
      } else if (path == s"object/export/$unknownExportId") {
        """{"FileId":"unknown"}"""
      } else {
        """{"invalid":"invalid"}"""
      }
    }
  }

  behavior of "ZuoraExportDownloaderTest"

  it should "downloadGeneratedExportFile" in {
    val zuoraExportDownloader = new ZuoraExportDownloadService
    val csvFile = zuoraExportDownloader.downloadGeneratedExportFile(exportId)
    assert(csvFile.contains(expectedCSV))
    val unknownCsvFile = zuoraExportDownloader.downloadGeneratedExportFile(unknownExportId)
    assert(unknownCsvFile.isEmpty)
    val invalidCsvFile1 = zuoraExportDownloader.downloadGeneratedExportFile(invalidExportId)
    assert(invalidCsvFile1.isEmpty)
    val invalidCsvFile2 = zuoraExportDownloader.downloadGeneratedExportFile(null)
    assert(invalidCsvFile2.isEmpty)
    val invalidCsvFile3 = zuoraExportDownloader.downloadGeneratedExportFile("")
    assert(invalidCsvFile3.isEmpty)
  }

}
