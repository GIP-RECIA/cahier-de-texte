WITH sp as (
select 
emp.couleur_case,
lib.libelle as LibEtab,
mat.designation as Lib,
emp.code_salle,
emp.description as EmpDesc,
sea.id as SeanceId, 
emp.id as EmploiId, 
p.date_debut as PeriodeDate,
c.designation as ClassDesc,  
g.designation as GroupDesc,
emp.type_semaine 
FROM
cahier_courant.cahier_seance sea 
INNER JOIN
cahier_courant.cahier_enseignant ens ON ens.id = sea.id_enseignant
INNER JOIN
cahier_courant.cahier_emploi emp ON emp.id_enseignant = ens.id
INNER JOIN 
cahier_courant.cahier_periode_emploi p ON p.id = emp.id_periode_emploi
INNER JOIN 
cahier_courant.cahier_sequence seq ON seq.id = sea.id_sequence
INNER JOIN
cahier_courant.cahier_enseignement mat ON mat.id = emp.id_enseignement
LEFT JOIN
cahier_courant.cahier_groupe g ON g.id = seq.id_groupe
LEFT JOIN
cahier_courant.cahier_classe c ON c.id = seq.id_classe
LEFT JOIN
cahier_courant.cahier_libelle_enseignement lib ON lib.id_enseignement = emp.id_enseignement
WHERE 
emp.heure_debut = sea.heure_debut AND
emp.heure_fin = sea.heure_fin AND
emp.minute_debut = sea.minute_debut AND
emp.minute_fin = sea.minute_fin AND
emp.couleur_case IS NOT NULL AND
seq.id_etablissement = p.id_etablissement AND
(lib.id_etablissement IS NULL OR lib.id_etablissement = seq.id_etablissement) AND 
sea.date >= p.date_debut AND
 (seq.id_classe = emp.id_classe OR seq.id_groupe = emp.id_groupe) 
 AND sea.id = ?
AND emp.type_semaine = ?
AND emp.jour = ?
) 
select * from sp sp_o
WHERE sp_o.periodedate = (select max(sp_i.periodedate) 
from sp sp_i where sp_i.type_semaine = sp_o.type_semaine and sp_i.seanceid = sp_o.seanceid group by seanceid, type_semaine)


