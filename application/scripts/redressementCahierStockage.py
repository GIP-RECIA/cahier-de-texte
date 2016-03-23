#!/usr/bin/python -d
# -*- coding: UTF-8 -*-

import sys
import ldap
from psycopg2 import connect
import psycopg2.extras

nomBDCahier = "ent-cahier-texte"
userCahier = "ent"
passwordCahier = "ent"
hostCahier = "FRMP0151.frml.bull.fr"
portCahier = "5432"

nomBDStockage = "ent-stockage"
userStockage = "ent"
passwordStockage = "ent"
hostStockage = "FRMP0151.frml.bull.fr"
portStockage = "5432"



try:
    connStock = connect("dbname="+nomBDStockage+" user="+userStockage+" password="+passwordStockage+" host="+hostStockage+" port="+portStockage)
    markStock = connStock.cursor()
    
    connCahier= connect("dbname="+nomBDCahier+" user="+userCahier+" password="+passwordCahier+" host="+hostCahier+" port="+portCahier)
    markCahier = connCahier.cursor()
    

    s = "SELECT U.uid,U.depotid FROM tusers as U INNER JOIN acontains as A ON (A.uid = U.uid) WHERE A.profilename='ENTAuxEnseignant' AND u.depotid IS NOT NULL "                    
    markStock.execute(s)
    
    record = markStock.fetchall() 
    for i in record: 
        uid = i[0]
        depot=i[1]
        s = "UPDATE cahier_courant.cahier_enseignant SET depot_stockage='depot_"+str(depot)+"' WHERE uid='"+uid+"'"
        markCahier.execute(s)


    print "fin de la transaction"

    connCahier.commit()

    print "Mise à jour effectués avec succès"

except StandardError, err:
    print "Un problème est apparu", err
    sys.exit(2)

markStock.close()
connStock.close()
markCahier.close()
connCahier.close()

