--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 15.3

-- Started on 2024-02-02 21:35:20

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE pai_6xs4;
--
-- TOC entry 3220 (class 1262 OID 16389)
-- Name: pai_6xs4; Type: DATABASE; Schema: -; Owner: pwojcik
--

CREATE DATABASE pai_6xs4 WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.UTF8';


ALTER DATABASE pai_6xs4 OWNER TO pwojcik;

\connect pai_6xs4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3221 (class 0 OID 0)
-- Name: pai_6xs4; Type: DATABASE PROPERTIES; Schema: -; Owner: pwojcik
--

ALTER DATABASE pai_6xs4 SET "TimeZone" TO 'utc';


\connect pai_6xs4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pwojcik
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pwojcik;

--
-- TOC entry 3222 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pwojcik
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 214 (class 1259 OID 17049)
-- Name: addresses_seq; Type: SEQUENCE; Schema: public; Owner: pwojcik
--

CREATE SEQUENCE public.addresses_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.addresses_seq OWNER TO pwojcik;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 17233)
-- Name: app_user; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.app_user (
    id uuid NOT NULL,
    activated boolean NOT NULL,
    activation_token character varying(255),
    avatar_url character varying(255),
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    reset_token character varying(255),
    reset_token_expiry_date timestamp(6) without time zone,
    username character varying(255) NOT NULL
);


ALTER TABLE public.app_user OWNER TO pwojcik;

--
-- TOC entry 219 (class 1259 OID 17408)
-- Name: authors; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.authors (
    id uuid NOT NULL,
    contribution integer,
    external_profile_url character varying(255),
    name character varying(255) NOT NULL,
    surname character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);


ALTER TABLE public.authors OWNER TO pwojcik;

--
-- TOC entry 220 (class 1259 OID 17415)
-- Name: comments; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.comments (
    id uuid NOT NULL,
    content character varying(255),
    repo_id uuid,
    user_id uuid
);


ALTER TABLE public.comments OWNER TO pwojcik;

--
-- TOC entry 224 (class 1259 OID 17484)
-- Name: reactions; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.reactions (
    id uuid NOT NULL,
    reaction_type character varying(255) NOT NULL,
    repo_id uuid NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT reactions_reaction_type_check CHECK (((reaction_type)::text = ANY ((ARRAY['LIKE'::character varying, 'DISLIKE'::character varying])::text[])))
);


ALTER TABLE public.reactions OWNER TO pwojcik;

--
-- TOC entry 221 (class 1259 OID 17426)
-- Name: repo; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.repo (
    id uuid NOT NULL,
    add_time bigint NOT NULL,
    comment character varying(255),
    image character varying(255),
    name character varying(255),
    url character varying(255),
    created_by_id uuid
);


ALTER TABLE public.repo OWNER TO pwojcik;

--
-- TOC entry 222 (class 1259 OID 17433)
-- Name: repo_authors; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.repo_authors (
    repo_id uuid NOT NULL,
    authors_id uuid NOT NULL
);


ALTER TABLE public.repo_authors OWNER TO pwojcik;

--
-- TOC entry 223 (class 1259 OID 17436)
-- Name: repo_comments; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.repo_comments (
    repo_id uuid NOT NULL,
    comments_id uuid NOT NULL
);


ALTER TABLE public.repo_comments OWNER TO pwojcik;

--
-- TOC entry 225 (class 1259 OID 17490)
-- Name: repo_reactions; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.repo_reactions (
    repo_id uuid NOT NULL,
    reactions_id uuid NOT NULL
);


ALTER TABLE public.repo_reactions OWNER TO pwojcik;

--
-- TOC entry 218 (class 1259 OID 17277)
-- Name: user_role; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.user_role (
    id integer NOT NULL,
    name character varying(30),
    CONSTRAINT user_role_name_check CHECK (((name)::text = ANY ((ARRAY['ROLE_USER'::character varying, 'ROLE_MODERATOR'::character varying, 'ROLE_ADMIN'::character varying])::text[])))
);


ALTER TABLE public.user_role OWNER TO pwojcik;

--
-- TOC entry 217 (class 1259 OID 17276)
-- Name: user_role_id_seq; Type: SEQUENCE; Schema: public; Owner: pwojcik
--

CREATE SEQUENCE public.user_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_role_id_seq OWNER TO pwojcik;

--
-- TOC entry 3223 (class 0 OID 0)
-- Dependencies: 217
-- Name: user_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: pwojcik
--

ALTER SEQUENCE public.user_role_id_seq OWNED BY public.user_role.id;


