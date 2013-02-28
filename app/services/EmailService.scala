
package services

import akka.actor._
import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor.SupervisorStrategy._
import scala.concurrent.duration._
import akka.routing.SmallestMailboxRouter
import org.apache.commons.mail.{HtmlEmail, DefaultAuthenticator, EmailException}
import akka.actor.ActorDSL._


/**
 * Smtp config
 * @param tls if tls should be used with the smtp connections
 * @param ssl if ssl should be used with the smtp connections
 * @param port the smtp port
 * @param host the smtp host name
 * @param user the smtp user
 * @param password thw smtp password
 */
case class SmtpConfig(tls: Boolean = false,
                      ssl: Boolean = false,
                      port: Int = 25,
                      host: String,
                      user: String,
                      password: String)

/**
 * The email message sent to Actors in charge of delivering email
 *
 * @param subject the email subject
 * @param recipient the recipient
 * @param from the sender
 * @param text alternative simple text
 * @param html html body
 */
case class EmailMessage(
                         subject: String,
                         recipient: String,
                         from: String,
                         text: String,
                         html: String,
                         smtpConfig: SmtpConfig,
                         retryOn: FiniteDuration,
                         var deliveryAttempts: Int = 0)

/**
 * Email service
 */
object EmailService {

  /**
   * Uses the smallest inbox strategy to keep 20 instances alive ready to send out email
   * @see SmallestMailboxRouter
   */
  val emailServiceActor = Akka.system.actorOf(
    Props[EmailServiceActor].withRouter(
      SmallestMailboxRouter(nrOfInstances = 50)
    ), name = "emailService"
  )


  /**
   * public interface to send out emails that dispatch the message to the listening actors
   * @param emailMessage the email message
   */
  def send(emailMessage: EmailMessage) {
    emailServiceActor ! emailMessage
  }

  /**
   * Private helper invoked by the actors that sends the email
   * @param emailMessage the email message
   */
  private def sendEmailSync(emailMessage: EmailMessage) {

    // Create the email message
    val email = new HtmlEmail()
    email.setTLS(emailMessage.smtpConfig.tls)
    email.setSSL(emailMessage.smtpConfig.ssl)
    email.setSmtpPort(emailMessage.smtpConfig.port)
    email.setHostName(emailMessage.smtpConfig.host)

    email.setAuthenticator(new DefaultAuthenticator(
      emailMessage.smtpConfig.user,
      emailMessage.smtpConfig.password
    ))
    email.setContent(emailMessage.html, "text/html; charset= UTF-8")
    val x = email.setHtmlMsg(emailMessage.html)
      .setTextMsg(emailMessage.text)
      .addTo(emailMessage.recipient)
      .setFrom(emailMessage.from)
      .setSubject(emailMessage.subject)
      .send()

    println(x)
  }

  /**
   * An Email sender actor that sends out email messages
   * Retries delivery up to 10 times every 5 minutes as long as it receives
   * an EmailException, gives up at any other type of exception
   */
  class EmailServiceActor extends Actor with ActorLogging {

    /**
     * The actor supervisor strategy attempts to send email up to 10 times if there is a EmailException
     */
    override val supervisorStrategy =
      OneForOneStrategy(maxNrOfRetries = 10) {
        case emailException: EmailException => {
          log.debug("Restarting after receiving EmailException : {}", emailException.getMessage)
          Restart
        }
        case unknownException: Exception => {
          log.debug("Giving up. Can you recover from this? : {}", unknownException)
          Stop
        }
        case unknownCase: Any => {
          log.debug("Giving up on unexpected case : {}", unknownCase)
          Stop
        }
      }

    /**
     * Forwards messages to child workers
     */
    def receive = {
      case message: Any => context.actorOf(Props[EmailServiceWorker]) ! message
    }

  }

  /**
   * Email worker that delivers the message
   */
  class EmailServiceWorker extends Actor with ActorLogging {

    /**
     * The email message in scope
     */
    private var emailMessage: Option[EmailMessage] = None

    /**
     * Delivers a message
     */
    def receive = {
      case email: EmailMessage => {
        emailMessage = Option(email)
        email.deliveryAttempts = email.deliveryAttempts + 1
        log.debug("Atempting to deliver message")
        println("Atempting to deliver message")
        sendEmailSync(email)
        log.debug("Message delivered")
      }
      case unexpectedMessage: Any => {
        log.debug("Received unexepected message : {}", unexpectedMessage)
        throw new Exception("can't handle %s".format(unexpectedMessage))
      }
    }

    /**
     * If this child has been restarted due to an exception attempt redelivery
     * based on the message configured delay
     */
    override def preRestart(reason: Throwable, message: Option[Any]) {
      if (emailMessage.isDefined) {
        log.debug("Scheduling email message to be sent after attempts: {}", emailMessage.get)
        // Use this Actors' Dispatcher as ExecutionContext
        import context.dispatcher

        context.system.scheduler.scheduleOnce(emailMessage.get.retryOn, self, emailMessage.get)
      }
    }

    override def postStop() {
      if (emailMessage.isDefined) {
        log.debug("Stopped child email worker after attempts {}, {}", emailMessage.get.deliveryAttempts, self)
      }
    }

  }

}