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
case class Subscription(email: String, dateTime: Option[DateTime])

  object Subscription extends ModelCompanion[Subscription, ObjectId] {
  RegisterJodaTimeConversionHelpers()
  val collection = MongoConnection()("b13_web")("subscriptions")
  val dao = new SalatDAO[Subscription, ObjectId](collection = collection) {}

}