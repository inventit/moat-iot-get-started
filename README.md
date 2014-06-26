MOAT IoT Application Example
===
This is an example application using MOAT IoT API set.

The application is composed of 4 parts:

 1. An Android project using [MOAT Java](http://dev.inventit.io/references/moat-java-api-document.html)
 2. Server side Javascripts using [MOAT js](http://dev.inventit.io/references/moat-js-api-document.html)
 3. Rails application using [MOAT REST](http://dev.inventit.io/references/moat-rest-api-document.html)
 4. Google App Engine application using [MOAT REST](http://dev.inventit.io/references/moat-rest-api-document.html)

For users who aren't familiar with Rails or are using Windows OS, please choose Google App Engine application.

You need to sign up Inventit IoT Developer Network Sandbox Server with [IIDN Command Line Tool](https://github.com/inventit/iidn-cli) in order to deploy the application.

## Get Started

See [the tutorial](http://dev.inventit.io/guides/get-started.html) to learn more.

## Setup Helper

As of 1.0.8, a setup helper script is included, which helps you to prepare building environment.

After signing up to IIDN, on the root of this project (same as where this README.md is) enter:

    moat-iot-get-started:$ rake

After a couple of minutes, the script will be terminated. And you need to run a few commands regarding signing secure token. The detailed commands are shown on the script log message. You can copy and paste the commands to run.

## Source Code License

All program source codes except MOAT IoT App Icon files are available under the MIT style License.

The use of IIDN service requires [our term of service](http://dev.inventit.io/legal/term-of-service).

Copyright (c) 2014 Inventit Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

## MOAT IoT App Icon Files (moat_icon.png,icon.png)
![Attribution-NonCommercial 3.0 Unported](http://i.creativecommons.org/l/by-nc/3.0/88x31.png "Attribution-NonCommercial 3.0 Unported")

## Change History

1.1.0 : June 26, 2014

* Modifies Android sample apps as MOAT Java/Android API is updated

1.0.13 : April 3, 2014

* Updates maven android plugin
* Fixes copyright year and dead links

1.0.12 : May 2, 2013

* Adds the background image gallery feature on Rails and GAE web apps. Have fun!
* GAE example web app now supports App Engine SDK 1.7.7.1
* Adds the login page to the GAE example web app in order to provide links to loign/logout
* Adds a new example app using Arduino, sensors and Android (3.1+)

1.0.11 : April 2, 2013

* Fixes an issue where index.html contains wrong redirection destination
* Fixes an issue where build.xml copies wrong version of jars from GAE Plugin directory

1.0.10 : April 2, 2013

* Adds a new web application project for Google App Engine

1.0.9 : February 24, 2013

* Fixes an issue where subtasks of setup task are performed in wrong order
* Modifies printed comments in the Rakefile

1.0.8 : February 15, 2013

* Adds [Vagrant](http://www.vagrantup.com/) support. Now you are free from preparing get-started app environment
* Adds the setup helper script
* Adds MOAT js helper script to create a zip package and to perform unit tests

1.0.7 : February 12, 2013

* Fixes an issue where OpenJDK fails to compile simple-example-android module because of OpenJDK specific issue (http://stackoverflow.com/a/9590042)

1.0.6 : January 27, 2013

* Fixes an issue where authentication info isn't retained in static scope but in instance scope of MOAT REST ActiveResource models (Rails App)

1.0.5 : January 26, 2013

* Fixes an issue where authentication token is always missing when DELETE method to /sys/auth object is issued (Rails App)
* Fixes an issue where a resource type attribute is not retrieved because of missing 'f' parameter for specifying the attribute (Rails App)
* Fixes an issue where get/put/post/delete cannot be attribute accessors as they are already defined in [activeresource/lib/active_resource/custom_methods.rb](http://api.rubyonrails.org/classes/ActiveResource/CustomMethods.html) (Rails App)
* Fixes an issue where the image URL is obtained via an Image object itself rather than the 'get' property of the Image object (Rails App)

1.0.4 : January 16, 2013  

* Fixes an issue where a Moat instance is not shutdown on MoatIoTService is destroyed

1.0.3 : January 13, 2013  

* Fixes the wrong license term in a file
* Fixes a bug where the wrong number of objects to be saved is counted in a server side script
* Fixes the wrong README description regarding library dependency in the Android app
* Updates Rails to 3.2.11 for the [security fix](http://weblog.rubyonrails.org/2013/1/8/Rails-3-2-11-3-1-10-3-0-19-and-2-3-15-have-been-released/)

1.0.2 : January 9, 2013  

* This README.md is updated

1.0.1 : January 9, 2013  

* Removes debug codes
* Adds README.md to each application to describe
* Updates this doc
* Applies format changes to package.json
* Removes the dependency to inventit-dmc-api

1.0.0 : January 5, 2013  
* Initial Release.
