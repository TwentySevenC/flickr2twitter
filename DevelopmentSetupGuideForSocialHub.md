# Introduction #
The SocialHub leverages the power of Google App Engine for providing the runtime environment, as well as an Android native application for configuration on the go. This guide would walk you through how to setup the development environment for SocialHub.

# Details #

## Download the Source Code ##
First of all, you would also need a SVN client for playing with the source code. You can try with [TortiseSVN](http://tortoisesvn.net/downloads.html) and [SubClipse](http://subclipse.tigris.org/download.html) for working with Eclipse.

Then you can check out the code from https://flickr2twitter.googlecode.com/svn/branches/ebaysocialhub/ with your SVN client. If you also intend to make contribution to our project, please do let me know(yuyang226@gmail.com) and I will consider if we can enroll you as a commiter for the project.

Please remember that checking in changes would require a generated password from http://code.google.com/hosting/settings. Your gmail password would not work in this case.

We currently have two projects SocialHub(folder name is actually AutoFlickr2Twitter) for the GAE runtime, and the SocialHub4Android android project.

## Setup Your Environment ##
For working with the SocialHub GAE version, please install the latest version of GAE from http://code.google.com/appengine/downloads.html#Google_App_Engine_SDK_for_Java. Make sure you install Java version and not the Python version.

For working with the Android client project, please install Android SDK as documented at http://developer.android.com/sdk/installing.html. We would need Android 2.2 API Level 8 for this project.

## Setup Your Environment with Eclipse ##
I assume you have already got Java installed, so I would just skip the installation of JDK. If not, please do make sure you install JDK with version 6 or higher.

We would Eclipse as our primary development platform. You can download the J2EE version from http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/heliossr2 for Eclipse 3.6.1 release.

Then install the Google plugin for Eclipse from http://code.google.com/eclipse/docs/getting_started.html#installing for the version of Eclipse you choose. You can find more information from http://code.google.com/appengine/docs/java/tools/eclipse.html#Installing_the_Google_Plugin_for_Eclipse.

If you are also interested in the Android client, then please install ADT from http://developer.android.com/sdk/eclipse-adt.html for the version of Eclipse you choose.

## Import the Projects ##
Now you can import the two SocialHub projects and start enjoying the development!