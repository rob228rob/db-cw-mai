--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4 (Debian 16.4-1.pgdg120+1)
-- Dumped by pg_dump version 16.4 (Debian 16.4-1.pgdg120+1)

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: articles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.articles (
    id uuid NOT NULL,
    author character varying(255),
    content text,
    display_order integer,
    title character varying(255),
    course_id uuid
);


ALTER TABLE public.articles OWNER TO postgres;

--
-- Name: certificates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.certificates (
    id uuid NOT NULL,
    sent boolean NOT NULL,
    subject character varying(255),
    user_email character varying(255),
    pdf_id uuid,
    course_id uuid
);


ALTER TABLE public.certificates OWNER TO postgres;

--
-- Name: courses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.courses (
    id uuid NOT NULL,
    creation_time timestamp(6) without time zone,
    description character varying(255),
    modified_time timestamp(6) without time zone,
    name character varying(255)
);


ALTER TABLE public.courses OWNER TO postgres;

--
-- Name: options; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.options (
    id uuid NOT NULL,
    correct boolean NOT NULL,
    option_text character varying(255),
    question_id uuid
);


ALTER TABLE public.options OWNER TO postgres;

--
-- Name: pdf_files; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pdf_files (
    id uuid NOT NULL,
    file oid
);


ALTER TABLE public.pdf_files OWNER TO postgres;

