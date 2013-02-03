/*
 * Copyright (c) 2012.
 * All Right Reserved, http://www.maxiru.com/
 */

package persistence

/**
 * User: max
 * Date: 12/17/12
 * Time: 10:10 AM
 */
import com.mongodb.casbah.{MongoCollection, MongoConnection}


object MongoFactory {

  private val SERVER = "localhost"
  private val DATABASE = "b13_ebay"
  private val ITEMS = "items"
  private val OPS = "ops"
  private val SELLER = "seller"

  val connection: MongoConnection = MongoConnection(SERVER)
  val ItemsCollection: MongoCollection = connection(DATABASE)(ITEMS)
  val OpsCollection: MongoCollection = connection(DATABASE)(OPS)
  val SellerCollection: MongoCollection = connection(DATABASE)(SELLER)
}