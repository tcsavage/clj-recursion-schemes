(ns clj-recursion-schemes.core
  (:require [clj-monad-trampoline.core :refer [done bind run-tramp]]))

(defmulti fmap
  "Slightly non-standard fmap using trampline.
  (a -> Tramp b) -> f a -> Tramp (f b)"
  {:arglists '([f fa])}
  (fn [f fa] (type fa)))

(defn cata*
  [alg fa]
  (bind (fmap (partial cata* alg) fa) alg))

(defn cata
  [alg fa]
  (run-tramp (cata* (comp done alg) fa)))

(defn ana*
  [coalg a]
  (fmap (partial ana* coalg) (coalg a)))

(defn ana
  [coalg a]
  (run-tramp (ana* (comp identity coalg) a)))

(defn hylo
  [alg coalg a]
  (cata alg (ana coalg a)))