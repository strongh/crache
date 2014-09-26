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

Then run `redis-cli monitor | grep -E ' "(G|S)ET" '`:
```
1411692039.884231 [0 127.0.0.1:50423] "SET" "key-prefix-for-fn(nil {:foo \"\xe5\", :bar 3, :eggs true})"
1411692039.906472 [0 127.0.0.1:50424] "GET" "key-prefix-for-fn(nil {:foo \"\xe5\", :bar 3, :eggs true})"
```
Awesome! :)

### Memoization client
-------
```clj
=> (require '[crache.memo :refer [memo-redis]])
```
```clj
; redis memoizes 'f', binding the client to localhost:6379 address of the redis server
=> (def memo-f (memo-redis f))
```
or
```clj
; redis memoizes 'f', binding the client to myapp.provider.com:6300 address of the redis server
=> (def memo-f (memo-redis f :host "myapp.provider.com" :port 6300))
```
then use `memo-f` like you would use your usual memoized fn:
```clj
=> (memo-f some-input) ;=> some-output
```

### License
-------
Copyright (C) 2014 Homer Strong

Distributed under the Eclipse Public License, the same as Clojure.
