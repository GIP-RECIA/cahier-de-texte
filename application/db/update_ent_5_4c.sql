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

begin;
drop table if exists  cahier_courant.cahier_preferences_etab;

-- Table: cahier_preferences_etab
CREATE TABLE cahier_courant.cahier_preferences_etab
(
	 id_etablissement integer NOT NULL,
  preferences character varying(200),
  CONSTRAINT pk_pref_etab PRIMARY KEY (id_etablissement)
);

commit ;


BEGIN;

ALTER TABLE cahier_courant.cahier_groupe 
	ADD COLUMN groupe_collaboratif_local boolean DEFAULT false,
	add constraint is_collaboratif_local check (groupe_collaboratif = true or groupe_collaboratif_local = false or groupe_collaboratif_local is null);

COMMIT;


-- pour chaque schema archivé faire la meme modif:

BEGIN;

ALTER TABLE  "cahier_archive_2012-2013".cahier_groupe 
	ADD COLUMN groupe_collaboratif boolean DEFAULT false,	
	ADD COLUMN groupe_collaboratif_local boolean DEFAULT false,
	add constraint is_collaboratif_local check (groupe_collaboratif = true or groupe_collaboratif_local = false or groupe_collaboratif_local is null);

COMMIT;


BEGIN;

ALTER TABLE  "cahier_archive_2011-2012".cahier_groupe 
	ADD COLUMN groupe_collaboratif boolean DEFAULT false,	
	ADD COLUMN groupe_collaboratif_local boolean DEFAULT false,
	add constraint is_collaboratif_local check (groupe_collaboratif = true or groupe_collaboratif_local = false or groupe_collaboratif_local is null);

commit;

