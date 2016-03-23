BEGIN;

-- Mise a niveau de la base de donnees en 3.2 
-- ------------------------------------------

--Mise a jour de cahier_emploi
--ALTER TABLE "cahier_archive_2009-2010".cahier_emploi ADD COLUMN date_fin_valide date;
--ALTER TABLE "cahier_archive_2009-2010".cahier_emploi ADD COLUMN date_debut_valide date;

-- Mise à jour pour la fusion de cellule d'emploi du temps
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi ADD COLUMN ids_fusion text;
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi ADD COLUMN flag_fusion boolean;
ALTER TABLE "cahier_archive_2009-2010".cahier_etab_enseignant ADD COLUMN date_dernier_enregistrement date;




-- Mise a niveau de la base de donnees en 3.5
-- ------------------------------------------

-- Chapitre EP Cahier de texte 1.1.1.3: rendre le type de devoir factultatif
-- Mise a jour de la table cahier_devoir pour supprimer le not null 
-- sur la colonne id_type_devoir 
ALTER TABLE "cahier_archive_2009-2010".cahier_devoir ALTER COLUMN id_type_devoir DROP NOT NULL;


--Modification de la table piece jointe
ALTER TABLE "cahier_archive_2009-2010".cahier_piece_jointe ADD COLUMN chemin character varying(250) DEFAULT '/Mes Documents'::character varying;
ALTER TABLE "cahier_archive_2009-2010".cahier_piece_jointe ALTER COLUMN chemin SET STORAGE EXTENDED;
-- ALTER TABLE "cahier_archive_2009-2010".cahier_piece_jointe ALTER COLUMN chemin SET DEFAULT '/Mes Documents'::character varying;
ALTER TABLE "cahier_archive_2009-2010".cahier_piece_jointe ALTER COLUMN chemin SET NOT NULL;

--Mofification "cahier_archive_2009-2010" enseignant pour stockage
ALTER TABLE "cahier_archive_2009-2010".cahier_enseignant ADD COLUMN depot_stockage character varying(50);


--Creation de la table inspecteur
CREATE TABLE "cahier_archive_2009-2010".cahier_inspecteur
(
  id Integer NOT NULL,
  nom character varying(50) NOT NULL,
  prenom character varying(50) NOT NULL,
  uid character varying(8) NOT NULL,
  civilite character varying(10),
  CONSTRAINT id_cahier_inspecteur PRIMARY KEY (id)
);


--Creation de la table ouverture inspecteur
CREATE TABLE "cahier_archive_2009-2010".cahier_ouverture_inspecteur
(
  id_etablissement integer NOT NULL,
  id_enseignant integer NOT NULL,
  id_inspecteur integer NOT NULL,
  date date NOT NULL,
  nomDirecteur character varying(100) NOT NULL,
  uidDirecteur character varying(8) NOT NULL,
  CONSTRAINT id_ouverture_inspecteur PRIMARY KEY (id_etablissement, id_enseignant, id_inspecteur),
  CONSTRAINT id_etablissement FOREIGN KEY (id_etablissement)
      REFERENCES "cahier_archive_2009-2010".cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES "cahier_archive_2009-2010".cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_inspecteur FOREIGN KEY (id_inspecteur)
      REFERENCES "cahier_archive_2009-2010".cahier_inspecteur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION            
);


-- Mise a niveau de la base de donnees en version 4.1
-- --------------------------------------------------
-- Mise a jour sur la table annee scolaire
-- Ajout du champs periode scolaire
ALTER TABLE "cahier_archive_2009-2010".cahier_annee_scolaire ADD COLUMN periode_scolaire text;

-- Mise a jour sur la table etablissement
-- Modification de la taille du champs designation
ALTER TABLE "cahier_archive_2009-2010".cahier_etablissement ALTER designation TYPE character varying(100);

-- Ajout du champs import
ALTER TABLE "cahier_archive_2009-2010".cahier_etablissement ADD COLUMN import Boolean NOT NULL DEFAULT false;

-- Ajout du champs date_import
ALTER TABLE "cahier_archive_2009-2010".cahier_etablissement ADD COLUMN date_import character varying(15);