--
-- Name: privileges; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.privileges (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.privileges OWNER TO postgres;

--
-- Name: privileges_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.privileges ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.privileges_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: questions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.questions (
    id uuid NOT NULL,
    question_text character varying(1023),
    test_id uuid
);


ALTER TABLE public.questions OWNER TO postgres;

--
-- Name: ratings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ratings (
    id uuid NOT NULL,
    correct_count integer,
    course_id uuid,
    testing_id uuid,
    user_id uuid
);


ALTER TABLE public.ratings OWNER TO postgres;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id integer NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.roles ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: roles_privileges; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles_privileges (
    role_id integer NOT NULL,
    privilege_id bigint NOT NULL
);


ALTER TABLE public.roles_privileges OWNER TO postgres;

--
-- Name: tests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tests (
    id uuid NOT NULL,
    creation_time timestamp(6) without time zone,
    description character varying(255),
    display_order integer,
    modified_time timestamp(6) without time zone,
    name character varying(255),
    question_count integer,
    course_id uuid
);


ALTER TABLE public.tests OWNER TO postgres;

--
-- Name: user_answers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_answers (
    id uuid NOT NULL,
    correct boolean DEFAULT false,
    option_id uuid,
    question_id uuid,
    test_id uuid,
    user_id uuid
);


ALTER TABLE public.user_answers OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    email character varying(255) NOT NULL,
    enabled boolean NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    token_expired boolean NOT NULL,
    creation_time timestamp(6) without time zone,
    modified_time timestamp(6) without time zone
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_courses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_courses (
    course_id uuid NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE public.users_courses OWNER TO postgres;

--
-- Name: users_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_roles (
    user_id uuid NOT NULL,
    role_id integer NOT NULL
);


ALTER TABLE public.users_roles OWNER TO postgres;

--
-- Name: 16565; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16565');


ALTER LARGE OBJECT 16565 OWNER TO postgres;

--
-- Name: 16566; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16566');


ALTER LARGE OBJECT 16566 OWNER TO postgres;

--
-- Name: 16567; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16567');


ALTER LARGE OBJECT 16567 OWNER TO postgres;

--
-- Name: 16573; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16573');


ALTER LARGE OBJECT 16573 OWNER TO postgres;

--
-- Name: 16574; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16574');


ALTER LARGE OBJECT 16574 OWNER TO postgres;

--
-- Name: 16575; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16575');


ALTER LARGE OBJECT 16575 OWNER TO postgres;

--
-- Name: 16576; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16576');


ALTER LARGE OBJECT 16576 OWNER TO postgres;

--
-- Name: 16577; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16577');


ALTER LARGE OBJECT 16577 OWNER TO postgres;

--
-- Name: 16578; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16578');


ALTER LARGE OBJECT 16578 OWNER TO postgres;

--
-- Name: 16579; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16579');


ALTER LARGE OBJECT 16579 OWNER TO postgres;

--
-- Name: 16580; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16580');


ALTER LARGE OBJECT 16580 OWNER TO postgres;

--
-- Name: 16581; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16581');


ALTER LARGE OBJECT 16581 OWNER TO postgres;

--
-- Name: 16582; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16582');


ALTER LARGE OBJECT 16582 OWNER TO postgres;

--
-- Name: 16583; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16583');


ALTER LARGE OBJECT 16583 OWNER TO postgres;

--
-- Name: 16584; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16584');


ALTER LARGE OBJECT 16584 OWNER TO postgres;

--
-- Name: 16585; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16585');


ALTER LARGE OBJECT 16585 OWNER TO postgres;

--
-- Name: 16586; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16586');


ALTER LARGE OBJECT 16586 OWNER TO postgres;

--
-- Name: 16587; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16587');


ALTER LARGE OBJECT 16587 OWNER TO postgres;

--
-- Name: 16588; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16588');


ALTER LARGE OBJECT 16588 OWNER TO postgres;

--
-- Name: 16589; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16589');


ALTER LARGE OBJECT 16589 OWNER TO postgres;

--
-- Name: 16590; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16590');


ALTER LARGE OBJECT 16590 OWNER TO postgres;

--
-- Name: 16591; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16591');


ALTER LARGE OBJECT 16591 OWNER TO postgres;

--
-- Name: 16592; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16592');


ALTER LARGE OBJECT 16592 OWNER TO postgres;

--
-- Name: 16593; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16593');


ALTER LARGE OBJECT 16593 OWNER TO postgres;

--
-- Name: 16594; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16594');


ALTER LARGE OBJECT 16594 OWNER TO postgres;

--
-- Name: 16595; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16595');


ALTER LARGE OBJECT 16595 OWNER TO postgres;

--
-- Name: 16596; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16596');


ALTER LARGE OBJECT 16596 OWNER TO postgres;

--
-- Name: 16597; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16597');


ALTER LARGE OBJECT 16597 OWNER TO postgres;

--
-- Name: 16598; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16598');


ALTER LARGE OBJECT 16598 OWNER TO postgres;

--
-- Name: 16599; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16599');


ALTER LARGE OBJECT 16599 OWNER TO postgres;

--
-- Name: 16600; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16600');


ALTER LARGE OBJECT 16600 OWNER TO postgres;

--
-- Name: 16601; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16601');


ALTER LARGE OBJECT 16601 OWNER TO postgres;

--
-- Name: 16602; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16602');


ALTER LARGE OBJECT 16602 OWNER TO postgres;

--
-- Name: 16603; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16603');


ALTER LARGE OBJECT 16603 OWNER TO postgres;

--
-- Name: 16604; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16604');


ALTER LARGE OBJECT 16604 OWNER TO postgres;

--
-- Name: 16605; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16605');


ALTER LARGE OBJECT 16605 OWNER TO postgres;

--
-- Name: 16606; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16606');


ALTER LARGE OBJECT 16606 OWNER TO postgres;

--
-- Name: 16607; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16607');


ALTER LARGE OBJECT 16607 OWNER TO postgres;

--
-- Name: 16608; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16608');


ALTER LARGE OBJECT 16608 OWNER TO postgres;

--
-- Name: 16609; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16609');


ALTER LARGE OBJECT 16609 OWNER TO postgres;

--
-- Name: 16610; Type: BLOB; Schema: -; Owner: postgres
--

SELECT pg_catalog.lo_create('16610');


ALTER LARGE OBJECT 16610 OWNER TO postgres;

--
-- Data for Name: articles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.articles (id, author, content, display_order, title, course_id) FROM stdin;
01929728-e1ee-7303-a88e-a7aaacee75db	Robbs	<h1> article 1</h1>\n\n<h> asdkoaspdmasdmasdfplklwdf</h>	0	asdf	01929728-429a-7b47-a1a8-239929aadbb4
\.


--
-- Data for Name: certificates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.certificates (id, sent, subject, user_email, pdf_id, course_id) FROM stdin;
01929afb-1df2-708d-830e-ab573ab51e36	t	РџРѕР·РґСЂР°РІР»СЏРµРј СЃ РїСЂРѕС…РѕР¶РґРµРЅРёРµРј РєСѓСЂСЃР° aasa	test@gmail.com	01929afb-1e02-75b2-ab57-0a97e23ec2be	01929728-429a-7b47-a1a8-239929aadbb4
01929afb-23c8-7842-85e3-abf717038dfa	t	РџРѕР·РґСЂР°РІР»СЏРµРј СЃ РїСЂРѕС…РѕР¶РґРµРЅРёРµРј РєСѓСЂСЃР° aasa	rlbatoyan@gmail.com	01929afb-23cb-7a92-801d-02b5e37a5f64	01929728-429a-7b47-a1a8-239929aadbb4
\.


--
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.courses (id, creation_time, description, modified_time, name) FROM stdin;
01929728-429a-7b47-a1a8-239929aadbb4	2024-10-16 21:07:31.106256	asasa	2024-10-16 21:07:31.106274	aasa
\.


--
-- Data for Name: options; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.options (id, correct, option_text, question_id) FROM stdin;
01929729-5328-7fb1-865d-4a388f33d610	f	РґР°	01929729-531f-71b4-b4a2-c5b9d44231d2
01929729-532f-7835-8c64-d98b70359770	f	РґР°	01929729-531f-71b4-b4a2-c5b9d44231d2
01929729-5331-71c2-9400-10105c4af502	f	РґР°	01929729-531f-71b4-b4a2-c5b9d44231d2
01929729-5333-7b6e-a04b-22b01ee2ac96	t	РЅРµС‚	01929729-531f-71b4-b4a2-c5b9d44231d2
01929729-82d5-7144-9911-31c3e165d7c6	t	РѕС‡РµРІ	01929729-82d0-7a86-9987-0e31a7d958ae
01929729-82dc-73d0-b636-10fa813c8f82	f	РЅРµРµ	01929729-82d0-7a86-9987-0e31a7d958ae
01929729-e4cd-7b6c-8f90-29ebda133db7	t	Р·РµР»РµРЅС‹Р№	01929729-e4c9-75a2-935a-a257ffd073da
01929729-e4d3-775a-9493-80edabafb567	f	РєСЂР°СЃРЅС‹Р№	01929729-e4c9-75a2-935a-a257ffd073da
01929729-e4d7-7385-86a6-dd400ed03c43	f	С‚РµСЃС‚РёСЂРѕРІС‰РёРє	01929729-e4c9-75a2-935a-a257ffd073da
\.


--
-- Data for Name: pdf_files; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.pdf_files (id, file) FROM stdin;
01929af8-e057-73dd-9e73-dd68ed2580e6	16607
01929af8-e693-7d0b-887a-4863ef890afd	16608
01929afb-1e02-75b2-ab57-0a97e23ec2be	16609
01929afb-23cb-7a92-801d-02b5e37a5f64	16610
01929766-1222-7ffb-9e5d-fa5dd5c8b52f	16585
01929766-1765-7c37-b988-be7baf135aae	16586
01929767-fda1-7bdf-8db3-7bbb9ccc92d6	16587
01929768-020b-749f-aee7-c6341fc74d99	16588
01929769-840f-7c3f-acc9-1e17681a93ba	16589
01929769-88ea-7306-801c-2c52e9cc2a8c	16590
01929a78-2df1-708f-b39f-9d12d559ac16	16591
01929a78-33f8-7d4b-b59a-c001e11dd53b	16592
01929a81-5340-7bc1-83d3-59088537c1c2	16593
01929a81-5945-7b5e-b3d5-0a3a48724d9e	16594
01929acd-7561-7270-9062-fb8db152e279	16595
01929acd-7bc6-7678-a1e2-fc4494f1cf09	16596
01929adb-4fb6-71a4-a059-65d6be81a54c	16597
01929adb-5a16-7d22-9faa-2fa7256644d1	16598
01929ade-f4e7-7ba7-849b-75623a5bd4b4	16599
01929ade-fe08-7a24-9b4a-e74fd0edbf33	16600
01929ae1-46fa-7682-b36f-3dfff63d6974	16601
01929ae1-51f4-79a2-9bfb-461c6249737d	16602
01929ae9-a638-7c56-9608-48799824f309	16603
01929ae9-ab1f-79c8-aa5f-045656d86e8c	16604
01929af0-d0ec-70b3-9500-fdbe8bde8af0	16605
01929af0-d77f-787c-abb8-eb1091a20306	16606
\.


--
-- Data for Name: privileges; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.privileges (id, name) FROM stdin;
1	READ_PRIVILEGE
2	WRITE_PRIVILEGE
\.


--
-- Data for Name: questions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.questions (id, question_text, test_id) FROM stdin;
01929729-531f-71b4-b4a2-c5b9d44231d2	РџСЂРѕС€РµР» С‚РµСЃС‚ РЅР° 100?	c555ea86-8c02-11ef-b1f8-1b4d790a3451
01929729-82d0-7a86-9987-0e31a7d958ae	Р’С‹ РєСЂСѓС‚РѕР№?	c555ea86-8c02-11ef-b1f8-1b4d790a3451
01929729-e4c9-75a2-935a-a257ffd073da	300Рє РЅР°РЅРѕСЃРµРє РёР»Рё РЅРёРєРѕРіРґР° РЅРµ РµСЃС‚СЊ РїРµС‡РµРЅСЊ?	c555ea86-8c02-11ef-b1f8-1b4d790a3451
\.


--
-- Data for Name: ratings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ratings (id, correct_count, course_id, testing_id, user_id) FROM stdin;
0e64c437-8c03-11ef-b1f8-1f8eb631f7f4	2	01929728-429a-7b47-a1a8-239929aadbb4	\N	01929727-a406-7937-895b-70c7267123c7
223c6ad8-8c03-11ef-b1f8-33ce75d75ceb	3	01929728-429a-7b47-a1a8-239929aadbb4	\N	01929727-a406-7937-895b-70c7267123c7
226e2939-8c03-11ef-b1f8-b3a1c8dcab7a	3	01929728-429a-7b47-a1a8-239929aadbb4	\N	01929727-a406-7937-895b-70c7267123c7
ef4b1eab-8c04-11ef-bf9e-d9d8d82d893f	3	01929728-429a-7b47-a1a8-239929aadbb4	\N	e1310afa-8c04-11ef-bf9e-3983f4d93118
49dc7807-8ecd-11ef-9596-afcf3f822848	3	01929728-429a-7b47-a1a8-239929aadbb4	\N	01929727-a406-7937-895b-70c7267123c7
5b334028-8ecd-11ef-9596-afab936746cc	3	01929728-429a-7b47-a1a8-239929aadbb4	\N	01929727-a406-7937-895b-70c7267123c7
94646827-8ece-11ef-bbbc-2dca8a5ee7c9	0	01929728-429a-7b47-a1a8-239929aadbb4	\N	01929b0b-269a-7e15-ba9f-0bc96ea04e53
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, name) FROM stdin;
1	ROLE_ADMIN
2	ROLE_USER
\.


--
-- Data for Name: roles_privileges; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles_privileges (role_id, privilege_id) FROM stdin;
1	1
1	2
2	1
\.


--
-- Data for Name: tests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tests (id, creation_time, description, display_order, modified_time, name, question_count, course_id) FROM stdin;
c555ea86-8c02-11ef-b1f8-1b4d790a3451	2024-10-16 21:08:20.03882	asfasasdf	0	2024-10-16 21:08:20.038836	test awf	3	01929728-429a-7b47-a1a8-239929aadbb4
\.


--
-- Data for Name: user_answers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_answers (id, correct, option_id, question_id, test_id, user_id) FROM stdin;
01929737-2feb-780e-8c2c-a33c6cd3a496	t	01929729-5333-7b6e-a04b-22b01ee2ac96	01929729-531f-71b4-b4a2-c5b9d44231d2	c555ea86-8c02-11ef-b1f8-1b4d790a3451	e1310afa-8c04-11ef-bf9e-3983f4d93118
01929737-300c-7e69-951a-9b968ec532ac	t	01929729-82d5-7144-9911-31c3e165d7c6	01929729-82d0-7a86-9987-0e31a7d958ae	c555ea86-8c02-11ef-b1f8-1b4d790a3451	e1310afa-8c04-11ef-bf9e-3983f4d93118
01929737-3019-741d-9201-2c30ed4038fc	t	01929729-e4cd-7b6c-8f90-29ebda133db7	01929729-e4c9-75a2-935a-a257ffd073da	c555ea86-8c02-11ef-b1f8-1b4d790a3451	e1310afa-8c04-11ef-bf9e-3983f4d93118
0192a974-1d07-7406-8e75-53912fff576a	t	01929729-5333-7b6e-a04b-22b01ee2ac96	01929729-531f-71b4-b4a2-c5b9d44231d2	c555ea86-8c02-11ef-b1f8-1b4d790a3451	01929727-a406-7937-895b-70c7267123c7
0192a974-1d0e-7cba-b36b-71255463408a	t	01929729-82d5-7144-9911-31c3e165d7c6	01929729-82d0-7a86-9987-0e31a7d958ae	c555ea86-8c02-11ef-b1f8-1b4d790a3451	01929727-a406-7937-895b-70c7267123c7
0192a974-1d16-7a88-bf47-cea80e8ae0bc	t	01929729-e4cd-7b6c-8f90-29ebda133db7	01929729-e4c9-75a2-935a-a257ffd073da	c555ea86-8c02-11ef-b1f8-1b4d790a3451	01929727-a406-7937-895b-70c7267123c7
0192a97c-2165-7a9f-a8f1-6051f39d1c31	f	01929729-5328-7fb1-865d-4a388f33d610	01929729-531f-71b4-b4a2-c5b9d44231d2	c555ea86-8c02-11ef-b1f8-1b4d790a3451	01929b0b-269a-7e15-ba9f-0bc96ea04e53
0192a97c-2188-78c2-b0fe-475cd9ba8a37	f	01929729-82dc-73d0-b636-10fa813c8f82	01929729-82d0-7a86-9987-0e31a7d958ae	c555ea86-8c02-11ef-b1f8-1b4d790a3451	01929b0b-269a-7e15-ba9f-0bc96ea04e53
0192a97c-2197-70b6-8d3e-6d485503ceaa	f	01929729-e4d3-775a-9493-80edabafb567	01929729-e4c9-75a2-935a-a257ffd073da	c555ea86-8c02-11ef-b1f8-1b4d790a3451	01929b0b-269a-7e15-ba9f-0bc96ea04e53
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, enabled, first_name, last_name, password, token_expired, creation_time, modified_time) FROM stdin;
01929727-a406-7937-895b-70c7267123c7	test@gmail.com	t	Test	Test	$2a$10$ICqDWlYw3ydMBXN2P9RFyeZh8VEnnTMUUH9EktY4CVk31J3mF.ETa	f	\N	2024-10-16 21:06:50.64918
e1310afa-8c04-11ef-bf9e-3983f4d93118	rlbatoyan@gmail.com	t	Р РѕР±РµСЂС‚	Р‘Р°С‚РѕСЏРЅ	$2a$10$Pjp.vPOf79iKt/sCZwIlXe4VpZ105/W/CGG3YBwn5KlGN2vvWHfO2	f	\N	2024-10-16 21:23:25.77985
01929b0b-269a-7e15-ba9f-0bc96ea04e53	admin@gmail.com	t	Admin	Account	$2a$10$RRQ2GjxiCsQX6qJHd8vkaODucEmw/XWW.6HvUTDfsQSPV/kLYQ0Qq	f	\N	2024-10-17 15:14:12.414073
\.


