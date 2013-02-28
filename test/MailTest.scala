import concurrent.duration._
import org.scalatest.FunSuite
import play.api.test.FakeApplication
import play.api.test.Helpers._
import services.{EmailService, EmailMessage, SmtpConfig}

/**
 * User: max
 * Date: 2/27/13
 * Time: 12:21 PM
 */
class MailTest extends FunSuite {

  test("Email should be sent using html template") {
    running(FakeApplication()) {

      val name = "Maxim Matvienko"

      val messageHtml =
        s"""
          | <b> Thank you for your order !! </b> <br>
          |
          | Brand 13 is pleased to work with you $name
          |
          |
        """.stripMargin


      val config = SmtpConfig(
        tls = true,
        ssl = true,
        465,
        "smtp.gmail.com",
        "mmatvien@gmail.com",
        "freed0M1"
      )

      val email = EmailMessage(
        "testing",
        "mmatvien@gmail.com",
        "services@brand13.com",
        "text string",
        messageHtml,
        config,
        10 seconds,
        3
      )

      EmailService.send(email)
      assert(List().isEmpty)
    }
  }
}
