/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

package persistence

import com.novus.salat.dao.{SalatDAO, ModelCompanion}
import com.mongodb.casbah.Imports._
import org.joda.time.DateTime
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers

/**
 * User: max
 * Date: 1/27/13
 * Time: 9:28 PM
 */
case class Question(email: String, question: String, dateTime: Option[DateTime], state: Option[Int])

object Question extends ModelCompanion[Question, ObjectId] {
  RegisterJodaTimeConversionHelpers()
  val collection = MongoConnection()("b13_web")("questions")
  val dao = new SalatDAO[Question, ObjectId](collection = collection) {}

}