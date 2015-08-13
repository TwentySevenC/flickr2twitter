This page introduces how to setup this program on your GAE server.

# Introduction #
This program requires you to setup OAuth token with your Flickr and Twitter account in order to make it work. You would also need to setup a Google App Engine account in order to host this application for running the scheduled task.


# Details #
## Setup Google App Engine ##
  * Create a GAE account
  * Create a GAE app
  * Download the latest version from the tab Downloads
  * Unzip the downloaded archive and modify the file war\WEB-INF\appengine-web.xml with the app ID you created. `<application>{your app id}</application>`
  * Optionally you can also modify war\WEB-INF\cron.xml with a desired time interval. More information could be found at http://code.google.com/appengine/docs/java/config/cron.html
  * Upload the application to your GAE account by following this guide: http://code.google.com/appengine/docs/java/gettingstarted/uploading.html


## Login to the Application ##
  * after the deployment of the application, now go to the page: http://youappid.appspot.com
  * You can either use the default user (user: admin, password: admin), or create another normal user.
  * Login with your chosen user
  * Navigate to page _Authorize Source & Target_ and now you can authorize this application to access your Flickr/Twitter or whatever online servers you want to interact with.

## Supported OAuth Callback Services ##

### Source Service Providers ###
<a href='http://www.flickr.com'>Flickr</a>, <a href='http://picasaweb.google.com/'>Picasa</a>

### Target Service Providers ###
<a href='http://twitter.com'>Twitter</a>, <a href='http://t.sina.com.cn/'>Sina</a>, eMail