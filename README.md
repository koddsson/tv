# What's on TV in Iceland today?

## What?

A simple ClojureScript application which shows a daily schedule for what's on Iceland's public TV channel RÚV today.

This is very much a work-in-progress. Currently the app only shows the upcoming schedule for RÚV but the plan is to support all the TV stations supported by the [apis.is][apis.is] `/tv` API endpoint.

## How?

### Prerequisites

To hack on this app yourself, you will need to have a recent [JVM][jvm] installed and the [Clojure][clojure] build tool [Leiningen][lein]. Figuring out how to install these on whatever platform you're using is entirely up to you! 

### Build and run

Once everything is installed, do the following to hack on the app in debug mode:

```
$ lein cljsbuild auto debug
```

Now open `index.html` in your favorite web browser.

To package the app for release, do the following:

```
$ lein cljsbuild once release
```

Now open `index.html` in your favorite web browser to check that everything worked. Note that `release` must be run **every time** you make changes to the code, whereas `debug` will automatically watch and recompile your code.

## Who?

This app was written by [Paul Burt][pb].

[apis.is]: http://docs.apis.is/#endpoint-tv
[clojure]: http://clojure.org/
[jvm]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[lein]: http://leiningen.org/
[pb]: https://twitter.com/pycurious
