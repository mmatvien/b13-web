package services

import com.ning.http.client.RequestBuilder
import dispatch._
import persistence.{CartItem, CartItemVariation, Cart}

/**
 * User: max
 * Date: 3/2/13
 * Time: 1:47 PM
 */
object EbayService {


  val ebay_token = "AgAAAA**AQAAAA**aAAAAA**TQHzUA**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AFlIunDpGGpAqdj6x9nY+seQ**L7cBAA" +
    "**AAMAAA**TaHHQtwlDGfwWV+qrCHjVwVT7PLxjn1Bw78ChwQBZTKva0Y+V8VFdPUODloKUkkWDW3J2rubUtX+wvGz8BCeUjvlN" +
    "+FtWlhb8umqlWiz3aIwYWrXO5GQj8DrtJ8xOwSyYsqf0s7zf4FvcLCH+ffucR2ypk" +
    "/xuYoN3Cw655LIxTuIRlySJZ8CH9Zo3VCGSuYZrdFrRJeAX1AFezRiJtTVhmhptP2ulZR" +
    "FiIb8WZiHuxqXzUIE1hUcGZNWPbZrlvvtS24gtwiNPuLx9N7LRcQWZgx+TgnVlnDqPSShVnuKg7eqIdFAODBGBs1oDqWucMCmnEbXqVZMlA0" +
    "pRJe92twPrCoAORk2Cqi8KVa7dYeA+XpZ8zmJxeI2AWxDVybVc880nCYrM17WQ0SR3L+xMqZaAOuTr6asL/HWFv31pjGaa0XDeYQUAvThmJ1QB" +
    "poFjtiR+uzj2F9hWLxa8XTLXALZlz5XkiE4YizG2XpMOi5Kry6RO7UukCK49oz+7352ZWVhOiRDLgw+KocXxlWfvl9f2eNb+K7AABScLMl7WDTRh" +
    "X4m/rmwJ/KPGv3TXda7KsE1+5xlR0hPJP8T9+7Hrih3aEp8uXAQALzOviEfKoiXjYff2VNa2/qaloYOVONWuR9Lb1ShILQvh8veoNFdmM0punVa" +
    "AwSjeVkKoXXU799F7924QPA3XSX4fnybxkMIeIf44tcnL9MvHXm2mTcoOUvMDbdfx33Aov4jbzURqTf3J0rKoXqJoM2KctjFUYbtDcrN"

  val X_EBAY_API_COMPATIBILITY_LEVEL = "813"
  val X_EBAY_API_SITEID = "0"
  val X_EBAY_API_DEV_NAME = "66d9b1b9-4bb0-4210-b8c4-1a7b3ae17da0"
  val X_EBAY_API_APP_NAME = "MAXIRU943-2e8a-46d0-8cfb-6dcbe3f1c5a"
  val X_EBAY_API_CERT_NAME = "921d566f-25b8-40bd-86da-0dc50efad38f"


  def createTradingHeaders(apiName: String): RequestBuilder = {

    val svc: RequestBuilder = url("https://api.ebay.com/ws/api.dll").POST

    svc.addHeader("X-EBAY-API-COMPATIBILITY-LEVEL", X_EBAY_API_COMPATIBILITY_LEVEL)
    svc.addHeader("X-EBAY-API-DEV-NAME", X_EBAY_API_DEV_NAME)
    svc.addHeader("X-EBAY-API-APP-NAME", X_EBAY_API_APP_NAME)
    svc.addHeader("X-EBAY-API-CERT-NAME", X_EBAY_API_CERT_NAME)
    svc.addHeader("X-EBAY-API-SITEID", X_EBAY_API_SITEID)
    svc.addHeader("X-EBAY-API-CALL-NAME", apiName)

    svc
  }


  def getItemInfo(itemId: String): String = {

    val xml =
      <GetItemRequest xmlns="urn:ebay:apis:eBLBaseComponents">
        <RequesterCredentials>
          <eBayAuthToken>{ebay_token}</eBayAuthToken>
        </RequesterCredentials>
        <IncludeItemSpecifics>true</IncludeItemSpecifics>
        <ItemID>{itemId}</ItemID>
      </GetItemRequest>

    "<?api version=\"1.0\" encoding=\"utf-8\"?>" + xml
  }

  def checkLineItemWithVendor(cartQuantity: Int, itemId: String, cartVariations: List[CartItemVariation]): Boolean = {
    val itemInfoRequest = createTradingHeaders("GetItem").setBody(getItemInfo(itemId))
    val itemInfo = Http(itemInfoRequest OK as.xml.Elem)
    var itemOKToBuy = false
    for (itemXml <- itemInfo()) {
      println(itemXml)
      val status = (itemXml \ "Item" \ "SellingStatus" \ "ListingStatus").text
      val variations = (itemXml \ "Item" \ "Variations" \ "Variation")

      if (variations.isEmpty) {
        val quantity = (itemXml \ "Item" \ "Quantity").text.toInt
        if (quantity >= cartQuantity && "Active" == status) itemOKToBuy = true
      } else {
        variations.foreach {
          variation =>
            val q = (variation \ "Quantity").text.toInt
            val namList = (variation \ "VariationSpecifics" \ "NameValueList")
            val varSet = namList.map(nn => CartItemVariation((nn \ "Name").text, (nn \ "Value").text))
            if (cartVariations == varSet && q >= cartQuantity && "Active" == status) itemOKToBuy = true
        }
      }
    }
    itemOKToBuy
  }


  def checkCartWithVendor(cart: Cart): List[CartItem] = {
    for (cartItem <- cart.cartItems if !checkLineItemWithVendor(cartItem.quantity, cartItem.itemId,
      cartItem.variations)) yield cartItem
  }
}
