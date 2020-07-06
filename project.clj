(defproject mnemosyne "1.0.0.0-SNAPSHOT"
  :description "mnemosyne: database library"
  :url "http://tomaszgigiel.pl"
  :license {:name "Apache License"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/tools.logging "0.5.0"]
                 [seancorfield/next.jdbc "1.1.547"]
                 [org.hsqldb/hsqldb "2.5.1"]
                 [org.clojure/data.csv "1.0.0"]]

  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure" "src/main/clojure"]
  :resource-paths ["src/main/resources"]
  :target-path "target/%s"
  :jar-name "mnemosyne.jar"
  :uberjar-name "mnemosyne-uberjar.jar"
  :main pl.tomaszgigiel.mnemosyne.core
  :aot [pl.tomaszgigiel.mnemosyne.core]
  :profiles {:test {:resource-paths ["src/test/resources"]}}
)
