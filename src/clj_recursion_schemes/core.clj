(ns clj-recursion-schemes.core
  (:require [clj-free.core :refer [done bind]]
            [clj-free.trampoline :refer [run-trampoline]]))

(defmulti fmap
  "Slightly non-standard fmap using trampline.
  (a -> Tramp b) -> f a -> Tramp (f b)"
  {:arglists '([f fa])}
  (fn [f fa] (type fa)))

(defn ^:no-doc cata*
  [alg fa]
  (bind (fmap (partial cata* alg) fa) alg))

(defn cata
  [alg fa]
  (run-trampoline (cata* (comp done alg) fa)))

(defn ^:no-doc ana*
  [coalg a]
  (fmap (partial ana* coalg) (coalg a)))

(defn ana
  [coalg a]
  (run-trampoline (ana* (comp identity coalg) a)))

(defn hylo
  [alg coalg a]
  (cata alg (ana coalg a)))

(defn ^:no-doc para*
  [alg fa]
  (bind (fmap (juxt (partial para* alg) identity) fa) alg))

(defn para
  [alg fa]
  (run-trampoline (para* (comp done alg) fa)))
