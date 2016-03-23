--  
-- Mise en place du modèle de données utilisé pour la gestion du carnet de bord
-- - Creation des tables :
--     cahier_cycle                : sequence pedagogique
--     cahier_cycle_groupe         : table d'association avec la table cahier_groupe
--     cahier_cycle_seance         : seance d'une sequence pedagogique
--     cahier_cycle_devoir         : devoir associe a une seance d'une sequence pedagogique
--     cahier_piece_jointe_cycle_seance : table d'association entre la seance d'une sequence
--                                        pedagogique et une piece jointe
--     cahier_piece_jointe_cycle_devoir : table d'association entre une devoir d'une seance 
--                                        de sequence pedagogique et une piece jointe
-- - Modification de la table suivante : 
--     cahier_groupe : ajout de la colonne groupe_collaboratif (o/n) pour identifier les groupes 
--                     collaboratif des groupes scolaires     
-- ----------------------------------------------------------------------------------------------
BEGIN;

DROP SEQUENCE IF EXISTS cahier_cyle cascade;
DROP SEQUENCE IF EXISTS cahier_cycle_seance cascade;
DROP SEQUENCE IF EXISTS cahier_cycle_devoir cascade;


drop table if exists cahier_courant.cahier_piece_jointe_cycle_seance;
drop table if exists cahier_courant.cahier_piece_jointe_cycle_devoir;
drop table if exists cahier_courant.cahier_cycle_groupe;
drop table if exists cahier_courant.cahier_cycle_devoir;
drop table if exists cahier_courant.cahier_cycle_seance;
drop table if exists cahier_courant.cahier_cycle;

create table cahier_courant.cahier_cycle (
  id integer NOT NULL, 
  id_enseignant integer not null,
  uid_enseignant character varying(8) NOT NULL,
  titre varchar(100) not null,
  objectif text,
  prerequis text, 
  description text,
  CONSTRAINT id_cahier_cycle PRIMARY KEY (id ),
  CONSTRAINT cahier_cycle_enseignant_id FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table cahier_courant.cahier_cycle_seance (
  id integer NOT NULL, 
  id_cycle integer not null,
  id_enseignant integer not null,
  uid_enseignant character varying(8) NOT NULL,
  enseignement varchar(100),
  objectif text,
  intitule varchar(50) not null,
  description text,
  annotations text,
  annotations_visible boolean,
  indice integer not null,
  CONSTRAINT id_cahier_cycle_seance PRIMARY KEY (id ),
  CONSTRAINT cahier_cycle_seance_enseignant_id FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_cycle_seance_cycle_id FOREIGN KEY (id_cycle)
      REFERENCES cahier_courant.cahier_cycle (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table cahier_courant.cahier_cycle_devoir (
  id integer NOT NULL, 
  id_cycle_seance integer not null,
  intitule varchar(50) not null,
  description text,
  id_type_devoir integer,
  date_remise integer not null,
  CONSTRAINT id_cahier_cycle_devoir PRIMARY KEY (id ),
  CONSTRAINT cahier_cycle_devoir_seance_id FOREIGN KEY (id_cycle_seance)
      REFERENCES cahier_courant.cahier_cycle_seance (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table cahier_courant.cahier_cycle_groupe (
  id_cycle integer NOT NULL, 
  id_groupe integer not null,
  CONSTRAINT id_cahier_cycle_groupe PRIMARY KEY (id_cycle, id_groupe),
  CONSTRAINT cahier_cycle_groupe_cycle_id FOREIGN KEY (id_cycle)
      REFERENCES cahier_courant.cahier_cycle (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_cycle_groupe_groupe_id FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table cahier_courant.cahier_piece_jointe_cycle_seance (
  id_piece_jointe integer NOT NULL, 
  id_cycle_seance integer not null,
  CONSTRAINT id_cahier_piece_jointe_cycle_seance PRIMARY KEY (id_piece_jointe, id_cycle_seance),
  CONSTRAINT cahier_piece_jointe_cycle_seance_seance_id FOREIGN KEY (id_cycle_seance)
      REFERENCES cahier_courant.cahier_cycle_seance (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_piece_jointe_cycle_seance_pj_id FOREIGN KEY (id_piece_jointe)
      REFERENCES cahier_courant.cahier_piece_jointe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table cahier_courant.cahier_piece_jointe_cycle_devoir (
  id_piece_jointe integer NOT NULL, 
  id_cycle_devoir integer not null,
  CONSTRAINT id_cahier_piece_jointe_cycle_devoir PRIMARY KEY (id_piece_jointe, id_cycle_devoir),
  CONSTRAINT cahier_piece_jointe_cycle_devoir_devoir_id FOREIGN KEY (id_cycle_devoir)
      REFERENCES cahier_courant.cahier_cycle_devoir (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_piece_jointe_cycle_devoir_pj_id FOREIGN KEY (id_piece_jointe)
      REFERENCES cahier_courant.cahier_piece_jointe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE cahier_courant.cahier_groupe ADD COLUMN groupe_collaboratif boolean DEFAULT false;
ALTER TABLE cahier_courant.cahier_groupe ADD COLUMN nom_long varchar(255) DEFAULT null;

CREATE SEQUENCE cahier_cycle MINVALUE 1;
CREATE SEQUENCE cahier_cycle_seance MINVALUE 1;
CREATE SEQUENCE cahier_cycle_devoir MINVALUE 1;


COMMIT;
