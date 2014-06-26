MOAT IoT Application Example
========
Simple Example Android with Arduino Application
--------

This application requires the following private library:

- inventit-dmc-android-lib-api-4.1.0-prod.jar

You can download it via [iidn command line tool](https://github.com/inventit/iidn-cli) (signup required).

And it also requires the following LGPL library (already bundled in the project):

- usb-serial-for-android-v0.1.0-5c8a655-inventit-0.1.0.jar

The following APK must be installed into a device where this application runs.

- Inventit ServiceSync Gateway

You can get it from [Goole Play](https://play.google.com/store/search?q=inventit+service-sync&c=apps) for free.

See [the tutorial](http://dev.inventit.io/guides/get-started.html) to learn more.

The directory structure of this application is as follows:

    |-- .settings (E)
    |-- assets
    |   `-- moat (1)
    |-- gen (E)
    |-- libs
    |   `-- com
    |       `-- hoho
    |           `-- usb-serial-for-android
    |               `-- v0.1.0-5c8a655-inventit-0.1.0
    |-- res
    |   |-- drawable
    |   |-- drawable-hdpi
    |   |-- drawable-ldpi
    |   |-- drawable-mdpi
    |   |-- drawable-xhdpi
    |   |-- layout
    |   `-- values
    |-- src
    |   `-- main
    |       `-- java

- (1) where ``signed.bin`` file is placed
- (E) for Eclipse specific directories