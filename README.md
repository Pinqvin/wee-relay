# wee-relay

A WeeChat mobile client utilizing the WeeChat Relay protocol.

## Development

To build the application:

```
lein prod-build
```

### iOS development

To start development you can run the application in iOS by running the following command

```bash
react-native run-ios
```

After the simulator is running, you can start figwheel with

```bash
lein run-figwheel ios
```

or by running the following function inside a REPL

```clojure
(start-figwheel "ios")
```

## License

Copyright © 2016 Juuso Tapaninen

Distributed under the MIT license.