-- Mise à jour sur la table cahier_emploi
-- Ajout du champs couleur_case
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi ADD COLUMN couleur_case character varying(10);

-- Ajout du champs code_salle
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi ADD COLUMN code_salle character varying(15);


-- Modification de la table de parametrage etablissement :
--    - Ajoute la colonne fractionnement
--    - Supprime la colonne horaire_scindee
-- -------------------------------------------------------
ALTER TABLE "cahier_archive_2009-2010".cahier_etablissement ADD COLUMN fractionnement integer NOT NULL DEFAULT 1;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_etablissement.fractionnement IS 'Rapport de fractionnement des plage dans la saisie emploi du temps. 1:pas de fractionnement, 2:demie plage, 4:quart de plage';
ALTER TABLE "cahier_archive_2009-2010".cahier_etablissement DROP COLUMN horaire_scindee;

-- Modification de la table seance :
--   - Ajoute la colonne annotations
-- ----------------------------------
ALTER TABLE "cahier_archive_2009-2010".cahier_seance ADD COLUMN annotations text DEFAULT null;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_seance.annotations IS 'Annotations saisies par l enseignant et visibles uniquement par lui';


-- Modification de la table cahier_etab_enseignant :
--    - Suppression du champ date_dernier_enregistrement
-- ----------------------------------------------------------------
ALTER TABLE "cahier_archive_2009-2010".cahier_etab_enseignant DROP COLUMN date_dernier_enregistrement;


