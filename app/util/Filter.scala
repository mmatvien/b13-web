/*
 * Copyright (c) 2012.
 * All Right Reserved, http://www.maxiru.com/
 */

package util

import persistence.{Seller, Item}
import scala.Predef._
import scala.Some

/**
 * User: max
 * Date: 12/27/12
 * Time: 3:51 PM
 */
object Filter {

  def collectBrandsForSeller(seller: String): Set[String] = {
    var brandSet: Set[String] = Set()
    Item.findAllSellerItems(seller).map {
      item =>
        item.specifics.get("Brand") match {
          case Some(brand) => brandSet += brand
          case None =>
        }
    }
    brandSet
  }

  def collectCategoriesForBrand(brands: List[String]): Set[String] = {
    var categorySet: Set[String] = Set()

    Item.findItemsByBrand(brands).map {
      item =>
        print(".")
        categorySet += item.categoryName.toString
    }
    categorySet
  }

  def collectAllBrands(): List[String] = {
    var brandSet: List[String] = List()

    Seller.findAllSellers().map {
      seller =>
        Item.findAllSellerItems(seller.name).map {
          item =>
            item.specifics.get("Brand") match {
              case Some(brand) => brandSet = brand :: brandSet
              case None =>
            }
        }
    }


    brandSet.sortWith(_.toLowerCase < _.toLowerCase)
  }

  def extractCategory(position: Int, fullCategory: List[String]): List[String] = {

    for (cat <- fullCategory
    ) yield {
      val arr = cat.split(":")
      arr(position)
    }

  }

  def collectAllCategories(): List[String] = {
    var categorySet: List[String] = List()
    Seller.findAllSellers().map {
      seller =>
        Item.findAllSellerItems(seller.name).map {
          item =>
            categorySet = item.categoryName :: categorySet
        }
    }
    categorySet.sortWith(_.toLowerCase < _.toLowerCase)
  }

  def collectBrandsForCategory(category: String): List[String] = {
    var brandSet: Set[String] = Set()

    Item.findAllItems().map {
      item =>
        if (item.categoryName.contains(category)) {
          item.specifics.get("Brand") match {
            case Some(brand) => brandSet += brand
            case None =>
          }
        }
    }
    brandSet.toList.sortWith(_.toLowerCase < _.toLowerCase)
  }
}
