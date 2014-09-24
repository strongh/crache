crache
==========
Redis backed Caching and Memoization, following `core.cache` and `core.memoize`.

### Installing
-------
Add the following to the `:dependencies` vector of your `project.clj` file:
[![clojars version](https://clojars.org/crache/latest-version.svg?raw=true)]
(https://clojars.org/crache)

## Usage
-------
Start your `redis-server` and connect a crache client to it. 
Then check your `redis-server` logs:
```
[66942] 23 Sep 03:26:02.328 # Server started, Redis version 2.8.6
[66942] 23 Sep 03:26:02.330 * The server is now ready to accept connections on port 6379
[66942] 23 Sep 03:27:43.111 * 1 changes in 60 seconds. Saving...
[66942] 23 Sep 03:27:43.158 * Background saving started by pid 66972
[66972] 23 Sep 03:27:43.255 * DB saved on disk
```
Awesome! :)

### Memoization client
-------
```clj
(require '[crache.memo :refer [memo-redis]]) ; the ns
```
```clj
; redis memoizes 'f', binding the client to localhost:6379
=> (def memo-f (memo-redis f))
```
or
```clj
; redis memoizes 'f', binding the client to myapp.provider.com:6300
=> (def memo-f (memo-redis f :host "myapp.provider.com" :port 6300))
```
then use `memo-f` like you would use your usual memoized fn:
```clj
(memo-f some-input) ;=> some-output
```

### License
-------
Copyright (C) 2014 Homer Strong

Distributed under the Eclipse Public License, the same as Clojure.
