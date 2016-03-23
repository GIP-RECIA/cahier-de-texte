
--Mise à jour de cahier_emploi
--ALTER TABLE cahier_courant.cahier_emploi ADD COLUMN date_fin_valide date;
--ALTER TABLE cahier_courant.cahier_emploi ADD COLUMN date_debut_valide date;

-- Mise à jour pour la fusion de cellule d'emploi du temps
ALTER TABLE cahier_courant.cahier_emploi ADD COLUMN ids_fusion text;
ALTER TABLE cahier_courant.cahier_emploi ADD COLUMN flag_fusion boolean;
ALTER TABLE cahier_courant.cahier_etab_enseignant ADD COLUMN date_dernier_enregistrement date;
