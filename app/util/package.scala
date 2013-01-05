/**
 * User: max
 * Date: 1/3/13
 * Time: 10:33 PM
 */


import org.apache.http.conn.ClientConnectionManager

package object stripe {
  var apiKey: String = ""
  var connectionManager: ClientConnectionManager = null
}