--
-- Data for Name: users_courses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_courses (course_id, user_id) FROM stdin;
01929728-429a-7b47-a1a8-239929aadbb4	01929727-a406-7937-895b-70c7267123c7
01929728-429a-7b47-a1a8-239929aadbb4	e1310afa-8c04-11ef-bf9e-3983f4d93118
01929728-429a-7b47-a1a8-239929aadbb4	01929b0b-269a-7e15-ba9f-0bc96ea04e53
\.


--
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users_roles (user_id, role_id) FROM stdin;
01929727-a406-7937-895b-70c7267123c7	1
e1310afa-8c04-11ef-bf9e-3983f4d93118	2
01929b0b-269a-7e15-ba9f-0bc96ea04e53	1
\.


--
-- Name: privileges_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.privileges_id_seq', 2, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 2, true);


--
-- Data for Name: BLOBS; Type: BLOBS; Schema: -; Owner: -
--

BEGIN;

SELECT pg_catalog.lo_open('16565', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16566', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16567', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16573', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16574', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16575', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16576', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16577', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16578', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16579', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16580', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16581', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16582', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16583', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16584', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16585', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16586', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16587', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16588', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16589', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16590', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16591', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16592', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16593', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16594', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16595', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16596', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16597', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16598', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16599', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16600', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16601', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16602', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16603', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16604', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16605', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16606', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16607', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16608', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16609', 131072);
SELECT pg_catalog.lo_close(0);

SELECT pg_catalog.lo_open('16610', 131072);
SELECT pg_catalog.lo_close(0);

COMMIT;

--
-- Name: articles articles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.articles
    ADD CONSTRAINT articles_pkey PRIMARY KEY (id);


--
-- Name: certificates certificates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificates
    ADD CONSTRAINT certificates_pkey PRIMARY KEY (id);


--
-- Name: courses courses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (id);


--
-- Name: options options_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.options
    ADD CONSTRAINT options_pkey PRIMARY KEY (id);


--
-- Name: pdf_files pdf_files_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.pdf_files
    ADD CONSTRAINT pdf_files_pkey PRIMARY KEY (id);


--
-- Name: privileges privileges_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.privileges
    ADD CONSTRAINT privileges_pkey PRIMARY KEY (id);


--
-- Name: questions questions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.questions
    ADD CONSTRAINT questions_pkey PRIMARY KEY (id);


--
-- Name: ratings ratings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT ratings_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: tests tests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tests
    ADD CONSTRAINT tests_pkey PRIMARY KEY (id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: certificates uk9ymkn8t2fbopytlyfgjdfrs6q; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificates
    ADD CONSTRAINT uk9ymkn8t2fbopytlyfgjdfrs6q UNIQUE (pdf_id);


--
-- Name: user_answers user_answers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_answers
    ADD CONSTRAINT user_answers_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users_roles fk2o0jvgh89lemvvo17cbqvdxaa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: user_answers fk4uqsfqggmgoch1muw76ojamvu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_answers
    ADD CONSTRAINT fk4uqsfqggmgoch1muw76ojamvu FOREIGN KEY (option_id) REFERENCES public.options(id);


--
-- Name: options fk5bmv46so2y5igt9o9n9w4fh6y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.options
    ADD CONSTRAINT fk5bmv46so2y5igt9o9n9w4fh6y FOREIGN KEY (question_id) REFERENCES public.questions(id);


--
-- Name: roles_privileges fk5duhoc7rwt8h06avv41o41cfy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles_privileges
    ADD CONSTRAINT fk5duhoc7rwt8h06avv41o41cfy FOREIGN KEY (privilege_id) REFERENCES public.privileges(id);


--
-- Name: roles_privileges fk629oqwrudgp5u7tewl07ayugj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles_privileges
    ADD CONSTRAINT fk629oqwrudgp5u7tewl07ayugj FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_answers fk6b46l4bb7a6wfxvmn6l7ig8vo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_answers
    ADD CONSTRAINT fk6b46l4bb7a6wfxvmn6l7ig8vo FOREIGN KEY (question_id) REFERENCES public.questions(id);


--
-- Name: articles fk9xvv5pelttuc5574d3gjm0k7e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.articles
    ADD CONSTRAINT fk9xvv5pelttuc5574d3gjm0k7e FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: ratings fka9dafwq48mjdyva0f7tx5mp2e; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT fka9dafwq48mjdyva0f7tx5mp2e FOREIGN KEY (testing_id) REFERENCES public.tests(id);


--
-- Name: ratings fkb3354ee2xxvdrbyq9f42jdayd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT fkb3354ee2xxvdrbyq9f42jdayd FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: ratings fkbegfifgmkbhd1pj5vitred35g; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT fkbegfifgmkbhd1pj5vitred35g FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: certificates fkf7psop1yghmlqa270w3iqm2bq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificates
    ADD CONSTRAINT fkf7psop1yghmlqa270w3iqm2bq FOREIGN KEY (pdf_id) REFERENCES public.pdf_files(id);


--
-- Name: users_courses fkf9urfrtqmay7r1ee9s5v2ngk5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_courses
    ADD CONSTRAINT fkf9urfrtqmay7r1ee9s5v2ngk5 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: users_courses fkhnobs8cb619w5klgkfp61f7nx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_courses
    ADD CONSTRAINT fkhnobs8cb619w5klgkfp61f7nx FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: users_roles fkj6m8fwv7oqv74fcehir1a9ffy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fkj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_answers fkk4u357ronsopa0vqf16deuxbt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_answers
    ADD CONSTRAINT fkk4u357ronsopa0vqf16deuxbt FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: user_answers fkmyeehmkea9a6p39mqxhr2ad3c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_answers
    ADD CONSTRAINT fkmyeehmkea9a6p39mqxhr2ad3c FOREIGN KEY (test_id) REFERENCES public.tests(id);


--
-- Name: tests fknn88a30eakyhdu5nt1m5trxit; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tests
    ADD CONSTRAINT fknn88a30eakyhdu5nt1m5trxit FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: questions fkoc6xkgj16nhyyes4ath9dyxxw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.questions
    ADD CONSTRAINT fkoc6xkgj16nhyyes4ath9dyxxw FOREIGN KEY (test_id) REFERENCES public.tests(id);


--
-- Name: certificates fks5rftqrsgkog7h4piv3f7a9s6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.certificates
    ADD CONSTRAINT fks5rftqrsgkog7h4piv3f7a9s6 FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- PostgreSQL database dump complete
--

