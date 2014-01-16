-- ----------------------------------------------------------------------------------------------
--  
-- Mise en place du modèle de données pour la gestion des remplacements
-- - Creation des tables :
--     cahier_remplcement          : designation des remplacements avec periode
-- 
-- ----------------------------------------------------------------------------------------------
BEGIN;

DROP SEQUENCE IF EXISTS cahier_remplacement cascade;

drop table if exists cahier_courant.cahier_remplacement;

create table cahier_courant.cahier_remplacement (
  id integer NOT NULL, 
  id_etablissement integer not null,
  id_enseignant_absent integer not null,
  id_enseignant_remplacant integer not null,
  date_debut timestamp not null,
  date_fin timestamp not null,
  CONSTRAINT id_cahier_remplacement PRIMARY KEY (id),
  CONSTRAINT cahier_remplacement_id_etablissement FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_remplacement_id_enseignant_absent FOREIGN KEY (id_enseignant_absent)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_remplacement_id_enseignant_remplacant FOREIGN KEY (id_enseignant_remplacant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE cahier_remplacement MINVALUE 1;

COMMIT;
