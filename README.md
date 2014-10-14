crache
==========
Redis backed Caching and Memoization, following `core.cache` and `core.memoize`.

### Installing
-------
Add the following to the `:dependencies` vector of your `project.clj` file:
[![clojars version][1]][2]

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

[Redis client connection specification][3]:

```clj
(def conn {:pool {} :spec {:host "localhost" :port 6379}})
```

```clj
; redis memoizes 'f', using a key-prefix
=> (def memo-f (memo-redis f conn (str ::f)))
```

or

```clj
; redis memoizes 'f', using a key-prefix and an expire time (60 seconds)
=> (def memo-f (memo-redis f conn (str ::f) 60))
```

then use `memo-f` like you would use your usual memoized fn:

```clj
=> (memo-f some-input) ;=> some-output
```

### License
-------
Copyright (C) 2014 Homer Strong

Distributed under the Eclipse Public License, the same as Clojure.

[1]: https://clojars.org/crache/latest-version.svg?raw=true
[2]: https://clojars.org/crache
[3]: https://github.com/ptaoussanis/carmine#connections
