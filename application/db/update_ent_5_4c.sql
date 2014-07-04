--  
-- update_ent_5_1_7c doit être appliqué 
--
-- Modification du modèle de données utilisé pour la gestion du carnet de bord :
-- 	Adaptation aux groupes collaboratifs locaux. 
-- 
-- - Modification de la table suivante : 
--     cahier_groupe : ajout de la colonne groupe_collaboratif_local (o/n) pour distinguer les groupes 
--                     collaboratif externe des groupes collaboratifs locaux.     
-- ----------------------------------------------------------------------------------------------
BEGIN;

ALTER TABLE cahier_courant.cahier_groupe 
	ADD COLUMN groupe_collaboratif_local boolean DEFAULT false,
	add constraint is_collaboratif_local check (groupe_collaboratif = true or groupe_collaboratif_local = false or groupe_collaboratif_local is null);

COMMIT;