--
-- TOC entry 216 (class 1259 OID 17271)
-- Name: user_roles; Type: TABLE; Schema: public; Owner: pwojcik
--

CREATE TABLE public.user_roles (
    app_user_id uuid NOT NULL,
    user_role_id integer NOT NULL
);


ALTER TABLE public.user_roles OWNER TO pwojcik;

--
-- TOC entry 3029 (class 2604 OID 17280)
-- Name: user_role id; Type: DEFAULT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.user_role ALTER COLUMN id SET DEFAULT nextval('public.user_role_id_seq'::regclass);


--
-- TOC entry 3204 (class 0 OID 17233)
-- Dependencies: 215
-- Data for Name: app_user; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.app_user (id, activated, activation_token, avatar_url, email, password, reset_token, reset_token_expiry_date, username) FROM stdin;
1f5b5bc3-7af4-4e13-a196-12caf425d0c4	t	be9a5600-ff6e-48c0-b602-4efd2efe1f58	\N	string@string.pl	$2a$10$Y/iCaO.FuE44Lw89ytj5fugYG7r40P.TuKjwBa4XqeHiCOnXiN3MG	\N	\N	string
0e4f564e-a178-4114-9257-7cfbab0ad70f	f	4bfc31bc-d397-4960-bfa7-44fda65209e5	\N	test@test	$2a$10$HAH60mihtQ6Z/QUeG/00o.Vs.xnXOL6j20sdcYsmWH.mhuwxj7ij2	\N	\N	test
8e523e9c-f44e-4394-9f8d-5541f5d6b017	f	49f50b5c-f856-4cb5-907a-2cbb984c6d82	\N	test@dsafasofgjo	$2a$10$EmOYQITav.oW3falLal6z.UrOoEjrsYKZE574to/1f.ksdFdnJ84q	\N	\N	adsadsads
\.


--
-- TOC entry 3208 (class 0 OID 17408)
-- Dependencies: 219
-- Data for Name: authors; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.authors (id, contribution, external_profile_url, name, surname, username) FROM stdin;
967a58c4-28d1-4e76-8be0-8e9a5a05b160	50	string	string	string	string
1a8b661e-4863-4444-9c2f-f4fde9c87e29	50	string	string	string	string
9e840ce6-f3fe-45eb-86b6-86558009e429	50	string	string	string	string
52963ca5-f566-4b40-824e-153eb31b37ec	50	string	string	string	string
c42ff91c-7759-4389-b65f-06c5c90ca877	66	string	asddsadsadasdas	asddsadsadasdas	asddsadsadasdas
85675292-f78d-4567-915a-11dced2e925b	5	string	asdadsasddas	asdadsasddas	asdadsasddas
79bdc8a0-d958-4654-890f-13a0d9cd669a	49	string	dasasdasddsaads	dasasdasddsaads	dasasdasddsaads
353686da-02a2-4d9a-895a-e346fec0fde0	86	string	adssadsdaasd	adssadsdaasd	adssadsdaasd
d76e9317-722c-45b5-bc96-ca5fe115f7fb	61	string	fakafknjfasnadg	fakafknjfasnadg	fakafknjfasnadg
\.


--
-- TOC entry 3209 (class 0 OID 17415)
-- Dependencies: 220
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.comments (id, content, repo_id, user_id) FROM stdin;
8b5603dc-ae73-46a2-96f3-cb38c9db42d5	Testowy komentarz	db471ad9-07ac-409d-b2a2-c211b1cf9668	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
fec61f4b-290c-4723-a4a7-a7683dbe8b27	Testowy komentarz	db471ad9-07ac-409d-b2a2-c211b1cf9668	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
7400de71-91f5-4c32-83d8-8ab82f3e7307	Testowy komentarz	db471ad9-07ac-409d-b2a2-c211b1cf9668	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
06750de8-6808-4942-ad4f-102dab34c977	asddadsasdasd	7332f44a-1183-4c91-85d0-ff53a85936e3	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
1dee0828-6f66-4d10-9712-ca38ef9980fc	rest	7332f44a-1183-4c91-85d0-ff53a85936e3	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
407c3285-540e-4076-a137-185a3ba06398	test	7332f44a-1183-4c91-85d0-ff53a85936e3	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
26d35bf6-854a-4d28-9f87-677405f4149a	Testowa wiadomość	1521f833-6872-41b0-8513-a86255558377	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
06c445cb-bb42-48ed-805f-ba13e474da58	test	1521f833-6872-41b0-8513-a86255558377	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
26649ef6-6f59-4104-a1f7-7ef74a34d8c6	test	1521f833-6872-41b0-8513-a86255558377	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
a4ab3706-cf93-4945-9146-2d4828763d82	test1	1521f833-6872-41b0-8513-a86255558377	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
da3101ea-9436-4434-a667-78f90e428dd3	test	1521f833-6872-41b0-8513-a86255558377	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
71cae9eb-66b8-4750-95cb-4c6db660ddda	test	6154b6d8-ca12-43a9-a56c-ab4648996d25	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
baff83ec-5fff-499c-96d3-3a2eb0e3d0f1	testowy komentarz	6154b6d8-ca12-43a9-a56c-ab4648996d25	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
39809612-57f3-4ed3-bdf0-2ce06e901a6e	test	6154b6d8-ca12-43a9-a56c-ab4648996d25	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
5a030004-6254-4ad5-9a18-08877291d8b6	test121adsads	b10558c4-60be-44d0-9a25-adeb63fe1b02	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
\.


