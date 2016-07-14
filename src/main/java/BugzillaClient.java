import core.Configurations;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.util.ClientFactory;
import scala.collection.mutable.HashTable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class BugzillaClient extends Configurations {

    public static void main(String[] args) {
        System.out.println("Adding ODL's bugzilla certificate into javax.net.ssl.trustStore");
        System.setProperty("javax.net.ssl.trustStore", "odlbugzilla.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
        System.out.println("Getting ODL bugs from Bugzilla...");
        getBug("https://bugs.opendaylight.org/xmlrpc.cgi", getBugzillaCredentials().get("username").get(), getBugzillaCredentials().get("password").get());
        System.out.println("Done");
    }

    private static XmlRpcClient getClient(String serverURL) {
        try {
            String apiURL = serverURL;
            XmlRpcClient rpcClient;
            XmlRpcClientConfigImpl config;

            config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(apiURL));
            rpcClient = new XmlRpcClient();
            rpcClient.setConfig(config);

            return rpcClient;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String, String> getBug(String serverURL, String login, String password) {
        try {
            XmlRpcClient rpcClient = getClient(serverURL);
            ArrayList<Object> params = new ArrayList<>();
            Hashtable<String, Object> executionData = new Hashtable<>();
            executionData.put("login", login);
            executionData.put("password", password);
            executionData.put("remember", true);
            executionData.put("ids", 6157);
            params.add(executionData);

            HashMap result = (HashMap) rpcClient.execute("Bug.get", params);
            //Object json = rpcClient.execute("Bug.get_bugs", params);

            result.forEach((k, v) -> System.out.println("k: " + k.toString() + " v: " + v.toString()));
            Object b = result.get("bugs");
            System.out.println(b);

            return result;
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getBugs(String serverURL, String login, String password) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            URL url = new URL(serverURL);
            System.out.println("server url: " + url);
            config.setServerURL(url);
            config.setBasicUserName(login);
            config.setBasicPassword(password);
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            ClientFactory factory = new ClientFactory(client);
            //BugInfo info = (BugInfo) factory.newInstance(BugInfo.class);
            //System.out.print("BugId: " + info.getId());
            //Bugzilla info = (Bugzilla) factory.newInstance(Bugzilla.class);
            Bug info = (Bug) factory.newInstance(Bug.class);
            //info.id(6157);
            //ArrayList<Integer> ids = new ArrayList<>();
            //ids.add(5157);

            HashSet<Integer> ids2 = new HashSet<>();
            ids2.add(6157);
            System.out.println("bug: " + info.get(ids2));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public static void getServerVersion(String serverURL, String login, String password) {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        try {
            URL url = new URL(serverURL);
            System.out.println("server url: " + url);
            config.setServerURL(url);
            config.setBasicUserName(login);
            config.setBasicPassword(password);
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
            ClientFactory factory = new ClientFactory(client);
            //BugInfo info = (BugInfo) factory.newInstance(BugInfo.class);
            //System.out.print("BugId: " + info.getId());
            Bugzilla info = (Bugzilla) factory.newInstance(Bugzilla.class);
            System.out.println("version: " + info.version());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
