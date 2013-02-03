/**
 * Copyright (c) 2013.
 * Maxim Matvienko - maxiru (r) LLC.
 * All Right Reserved, http://www.maxiru.com/
 */

import com.mongodb.casbah.Imports._

import persistence.{NormalUser, Administrator, Account}
import play.api._

object Global extends GlobalSettings {


  override def onStart(app: Application) {

    if (Account.count(DBObject(), Nil, Nil) == 0) {
      Logger.info("Loading Default Accounts")

      Account.save(Account(
        username = "max",
        password = Account.hash("freed0M11", "mmatvien@gmail.commaxH"),
        email = "mmatvien@gmail.com",
        permission = Administrator
      ))


      Account.save(Account(
        username = "lena",
        password = Account.hash("skorpion4ik'", "lena.matvien@gmail.commaxH"),
        email = "lena.matvien@gmail.com",
        permission = NormalUser
      ))
    }
  }
}