--
-- TOC entry 3213 (class 0 OID 17484)
-- Dependencies: 224
-- Data for Name: reactions; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.reactions (id, reaction_type, repo_id, user_id) FROM stdin;
03457062-09f3-4458-a586-676da891bc74	DISLIKE	75c447f2-b6fd-47c0-819d-f2be68c2c6be	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
aefbd985-391b-414c-84c0-e43b70a396fa	DISLIKE	db471ad9-07ac-409d-b2a2-c211b1cf9668	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
84cbee27-c596-49c0-add7-80ecb1211821	LIKE	6154b6d8-ca12-43a9-a56c-ab4648996d25	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
05ab1bc2-247b-42dd-90e4-6443ea143773	LIKE	1521f833-6872-41b0-8513-a86255558377	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
a3996f6b-469c-48e7-a619-ca1200e08848	LIKE	b10558c4-60be-44d0-9a25-adeb63fe1b02	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
\.


--
-- TOC entry 3210 (class 0 OID 17426)
-- Dependencies: 221
-- Data for Name: repo; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.repo (id, add_time, comment, image, name, url, created_by_id) FROM stdin;
6154b6d8-ca12-43a9-a56c-ab4648996d25	0	string	string	string	string	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
db471ad9-07ac-409d-b2a2-c211b1cf9668	0	string	string	string	string	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
1521f833-6872-41b0-8513-a86255558377	0	string	string	string	string	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
75c447f2-b6fd-47c0-819d-f2be68c2c6be	0	string	string	string	string	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
b10558c4-60be-44d0-9a25-adeb63fe1b02	0	saddasadsads	\N	asdasdads	dsadasdasdas	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
207146c9-39c4-43b4-9efc-40cde9d931d1	0	adsadsdas	\N	dasddsasad	asddsa	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
faa1415c-92f0-434f-a83d-d975642fbd88	0	adsadsasd	\N	asddasads	adsasdsad	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
7332f44a-1183-4c91-85d0-ff53a85936e3	1706782146792	adsadsasd	\N	sdadsa	adsdasads	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
531579de-726b-4938-a798-85bfeed8a5c9	1706905306276	jjaisfif	\N	TestRepo	j8t3hiwefio	1f5b5bc3-7af4-4e13-a196-12caf425d0c4
\.


--
-- TOC entry 3211 (class 0 OID 17433)
-- Dependencies: 222
-- Data for Name: repo_authors; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.repo_authors (repo_id, authors_id) FROM stdin;
6154b6d8-ca12-43a9-a56c-ab4648996d25	967a58c4-28d1-4e76-8be0-8e9a5a05b160
db471ad9-07ac-409d-b2a2-c211b1cf9668	1a8b661e-4863-4444-9c2f-f4fde9c87e29
1521f833-6872-41b0-8513-a86255558377	9e840ce6-f3fe-45eb-86b6-86558009e429
75c447f2-b6fd-47c0-819d-f2be68c2c6be	52963ca5-f566-4b40-824e-153eb31b37ec
b10558c4-60be-44d0-9a25-adeb63fe1b02	c42ff91c-7759-4389-b65f-06c5c90ca877
207146c9-39c4-43b4-9efc-40cde9d931d1	85675292-f78d-4567-915a-11dced2e925b
faa1415c-92f0-434f-a83d-d975642fbd88	79bdc8a0-d958-4654-890f-13a0d9cd669a
7332f44a-1183-4c91-85d0-ff53a85936e3	353686da-02a2-4d9a-895a-e346fec0fde0
531579de-726b-4938-a798-85bfeed8a5c9	d76e9317-722c-45b5-bc96-ca5fe115f7fb
\.


