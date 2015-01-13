# Dogeskrá

A simple ClojureScript application

"Dagsskrá" is the Icelandic for "schedule", so "dogeskrá" is a **bad** pun on this - a Doge-flavored schedule showing what's on Iceland's public TV channel RÚV today.

## Build

### Prerequisites

To hack on this app yourself, you will need to have a recent [JVM][jvm] installed and the [Clojure][clojure] build tool [boot][boot]. Figuring out how to install these on whatever platform you're using is entirely up to you! 

[boot]: http://boot-clj.com/
[clojure]: http://clojure.org/
[jvm]: http://www.oracle.com/technetwork/java/javase/downloads/index.html

Once everything is installed, do the following to hack on the app in debug mode:

```
$ boot debug
```

Now open `localhost:8000` in your favorite web browser.

To package the app for release, do the following:

```
$ boot release
```

Now open the file `target/index.html` in your favorite web browser to check that everything worked. Note that `boot release` must be run **every time** you make changes to the code, whereas `boot debug` will automatically watch and recompile your code.