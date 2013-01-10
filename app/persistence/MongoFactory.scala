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
import com.mongodb.casbah.MongoConnection


object MongoFactory {

  private val SERVER = "localhost"

  private val DATABASE_B13_EBAY = "b13_ebay"

  val connection = MongoConnection(SERVER)

  val connection_b13_ebay = connection(DATABASE_B13_EBAY)("items")

}