--
-- TOC entry 3212 (class 0 OID 17436)
-- Dependencies: 223
-- Data for Name: repo_comments; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.repo_comments (repo_id, comments_id) FROM stdin;
db471ad9-07ac-409d-b2a2-c211b1cf9668	8b5603dc-ae73-46a2-96f3-cb38c9db42d5
db471ad9-07ac-409d-b2a2-c211b1cf9668	fec61f4b-290c-4723-a4a7-a7683dbe8b27
db471ad9-07ac-409d-b2a2-c211b1cf9668	7400de71-91f5-4c32-83d8-8ab82f3e7307
7332f44a-1183-4c91-85d0-ff53a85936e3	06750de8-6808-4942-ad4f-102dab34c977
7332f44a-1183-4c91-85d0-ff53a85936e3	1dee0828-6f66-4d10-9712-ca38ef9980fc
7332f44a-1183-4c91-85d0-ff53a85936e3	407c3285-540e-4076-a137-185a3ba06398
1521f833-6872-41b0-8513-a86255558377	26d35bf6-854a-4d28-9f87-677405f4149a
1521f833-6872-41b0-8513-a86255558377	06c445cb-bb42-48ed-805f-ba13e474da58
1521f833-6872-41b0-8513-a86255558377	26649ef6-6f59-4104-a1f7-7ef74a34d8c6
1521f833-6872-41b0-8513-a86255558377	a4ab3706-cf93-4945-9146-2d4828763d82
1521f833-6872-41b0-8513-a86255558377	da3101ea-9436-4434-a667-78f90e428dd3
6154b6d8-ca12-43a9-a56c-ab4648996d25	71cae9eb-66b8-4750-95cb-4c6db660ddda
6154b6d8-ca12-43a9-a56c-ab4648996d25	baff83ec-5fff-499c-96d3-3a2eb0e3d0f1
6154b6d8-ca12-43a9-a56c-ab4648996d25	39809612-57f3-4ed3-bdf0-2ce06e901a6e
b10558c4-60be-44d0-9a25-adeb63fe1b02	5a030004-6254-4ad5-9a18-08877291d8b6
\.


--
-- TOC entry 3214 (class 0 OID 17490)
-- Dependencies: 225
-- Data for Name: repo_reactions; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.repo_reactions (repo_id, reactions_id) FROM stdin;
75c447f2-b6fd-47c0-819d-f2be68c2c6be	03457062-09f3-4458-a586-676da891bc74
db471ad9-07ac-409d-b2a2-c211b1cf9668	aefbd985-391b-414c-84c0-e43b70a396fa
6154b6d8-ca12-43a9-a56c-ab4648996d25	84cbee27-c596-49c0-add7-80ecb1211821
1521f833-6872-41b0-8513-a86255558377	05ab1bc2-247b-42dd-90e4-6443ea143773
b10558c4-60be-44d0-9a25-adeb63fe1b02	a3996f6b-469c-48e7-a619-ca1200e08848
\.


--
-- TOC entry 3207 (class 0 OID 17277)
-- Dependencies: 218
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.user_role (id, name) FROM stdin;
\.


--
-- TOC entry 3205 (class 0 OID 17271)
-- Dependencies: 216
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: pwojcik
--

COPY public.user_roles (app_user_id, user_role_id) FROM stdin;
\.


--
-- TOC entry 3224 (class 0 OID 0)
-- Dependencies: 214
-- Name: addresses_seq; Type: SEQUENCE SET; Schema: public; Owner: pwojcik
--

SELECT pg_catalog.setval('public.addresses_seq', 1, false);


--
-- TOC entry 3225 (class 0 OID 0)
-- Dependencies: 217
-- Name: user_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: pwojcik
--

SELECT pg_catalog.setval('public.user_role_id_seq', 1, false);


--
-- TOC entry 3033 (class 2606 OID 17239)
-- Name: app_user app_user_pkey; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.app_user
    ADD CONSTRAINT app_user_pkey PRIMARY KEY (id);


--
-- TOC entry 3039 (class 2606 OID 17414)
-- Name: authors authors_pkey; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.authors
    ADD CONSTRAINT authors_pkey PRIMARY KEY (id);


--
-- TOC entry 3041 (class 2606 OID 17419)
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- TOC entry 3049 (class 2606 OID 17489)
-- Name: reactions reactions_pkey; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.reactions
    ADD CONSTRAINT reactions_pkey PRIMARY KEY (id);


