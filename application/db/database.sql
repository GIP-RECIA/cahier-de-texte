DROP SCHEMA IF EXISTS cahier_courant CASCADE;

CREATE SCHEMA cahier_courant;

 
drop table if exists cahier_courant.cahier_couleur_enseignement_classe;


DROP SEQUENCE IF EXISTS cahier_couleur_enseignement cascade;


DROP TABLE IF EXISTS cahier_courant.cahier_annee_scolaire CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_classe CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_type_devoir CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_enseignant CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_eleve CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_etablissement CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_sequence CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_seance CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_groupe CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_devoir CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_piece_jointe CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_enseignement CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_eleve_groupe CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_eleve_classe CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_etab_eleve CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_etab_enseignant CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_enseignant_enseignement CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_groupe_classe CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_enseignant_groupe CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_enseignant_classe CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_piece_jointe_seance CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_piece_jointe_devoir CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_inspecteur CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_ouverture_inspecteur CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_emploi CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_periode_emploi CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_libelle_enseignement CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_preferences_utilisateur CASCADE;

DROP TABLE IF EXISTS cahier_courant.cahier_archive_piece_jointe_devoir  CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_archive_piece_jointe_seance  CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_visa_seance  CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_archive_devoir  CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_archive_seance  CASCADE;
DROP TABLE IF EXISTS cahier_courant.cahier_visa  CASCADE;

DROP SEQUENCE IF EXISTS cahier_cyle cascade;
DROP SEQUENCE IF EXISTS cahier_cycle_seance cascade;
DROP SEQUENCE IF EXISTS cahier_cycle_devoir cascade;

drop table if exists cahier_courant.cahier_piece_jointe_cycle_seance;
drop table if exists cahier_courant.cahier_piece_jointe_cycle_devoir;
drop table if exists cahier_courant.cahier_cycle_groupe;
drop table if exists cahier_courant.cahier_cycle_devoir;
drop table if exists cahier_courant.cahier_cycle_seance;
drop table if exists cahier_courant.cahier_cycle;


-- TABLES ----------------------------------------------------------------------

-- Table: cahier_annee_scolaire


CREATE TABLE cahier_courant.cahier_annee_scolaire
(
  id Integer NOT NULL,
  exercice character varying(50),
  date_rentree date NOT NULL,
  date_sortie date NOT NULL,
  periode_vacance text,
  ouverture_ent Boolean,
  periode_scolaire text,
  CONSTRAINT cahier_annee_scolaire_id PRIMARY KEY (id)
);



-- Table: cahier_courant.cahier_couleur_enseignement_classe


