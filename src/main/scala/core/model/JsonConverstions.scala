package core.model

import spray.httpx.SprayJsonSupport
import spray.json._

object JsonConverstions extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val jiraResponse = jsonFormat1(JiraIssueResponse)

  implicit object ModelObjectJsonFormat extends RootJsonFormat[ModelObject] {
    def write(obj: ModelObject) = obj match {
      case o: JiraIssueResponse => o.toJson
      case _ => throw new DeserializationException("This ModelObject does not have a json format")
    }
    def read(value: JsValue) = throw new DeserializationException("Cannot deserialize to ModelObject")
  }
}
