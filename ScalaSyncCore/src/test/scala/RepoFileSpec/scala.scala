package RepoFileSpec

import org.scalatest._

import java.io.File
import scalasync.core._
import scalasync.core.server.mongoPersistances

/**
 * Created by lapinferoce on 6/22/14.
 */
object testConst{
   def home():String="/home/eric/devel/scalasync/ScalaSyncCore/"
   }

class RepoFileSpec extends FlatSpec with ShouldMatchers {
  "repofile as a record " should "behave as simple record" in {
    val repofile = new RepoFile("README.md", (new File("./testset/README.md")).getPath, "abcd")

    repofile.filename should equal("README.md")
    repofile.indep_path should equal((new File("./testset/README.md")).getPath)
    repofile.sum should equal("abcd")
  }
}

  class RepoFileSpecServer extends FlatSpec with ShouldMatchers {
    "repofile as a record " should "behave as simple record" in {
      val repofile = new RepoFileServer("README.md", (new File("./testset/README.md")).getPath, "abcd", true)

      repofile.filename should equal("README.md")
      repofile.indep_path should equal((new File("./testset/README.md")).getPath)
      repofile.sum should equal("abcd")
      repofile.deleted should equal(true)
    }
  }

class fsCrawlerSpec extends FlatSpec with ShouldMatchers {
  "find_file_recursively" should " return an array with only one file" in {
    val f = fsCrawler.find_file_recursively(new File("./testset/README.md"))
    f.length should equal(1)
  }
  it should " return a array with two file" in {
    val f = fsCrawler.find_file_recursively(new File("./testset"))
    f.length should equal(2)
  }
  it should "ignore file when not readable" in {
    try {
      fsCrawler.find_file_recursively(new File(testConst.home()))
    } catch {
      case e: Throwable => fail("that should not throw an exception !!")
    }
  }
}
class persistanceSpec extends FlatSpec with ShouldMatchers {
 "MongoPersistance" should "persiste well" in {

   val repofile = new RepoFileServer("README.md", (new File("./testset/README.md")).getPath, "abcd", true)
   mongoPersistances.addMetaData(repofile)
   mongoPersistances.findMetaBySum(repofile.sum) shouldBe Some(repofile)

 }
}



class RepoSpec extends FlatSpec with ShouldMatchers {
  "mkRepoFileFromFile" should " build a Repofile from a file" in {
    val repo = new RepoClient(new File(testConst.home))
    val f = repo.mkRepoFileFromFile(new File("./testset/README.md"))

    f.indep_path should equal((new File("./testset/README.md")).getCanonicalPath)
    f.filename should equal("/testset/README.md")

    f.sum should equal("ba20268676d4695fbd96d93b8d001d2ff03a0f50")
  }

  it should "scan a FS" in {
    if (!(new File(testConst.home + "./testset/").exists)) {
      println("testConst.home(" + testConst.home + ")+./testset/ does not exist")
    }

    val repo = new RepoClient(new File(testConst.home + "./testset/"))
    val repolist = repo.scanFs
    repolist.length shouldBe 2
    val intersection = repolist.intersect(List(
      RepoFile("/README.md", testConst.home + "testset/README.md", null).computeSum(),
      RepoFile("/sub1/README.md", testConst.home + "testset/sub1/README.md", null).computeSum()))
    intersection.length shouldBe 2
  }

  it should "repo map should be set" in {
    val repo = new RepoClient(new File(testConst.home + "./testset/"))
    val repolist = repo.scanFs
    repo.map.keySet.toList.size shouldBe 2

    val testlist = List(
      RepoFile("/README.md", testConst.home + "testset/README.md", null).computeSum().sum,
      RepoFile("/sub1/README.md", testConst.home + "testset/sub1/README.md", null).computeSum().sum)
    repo.map.keySet.toList.intersect(testlist).length shouldBe 2
  }

  it should "show all " in {
    val repo = new RepoClient(new File(testConst.home + "./testset/"))
    repo.scanFs()
    println(repo.dump())
    //repo.dump() shouldBe "cb4cbbb50fa9500f8f338382cb9f3ce6832861f8\n[README.md|/home/lapinferoce/devel/scala/ScalaSync/./testset/README.md|cb4cbbb50fa9500f8f338382cb9f3ce6832861f8]\nc2915def1bb99ddffd5498aa1d1429fd05168794\n[README.md|/home/lapinferoce/devel/scala/ScalaSync/./testset/sub1/README.md|c2915def1bb99ddffd5498aa1d1429fd05168794]"
  }

  it should "delete root repo and keep only sub" in {
    val repo = new RepoClient(new File(testConst.home + "./testset/"))
    //   repo.path shouldBe testConst.home+"testset/"
    //    println()
    repo.removeRoot(testConst.home + "/testset/README.md") shouldBe "/README.md"
  }

  it should "restore root repo and keep only sub" in {
    val repo = new RepoClient(new File(testConst.home + "./testset/"))

    repo.restoreRoot("README.md") shouldBe testConst.home + "testset/README.md"
  }

  it should "should be serialisable/unserialisable" in {

    val f = new File("/tmp/all.js")
    if (f.exists()) f.delete()

    val repo = new RepoClient(new File(testConst.home + "./testset/"))
    repo.scanFs()
    repo.serializeTo("/tmp/all.js")
    val repo2 = new RepoClient(new File(testConst.home + "./testset/"))
    repo2.unSerializeFrom("/tmp/all.js")

    val test = repo.map.keySet.toList.intersect(repo2.map.keySet.toList)
    test.length shouldBe 2
  }

  it should "should return empty map on non existing js " in {

    val f = new File("/tmp/all.js")
    if (f.exists()) f.delete()

    val repo2 = new RepoClient(new File(testConst.home + "./testset/"))
    repo2.unSerializeFrom("/tmp/all.js")

    repo2.map shouldBe Map.empty

  }

  it should "should compute the diff" in {
    val repo = new RepoClient(new File(testConst.home + "./testset/"))
    val repo2 = new RepoClient(new File(testConst.home + "./testset2/"))
    repo.scanFs()
    println(repo.dump())
    repo2.scanFs()
    println(repo2.dump())
    repo.diff(repo2) match {
      case (added, deleted) => {
        added.size shouldBe 1
        assert(added.contains("285aa06a152f26680d85f5c089b42a8f2f562144") == true)
        deleted.size shouldBe 1
        assert(deleted.contains("3dc7c61288d588d5c5aa624153401266f28617ee") == true)
      }
      //added.map{x=>println ("added "+x)};deleted.map{x=>println ("deleted "+x)}
      case _ => fail("xhould never happen")
    }

  }

}
