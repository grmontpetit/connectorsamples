package core

object Configurations {

  val configs = new Configurations

  def getBugzillaCredentials = {
    Map("password" -> configs.bugzillaUserName, "password" -> configs.bugzillaPassword)
  }

  def getJiraCredentials: Map[String, String] = {
    Map("username" -> configs.jiraPassword, "password" -> configs.jiraPassword)
  }
}

class Configurations {
  val bugzillaUserName: String = "USERNAME" // name before the @ in your email address
  val bugzillaPassword: String = "PASSWORD"

  val jiraUserName: String = "USERNAME" // name before the @ in your email address
  val jiraPassword: String = "PASSWORD"

  val jqlUrl = "https://inocybe.atlassian.net/rest/api/2/search?jql={field}{operator}{value}"
}
