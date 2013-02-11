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


  val mainBrands = List("Adidas",
    "Aeropostale",
    "Anne Klein",
    "Armani",
    "AX Armani Exchange",
    "Bali",
    "Banana Republic",
    "BOSS",
    "Bulova",
    "Burberry",
    "Bvlgari",
    "Calvin Klein",
    "Carlo Fellini",
    "Cartier",
    "Casio",
    "Champion",
    "Chanel",
    "Christian Dior",
    "Citizen",
    "COACH",
    "Converse",
    "Diesel",
    "DKNY",
    "D&G",
    "DOLCE & GABBANA",
    "Dooney & Bourke",
    "Dr. Koffer Fine Leather Accessories",
    "eBags",
    "Echo",
    "Emporio Armani",
    "Fila",
    "Fossil",
    "Giorgio Armani",
    "GUCCI",
    "GUESS",
    "Gymboree",
    "Hanes",
    "Harley-Davidson",
    "Hugo Boss",
    "Invicta",
    "Jessica Simpson",
    "JJ Cole",
    "Jos. A. Bank Clothiers",
    "Juicy Couture",
    "Keds",
    "Kenneth Cole",
    "Kenneth Cole Reaction",
    "Lacoste",
    "Lancaster",
    "Levis",
    "Marc Jacobs",
    "Michael Kors",
    "Movado",
    "Nautica",
    "New Balance",
    "NIKE",
    "Oakley",
    "Paris Hilton",
    "Playtex",
    "Polaroid",
    "Polo Ralph Lauren",
    "PRADA",
    "PUMA",
    "Reebok",
    "Reeds",
    "Roberto Cavalli",
    "Rolex",
    "Roxy",
    "SAKS FIFTH AVENUE",
    "SALVATORE FERRAGAMO",
    "Seiko",
    "Skagen",
    "Skechers",
    "Swatch",
    "Tag Heuer",
    "TIFFANY",
    "Timberland",
    "Timex",
    "Tommy Hilfiger",
    "Under Armour",
    "VALENTINO",
    "VERSACE",
    "Wonderbra",
    "YVES SAINT LAURENT")

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
