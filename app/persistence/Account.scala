/*
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */
package persistence

import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import org.apache.commons.codec.digest.DigestUtils._

case class Account(
                    id: ObjectId = new ObjectId,
                    username: String,
                    password: String,
                    email: String,
                    permission: Permission
                    )

object Account extends ModelCompanion[Account, ObjectId] {
  //val log = LoggerFactory getLogger(this getClass)
  val collection = MongoConnection()("b13_web")("account")
  val dao = new SalatDAO[Account, ObjectId](collection = collection) {}

  def authenticate(email: String, password: String): Option[Account] = {
    findByEmail(email).filter { user => user.password == hash(password, user.email + "maxH") }
  }

  def hash(pass: String, salt: String): String = sha256Hex(salt.padTo('0', 256) + pass)

  def findByEmail(email: String): Option[Account] = {
    dao.findOne(MongoDBObject("email" -> email))
  }

  def findOneByAccountName(username: String): Option[Account] = dao.findOne(MongoDBObject("username" -> username))

  def findByCountry(country: String) = dao.find(MongoDBObject("address.country" -> country))
}