--
-- TOC entry 3043 (class 2606 OID 17432)
-- Name: repo repo_pkey; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo
    ADD CONSTRAINT repo_pkey PRIMARY KEY (id);


--
-- TOC entry 3045 (class 2606 OID 17443)
-- Name: repo_authors uk_4gra2dp6gw8o6qh5f7kuj2hwr; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo_authors
    ADD CONSTRAINT uk_4gra2dp6gw8o6qh5f7kuj2hwr UNIQUE (authors_id);


--
-- TOC entry 3051 (class 2606 OID 17494)
-- Name: repo_reactions uk_c8po491o925tveu1ohouhym2o; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo_reactions
    ADD CONSTRAINT uk_c8po491o925tveu1ohouhym2o UNIQUE (reactions_id);


--
-- TOC entry 3047 (class 2606 OID 17445)
-- Name: repo_comments uk_p70ewp6cii7k58mhfxawpeill; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo_comments
    ADD CONSTRAINT uk_p70ewp6cii7k58mhfxawpeill UNIQUE (comments_id);


--
-- TOC entry 3037 (class 2606 OID 17283)
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (id);


--
-- TOC entry 3035 (class 2606 OID 17275)
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (app_user_id, user_role_id);


--
-- TOC entry 3057 (class 2606 OID 17468)
-- Name: repo_comments fk3nv68the04989l3ulxrbhj2yg; Type: FK CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo_comments
    ADD CONSTRAINT fk3nv68the04989l3ulxrbhj2yg FOREIGN KEY (repo_id) REFERENCES public.repo(id);


--
-- TOC entry 3059 (class 2606 OID 17500)
-- Name: repo_reactions fk8u2jegjemskd5bve85peqpsow; Type: FK CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo_reactions
    ADD CONSTRAINT fk8u2jegjemskd5bve85peqpsow FOREIGN KEY (repo_id) REFERENCES public.repo(id);


--
-- TOC entry 3054 (class 2606 OID 17448)
-- Name: repo fkhc6px0qrgu7e3vbt7mdn5nb7c; Type: FK CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo
    ADD CONSTRAINT fkhc6px0qrgu7e3vbt7mdn5nb7c FOREIGN KEY (created_by_id) REFERENCES public.app_user(id);


--
-- TOC entry 3055 (class 2606 OID 17453)
-- Name: repo_authors fklkuplpj1et0qdpelahiknpgh5; Type: FK CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo_authors
    ADD CONSTRAINT fklkuplpj1et0qdpelahiknpgh5 FOREIGN KEY (authors_id) REFERENCES public.authors(id);


--
-- TOC entry 3056 (class 2606 OID 17458)
-- Name: repo_authors fkoue60op3l48phaqggk2gd7wfq; Type: FK CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo_authors
    ADD CONSTRAINT fkoue60op3l48phaqggk2gd7wfq FOREIGN KEY (repo_id) REFERENCES public.repo(id);


--
-- TOC entry 3052 (class 2606 OID 17320)
-- Name: user_roles fkowf3vvo8ufljmarbdf820di0t; Type: FK CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkowf3vvo8ufljmarbdf820di0t FOREIGN KEY (user_role_id) REFERENCES public.user_role(id);


--
-- TOC entry 3053 (class 2606 OID 17325)
-- Name: user_roles fkq0a2ff2ouxxlo7sd6iimtusi4; Type: FK CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkq0a2ff2ouxxlo7sd6iimtusi4 FOREIGN KEY (app_user_id) REFERENCES public.app_user(id);


--
-- TOC entry 3058 (class 2606 OID 17463)
-- Name: repo_comments fkruolabeqwjf440u61svcpm3tg; Type: FK CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo_comments
    ADD CONSTRAINT fkruolabeqwjf440u61svcpm3tg FOREIGN KEY (comments_id) REFERENCES public.comments(id);


--
-- TOC entry 3060 (class 2606 OID 17495)
-- Name: repo_reactions fksf3x68hhud9lb99iffgqulcnx; Type: FK CONSTRAINT; Schema: public; Owner: pwojcik
--

ALTER TABLE ONLY public.repo_reactions
    ADD CONSTRAINT fksf3x68hhud9lb99iffgqulcnx FOREIGN KEY (reactions_id) REFERENCES public.reactions(id);


-- Completed on 2024-02-02 21:35:24

--
-- PostgreSQL database dump complete
--