-- Creation de la table periode_emploi
-- -----------------------------------
CREATE TABLE "cahier_archive_2009-2010".cahier_periode_emploi
(
  id integer NOT NULL, 
  id_etablissement integer NOT NULL,
  id_enseignant integer NOT NULL,
  date_debut date NOT NULL,
  CONSTRAINT id_cahier_periode_emploi PRIMARY KEY (id),
  CONSTRAINT id_etablissement_periode_emploi FOREIGN KEY (id_etablissement)
      REFERENCES "cahier_archive_2009-2010".cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_periode_emploi.id IS 'Identifiant de la periode emploi du temps pour l etablissement' ;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_periode_emploi.id_etablissement IS 'id de l etablissement' ;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_periode_emploi.id_enseignant IS 'id de l enseignant' ;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_periode_emploi.date_debut IS 'Date de debut de la periode. La date de fin de la periode est la date de debut de periode suivante moins un jour';


-- Modification de la table emploi :
--   - Creation d'une periode par defaut pour chaque etablissement 
--   - Ajout de la colonne id_periode_emploi
--   - Fusion des plages fusionnees 
--   - Suppression des colonnes id_sequence, flag_fusion, ids_fusion
-- -----------------------------------------------------------------
-- Creation d'une periode par etablissement / enseignant
INSERT INTO "cahier_archive_2009-2010".cahier_periode_emploi (id, id_etablissement, id_enseignant, date_debut) 
SELECT 
    nextval('cahier_periode_emploi'), e.id, ee.id_enseignant, a.date_rentree
FROM
    "cahier_archive_2009-2010".cahier_etablissement e,
    "cahier_archive_2009-2010".cahier_etab_enseignant ee,
    "cahier_archive_2009-2010".cahier_annee_scolaire a 
WHERE 
    a.exercice = '2011-2012' AND
    e.id = ee.id_etablissement
    ;

-- Ajout de la colonne id_periode_emploi 
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi ADD COLUMN id_periode_emploi integer;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_etablissement.fractionnement IS 'Rapport de fractionnement des plage dans la saisie emploi du temps. 1:pas de fractionnement, 2:demie plage, 4:quart de plage';

-- Positionne la periode pour chaque case de l'emploi du temps
UPDATE 
    "cahier_archive_2009-2010".cahier_emploi
SET 
    id_periode_emploi = "cahier_archive_2009-2010".cahier_periode_emploi.id
FROM
    "cahier_archive_2009-2010".cahier_periode_emploi 
WHERE 
    "cahier_archive_2009-2010".cahier_periode_emploi.id_etablissement = "cahier_archive_2009-2010".cahier_emploi.id_etablissement AND
    "cahier_archive_2009-2010".cahier_periode_emploi.id_enseignant    = "cahier_archive_2009-2010".cahier_emploi.id_enseignant;
    
--Supression des données liées aux changement d'établissement.
DELETE FROM "cahier_archive_2009-2010".cahier_emploi WHERE id_periode_emploi IS NULL;
-- Ajout la contrainte not null sur la colonne id_periode_emploi
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi ALTER COLUMN id_periode_emploi SET NOT NULL;
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi ADD CONSTRAINT 
      id_periode_emploi_emploi FOREIGN KEY (id_periode_emploi)
      REFERENCES "cahier_archive_2009-2010".cahier_periode_emploi (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Fusionne les plages qui sont fusinnees dans une nouvelle case
INSERT INTO "cahier_archive_2009-2010".cahier_emploi (
    id,
    jour,
    type_semaine,
    heure_debut,
    heure_fin,
    minute_debut,
    minute_fin,
    id_enseignant,
    id_classe,
    id_groupe,
    id_enseignement,
    id_etablissement,
    ids_fusion,
    flag_fusion,
    description,
    couleur_case,
    code_salle,
    id_periode_emploi
    )
SELECT 
    nextval('cahier_emploi'),
    tmp.jour,
    tmp.type_semaine,
    floor(tmp.debut / 60),
    floor(tmp.fin / 60),
    mod(tmp.debut, 60),
    mod(tmp.fin,60),
    tmp.id_enseignant,
    tmp.id_classe,
    tmp.id_groupe,
    tmp.id_enseignement,
    tmp.id_etablissement,
    null,
    false,
    tmp.description,
    tmp.couleur_case,
    tmp.code_salle,
    tmp.id_periode_emploi
FROM (
    SELECT 
        jour,
        type_semaine,
        id_enseignant,
        id_classe,
        id_groupe,
        id_enseignement,
        id_etablissement,
        description,
        couleur_case,
        code_salle,
        id_periode_emploi,
        ids_fusion, 
        flag_fusion, 
        min(heure_debut*60 + minute_debut) as debut, 
        max(heure_fin*60 + minute_fin) as fin
    FROM 
        "cahier_archive_2009-2010".cahier_emploi
    WHERE flag_fusion = true
    GROUP BY 
    jour,
        type_semaine,
        id_enseignant,
        id_classe,
        id_groupe,
        id_enseignement,
        id_etablissement,
        description,
        couleur_case,
        code_salle,
        id_periode_emploi,
        ids_fusion, 
        flag_fusion
    ORDER BY ids_fusion
    ) AS tmp;

 -- Supprime les cases unitaires qui correspondaient a une fusion
DELETE FROM 
    "cahier_archive_2009-2010".cahier_emploi
WHERE 
    flag_fusion = true;

-- Supprime les colonnes de fusion
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi DROP COLUMN flag_fusion;
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi DROP COLUMN ids_fusion;
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi DROP CONSTRAINT id_sequence_emploi;
ALTER TABLE "cahier_archive_2009-2010".cahier_emploi DROP COLUMN id_sequence;

-- Creation de la table cahier_visa
-- --------------------------------
CREATE TABLE "cahier_archive_2009-2010".cahier_visa
(
  id integer NOT NULL, 
  date_visa date NOT NULL,
  profil varchar(20) NOT NULL,
  id_seance integer NOT NULL,
  CONSTRAINT id_cahier_visa PRIMARY KEY (id ),
  CONSTRAINT id_seance_cahier_visa FOREIGN KEY (id_seance)
      REFERENCES "cahier_archive_2009-2010".cahier_seance (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_visa.id IS 'Identifiant du visa' ;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_visa.date_visa IS 'Date du visa';
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_visa.profil IS 'Profil de la personne ayant appose le visa. Correspond au object class du LDAP ' ;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_visa.id_seance IS 'id de la seance qui a ete visee' ;


-- Typologie des type de devoirs : travail normal ou devoir
-- --------------------------------------------------------
ALTER TABLE "cahier_archive_2009-2010".cahier_type_devoir ADD COLUMN categorie varchar(15) NOT NULL DEFAULT 'NORMAL';
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_type_devoir.categorie IS 'Categorie du type de devoir. NORMAL ou DEVOIR' ;


-- Fusion de la table cahier_enseignement_sequence avec cahier_sequence :
--    - Cr�ation des nouvelle colonnes dans la table cahier_sequence
--    - Recopie les identifiant de la table cahier_enseignement_sequence
--    - Applique les not null sur les colonnes des identifiants 
--    - Supprime la table cahier_enseignement_sequence 
--    - Ajoute les cle etrangeres vers les tables connexes
-- ----------------------------------------------------------------------
--    - Cr�ation des nouvelle colonnes dans la table cahier_sequence
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ADD COLUMN id_etablissement  integer;
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ADD COLUMN id_enseignement  integer;
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ADD COLUMN id_groupe  integer;
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ADD COLUMN id_classe  integer;

COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_sequence.id_etablissement IS 'id de l etablissement' ;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_sequence.id_enseignement IS 'id de l enseignement' ;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_sequence.id_groupe IS 'id du groupe (exclusif avec id_classe)' ;
COMMENT ON COLUMN "cahier_archive_2009-2010".cahier_sequence.id_classe IS 'id de la classe (exclusif avec id_groupe)' ;


--    - Recopie les identifiant de la table cahier_enseignement_sequence
UPDATE 
    "cahier_archive_2009-2010".cahier_sequence
SET 
    id_etablissement = coalesce(c.id_etablissement,g.id_etablissement, null),
    id_enseignement = es.id_enseignement,
    id_groupe = CASE es.id_groupe WHEN 0 THEN null ELSE es.id_groupe END,
    id_classe = CASE es.id_classe WHEN 0 THEN null ELSE es.id_classe END
FROM
    "cahier_archive_2009-2010".cahier_enseignement_sequence es
        LEFT OUTER JOIN "cahier_archive_2009-2010".cahier_classe c ON(es.id_classe = c.id)
        LEFT OUTER JOIN "cahier_archive_2009-2010".cahier_groupe g ON(es.id_groupe = g.id)
WHERE
    "cahier_archive_2009-2010".cahier_sequence.id = es.id_sequence;
    

--    - Applique les not null sur les colonnes des identifiants 
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ALTER COLUMN id_etablissement SET NOT NULL;  
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ALTER COLUMN id_enseignement SET NOT NULL;   

--    - Supprime la table cahier_enseignement_sequence 
DROP TABLE "cahier_archive_2009-2010".cahier_enseignement_sequence;

--    - Ajoute les cle etrangeres vers les tables connexes
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ADD CONSTRAINT id_etablissement_cahier_sequence FOREIGN KEY (id_etablissement)
      REFERENCES "cahier_archive_2009-2010".cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ADD CONSTRAINT id_enseignement_cahier_sequence FOREIGN KEY (id_enseignement)
      REFERENCES "cahier_archive_2009-2010".cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ADD CONSTRAINT id_groupe_cahier_sequence FOREIGN KEY (id_groupe)
      REFERENCES "cahier_archive_2009-2010".cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE "cahier_archive_2009-2010".cahier_sequence ADD CONSTRAINT id_classe_cahier_sequence FOREIGN KEY (id_classe)
      REFERENCES "cahier_archive_2009-2010".cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
-- Vide par defaut la colonne periode_scolaire de la table annee_scolaire     
UPDATE "cahier_archive_2009-2010".cahier_annee_scolaire SET periode_scolaire = '';  


-- Modifie la table des pieces jointes pour specifier le chemin par defaut
-- "Documents du CTN"
-- -----------------------------------------------------------------------
ALTER TABLE "cahier_archive_2009-2010".cahier_piece_jointe 
    ALTER COLUMN chemin SET DEFAULT '/Documents du CTN';


-- Remplace dans la table des pieces jointes les noms des chemins 
-- commen�ant par "Mes Documents" en "Documents du CTN" 
-- --------------------------------------------------------------
update 
    "cahier_archive_2009-2010".cahier_piece_jointe 
set 
    chemin = '/Documents du CTN' || substring(chemin from '/Mes Documents(.*)')
where 
    chemin like '/Mes Documents%'
;


COMMIT;