CREATE TABLE cahier_courant.cahier_couleur_enseignement_classe
(
  id integer NOT NULL, 
  id_etablissement integer not null,
  id_enseignant integer not null,
  id_enseignement integer not null,
  id_classe integer, 
  id_groupe integer,
  couleur varchar(10),
  CONSTRAINT id_couleur_enseignement_classe PRIMARY KEY (id ),
  constraint unique_couleur_enseignement_classe UNIQUE (id_etablissement, id_enseignant, id_enseignement, id_classe),
  constraint unique_couleur_enseignement_groupe unique (id_etablissement, id_enseignant, id_enseignement, id_groupe),
  CONSTRAINT cahier_couleur_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_couleur_enseignement FOREIGN KEY (id_enseignement)
      REFERENCES cahier_courant.cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_couleur_classe FOREIGN KEY (id_classe)
      REFERENCES cahier_courant.cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_couleur_groupe FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE cahier_couleur_enseignement MINVALUE 1;


-- Table: cahier_etablissement


CREATE TABLE cahier_courant.cahier_etablissement
(
  id Integer NOT NULL,
  code character varying(20) NOT NULL,
  designation character varying(100) NOT NULL,
  horaire_cours text,
  alternance_semaine text,
  jours_ouvres text,
  duree_cours integer,
  heure_debut integer,
  heure_fin integer,
  minute_debut integer,
  ouverture Boolean,
  import Boolean NOT NULL DEFAULT false,
  date_import character varying(15),
  fractionnement integer NOT NULL DEFAULT 1,
  CONSTRAINT id_cahier_etablissement PRIMARY KEY (id)
);
COMMENT ON COLUMN cahier_courant.cahier_etablissement.fractionnement IS 'Rapport de fractionnement des plage dans la saisie emploi du temps. 1:pas de fractionnement, 2:demie plage, 4:quart de plage';


-- Table: cahier_sequence



-- Table: cahier_classe



CREATE TABLE cahier_courant.cahier_classe
(
  id Integer NOT NULL,
  code character varying(20) NOT NULL,
  designation character varying(50) NOT NULL,
  id_etablissement Integer NOT NULL,
  id_annee_scolaire integer NOT NULL,
  CONSTRAINT cahier_classe_id PRIMARY KEY (id),
  CONSTRAINT id_etablissement_classes FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_annee_scolaire FOREIGN KEY (id_annee_scolaire)
      REFERENCES cahier_courant.cahier_annee_scolaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--DROP INDEX IF EXISTS fki_id_annee_scolaire_classe;

CREATE INDEX fki_id_annee_scolaire_classe
  ON cahier_courant.cahier_classe
  USING btree
  (id_annee_scolaire);
  
 CREATE INDEX classe_designation
 ON cahier_courant.cahier_classe
 USING btree
 (designation);



-- Table: cahier_type_devoir



CREATE TABLE cahier_courant.cahier_type_devoir
(
  id Integer NOT NULL,
  code character varying(10) NOT NULL,
  libelle character varying(20) NOT NULL,
  id_etablissement Integer NOT NULL,
  categorie varchar(15) NOT NULL DEFAULT 'NORMAL',
  CONSTRAINT cahier_type_devoir_id PRIMARY KEY (id),
  CONSTRAINT id_etablissement_type_devoir FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON COLUMN cahier_courant.cahier_type_devoir.categorie IS 'Categorie du type de devoir. NORMAL ou DEVOIR' ;


-- Table: cahier_enseignant



CREATE TABLE cahier_courant.cahier_enseignant
(
  id Integer NOT NULL,
  nom character varying(50) NOT NULL,
  prenom character varying(50) NOT NULL,
  uid character varying(8) NOT NULL,
  civilite character varying(10),
  depot_stockage character varying(50),
  CONSTRAINT id_cahier_enseignant PRIMARY KEY (id)
);


-- Table: cahier_eleve



CREATE TABLE cahier_courant.cahier_eleve
(
  id Integer NOT NULL,
  nom character varying(50) NOT NULL,
  prenom character varying(50) NOT NULL,
  uid character varying(50) NOT NULL,
  CONSTRAINT id_cahier_eleve PRIMARY KEY (id)
);



-- Index: fki_cahier_enseignant_id


  


-- Table: cahier_groupe
CREATE TABLE cahier_courant.cahier_groupe
(
  id Integer NOT NULL,
  code character varying(20) NOT NULL,
  designation character varying(255) NOT NULL,
  id_etablissement Integer NOT NULL,
  id_annee_scolaire integer NOT NULL,
  groupe_collaboratif boolean DEFAULT false,
  groupe_collaboratif_local boolean DEFAULT false,
	
  
  nom_long varchar(255) DEFAULT null,
  CONSTRAINT cahier_groupe_id PRIMARY KEY (id),
  CONSTRAINT id_etablissement_groupes FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_annee_scolaire FOREIGN KEY (id_annee_scolaire)
      REFERENCES cahier_courant.cahier_annee_scolaire (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  constraint is_collaboratif_local check (groupe_collaboratif = true or groupe_collaboratif_local = false or groupe_collaboratif_local is null)
);
-- Index: fki_id_annee_scolaire

--DROP INDEX IF EXISTS cahier_courant.fki_id_annee_scolaire;

CREATE INDEX fki_id_annee_scolaire
  ON cahier_courant.cahier_groupe
  USING btree
  (id_annee_scolaire);
  
  CREATE INDEX groupe_designation
  ON cahier_courant.cahier_groupe
  USING btree
  (designation);
  


  

  
  
-- Table: cahier_piece_jointe


CREATE TABLE cahier_courant.cahier_piece_jointe
(
  id Integer NOT NULL,
  code character varying(10) NOT NULL,
  nom_fichier character varying(255) NOT NULL,
  uid character varying(8) NOT NULL,
  chemin character varying(250) NOT NULL,
  CONSTRAINT cahier_piece_jointe_id PRIMARY KEY (id)
);

ALTER TABLE cahier_courant.cahier_piece_jointe ALTER COLUMN chemin SET STORAGE EXTENDED;
ALTER TABLE cahier_courant.cahier_piece_jointe ALTER COLUMN chemin SET DEFAULT '/Mes Documents'::character varying;
-- Index: fki_id_devoir
--DROP INDEX IF EXISTS  cahier_courant.fki_id_devoir;
--DROP INDEX IF EXISTS  cahier_courant.fki_id_seance;




-- Table: cahier_enseignement

CREATE TABLE cahier_courant.cahier_enseignement
(
  id Integer NOT NULL,
  code character varying(10),
  designation character varying(50) NOT NULL,
  CONSTRAINT cahier_enseignement_id PRIMARY KEY (id)
);
-- Index: fki_id_classe
--DROP INDEX IF EXISTS  cahier_courant.fki_id_classe;
-- Index: fki_id_groupe
--DROP INDEX IF EXISTS  cahier_courant.fki_id_groupe;
  

CREATE TABLE cahier_courant.cahier_sequence
(
  id Integer NOT NULL,
  code character varying(10) NOT NULL,
  intitule character varying(50) NOT NULL,
  description text,
  date_debut date NOT NULL,
  date_fin date NOT NULL,
  id_enseignant integer,
  id_etablissement  integer NOT NULL,
  id_enseignement  integer NOT NULL,
  id_groupe  integer,
  id_classe  integer,
  CONSTRAINT id_cahier_sequence PRIMARY KEY (id),
  CONSTRAINT cahier_enseignant_id FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_etablissement_cahier_sequence FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignement_cahier_sequence FOREIGN KEY (id_enseignement)
      REFERENCES cahier_courant.cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_groupe_cahier_sequence FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_classe_cahier_sequence FOREIGN KEY (id_classe)
      REFERENCES cahier_courant.cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON COLUMN cahier_courant.cahier_sequence.id_etablissement IS 'id de l etablissement' ;
COMMENT ON COLUMN cahier_courant.cahier_sequence.id_enseignement IS 'id de l enseignement' ;
COMMENT ON COLUMN cahier_courant.cahier_sequence.id_groupe IS 'id du groupe (exclusif avec id_classe)' ;
COMMENT ON COLUMN cahier_courant.cahier_sequence.id_classe IS 'id de la classe (exclusif avec id_groupe)' ;

--DROP INDEX IF EXISTS  cahier_courant.fki_cahier_enseignant_id;
CREATE INDEX fki_cahier_enseignant_id
  ON cahier_courant.cahier_sequence
  USING btree
  (id_enseignant);

 --DROP INDEX IF EXISTS fki_id_ens_seq_groupe;
CREATE INDEX fki_id_ens_seq_groupe
  ON cahier_courant.cahier_sequence
  USING btree
  (id_groupe);

--DROP INDEX IF EXISTS fki_id_ens_seq_classe;
CREATE INDEX fki_id_ens_seq_classe
  ON cahier_courant.cahier_sequence
  USING btree
  (id_classe);

CREATE INDEX fki_id_seq_etablissement
  ON cahier_courant.cahier_sequence
  USING btree
  (id_etablissement);
  
 

-- Table: cahier_seance

CREATE TABLE cahier_courant.cahier_seance
(
  id Integer NOT NULL,
  code character varying(10) NOT NULL,
  intitule character varying(50),
  date date,
  heure_debut integer,
  heure_fin integer,
  minute_debut integer,
  minute_fin integer,
  description text,
  id_sequence integer NOT NULL,
  id_enseignant integer NOT NULL,
  annotations text DEFAULT null,
  date_maj timestamp,
  CONSTRAINT cahier_seance_id PRIMARY KEY (id),
  CONSTRAINT cahier_sequence_id FOREIGN KEY (id_sequence)
      REFERENCES cahier_courant.cahier_sequence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON COLUMN cahier_courant.cahier_seance.annotations IS 'Annotations saisies par l enseignant et visibles uniquement par lui';

-- Table: cahier_devoir


CREATE TABLE cahier_courant.cahier_devoir
(
  id Integer NOT NULL,
  date_remise date NOT NULL,
  code character varying(10) NOT NULL,
  intitule character varying(50),
  description text,
  id_type_devoir integer ,
  id_seance integer NOT NULL,
  CONSTRAINT cahier_devoir_id PRIMARY KEY (id),
  CONSTRAINT cahier_seance_id FOREIGN KEY (id_seance)
      REFERENCES cahier_courant.cahier_seance (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT clef_etrangere_id_type_devoir FOREIGN KEY (id_type_devoir)
      REFERENCES cahier_courant.cahier_type_devoir (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
-- Index: fki_cahier_seance_id

--DROP INDEX IF EXISTS  cahier_courant.fki_cahier_seance_id;
CREATE INDEX fki_cahier_seance_id
  ON cahier_courant.cahier_devoir
  USING btree
  (id_seance);

-- Index: fki_clef_etrangere_id_type_devoir
--DROP INDEX IF EXISTS  cahier_courant.fki_clef_etrangere_id_type_devoir;
CREATE INDEX fki_clef_etrangere_id_type_devoir
  ON cahier_courant.cahier_devoir
  USING btree
  (id_type_devoir);


-- cahier_visa  
-- un visa sur un cahier de texte (enseignant, classe, enseignement)
-- -----------------------------------------------------------------
CREATE TABLE cahier_courant.cahier_visa
(
  id integer NOT NULL, 
  date_visa timestamp,
  profil varchar(20) not null,
  type_visa varchar(10),
  id_etablissement integer not null,
  id_enseignant integer not null,
  id_enseignement integer not null,
  id_classe integer, 
  id_groupe integer,
  date_dernier_maj timestamp,
  CONSTRAINT id_cahier_visa PRIMARY KEY (id ),
  CONSTRAINT cahier_visa_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_visa_enseignement FOREIGN KEY (id_enseignement)
      REFERENCES cahier_courant.cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_visa_classe FOREIGN KEY (id_classe)
      REFERENCES cahier_courant.cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_visa_groupe FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON COLUMN cahier_courant.cahier_visa.id IS 'Identifiant du visa' ;
COMMENT ON COLUMN cahier_courant.cahier_visa.date_visa IS 'Date du visa';
COMMENT ON COLUMN cahier_courant.cahier_visa.profil IS 'Profil de la personne ayant appose le visa. Correspond au object class du LDAP ' ;
COMMENT ON COLUMN cahier_courant.cahier_visa.type_visa IS 'Type du visa : MEMO ou SIMPLE' ;
COMMENT ON COLUMN cahier_courant.cahier_visa.id_etablissement IS 'id de l etablissement' ;
COMMENT ON COLUMN cahier_courant.cahier_visa.id_enseignant IS 'id de l enseignant' ;
COMMENT ON COLUMN cahier_courant.cahier_visa.id_enseignement IS 'id de l enseignement ' ;
COMMENT ON COLUMN cahier_courant.cahier_visa.id_classe IS 'id de la classe ' ;
COMMENT ON COLUMN cahier_courant.cahier_visa.id_groupe IS 'id du groupe' ;



-- Index: fki_cahier_sequence_id

--DROP INDEX IF EXISTS cahier_courant.fki_cahier_sequence_id;

CREATE INDEX fki_cahier_sequence_id
  ON cahier_courant.cahier_seance
  USING btree
  (id_sequence);


-- Table: cahier_eleve_groupe


CREATE TABLE cahier_courant.cahier_eleve_groupe
(
  id_eleve integer NOT NULL,
  id_groupe integer NOT NULL,
  CONSTRAINT id_cahier_eleve_groupe PRIMARY KEY (id_eleve, id_groupe),
  CONSTRAINT id_eleve FOREIGN KEY (id_eleve)
      REFERENCES cahier_courant.cahier_eleve (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_groupe FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



-- Table: cahier_eleve_classe


CREATE TABLE cahier_courant.cahier_eleve_classe
(
  id_classe integer NOT NULL,
  id_eleve integer NOT NULL,
  CONSTRAINT id_cahier_eleve_classe PRIMARY KEY (id_classe, id_eleve),
  CONSTRAINT id_classe FOREIGN KEY (id_classe)
      REFERENCES cahier_courant.cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_eleve FOREIGN KEY (id_eleve)
      REFERENCES cahier_courant.cahier_eleve (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: cahier_etab_eleve


CREATE TABLE cahier_courant.cahier_etab_eleve
(
  id_eleve integer NOT NULL,
  id_etablissement integer NOT NULL,
  CONSTRAINT id_cahier_etab_eleve PRIMARY KEY (id_eleve, id_etablissement),
  CONSTRAINT fk6b0b3b311c6c3660 FOREIGN KEY (id_eleve)
      REFERENCES cahier_courant.cahier_eleve (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk6b0b3b31ad078fb2 FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



-- Table: cahier_etab_enseignant


CREATE TABLE cahier_courant.cahier_etab_enseignant
(
  id_enseignant integer NOT NULL,
  id_etablissement integer NOT NULL,
  saisie_simplifiee boolean,
  CONSTRAINT id_cahier_etab_enseignant PRIMARY KEY (id_enseignant, id_etablissement),
  CONSTRAINT id_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_etablissement FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
-- Index: fki_id_etablissement
--DROP INDEX IF EXISTS  cahier_courant.fki_id_etablissement;
CREATE INDEX fki_id_etablissement
  ON cahier_courant.cahier_etab_enseignant
  USING btree
  (id_etablissement);
  

-- Table: cahier_enseignant_enseignement
CREATE TABLE cahier_courant.cahier_enseignant_enseignement
(
  id_enseignement integer NOT NULL,
  id_enseignant integer NOT NULL,
  id_etablissement integer NOT NULL,
  CONSTRAINT id_enseignant_enseignement PRIMARY KEY (id_enseignement, id_enseignant, id_etablissement),
  CONSTRAINT id_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignement FOREIGN KEY (id_enseignement)
      REFERENCES cahier_courant.cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_etablissement FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION    
);
-- Index: fki_id_enseignant
--DROP INDEX IF EXISTS  cahier_courant.fki_id_enseignant;
CREATE INDEX fki_id_enseignant
  ON cahier_courant.cahier_enseignant_enseignement
  USING btree
  (id_enseignant);
  

  
-- Table: cahier_groupe_classe
CREATE TABLE cahier_courant.cahier_groupe_classe
(
  id_groupe integer NOT NULL,
  id_classe integer NOT NULL,
  CONSTRAINT id_cahier_groupe_classe PRIMARY KEY (id_groupe, id_classe),
  CONSTRAINT id_classe FOREIGN KEY (id_classe)
      REFERENCES cahier_courant.cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_groupe FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
      
--MISE A JOUR MODELE      
      
CREATE TABLE cahier_courant.cahier_enseignant_groupe
(
  id_enseignant integer NOT NULL,
  id_groupe integer NOT NULL,
  CONSTRAINT id_cahier_enseignant_groupe PRIMARY KEY (id_enseignant, id_groupe),
  CONSTRAINT id_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_groupe FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);



CREATE TABLE cahier_courant.cahier_enseignant_classe
(
  id_enseignant integer NOT NULL,
  id_classe integer NOT NULL,
  CONSTRAINT id_cahier_enseignant_classe PRIMARY KEY (id_enseignant, id_classe),
  CONSTRAINT id_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_classe FOREIGN KEY (id_classe)
      REFERENCES cahier_courant.cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE cahier_courant.cahier_piece_jointe_seance
(
  id_piece_jointe integer NOT NULL,
  id_seance integer NOT NULL,
  CONSTRAINT id_cahier_piece_jointe_seance PRIMARY KEY (id_piece_jointe, id_seance),
  CONSTRAINT id_piece_jointe FOREIGN KEY (id_piece_jointe)
      REFERENCES cahier_courant.cahier_piece_jointe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_seance FOREIGN KEY (id_seance)
      REFERENCES cahier_courant.cahier_seance (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE cahier_courant.cahier_piece_jointe_devoir
(
  id_piece_jointe integer NOT NULL,
  id_devoir integer NOT NULL,
  CONSTRAINT id_cahier_piece_jointe_devoir PRIMARY KEY (id_piece_jointe, id_devoir),
  CONSTRAINT id_piece_jointe FOREIGN KEY (id_piece_jointe)
      REFERENCES cahier_courant.cahier_piece_jointe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_devoir FOREIGN KEY (id_devoir)
      REFERENCES cahier_courant.cahier_devoir (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: cahier_libelle_enseignement

CREATE TABLE cahier_courant.cahier_libelle_enseignement
(
  id_enseignement integer NOT NULL,
  id_etablissement integer NOT NULL,
  libelle character varying(8) NOT NULL,
  CONSTRAINT id_libelle_enseignement_etab_enseig PRIMARY KEY (id_enseignement, id_etablissement),
  CONSTRAINT id_etablissement FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignement FOREIGN KEY (id_enseignement)
      REFERENCES cahier_courant.cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Creation de la table periode_emploi
-- -----------------------------------
CREATE TABLE cahier_courant.cahier_periode_emploi
(
  id integer NOT NULL, 
  id_etablissement integer NOT NULL,
  id_enseignant integer NOT NULL,
  date_debut date NOT NULL,
  CONSTRAINT id_cahier_periode_emploi PRIMARY KEY (id),
  CONSTRAINT id_etablissement_periode_emploi FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON COLUMN cahier_courant.cahier_periode_emploi.id IS 'Identifiant de la periode emploi du temps pour l etablissement' ;
COMMENT ON COLUMN cahier_courant.cahier_periode_emploi.id_etablissement IS 'id de l etablissement' ;
COMMENT ON COLUMN cahier_courant.cahier_periode_emploi.id_enseignant IS 'id de l enseignant' ;
COMMENT ON COLUMN cahier_courant.cahier_periode_emploi.date_debut IS 'Date de debut de la periode. La date de fin de la periode est la date de debut de periode suivante moins un jour';



-- Table: cahier_emploi
CREATE TABLE cahier_courant.cahier_emploi
(
  id integer NOT NULL,
  jour character varying(10) NOT NULL,
  type_semaine character(1) NOT NULL,
  heure_debut integer NOT NULL,
  heure_fin integer NOT NULL,
  minute_debut integer NOT NULL,
  minute_fin integer NOT NULL,
  id_enseignant integer NOT NULL,
  id_classe integer,
  id_groupe integer,
  id_enseignement integer NOT NULL,
  id_etablissement integer NOT NULL,
  description character varying(50),
  couleur_case character varying(10),
  code_salle character varying(15),
  id_periode_emploi integer NOT NULL,
  --date_fin_valide date,
  --date_debut_valide date,
  CONSTRAINT pimaire_id_emploi PRIMARY KEY (id),
  CONSTRAINT id_classe_emploi FOREIGN KEY (id_classe)
      REFERENCES cahier_courant.cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignant_emploi FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignement_emploi FOREIGN KEY (id_enseignement)
      REFERENCES cahier_courant.cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_groupe_emploi FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_periode_emploi_emploi FOREIGN KEY (id_periode_emploi)
      REFERENCES cahier_courant.cahier_periode_emploi (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: cahier_preferences_utilisateur
CREATE TABLE cahier_courant.cahier_preferences_utilisateur
(
  uid character varying(8) NOT NULL,
  preferences character varying(200),
  CONSTRAINT pk_pref PRIMARY KEY (uid)
);

--Création de la table inspecteur
CREATE TABLE cahier_courant.cahier_inspecteur
(
  id Integer NOT NULL,
  nom character varying(50) NOT NULL,
  prenom character varying(50) NOT NULL,
  uid character varying(8) NOT NULL,
  civilite character varying(10),
  CONSTRAINT id_cahier_inspecteur PRIMARY KEY (id)
);


--Création de la table ouverture inspecteur
CREATE TABLE cahier_courant.cahier_ouverture_inspecteur
(
  id_etablissement integer NOT NULL,
  id_enseignant integer NOT NULL,
  id_inspecteur integer NOT NULL,
  date_debut date NOT NULL,
  nomDirecteur character varying(100) NOT NULL,
  uidDirecteur character varying(8) NOT NULL,
  date_fin date,
  CONSTRAINT id_ouverture_inspecteur PRIMARY KEY (id_etablissement, id_enseignant, id_inspecteur),
  CONSTRAINT id_etablissement FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_inspecteur FOREIGN KEY (id_inspecteur)
      REFERENCES cahier_courant.cahier_inspecteur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION            
);


-- cahier_archive_seance              
-- une seance archive correspondant a un visa memo
CREATE TABLE cahier_courant.cahier_archive_seance
(
  id_archive_seance integer NOT NULL, 
  date_archive timestamp,
  id_visa integer NOT NULL,
  id_seance integer NOT NULL,
  code character varying(10) NOT NULL,
  intitule character varying(50),
  date date,
  heure_debut integer,
  heure_fin integer,
  minute_debut integer,
  minute_fin integer,
  description text,
  id_sequence integer NOT NULL,
  id_enseignant integer NOT NULL,
  annotations text,
  CONSTRAINT cahier_archive_seance_id PRIMARY KEY (id_archive_seance),
  CONSTRAINT cahier_visa_id FOREIGN KEY (id_visa)
      REFERENCES cahier_courant.cahier_visa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_seance_id FOREIGN KEY (id_seance)
      REFERENCES cahier_courant.cahier_seance (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION, 
  CONSTRAINT cahier_sequence_id FOREIGN KEY (id_sequence)
      REFERENCES cahier_courant.cahier_sequence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unique_archive_seance_visa UNIQUE(id_visa, id_seance)
);

COMMENT ON COLUMN cahier_courant.cahier_archive_seance.id_archive_seance IS 'Identifiant d''archive de la séance' ;
COMMENT ON COLUMN cahier_courant.cahier_archive_seance.id_seance IS 'Identifiant du séance orignale' ;


-- cahier_visa_seance                 
-- table d'assiciation : un visa sur une seance
-- --------------------------------------------
CREATE TABLE cahier_courant.cahier_visa_seance
(
  id_visa integer NOT NULL, 
  id_seance integer NOT NULL,
  CONSTRAINT id_cahier_visa_seance PRIMARY KEY (id_visa, id_seance),
  CONSTRAINT cahier_visa_seance_seance FOREIGN KEY (id_seance)
      REFERENCES cahier_courant.cahier_seance (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_visa_seance_visa FOREIGN KEY (id_visa)
      REFERENCES cahier_courant.cahier_visa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON COLUMN cahier_courant.cahier_visa_seance.id_visa   IS 'Identifiant du visa' ;
COMMENT ON COLUMN cahier_courant.cahier_visa_seance.id_seance IS 'id de la seance qui a ete visee' ;


-- cahier_archive_devoir              
-- un devoir archive correspondant a un visa memo
-- -----------------------------------------------
CREATE TABLE cahier_courant.cahier_archive_devoir
(
  id_archive_devoir integer NOT NULL,  
  date_remise date NOT NULL,
  code character varying(10) NOT NULL,
  intitule character varying(50),
  description text,
  id_type_devoir integer,
  id_archive_seance integer NOT NULL,
  CONSTRAINT cahier_archive_devoir_id PRIMARY KEY (id_archive_devoir ),
  CONSTRAINT cahier_archive_seance_id FOREIGN KEY (id_archive_seance)
      REFERENCES cahier_courant.cahier_archive_seance (id_archive_seance) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT clef_etrangere_id_type_devoir FOREIGN KEY (id_type_devoir)
      REFERENCES cahier_courant.cahier_type_devoir (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION      
);

-- cahier_archive_piece_jointe_seance 
-- association entre une seance archive et une piece jointe
-- ----------------------------------------------------------
CREATE TABLE cahier_courant.cahier_archive_piece_jointe_seance
(
  id_piece_jointe integer NOT NULL,
  id_archive_seance integer NOT NULL,
  CONSTRAINT id_cahier_archive_piece_jointe_seance PRIMARY KEY (id_piece_jointe, id_archive_seance),
  CONSTRAINT id_piece_jointe FOREIGN KEY (id_piece_jointe)
      REFERENCES cahier_courant.cahier_piece_jointe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_archive_seance FOREIGN KEY (id_archive_seance)
      REFERENCES cahier_courant.cahier_archive_seance (id_archive_seance) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

COMMENT ON COLUMN cahier_courant.cahier_archive_devoir.id_archive_seance IS 'Identifiant d''archive du devoir de la séance' ;
COMMENT ON COLUMN cahier_courant.cahier_archive_devoir.id_archive_seance IS 'Identifiant du devoir orignal' ;


  
-- cahier_archive_piece_jointe_devoir : 
-- association entre un devoir archive et une piece jointe
-- -------------------------------------------------------
CREATE TABLE cahier_courant.cahier_archive_piece_jointe_devoir
(
  id_piece_jointe integer NOT NULL,
  id_archive_devoir integer NOT NULL,
  CONSTRAINT id_cahier_archive_piece_jointe_devoir PRIMARY KEY (id_piece_jointe , id_archive_devoir ),
  CONSTRAINT id_archive_devoir FOREIGN KEY (id_archive_devoir)
      REFERENCES cahier_courant.cahier_archive_devoir (id_archive_devoir) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_piece_jointe FOREIGN KEY (id_piece_jointe)
      REFERENCES cahier_courant.cahier_piece_jointe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION);


--Création des indexes
CREATE INDEX "indexCahierEleve"
  ON cahier_courant.cahier_eleve
  USING btree
  (uid);

CREATE INDEX "indexCahierEleveClasse"
  ON cahier_courant.cahier_eleve_classe
  USING btree
  (id_eleve,id_classe);
 
CREATE INDEX "indexCahierEleveGroupe"
  ON cahier_courant.cahier_eleve_groupe
  USING btree
  (id_eleve,id_groupe);
 
CREATE INDEX "indexCahierDevoir"
  ON cahier_courant.cahier_devoir
  USING btree
  (id_seance, date_remise);
 
CREATE INDEX "indexCahierSeance"
  ON cahier_courant.cahier_seance
  USING btree
  (id_sequence, date); 
 
CREATE INDEX "indexCahierEmploi"
  ON cahier_courant.cahier_emploi
  USING btree
  (id_etablissement, type_semaine, id_enseignant);

CREATE INDEX "indexCahierEnseignant"
  ON cahier_courant.cahier_enseignant
  USING btree
  (uid);
 
CREATE INDEX "indexCahierClasse"
  ON cahier_courant.cahier_classe
  USING btree
  (id_etablissement);     
 
CREATE INDEX "indexCahierEnseignantClasse"
  ON cahier_courant.cahier_enseignant_classe
  USING btree
  (id_enseignant, id_classe);
 
CREATE INDEX "indexCahierEnseignantGroupe"
  ON cahier_courant.cahier_enseignant_groupe
  USING btree
  (id_enseignant, id_groupe);
 
CREATE INDEX "indexCahierEtabEleve"
  ON cahier_courant.cahier_etab_eleve
  USING btree
  (id_etablissement); 
 
CREATE INDEX "indexCahierGroupe"
  ON cahier_courant.cahier_groupe_classe
  USING btree
  (id_groupe);
 
CREATE INDEX "indexCahierGroupeClasse"
  ON cahier_courant.cahier_groupe_classe
  USING btree
  (id_classe);

CREATE INDEX "indexCahierInspecteur"
  ON cahier_courant.cahier_inspecteur
  USING btree
  (uid); 
 
CREATE INDEX "indexCahierLibelleEnseignement"
  ON cahier_courant.cahier_libelle_enseignement
  USING btree
  (id_etablissement, id_enseignement);
 
CREATE INDEX "indexCahierOuvertureInspecteur"
  ON cahier_courant.cahier_ouverture_inspecteur
  USING btree
  (id_etablissement, id_inspecteur);
 
CREATE INDEX "indexCahierPieceJointe"
  ON cahier_courant.cahier_piece_jointe
  USING btree
  (uid); 
 
CREATE INDEX "indexCahierPieceJointeDevoir"
  ON cahier_courant.cahier_piece_jointe_devoir
  USING btree
  (id_devoir, id_piece_jointe); 
 
CREATE INDEX "indexCahierPieceJointeSeance"
  ON cahier_courant.cahier_piece_jointe_seance
  USING btree
  (id_seance, id_piece_jointe);
 
CREATE INDEX "indexCahierPreferencesUtilisateur"
  ON cahier_courant.cahier_preferences_utilisateur
  USING btree
  (uid); 
 
CREATE INDEX "indexCahierTypeDevoir"
  ON cahier_courant.cahier_type_devoir
  USING btree
  (id_etablissement);
  
CREATE INDEX fki_cahier_archive_seance_id
  ON cahier_courant.cahier_archive_devoir
  USING btree
  (id_archive_seance);
  
CREATE INDEX "indexCahierArchivePieceJointeSeance"
  ON cahier_courant.cahier_archive_piece_jointe_seance
  USING btree
  (id_archive_seance , id_piece_jointe );
  
CREATE INDEX "indexCahierPieceJointeDevoirArchive"
  ON cahier_courant.cahier_archive_piece_jointe_devoir
  USING btree
  (id_archive_devoir , id_piece_jointe );
  
CREATE INDEX fki_cahier_visa_enseignant
  ON cahier_courant.cahier_visa
  USING btree
  (id_enseignant);
  
  
    

--Création des séquences
DROP SEQUENCE IF EXISTS cahier_annee_scolaire;
CREATE SEQUENCE cahier_annee_scolaire START 1;

DROP SEQUENCE IF EXISTS cahier_classe;
CREATE SEQUENCE cahier_classe START 1;

DROP SEQUENCE IF EXISTS cahier_devoir;
CREATE SEQUENCE cahier_devoir START 1;

DROP SEQUENCE IF EXISTS cahier_eleve;
CREATE SEQUENCE cahier_eleve START 1;

DROP SEQUENCE IF EXISTS cahier_emploi;
CREATE SEQUENCE cahier_emploi START 1;

DROP SEQUENCE IF EXISTS cahier_enseignant;
CREATE SEQUENCE cahier_enseignant START 1;

DROP SEQUENCE IF EXISTS cahier_enseignement;
CREATE SEQUENCE cahier_enseignement START 1;

DROP SEQUENCE IF EXISTS cahier_etablissement;
CREATE SEQUENCE cahier_etablissement START 1;

DROP SEQUENCE IF EXISTS cahier_groupe;
CREATE SEQUENCE cahier_groupe START 1;

DROP SEQUENCE IF EXISTS cahier_inspecteur;
CREATE SEQUENCE cahier_inspecteur START 1;

DROP SEQUENCE IF EXISTS cahier_piece_jointe;
CREATE SEQUENCE cahier_piece_jointe START 1;

DROP SEQUENCE IF EXISTS cahier_seance;
CREATE SEQUENCE cahier_seance START 1;

DROP SEQUENCE IF EXISTS cahier_sequence;
CREATE SEQUENCE cahier_sequence START 1;

DROP SEQUENCE IF EXISTS cahier_type_devoir;
CREATE SEQUENCE cahier_type_devoir START 1;

--Céla supprime la règle sur laquelle la séquence dépend 
DROP SEQUENCE IF EXISTS cahier_visa cascade;
CREATE SEQUENCE cahier_visa MINVALUE 1;

DROP SEQUENCE IF EXISTS cahier_archive_seance;
CREATE SEQUENCE cahier_archive_seance MINVALUE 1;

DROP SEQUENCE IF EXISTS cahier_archive_devoir;
CREATE SEQUENCE cahier_archive_devoir MINVALUE 1;

DROP SEQUENCE IF EXISTS cahier_periode_emploi;
CREATE SEQUENCE cahier_periode_emploi MINVALUE 1;


--Création des règles
     
-- Mise en place de regle sur la mise à jour de la table seance ou devoir pour reporter la date
-- de dernier modification dans les visas
-- Si la seance porte sur une sequence differente, deux cahiers ont impactes : l'ancien et le nouveau
-- il faut mettre a jour les deux date de dernier enregistrement sur les 2 visas
-- Puis il faut rattacher les seances attachées à l'ancien visa vers le nouveau
-- DROP RULE reporte_update_seance_visa ON cahier_courant.cahier_seance;
CREATE RULE reporte_update_seance_visa AS ON UPDATE TO cahier_courant.cahier_seance DO 
    (
    
       
    
    -- Dans le cas d'un changement de sequence, il faut rattacher la visa_seance au nouveau visa
    update cahier_courant.cahier_visa_seance 
    set    id_visa = visa_new.id 
    from   
        cahier_courant.cahier_visa visa_new,
        cahier_courant.cahier_visa visa_old,
        cahier_courant.cahier_sequence seq_new,
        cahier_courant.cahier_sequence seq_old
    where
        NEW.id_sequence != OLD.id_sequence and 
        
        -- Jointure avec l'ancien visa
        cahier_courant.cahier_visa_seance.id_visa = visa_old.id and 
        
        -- Ancienne sequence 
        seq_old.id = OLD.id_sequence and

        -- Jointure ancienne sequence / visa  
        seq_old.id_enseignant = visa_old.id_enseignant and
        (seq_old.id_classe = visa_old.id_classe or (seq_old.id_classe is null and visa_old.id_classe is null)) and 
        (seq_old.id_groupe = visa_old.id_groupe or (seq_old.id_groupe is null and visa_old.id_groupe is null)) and
        seq_old.id_enseignement = visa_old.id_enseignement and
        
        -- Nouvelle sequence 
        seq_new.id = NEW.id_sequence and
        
        -- Jointure new sequence / visa  
        seq_new.id_enseignant = visa_new.id_enseignant and
        (seq_new.id_classe = visa_new.id_classe or (seq_new.id_classe is null and visa_new.id_classe is null)) and 
        (seq_new.id_groupe = visa_new.id_groupe or (seq_new.id_groupe is null and visa_new.id_groupe is null)) and
        seq_new.id_enseignement = visa_new.id_enseignement;    );
     
-- DROP RULE reporte_insert_seance_visa ON cahier_courant.cahier_seance;    

  

    
-- Lors la creation d'une sequence
-- Ajout si non deja existant un cahier visa pour la sequence et les profils chef d'etablissement et inspecteur
-- DROP RULE reporte_insert_sequence_visa ON cahier_courant.cahier_sequence;  
DROP RULE if exists reporte_insert_sequence_visa ON cahier_courant.cahier_sequence;  
DROP RULE if exists reporte_insert_sequence_visa1 ON cahier_courant.cahier_sequence;  
DROP RULE if exists reporte_insert_sequence_visa2 ON cahier_courant.cahier_sequence;  

CREATE RULE reporte_insert_sequence_visa1 AS ON INSERT TO cahier_courant.cahier_sequence
    DO
    (
    -- Ajout si non deja existant un cahier visa pour la sequence et le profil chef d'etablissement
    insert into cahier_courant.cahier_visa  
    (id, date_visa,  profil, 
    type_visa, id_enseignant, id_enseignement, 
    id_classe, id_groupe, date_dernier_maj, 
    id_etablissement)
    select nextval('cahier_visa'), null, 'ENTDirecteur',
     null, NEW.id_enseignant, NEW.id_enseignement, 
     NEW.id_classe, NEW.id_groupe, null,
     NEW.id_etablissement 
    where not exists 
        (select 1 from
            (
            select id_enseignant, id_enseignement, id_classe, id_groupe 
            from cahier_courant.cahier_visa V 
            where 
                V.id_enseignant = NEW.id_enseignant and 
                V.id_enseignement = NEW.id_enseignement and 
                V.profil = 'ENTDirecteur' and 
                (V.id_classe = NEW.id_classe or (V.id_classe is null and NEW.id_classe is null)) and 
                (V.id_groupe = NEW.id_groupe or (V.id_groupe is null and NEW.id_groupe is null))
            ) as tmp
        )
    );
    
CREATE RULE reporte_insert_sequence_visa2 AS ON INSERT TO cahier_courant.cahier_sequence
    DO
    (
    -- Ajout si non deja existant un cahier visa pour la sequence et le profil inspecteur
    insert into cahier_courant.cahier_visa  
    (id, date_visa,  profil, type_visa, id_enseignant, id_enseignement, id_classe, id_groupe, date_dernier_maj, id_etablissement)
    select nextval('cahier_visa'), null, 'ENTInspecteur', null, NEW.id_enseignant, NEW.id_enseignement, NEW.id_classe, NEW.id_groupe, null,
    NEW.id_etablissement
    where not exists 
        (select 1 from
            (
            select id_enseignant, id_enseignement, id_classe, id_groupe 
            from cahier_courant.cahier_visa V 
            where 
                V.id_enseignant = NEW.id_enseignant and 
                V.id_enseignement = NEW.id_enseignement and 
                V.profil = 'ENTInspecteur' and 
                (V.id_classe = NEW.id_classe or (V.id_classe is null and NEW.id_classe is null)) and 
                (V.id_groupe = NEW.id_groupe or (V.id_groupe is null and NEW.id_groupe is null))
            ) as tmp
        )
    );
            
    
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
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_type_devoir_cahier_cycle_devoir FOREIGN KEY (id_type_devoir)
      REFERENCES cahier_courant.cahier_type_devoir (id) MATCH SIMPLE
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

--ALTER TABLE cahier_courant.cahier_groupe ADD COLUMN groupe_collaboratif boolean DEFAULT false;
--ALTER TABLE cahier_courant.cahier_groupe ADD COLUMN nom_long varchar(255) DEFAULT null;

DROP SEQUENCE IF EXISTS cahier_cycle;
CREATE SEQUENCE cahier_cycle MINVALUE 1;

DROP SEQUENCE IF EXISTS cahier_cycle_seance;
CREATE SEQUENCE cahier_cycle_seance MINVALUE 1;

DROP SEQUENCE IF EXISTS cahier_cycle_devoir;
CREATE SEQUENCE cahier_cycle_devoir MINVALUE 1;

  