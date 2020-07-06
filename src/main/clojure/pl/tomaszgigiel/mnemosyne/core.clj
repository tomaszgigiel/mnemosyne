(ns pl.tomaszgigiel.mnemosyne.core
  (:require [clojure.data.csv :as csv])
  (:require [clojure.edn :as edn])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.tools.logging :as log])
  (:require [next.jdbc :as jdbc])
  (:require [pl.tomaszgigiel.mnemosyne.cmd :as cmd])
  (:require [pl.tomaszgigiel.mnemosyne.common :as common])
  (:import java.util.zip.ZipFile)
  (:gen-class))

(defn table-name-prepared [candidate]
  (let [a (str/replace candidate #"\.txt|\.csv" "")
        b (str/replace a #"-|\." "_")
        c (str/lower-case b)
        d (str "table_" c)]
    d))

(defn column-name-prepared [candidate]
  (str/lower-case candidate))

(defn ceil-prepared [candidate]
  (str "'" candidate "'"))

(defn sql-table-create [table-name columns]
  (let [columns-prepared (map #(column-name-prepared %) columns)
        columns-typed (map #(str % " nvarchar(999)") columns-prepared)
        column-listed (str/join ", " columns-typed)
        create-table (format "create table %s (id int identity primary key, %s)" table-name column-listed)]
    (log/info create-table)
    create-table))

(defn sql-insert [table-name columns row]
  (let [columns-prepared (map #(column-name-prepared %) columns)
        column-listed (str/join ", " columns-prepared)
        ceil-prepared (map #(ceil-prepared %) row)
        ceil-listed (str/join ", " ceil-prepared)
        insert (format "insert into %s (%s) values(%s)" table-name column-listed ceil-listed)]
    (log/debug insert)
    insert))

(defn create-table [connection in table-name]
  (with-open [reader (io/reader in)]
    (let [columns (first (csv/read-csv reader))
          sql-table-create (sql-table-create table-name columns)
          rows (rest (csv/read-csv reader))
          sql-inserts (map #(sql-insert table-name columns %) rows)]
      (jdbc/execute! connection [sql-table-create])
      (run! #(jdbc/execute! connection [%]) sql-inserts))))

(defn- work [path]
  (let [props (->> path slurp edn/read-string)
        db (:db props)
        ds (jdbc/get-datasource db)
        source (:source props)]
    (with-open [c (jdbc/get-connection ds)
                z (-> source io/file ZipFile.)]
      (doseq [entry (-> z .entries enumeration-seq)]
        (create-table c (.getInputStream z entry) (-> entry .getName table-name-prepared))))))

(defn -main [& args]
  "mnemosyne: database library"
    (let [{:keys [uri options exit-message ok?]} (cmd/validate-args args)]
      (if exit-message
        (cmd/exit (if ok? 0 1) exit-message)
        (log/info (work (first args))))
      (log/info "ok")
      (shutdown-agents)))
