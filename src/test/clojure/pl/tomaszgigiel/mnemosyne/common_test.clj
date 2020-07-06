(ns pl.tomaszgigiel.mnemosyne.common-test
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.mnemosyne.common :as common])
  (:require [pl.tomaszgigiel.mnemosyne.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)
