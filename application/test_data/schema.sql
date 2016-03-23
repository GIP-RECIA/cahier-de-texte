
/* C:\ent\cahier-texte-richfaces4\maven_jars\target\lib>java -jar sqltool-2.3.0.jar
 --rcfile dbmanager.rc mem ..\..\..\application\test_data\cahier-demo.sql
 */
CREATE SCHEMA cahier_courant AUTHORIZATION DBA;


SET SCHEMA cahier_courant;

set database sql syntax PGS true;

CREATE TABLE cahier_annee_scolaire (
    id integer NOT NULL,
    exercice character varying(50),
    date_rentree date NOT NULL,
    date_sortie date NOT NULL,
    periode_vacance text,
    ouverture_ent boolean,
    periode_scolaire text
);

--
-- Name: cahier_archive_devoir; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_archive_devoir (
    id_archive_devoir integer NOT NULL,
    date_remise date NOT NULL,
    code character varying(10) NOT NULL,
    intitule character varying(50),
    description text,
    id_type_devoir integer,
    id_archive_seance integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_archive_devoir OWNER TO ent;

--
-- Name: COLUMN cahier_archive_devoir.id_archive_seance; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_archive_devoir.id_archive_seance IS 'Identifiant du devoir orignal';


--
-- Name: cahier_archive_piece_jointe_devoir; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_archive_piece_jointe_devoir (
    id_piece_jointe integer NOT NULL,
    id_archive_devoir integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_archive_piece_jointe_devoir OWNER TO ent;

--
-- Name: cahier_archive_piece_jointe_seance; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_archive_piece_jointe_seance (
    id_piece_jointe integer NOT NULL,
    id_archive_seance integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_archive_piece_jointe_seance OWNER TO ent;

--
-- Name: cahier_archive_seance; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_archive_seance (
    id_archive_seance integer NOT NULL,
    date_archive timestamp without time zone,
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
    annotations text
);


--ALTER TABLE cahier_courant.cahier_archive_seance OWNER TO ent;

--
-- Name: COLUMN cahier_archive_seance.id_archive_seance; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_archive_seance.id_archive_seance IS 'Identifiant d''archive de la séance';


--
-- Name: COLUMN cahier_archive_seance.id_seance; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_archive_seance.id_seance IS 'Identifiant du séance orignale';


--
-- Name: cahier_classe; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_classe (
    id integer NOT NULL,
    code character varying(20) NOT NULL,
    designation character varying(50) NOT NULL,
    id_etablissement integer NOT NULL,
    id_annee_scolaire integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_classe OWNER TO ent;

--
-- Name: cahier_cycle; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_cycle (
    id integer NOT NULL,
    id_enseignant integer NOT NULL,
    uid_enseignant character varying(8) NOT NULL,
    titre character varying(100) NOT NULL,
    objectif text,
    prerequis text,
    description text
);


--ALTER TABLE cahier_courant.cahier_cycle OWNER TO ent;

--
-- Name: cahier_cycle_devoir; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_cycle_devoir (
    id integer NOT NULL,
    id_cycle_seance integer NOT NULL,
    intitule character varying(50) NOT NULL,
    description text,
    id_type_devoir integer,
    date_remise integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_cycle_devoir OWNER TO ent;

--
-- Name: cahier_cycle_groupe; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_cycle_groupe (
    id_cycle integer NOT NULL,
    id_groupe integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_cycle_groupe OWNER TO ent;

--
-- Name: cahier_cycle_seance; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_cycle_seance (
    id integer NOT NULL,
    id_cycle integer NOT NULL,
    id_enseignant integer NOT NULL,
    uid_enseignant character varying(8) NOT NULL,
    enseignement character varying(100),
    objectif text,
    intitule character varying(50) NOT NULL,
    description text,
    annotations text,
    annotations_visible boolean,
    indice integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_cycle_seance OWNER TO ent;

--
-- Name: cahier_devoir; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_devoir (
    id integer NOT NULL,
    date_remise date NOT NULL,
    code character varying(10) NOT NULL,
    intitule character varying(50),
    description text,
    id_type_devoir integer,
    id_seance integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_devoir OWNER TO ent;

--
-- Name: cahier_eleve; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_eleve (
    id integer NOT NULL,
    nom character varying(50) NOT NULL,
    prenom character varying(50) NOT NULL,
    uid character varying(50) NOT NULL
);


--ALTER TABLE cahier_courant.cahier_eleve OWNER TO ent;

--
-- Name: cahier_eleve_classe; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_eleve_classe (
    id_classe integer NOT NULL,
    id_eleve integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_eleve_classe OWNER TO ent;

--
-- Name: cahier_eleve_groupe; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_eleve_groupe (
    id_eleve integer NOT NULL,
    id_groupe integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_eleve_groupe OWNER TO ent;

--
-- Name: cahier_emploi; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_emploi (
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
    id_periode_emploi integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_emploi OWNER TO ent;

--
-- Name: cahier_enseignant; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_enseignant (
    id integer NOT NULL,
    nom character varying(50) NOT NULL,
    prenom character varying(50) NOT NULL,
    uid character varying(8) NOT NULL,
    civilite character varying(10),
    depot_stockage character varying(50)
);


--ALTER TABLE cahier_courant.cahier_enseignant OWNER TO ent;

--
-- Name: cahier_enseignant_classe; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_enseignant_classe (
    id_enseignant integer NOT NULL,
    id_classe integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_enseignant_classe OWNER TO ent;

--
-- Name: cahier_enseignant_enseignement; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_enseignant_enseignement (
    id_enseignement integer NOT NULL,
    id_enseignant integer NOT NULL,
    id_etablissement integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_enseignant_enseignement OWNER TO ent;

--
-- Name: cahier_enseignant_groupe; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_enseignant_groupe (
    id_enseignant integer NOT NULL,
    id_groupe integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_enseignant_groupe OWNER TO ent;

--
-- Name: cahier_enseignement; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_enseignement (
    id integer NOT NULL,
    code character varying(10),
    designation character varying(50) NOT NULL
);


--ALTER TABLE cahier_courant.cahier_enseignement OWNER TO ent;

--
-- Name: cahier_etab_eleve; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_etab_eleve (
    id_eleve integer NOT NULL,
    id_etablissement integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_etab_eleve OWNER TO ent;

--
-- Name: cahier_etab_enseignant; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_etab_enseignant (
    id_enseignant integer NOT NULL,
    id_etablissement integer NOT NULL,
    saisie_simplifiee boolean
);


--ALTER TABLE cahier_courant.cahier_etab_enseignant OWNER TO ent;

--
-- Name: cahier_etablissement; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_etablissement (
    id integer NOT NULL,
    code character varying(20) NOT NULL,
    designation character varying(100) NOT NULL,
    horaire_cours text,
    alternance_semaine text,
    jours_ouvres text,
    duree_cours integer,
    heure_debut integer,
    heure_fin integer,
    minute_debut integer,
    ouverture boolean,
    import boolean DEFAULT false NOT NULL,
    date_import character varying(15),
    fractionnement integer DEFAULT 1 NOT NULL
);


--ALTER TABLE cahier_courant.cahier_etablissement OWNER TO ent;

--
-- Name: COLUMN cahier_etablissement.fractionnement; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_etablissement.fractionnement IS 'Rapport de fractionnement des plage dans la saisie emploi du temps. 1:pas de fractionnement, 2:demie plage, 4:quart de plage';


--
-- Name: cahier_groupe; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

--DEFAULT NULL::character varying

CREATE TABLE cahier_groupe (
    id integer NOT NULL,
    code character varying(20) NOT NULL,
    designation character varying(255) NOT NULL,
    id_etablissement integer NOT NULL,
    id_annee_scolaire integer NOT NULL,
    groupe_collaboratif boolean DEFAULT false,
    nom_long character varying(255) DEFAULT NULL  
);


--ALTER TABLE cahier_courant.cahier_groupe OWNER TO ent;

--
-- Name: cahier_groupe_classe; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_groupe_classe (
    id_groupe integer NOT NULL,
    id_classe integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_groupe_classe OWNER TO ent;

--
-- Name: cahier_inspecteur; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_inspecteur (
    id integer NOT NULL,
    nom character varying(50) NOT NULL,
    prenom character varying(50) NOT NULL,
    uid character varying(8) NOT NULL,
    civilite character varying(10)
);


--ALTER TABLE cahier_courant.cahier_inspecteur OWNER TO ent;

--
-- Name: cahier_libelle_enseignement; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_libelle_enseignement (
    id_enseignement integer NOT NULL,
    id_etablissement integer NOT NULL,
    libelle character varying(8) NOT NULL
);


--ALTER TABLE cahier_courant.cahier_libelle_enseignement OWNER TO ent;

--
-- Name: cahier_ouverture_inspecteur; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_ouverture_inspecteur (
    id_etablissement integer NOT NULL,
    id_enseignant integer NOT NULL,
    id_inspecteur integer NOT NULL,
    date_debut date NOT NULL,
    nomdirecteur character varying(100) NOT NULL,
    uiddirecteur character varying(8) NOT NULL,
    date_fin date
);


--ALTER TABLE cahier_courant.cahier_ouverture_inspecteur OWNER TO ent;

--
-- Name: cahier_periode_emploi; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_periode_emploi (
    id integer NOT NULL,
    id_etablissement integer NOT NULL,
    id_enseignant integer NOT NULL,
    date_debut date NOT NULL
);


--ALTER TABLE cahier_courant.cahier_periode_emploi OWNER TO ent;

--
-- Name: COLUMN cahier_periode_emploi.id; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_periode_emploi.id IS 'Identifiant de la periode emploi du temps pour l etablissement';


--
-- Name: COLUMN cahier_periode_emploi.id_etablissement; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_periode_emploi.id_etablissement IS 'id de l etablissement';


--
-- Name: COLUMN cahier_periode_emploi.id_enseignant; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_periode_emploi.id_enseignant IS 'id de l enseignant';


--
-- Name: COLUMN cahier_periode_emploi.date_debut; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_periode_emploi.date_debut IS 'Date de debut de la periode. La date de fin de la periode est la date de debut de periode suivante moins un jour';


--
-- Name: cahier_piece_jointe; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_piece_jointe (
    id integer NOT NULL,
    code character varying(10) NOT NULL,
    nom_fichier character varying(255) NOT NULL,
    uid character varying(8) NOT NULL,
    chemin character varying(250) DEFAULT  '/Mes Documents' 
);


--ALTER TABLE cahier_courant.cahier_piece_jointe OWNER TO ent;

--
-- Name: cahier_piece_jointe_cycle_devoir; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_piece_jointe_cycle_devoir (
    id_piece_jointe integer NOT NULL,
    id_cycle_devoir integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_piece_jointe_cycle_devoir OWNER TO ent;

--
-- Name: cahier_piece_jointe_cycle_seance; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_piece_jointe_cycle_seance (
    id_piece_jointe integer NOT NULL,
    id_cycle_seance integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_piece_jointe_cycle_seance OWNER TO ent;

--
-- Name: cahier_piece_jointe_devoir; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_piece_jointe_devoir (
    id_piece_jointe integer NOT NULL,
    id_devoir integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_piece_jointe_devoir OWNER TO ent;

--
-- Name: cahier_piece_jointe_seance; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_piece_jointe_seance (
    id_piece_jointe integer NOT NULL,
    id_seance integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_piece_jointe_seance OWNER TO ent;

--
-- Name: cahier_preferences_utilisateur; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_preferences_utilisateur (
    uid character varying(8) NOT NULL,
    preferences character varying(200)
);


--ALTER TABLE cahier_courant.cahier_preferences_utilisateur OWNER TO ent;

--
-- Name: cahier_remplacement; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_remplacement (
    id integer NOT NULL,
    id_etablissement integer NOT NULL,
    id_enseignant_absent integer NOT NULL,
    id_enseignant_remplacant integer NOT NULL,
    date_debut timestamp without time zone NOT NULL,
    date_fin timestamp without time zone NOT NULL
);


--ALTER TABLE cahier_courant.cahier_remplacement OWNER TO ent;

--
-- Name: cahier_seance; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_seance (
    id integer NOT NULL,
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
    date_maj timestamp without time zone
);


--ALTER TABLE cahier_courant.cahier_seance OWNER TO ent;

--
-- Name: COLUMN cahier_seance.annotations; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_seance.annotations IS 'Annotations saisies par l enseignant et visibles uniquement par lui';


--
-- Name: cahier_sequence; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_sequence (
    id integer NOT NULL,
    code character varying(10) NOT NULL,
    intitule character varying(50) NOT NULL,
    description text,
    date_debut date NOT NULL,
    date_fin date NOT NULL,
    id_enseignant integer,
    id_etablissement integer NOT NULL,
    id_enseignement integer NOT NULL,
    id_groupe integer,
    id_classe integer
);


--ALTER TABLE cahier_courant.cahier_sequence OWNER TO ent;

--
-- Name: COLUMN cahier_sequence.id_etablissement; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_sequence.id_etablissement IS 'id de l etablissement';


--
-- Name: COLUMN cahier_sequence.id_enseignement; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_sequence.id_enseignement IS 'id de l enseignement';


--
-- Name: COLUMN cahier_sequence.id_groupe; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_sequence.id_groupe IS 'id du groupe (exclusif avec id_classe)';


--
-- Name: COLUMN cahier_sequence.id_classe; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_sequence.id_classe IS 'id de la classe (exclusif avec id_groupe)';


--
-- Name: cahier_type_devoir; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_type_devoir (
    id integer NOT NULL,
    code character varying(10) NOT NULL,
    libelle character varying(20) NOT NULL,
    id_etablissement integer NOT NULL,
    categorie character varying(15) DEFAULT 'NORMAL' 
);


--ALTER TABLE cahier_courant.cahier_type_devoir OWNER TO ent;

--
-- Name: COLUMN cahier_type_devoir.categorie; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_type_devoir.categorie IS 'Categorie du type de devoir. NORMAL ou DEVOIR';


--
-- Name: cahier_visa; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_visa (
    id integer NOT NULL,
    date_visa timestamp without time zone,
    profil character varying(20) NOT NULL,
    type_visa character varying(10),
    id_etablissement integer NOT NULL,
    id_enseignant integer NOT NULL,
    id_enseignement integer NOT NULL,
    id_classe integer,
    id_groupe integer,
    date_dernier_maj timestamp without time zone
);


--ALTER TABLE cahier_courant.cahier_visa OWNER TO ent;

--
-- Name: COLUMN cahier_visa.id; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa.id IS 'Identifiant du visa';


--
-- Name: COLUMN cahier_visa.date_visa; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa.date_visa IS 'Date du visa';


--
-- Name: COLUMN cahier_visa.profil; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa.profil IS 'Profil de la personne ayant appose le visa. Correspond au object class du LDAP ';


--
-- Name: COLUMN cahier_visa.type_visa; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa.type_visa IS 'Type du visa : MEMO ou SIMPLE';


--
-- Name: COLUMN cahier_visa.id_etablissement; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa.id_etablissement IS 'id de l etablissement';


--
-- Name: COLUMN cahier_visa.id_enseignant; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa.id_enseignant IS 'id de l enseignant';


--
-- Name: COLUMN cahier_visa.id_enseignement; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa.id_enseignement IS 'id de l enseignement ';


--
-- Name: COLUMN cahier_visa.id_classe; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa.id_classe IS 'id de la classe ';


--
-- Name: COLUMN cahier_visa.id_groupe; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa.id_groupe IS 'id du groupe';


--
-- Name: cahier_visa_seance; Type: TABLE; Schema: cahier_courant; Owner: ent; Tablespace: 
--

CREATE TABLE cahier_visa_seance (
    id_visa integer NOT NULL,
    id_seance integer NOT NULL
);


--ALTER TABLE cahier_courant.cahier_visa_seance OWNER TO ent;

--
-- Name: COLUMN cahier_visa_seance.id_visa; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa_seance.id_visa IS 'Identifiant du visa';


--
-- Name: COLUMN cahier_visa_seance.id_seance; Type: COMMENT; Schema: cahier_courant; Owner: ent
--

COMMENT ON COLUMN cahier_visa_seance.id_seance IS 'id de la seance qui a ete visee';


--select * from cahier_courant.cahier_annee_scolaire;


--
-- Name: cahier_annee_scolaire_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_annee_scolaire
    ADD CONSTRAINT cahier_annee_scolaire_id PRIMARY KEY (id);


--
-- Name: cahier_archive_devoir_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_archive_devoir
    ADD CONSTRAINT cahier_archive_devoir_id PRIMARY KEY (id_archive_devoir);


--
-- Name: cahier_archive_seance_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_archive_seance
    ADD CONSTRAINT cahier_archive_seance_id PRIMARY KEY (id_archive_seance);


--
-- Name: cahier_classe_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_classe
    ADD CONSTRAINT cahier_classe_id PRIMARY KEY (id);


--
-- Name: cahier_devoir_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_devoir
    ADD CONSTRAINT cahier_devoir_id PRIMARY KEY (id);


--
-- Name: cahier_enseignement_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_enseignement
    ADD CONSTRAINT cahier_enseignement_id PRIMARY KEY (id);


--
-- Name: cahier_groupe_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_groupe
    ADD CONSTRAINT cahier_groupe_id PRIMARY KEY (id);


--
-- Name: cahier_piece_jointe_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_piece_jointe
    ADD CONSTRAINT cahier_piece_jointe_id PRIMARY KEY (id);


--
-- Name: cahier_seance_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_seance
    ADD CONSTRAINT cahier_seance_id PRIMARY KEY (id);


--
-- Name: cahier_type_devoir_id; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_type_devoir
    ADD CONSTRAINT cahier_type_devoir_id PRIMARY KEY (id);


--
-- Name: id_cahier_archive_piece_jointe_devoir; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_archive_piece_jointe_devoir
    ADD CONSTRAINT id_cahier_archive_piece_jointe_devoir PRIMARY KEY (id_piece_jointe, id_archive_devoir);


--
-- Name: id_cahier_archive_piece_jointe_seance; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_archive_piece_jointe_seance
    ADD CONSTRAINT id_cahier_archive_piece_jointe_seance PRIMARY KEY (id_piece_jointe, id_archive_seance);


--
-- Name: id_cahier_cycle; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_cycle
    ADD CONSTRAINT id_cahier_cycle PRIMARY KEY (id);


--
-- Name: id_cahier_cycle_devoir; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_cycle_devoir
    ADD CONSTRAINT id_cahier_cycle_devoir PRIMARY KEY (id);


--
-- Name: id_cahier_cycle_groupe; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_cycle_groupe
    ADD CONSTRAINT id_cahier_cycle_groupe PRIMARY KEY (id_cycle, id_groupe);


--
-- Name: id_cahier_cycle_seance; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_cycle_seance
    ADD CONSTRAINT id_cahier_cycle_seance PRIMARY KEY (id);


--
-- Name: id_cahier_eleve; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_eleve
    ADD CONSTRAINT id_cahier_eleve PRIMARY KEY (id);


--
-- Name: id_cahier_eleve_classe; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_eleve_classe
    ADD CONSTRAINT id_cahier_eleve_classe PRIMARY KEY (id_classe, id_eleve);


--
-- Name: id_cahier_eleve_groupe; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_eleve_groupe
    ADD CONSTRAINT id_cahier_eleve_groupe PRIMARY KEY (id_eleve, id_groupe);


--
-- Name: id_cahier_enseignant; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_enseignant
    ADD CONSTRAINT id_cahier_enseignant PRIMARY KEY (id);


--
-- Name: id_cahier_enseignant_classe; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_enseignant_classe
    ADD CONSTRAINT id_cahier_enseignant_classe PRIMARY KEY (id_enseignant, id_classe);


--
-- Name: id_cahier_enseignant_groupe; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_enseignant_groupe
    ADD CONSTRAINT id_cahier_enseignant_groupe PRIMARY KEY (id_enseignant, id_groupe);


--
-- Name: id_cahier_etab_eleve; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_etab_eleve
    ADD CONSTRAINT id_cahier_etab_eleve PRIMARY KEY (id_eleve, id_etablissement);


--
-- Name: id_cahier_etab_enseignant; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_etab_enseignant
    ADD CONSTRAINT id_cahier_etab_enseignant PRIMARY KEY (id_enseignant, id_etablissement);


--
-- Name: id_cahier_etablissement; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_etablissement
    ADD CONSTRAINT id_cahier_etablissement PRIMARY KEY (id);


--
-- Name: id_cahier_groupe_classe; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_groupe_classe
    ADD CONSTRAINT id_cahier_groupe_classe PRIMARY KEY (id_groupe, id_classe);


--
-- Name: id_cahier_inspecteur; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_inspecteur
    ADD CONSTRAINT id_cahier_inspecteur PRIMARY KEY (id);


--
-- Name: id_cahier_periode_emploi; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_periode_emploi
    ADD CONSTRAINT id_cahier_periode_emploi PRIMARY KEY (id);


--
-- Name: id_cahier_piece_jointe_cycle_devoir; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_piece_jointe_cycle_devoir
    ADD CONSTRAINT id_cahier_piece_jointe_cycle_devoir PRIMARY KEY (id_piece_jointe, id_cycle_devoir);


--
-- Name: id_cahier_piece_jointe_cycle_seance; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_piece_jointe_cycle_seance
    ADD CONSTRAINT id_cahier_piece_jointe_cycle_seance PRIMARY KEY (id_piece_jointe, id_cycle_seance);


--
-- Name: id_cahier_piece_jointe_devoir; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_piece_jointe_devoir
    ADD CONSTRAINT id_cahier_piece_jointe_devoir PRIMARY KEY (id_piece_jointe, id_devoir);


--
-- Name: id_cahier_piece_jointe_seance; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_piece_jointe_seance
    ADD CONSTRAINT id_cahier_piece_jointe_seance PRIMARY KEY (id_piece_jointe, id_seance);


--
-- Name: id_cahier_remplacement; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_remplacement
    ADD CONSTRAINT id_cahier_remplacement PRIMARY KEY (id);


--
-- Name: id_cahier_sequence; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_sequence
    ADD CONSTRAINT id_cahier_sequence PRIMARY KEY (id);


--
-- Name: id_cahier_visa; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_visa
    ADD CONSTRAINT id_cahier_visa PRIMARY KEY (id);


--
-- Name: id_cahier_visa_seance; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_visa_seance
    ADD CONSTRAINT id_cahier_visa_seance PRIMARY KEY (id_visa, id_seance);


--
-- Name: id_enseignant_enseignement; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_enseignant_enseignement
    ADD CONSTRAINT id_enseignant_enseignement PRIMARY KEY (id_enseignement, id_enseignant, id_etablissement);


--
-- Name: id_libelle_enseignement_etab_enseig; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_libelle_enseignement
    ADD CONSTRAINT id_libelle_enseignement_etab_enseig PRIMARY KEY (id_enseignement, id_etablissement);


--
-- Name: id_ouverture_inspecteur; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_ouverture_inspecteur
    ADD CONSTRAINT id_ouverture_inspecteur PRIMARY KEY (id_etablissement, id_enseignant, id_inspecteur);


--
-- Name: pimaire_id_emploi; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_emploi
    ADD CONSTRAINT pimaire_id_emploi PRIMARY KEY (id);


--
-- Name: pk_pref; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_preferences_utilisateur
    ADD CONSTRAINT pk_pref PRIMARY KEY (uid);


--
-- Name: unique_archive_seance_visa; Type: CONSTRAINT; Schema: cahier_courant; Owner: ent; Tablespace: 
--

ALTER TABLE  cahier_archive_seance
    ADD CONSTRAINT unique_archive_seance_visa UNIQUE (id_visa, id_seance);
	
--CREATE SCHEMA public AUTHORIZATION DBA;


SET SCHEMA public;

--SET search_path = public, pg_catalog;

CREATE SEQUENCE cahier_seance
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE;
	
CREATE SEQUENCE cahier_devoir
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE;

CREATE SEQUENCE cahier_piece_jointe
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE;

CREATE SEQUENCE cahier_archive_seance
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE;
	
CREATE SEQUENCE cahier_archive_devoir
    START WITH 100
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE;