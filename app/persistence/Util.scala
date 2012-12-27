package persistence

/**
 * User: max
 * Date: 12/17/12
 * Time: 9:10 PM
 */
object Util {
  def listU(li: String): List[String] = {
    val one = li.dropRight(1).drop(1)

    val arr = one.split(",")
    val result = arr.foldLeft(List(): List[String])((i, s) =>
    s.trim.toList.filter(x => x != '\"').mkString("") :: i
    )

    result
  }



}
