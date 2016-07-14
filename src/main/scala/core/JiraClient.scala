package core

import akka.actor.ActorSystem
import core.model.{JiraIssueResponse, ModelObject, JQLQuery}
import spray.client.pipelining._
import spray.http._
import spray.json.JsonParser
import scala.concurrent.duration._
import model.JsonConverstions._

import scala.concurrent.{Await, Future}

object JiraClient extends App {

  override def main(args: Array[String]) = {
    implicit val system = ActorSystem("jira-connector")
    val client = new Client()
    // field: String, operator: String, value: String
    val queries = List(JQLQuery("project", "=", "ovsdb"), JQLQuery("project", "=", "controller"))
    client.queryJira(queries)
  }
}

class Client(implicit system: ActorSystem) extends Configurations {
  import system.dispatcher

  // gets a single issue - DOESN'T USE JQL
  @Deprecated
  def getIssues(project: String): Future[HttpResponse] = {
    val uri = Uri(s"https://inocybe.atlassian.net/rest/api/2/search?jql=project=$project", Uri.ParsingMode.RelaxedWithRawQuery)
    exec(Get(uri))
  }

  // queries WITH JQL
  def queryJira(queries: List[JQLQuery]) = {
    val urls = queries.map(q => Uri(jqlUrl.replace("{field}", q.field).replace("{operator}", q.operator).replace("{value}", q.value), Uri.ParsingMode.RelaxedWithRawQuery))
    val responses = urls.map(u => exec(Get(u)))
    val httpResponses = responses.map(future => Await.result(future, 3 seconds))
    val jiraIssues = httpResponses.map(httpResponse => unmarshall(httpResponse))
    jiraIssues.foreach(i => println(i.get.expand))
  }

  // http executor using spray client
  def exec(request: HttpRequest, transformer: RequestTransformer =
  addCredentials(BasicHttpCredentials(jiraUserName, jiraPassword))) = {
    val pipeline = transformer ~> sendReceive
    pipeline(request)
  }

  // unmarshall an HTTP response to Jira Issue
  def unmarshall(httpResponse: HttpResponse): Option[JiraIssueResponse] = {
    httpResponse.status match {
      case StatusCodes.OK => Some(JsonParser(httpResponse.entity.asString).convertTo[JiraIssueResponse])
      case _              => None
    }
  }
}
