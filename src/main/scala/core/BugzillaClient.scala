package core

import com.j2bugzilla.base.BugzillaConnector
import com.j2bugzilla.rpc.{GetBug, BugSearch}
import com.j2bugzilla.rpc.BugSearch.{SearchLimiter, SearchQuery}
import collection.JavaConversions._


object BugzillaClient extends Configurations {

  def main(args: Array[String]) = {
    println("Starting client...")
    // contains bugs.opendaylight.org's certificate
    System.setProperty("javax.net.ssl.trustStore", "odlbugzilla.jks")
    System.setProperty("javax.net.ssl.trustStorePassword", "123456")

    // j2bugzilla connector (needed to send queries)
    val connector = new BugzillaConnector()

    // Connects to bugs.opendayklight.org using the connector
    connector.connectTo("https://bugs.opendaylight.org/xmlrpc.cgi", bugzillaUserName, bugzillaPassword)

    // Get an existing bug
    val bug = new GetBug(6157)
    connector.executeMethod(bug)
    println("bug alias: "
      + bug.getBug.getAlias + "\n"
      + " component: "
      + bug.getBug.getComponent + "\n"
      + " summary: "
      + bug.getBug.getSummary + "\n"
      + " platform: "
      + bug.getBug.getPlatform + "\n"
      + " product: "
      + bug.getBug.getProduct + "\n"
      + " id: "
      + bug.getBug.getID + "\n\n")

    println("link reconstruction: https://bugs.opendaylight.org/show_bug.cgi?id={ID}".replace("{ID}", bug.getBug.getID.toString) + "\n\n")

    println("searching bugs for product ovsdb...")
    // Create a query to search for all ovsdb bugs
    val query = new SearchQuery(SearchLimiter.PRODUCT, "ovsdb") // product is ovsdb, please see SearchLimiter enum
    val ovsdbBugSearch: BugSearch = new BugSearch(query)

    // Pass the query to the executeMethod in the connector for execution
    connector.executeMethod(ovsdbBugSearch)

    println("done. Outputting results as links: ")
    ovsdbBugSearch.getSearchResults.toList.foreach(b =>
      println("https://bugs.opendaylight.org/show_bug.cgi?id={ID}".replace("{ID}", bug.getBug.getID.toString) + "\n"))
  }
}
