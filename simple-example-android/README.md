MOAT IoT Application Example
========
Simple Example Android Application
--------

This application requires the following private library:

- inventit-dmc-android-lib-api-4.1.0-prod.jar

You can download it via [iidn command line tool](https://github.com/inventit/iidn-cli) (signup required).

The following APK must be installed into a device where this application runs.

- Inventit ServiceSync Gateway

You can get it from [Goole Play](https://play.google.com/store/search?q=inventit+service-sync&c=apps) for free.

See [the tutorial](http://dev.yourinventit.com/guides/get-started) to learn more.

The directory structure of this application is as follows:

    |-- .settings (E)
    |-- assets
    |   `-- moat (1)
    |-- gen (E)
    |-- res
    |   |-- drawable
    |   |-- drawable-hdpi
    |   |-- drawable-ldpi
    |   |-- drawable-mdpi
    |   |-- layout
    |   `-- values
    |-- src
    |   `-- main
    |       `-- java

- (1) where ``signed.bin`` file is placed
- (E) for Eclipse specific directories