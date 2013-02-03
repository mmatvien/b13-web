/*
 * Copyright (c) 2013.
 * All Right Reserved, http://www.maxiru.com/
 */

package persistence

import com.novus.salat.dao.{SalatDAO, ModelCompanion}
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers

/**
 * User: max
 * Date: 2/1/13
 * Time: 9:31 PM
 */

trait State
case object Active extends State
case object Passive extends State
case object New extends State

case class Seller(name: String, state: String)

object Seller extends ModelCompanion[Seller, ObjectId] {
  RegisterJodaTimeConversionHelpers()
  val collection = MongoConnection()("b13_ebay")("seller")
  val dao = new SalatDAO[Seller, ObjectId](collection = collection) {}
}

