--  
-- Mise en place du modèle de données utilisé pour la gestion des VISAS
-- - Suppression de la table visa cree initialement avec la version 5.0.0
-- - Creation des tables :
--     visa                        : un visa sur un cahier de texte (enseignant, classe, enseignement)
--     visa_seance                 : une visa sur une seance
--     archive_seance              : une seance archive correspondant a un visa memo
--     archive_devoir              : un devoir archive correspondant a un visa memo
--     archive_piece_jointe_seance : association entre une seance archive et une piece jointe 
--     archive_piece_jointe_devoir : association entre un devoir archive et une piece jointe
-- - Mise en place de regle sur la mise à jour de la table seance ou devoir pour reporter la date
--   de dernier modification dans les visas
-- - Creation des visa par defaut pour chaque sequence existante
-- ----------------------------------------------------------------------------------------------
BEGIN;

DROP RULE if exists reporte_insert_sequence_visa ON cahier_courant.cahier_sequence;
DROP RULE if exists reporte_insert_sequence_visa1 ON cahier_courant.cahier_sequence;
DROP RULE if exists reporte_insert_sequence_visa2 ON cahier_courant.cahier_sequence;
DROP RULE if exists reporte_delete_seance_visa ON cahier_courant.cahier_seance;
DROP RULE if exists reporte_insert_seance_visa ON cahier_courant.cahier_seance;
DROP RULE if exists reporte_update_seance_visa ON cahier_courant.cahier_seance;

drop table if exists cahier_courant.cahier_archive_piece_jointe_devoir;
drop table if exists cahier_courant.cahier_archive_piece_jointe_seance;
drop table if exists cahier_courant.cahier_visa_seance;
drop table if exists cahier_courant.cahier_archive_devoir;
drop table if exists cahier_courant.cahier_archive_seance;
drop table if exists cahier_courant.cahier_visa;

--Cela supprime la règle sur laquelle la séquence dépend 
DROP SEQUENCE IF EXISTS cahier_visa cascade;
DROP SEQUENCE IF EXISTS cahier_archive_seance cascade;
DROP SEQUENCE IF EXISTS cahier_archive_devoir cascade;

-- Modifie la table seance pour y ajouter une date de mise à jour
-- -----------------------------------------------------------------------
ALTER TABLE cahier_courant.cahier_seance ADD COLUMN date_maj timestamp;

-- Ajoute la colonne date_debut / date_fin
-- -----------------------------------------------------------------------
ALTER TABLE cahier_courant.cahier_ouverture_inspecteur ADD COLUMN date_fin date;
ALTER TABLE cahier_courant.cahier_ouverture_inspecteur RENAME COLUMN date TO date_debut;

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

CREATE SEQUENCE cahier_visa MINVALUE 1;
CREATE SEQUENCE cahier_archive_seance MINVALUE 1;
CREATE SEQUENCE cahier_archive_devoir MINVALUE 1;

CREATE INDEX fki_cahier_visa_enseignant
  ON cahier_courant.cahier_visa
  USING btree
  (id_enseignant);

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

COMMENT ON COLUMN cahier_courant.cahier_archive_devoir.id_archive_seance IS 'Identifiant d''archive du devoir de la séance' ;
COMMENT ON COLUMN cahier_courant.cahier_archive_devoir.id_archive_seance IS 'Identifiant du devoir orignal' ;

CREATE INDEX fki_cahier_archive_seance_id
  ON cahier_courant.cahier_archive_devoir
  USING btree
  (id_archive_seance);


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
CREATE INDEX "indexCahierArchivePieceJointeSeance"
  ON cahier_courant.cahier_archive_piece_jointe_seance
  USING btree
  (id_archive_seance , id_piece_jointe );


  
  
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

CREATE INDEX "indexCahierPieceJointeDevoirArchive"
  ON cahier_courant.cahier_archive_piece_jointe_devoir
  USING btree
  (id_archive_devoir , id_piece_jointe );

      
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
    
    
-- Creation des visa par defaut pour chaque sequence existante
-- -----------------------------------------------------------
insert into cahier_courant.cahier_visa
select 
  nextval('cahier_visa'),
  null,
  'ENTDirecteur',
  null,
  seq.id_etablissement,
  seq.id_enseignant,
  seq.id_enseignement,
  seq.id_classe, 
  seq.id_groupe,
  null
from 
    cahier_courant.cahier_sequence seq
group by
  seq.id_etablissement,
  seq.id_enseignant,
  seq.id_enseignement,
  seq.id_classe, 
  seq.id_groupe;
  
insert into cahier_courant.cahier_visa
select 
  nextval('cahier_visa'),
  null,
  'ENTInspecteur',
  null,
  seq.id_etablissement,
  seq.id_enseignant,
  seq.id_enseignement,
  seq.id_classe, 
  seq.id_groupe,
  null
from 
    cahier_courant.cahier_sequence seq
group by 
  seq.id_etablissement,
  seq.id_enseignant,
  seq.id_enseignement,
  seq.id_classe, 
  seq.id_groupe;

-- Mise a jour des date de maj des seances
UPDATE cahier_courant.cahier_seance SET date_maj = current_timestamp WHERE date_maj is null; 

COMMIT;
