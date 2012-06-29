/*
 * Copyright (c) 2012
 *
 * NetSysLab - ECE - UBC
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package ca.ubc.ece.netsyslab.tagvalue.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Provides the abstraction to a family of concrete implementations of tagging
 * systems.
 *
 * @author Elizeu Santos-Neto (elizeus at ece dot ubc dot ca)
 */
public abstract class TaggingSystem {
  /**
   * The base url used to send requests to the system. The concrete
   * implementations should initialize this on the constructor.
   */
  protected String url;

  /**
   * The user for which the tagging system session is attached to.
   */
  protected String userId;

  /**
   * The user password to access the tagging system session.
   */
  protected String userPassword;

  /**
   * A HTTP Client used to send/receive requests from/to the data source service
   */
  DefaultHttpClient httpClient;

  /**
   * The host where the backend is located
   */
  HttpHost host;

  public TaggingSystem(String baseURL) {
    url = baseURL;
  }

  /**
   * A convenience constructor that allows initializing the base URL
   */
  public TaggingSystem(String hostname, int port) {
    this(hostname, port, "", "");
  }

  public TaggingSystem(String username, String password) {
    this("localhost", 80, username, password);
  }

  /**
   * A convenience constructor that allows initializing the base URL
   */
  public TaggingSystem(String hostname, int port, String username,
      String password) {
    host = new HttpHost(hostname, port, "https");
    userId = username;
    userPassword = password;
    httpClient = new DefaultHttpClient();
    httpClient.getCredentialsProvider().setCredentials(
        new AuthScope(host.getHostName(), host.getPort()),
        new UsernamePasswordCredentials(userId, userPassword));
  }

  /**
   *
   * @param parameters
   * @return
   */
  protected String sendGet(Properties parameters) throws IOException {
    StringBuffer output = new StringBuffer();
    try {
      HttpGet request = new HttpGet(new URI(buildURL(parameters)));
      HttpResponse response = httpClient.execute(host, request);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(entity.getContent()));
        String line;
        while ((line = reader.readLine()) != null) {
          output.append(line);
        }
      }
    } catch (URISyntaxException e) {
      throw new IOException(e);
    } catch (ClientProtocolException e) {
      throw new IOException(e);
    }
    return output.toString();
  }

   /**
    * Append the the parameters to the base URL and encode it to produce a URL
    * encoded string representation of it.
    *
    * @param parameters
    * @return a URL enconded string
    * @throws UnsupportedEncodingException
    */
   protected String buildURL(Properties parameters)
       throws UnsupportedEncodingException {
     StringBuffer parameterString = new StringBuffer();
     for (Map.Entry<Object, Object> entry : parameters.entrySet()) {
       parameterString.append(entry.getKey());
       parameterString.append("=");
       parameterString.append(
           URLEncoder.encode((String)entry.getValue(), "UTF-8"));
       parameterString.append("&");
     }
     return url + parameterString.toString();
   }

  /**
   * Retrieve a set of items annotated by the authenticated user
   *
   * @return a set of items
   */
  abstract public Map<String, String> getItems();

  /**
   * Retrieve a set of items annotated by all the specified tags.
   *
   * @param tags a set of tags used to select items from the system
   * @return a set of items annotated by all the tags specified in the set
   */
  abstract public Map<String, String> getItemsByTag(List<String> tags);

  /**
   * Retrieves all the tags used by the authenticated user.
   *
   * @return the set of all tags used by the authenticated user.
   */
  abstract public Set<String> getTags();

  /**
   * Retrieve a set of tags assigned to the specified item.
   *
   * @param item the identifier of an item for which the tags should be obtained
   * @return a set of tags that annotate the specified item
   */
  abstract public Set<String> getTagsByItem(String item);

  /**
   * Retrieves all the tags and their corresponding frequencies.
   * @return
   */
  abstract public Map<String, Double> getTagsFrequency();

  /**
   * Download and store the user annotations.
   */
  abstract public void getAnnotations();
}
