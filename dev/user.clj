(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [clj-mogensen-scott.core :refer [defadt match-adt]]
            [clj-monad-trampoline.core :refer [done more bind run-tramp]]
            [clj-recursion-schemes.core :refer [fmap cata ana hylo]]))

(defadt ListF    ; * -> * -> *
  (cons* a la)   ; a -> r -> ListF a r
  (empty*))      ; ListF a r

(defmethod fmap ListF
  [f fa]
  (match-adt fa
             (cons* a as) (bind (more #(f as)) (comp done (partial cons* a)))
             (done fa)))

(defn length-alg
  [l]
  (match-adt l
             (cons* a as) (inc as)
             0))

(defn product-alg
  [l]
  (match-adt l
             (cons* a as) (* a as)
             1))

(defn range-coalg
  [n]
  (if (pos? n)
    (cons* n (dec n))
    empty*))

(def factorial
  (partial hylo product-alg range-coalg))
