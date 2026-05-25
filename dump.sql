--
-- PostgreSQL database dump
--

\restrict eE4DPx3bUKAT67n7YDlVYWqlv6KqMphtFjcv1VnNt0kfaERFkC6hkdjiOwL0Zw9

-- Dumped from database version 15.18
-- Dumped by pg_dump version 15.18

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

ALTER TABLE ONLY public.sessoes DROP CONSTRAINT fk_sessoes_users;
ALTER TABLE ONLY public.sessoes DROP CONSTRAINT fk_sessoes_patients;
ALTER TABLE ONLY public.reports DROP CONSTRAINT fk_reports_patients;
ALTER TABLE ONLY public.patients DROP CONSTRAINT fk_patients_users;
ALTER TABLE ONLY public.feedbacks DROP CONSTRAINT fk_feedbacks_patients;
DROP INDEX public.flyway_schema_history_s_idx;
ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
ALTER TABLE ONLY public.sessoes DROP CONSTRAINT sessoes_pkey;
ALTER TABLE ONLY public.reports DROP CONSTRAINT reports_pkey;
ALTER TABLE ONLY public.patients DROP CONSTRAINT patients_pkey;
ALTER TABLE ONLY public.patients DROP CONSTRAINT patients_email_key;
ALTER TABLE ONLY public.patients DROP CONSTRAINT patients_cpf_key;
ALTER TABLE ONLY public.flyway_schema_history DROP CONSTRAINT flyway_schema_history_pk;
ALTER TABLE ONLY public.feedbacks DROP CONSTRAINT feedbacks_pkey;
DROP TABLE public.users;
DROP TABLE public.sessoes;
DROP TABLE public.reports;
DROP TABLE public.patients;
DROP TABLE public.flyway_schema_history;
DROP TABLE public.feedbacks;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: feedbacks; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.feedbacks (
    id uuid NOT NULL,
    patient_id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL,
    content text NOT NULL,
    mood_score integer,
    sessao_id uuid
);


ALTER TABLE public.feedbacks OWNER TO postgres;

--
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- Name: patients; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.patients (
    id uuid NOT NULL,
    name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    phone character varying(255) NOT NULL,
    birth_date date,
    clinical_notes text,
    photo text,
    user_id uuid,
    cpf character varying(20),
    address character varying(255),
    neighborhood character varying(255),
    city character varying(255),
    state character varying(100),
    cep character varying(20),
    emergency_contact character varying(255),
    emergency_phone character varying(20),
    latitude double precision,
    longitude double precision
);


ALTER TABLE public.patients OWNER TO postgres;

--
-- Name: reports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reports (
    id uuid NOT NULL,
    patient_id uuid NOT NULL,
    generated_at timestamp without time zone NOT NULL,
    ai_analysis_content text NOT NULL
);


ALTER TABLE public.reports OWNER TO postgres;

--
-- Name: sessoes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sessoes (
    id uuid NOT NULL,
    patient_id uuid NOT NULL,
    psychologist_id uuid NOT NULL,
    start_time timestamp without time zone NOT NULL,
    end_time timestamp without time zone,
    status character varying(50),
    clinical_notes text
);


ALTER TABLE public.sessoes OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    name character varying(255),
    role character varying(50),
    photo text,
    crp character varying(50),
    telefone character varying(20)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Data for Name: feedbacks; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.feedbacks VALUES ('53b95409-d275-4e5a-8c06-f39ddccd6183', '8f81215a-c681-4b99-9559-75e4f2b91e83', '2026-05-24 13:23:34.85618', 'Me senti muito ansioso na segunda-feira por conta de uma entrega no trabalho, mas consegui usar a técnica de respiração sugerida.', 3, NULL);
INSERT INTO public.feedbacks VALUES ('a35bc15c-a53d-46f2-931b-dcc1e41ec709', '8f81215a-c681-4b99-9559-75e4f2b91e83', '2026-05-24 13:23:34.859634', 'Consegui me posicionar melhor na reunião hoje. Senti uma sensação de alívio e controle.', 4, NULL);
INSERT INTO public.feedbacks VALUES ('710de660-5284-49f7-9265-5c478b295503', '55499c0c-608f-4f5f-8852-5e3bfc14121f', '2026-05-24 13:23:34.861711', 'Lidando com muitas distrações para estudar para as provas finais. Tentei a técnica Pomodoro mas foi difícil manter a constância.', 2, NULL);
INSERT INTO public.feedbacks VALUES ('9cce7f99-6a2c-429c-a7db-24dfc493f2ac', 'fc4c2efd-1c1a-4e3c-984c-56259e9d63d6', '2026-05-24 13:23:34.864155', 'Sentimento de desmotivação muito forte essa semana, com dificuldades para levantar da cama.', 2, NULL);


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.flyway_schema_history VALUES (1, '1', 'init schema', 'SQL', 'V1__init_schema.sql', -742953167, 'postgres', '2026-05-24 13:23:31.857182', 81, true);
INSERT INTO public.flyway_schema_history VALUES (2, '2', 'add photo and sessoes', 'SQL', 'V2__add_photo_and_sessoes.sql', -2008206025, 'postgres', '2026-05-24 13:23:31.958124', 11, true);
INSERT INTO public.flyway_schema_history VALUES (3, '3', 'link patients users', 'SQL', 'V3__link_patients_users.sql', 1032016560, 'postgres', '2026-05-24 13:23:31.979752', 5, true);
INSERT INTO public.flyway_schema_history VALUES (4, '4', 'add missing patient fields', 'SQL', 'V4__add_missing_patient_fields.sql', -1561953836, 'postgres', '2026-05-24 13:23:31.992702', 8, true);
INSERT INTO public.flyway_schema_history VALUES (5, '5', 'add missing entity fields', 'SQL', 'V5__add_missing_entity_fields.sql', -407023845, 'postgres', '2026-05-24 13:23:32.008839', 2, true);


--
-- Data for Name: patients; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.patients VALUES ('8f81215a-c681-4b99-9559-75e4f2b91e83', 'João Paciente Teste', 'paciente@teste.com', '11999999999', '1990-05-15', 'Paciente apresenta sintomas leves de ansiedade generalizada.', NULL, '96da11d7-c16a-431d-b277-584a87781f91', '12345678901', 'Rua Teste, 123', NULL, 'São Paulo', 'SP', NULL, NULL, NULL, -23.55052, -46.633308);
INSERT INTO public.patients VALUES ('fc4c2efd-1c1a-4e3c-984c-56259e9d63d6', 'Maria Oliveira', 'maria@teste.com', '11988888888', '1985-08-22', 'Paciente busca acompanhamento para transtorno depressivo maior em remissão.', NULL, 'af2c68f7-5e79-4205-8efe-12bdfb5ef5cd', '98765432100', 'Rua Teste, 123', NULL, 'São Paulo', 'SP', NULL, NULL, NULL, -23.559616, -46.658027);
INSERT INTO public.patients VALUES ('55499c0c-608f-4f5f-8852-5e3bfc14121f', 'Carlos Silva', 'carlos@teste.com', '11977777777', '2000-02-10', 'Paciente universitário lidando com TDAH e dificuldades acadêmicas.', NULL, '42c69ac7-f547-446f-a675-90a33874e219', '45612378900', 'Rua Teste, 123', NULL, 'São Paulo', 'SP', NULL, NULL, NULL, -23.567849, -46.648908);


--
-- Data for Name: reports; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: sessoes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sessoes VALUES ('de68b2f9-76f0-492d-ae22-ca14c0330445', '8f81215a-c681-4b99-9559-75e4f2b91e83', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-10 09:00:34.819588', '2026-05-10 09:50:34.819588', 'CONCLUIDA', 'Alinhamento de expectativas e anamnese inicial.');
INSERT INTO public.sessoes VALUES ('2cf6db9a-3f20-4445-b793-f4549a03c3c0', '8f81215a-c681-4b99-9559-75e4f2b91e83', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-17 09:00:34.824246', '2026-05-17 09:50:34.824246', 'CONCLUIDA', 'Sessão inicial. Paciente relatou ansiedade no trabalho.');
INSERT INTO public.sessoes VALUES ('3c3ac752-9fbf-4384-9689-24dabf458062', '8f81215a-c681-4b99-9559-75e4f2b91e83', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-22 09:00:34.82718', '2026-05-22 09:50:34.82718', 'CONCLUIDA', 'Trabalhamos técnicas de relaxamento e regulação de respiração.');
INSERT INTO public.sessoes VALUES ('a4c0a180-9ac4-4a99-a3f5-e8d83717b3e3', '8f81215a-c681-4b99-9559-75e4f2b91e83', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-26 09:00:34.829404', '2026-05-26 09:50:34.829404', 'AGENDADA', '');
INSERT INTO public.sessoes VALUES ('cff62020-5f7b-4853-8568-8e82ecc44288', 'fc4c2efd-1c1a-4e3c-984c-56259e9d63d6', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-09 14:00:34.831412', '2026-05-09 14:50:34.831412', 'CONCLUIDA', 'Revisão de rotina e higiene do sono.');
INSERT INTO public.sessoes VALUES ('39dc86c4-91ce-4a08-942f-969ae8ecc27d', 'fc4c2efd-1c1a-4e3c-984c-56259e9d63d6', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-16 14:00:34.833803', '2026-05-16 14:50:34.833803', 'CANCELADA', 'Paciente cancelou por motivos de saúde.');
INSERT INTO public.sessoes VALUES ('03e9bc8f-f3fd-453e-aade-de2be79f92ed', 'fc4c2efd-1c1a-4e3c-984c-56259e9d63d6', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-23 14:00:34.835776', '2026-05-23 14:50:34.835776', 'CANCELADA', 'Paciente cancelou de última hora devido a imprevisto pessoal.');
INSERT INTO public.sessoes VALUES ('60c8022b-ee68-47cf-a79e-136a77713d18', 'fc4c2efd-1c1a-4e3c-984c-56259e9d63d6', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-30 14:00:34.837579', '2026-05-30 14:50:34.837579', 'AGENDADA', '');
INSERT INTO public.sessoes VALUES ('32345841-f1a5-4a68-b551-7933465770bc', '55499c0c-608f-4f5f-8852-5e3bfc14121f', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-21 16:00:34.839761', '2026-05-21 16:50:34.839761', 'CONCLUIDA', 'Sessão sobre técnicas de foco, TDAH e produtividade acadêmica.');
INSERT INTO public.sessoes VALUES ('146367b7-9b2e-4ada-9b47-43a89824c7ca', '55499c0c-608f-4f5f-8852-5e3bfc14121f', '75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', '2026-05-28 16:00:34.841636', '2026-05-28 16:50:34.841636', 'AGENDADA', '');


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users VALUES ('8974a308-63ae-4ea9-99ae-ab2b91e01cca', 'admin@teste.com', '$2a$10$Hm1pIkBvU2a5iNhHV4kxd.Bbt3DI3XBG9dsjXPmdQ6O0NS64FEeqS', 'Administrador', 'ADMIN', NULL, NULL, NULL);
INSERT INTO public.users VALUES ('75da56ff-2a3c-4ce8-a09d-f2c8c46ea7d3', 'psicologo@teste.com', '$2a$10$ex7Nc9LsibiRnMNJBujx8Oh6xryUr7B8NOZ0ML77Hg7x/xaLNvWtG', 'Psicólogo Teste', 'PSYCHOLOGIST', NULL, 'CRP 06/123456', '11999998888');
INSERT INTO public.users VALUES ('b05f8a9b-1a11-4499-8543-c2ae3e40c1fb', 'ana@teste.com', '$2a$10$LBh9Z1Y0GuCYiPhAPPHW2e/i4br5f01LMOanAjgA1ZUUW.p3LWqdq', 'Ana Psicóloga', 'PSYCHOLOGIST', NULL, 'CRP 06/654321', '11988887777');
INSERT INTO public.users VALUES ('76ca5892-099a-4533-bf1f-11af3cc40649', 'roberto@teste.com', '$2a$10$3pIPpP5xaXDgmHQ5qgnyuO01Gn6ur7NVPrSA8SNWkwAZfIfSyPLEe', 'Roberto Psicólogo', 'PSYCHOLOGIST', NULL, 'CRP 06/987654', '11977776666');
INSERT INTO public.users VALUES ('d8eb27a4-a49c-497f-9016-390f34d973cd', 'juliana@teste.com', '$2a$10$WIQS1cQnNkDj7OxEQJtpMOFMJZNbHJ0S7WJDjjzR6jdsHatglXOd.', 'Juliana Psicóloga', 'PSYCHOLOGIST', NULL, 'CRP 06/456789', '11966665555');
INSERT INTO public.users VALUES ('96da11d7-c16a-431d-b277-584a87781f91', 'paciente@teste.com', '$2a$10$NLKHOVP5ZUlKAnpad9CBVeF0/krUc7OGtE19O05Sh/zsrJsmJ6DWq', 'Paciente Teste', 'USER', NULL, NULL, NULL);
INSERT INTO public.users VALUES ('af2c68f7-5e79-4205-8efe-12bdfb5ef5cd', 'maria@teste.com', '$2a$10$jW5MB4bLoTd2ut1BtPE1jODI.2CrSM2a64FoMhTTDZdYpDNc4/9xu', 'Maria Oliveira', 'USER', NULL, NULL, NULL);
INSERT INTO public.users VALUES ('42c69ac7-f547-446f-a675-90a33874e219', 'carlos@teste.com', '$2a$10$XzB2HaxHR2jTzeSszBFC6O7vYdxQgHP5eGDTQi1bkImFJ20iLxMpu', 'Carlos Silva', 'USER', NULL, NULL, NULL);


--
-- Name: feedbacks feedbacks_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feedbacks
    ADD CONSTRAINT feedbacks_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- Name: patients patients_cpf_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.patients
    ADD CONSTRAINT patients_cpf_key UNIQUE (cpf);


--
-- Name: patients patients_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.patients
    ADD CONSTRAINT patients_email_key UNIQUE (email);


--
-- Name: patients patients_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.patients
    ADD CONSTRAINT patients_pkey PRIMARY KEY (id);


--
-- Name: reports reports_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reports
    ADD CONSTRAINT reports_pkey PRIMARY KEY (id);


--
-- Name: sessoes sessoes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sessoes
    ADD CONSTRAINT sessoes_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- Name: feedbacks fk_feedbacks_patients; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feedbacks
    ADD CONSTRAINT fk_feedbacks_patients FOREIGN KEY (patient_id) REFERENCES public.patients(id) ON DELETE CASCADE;


--
-- Name: patients fk_patients_users; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.patients
    ADD CONSTRAINT fk_patients_users FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE SET NULL;


--
-- Name: reports fk_reports_patients; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reports
    ADD CONSTRAINT fk_reports_patients FOREIGN KEY (patient_id) REFERENCES public.patients(id) ON DELETE CASCADE;


--
-- Name: sessoes fk_sessoes_patients; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sessoes
    ADD CONSTRAINT fk_sessoes_patients FOREIGN KEY (patient_id) REFERENCES public.patients(id) ON DELETE CASCADE;


--
-- Name: sessoes fk_sessoes_users; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sessoes
    ADD CONSTRAINT fk_sessoes_users FOREIGN KEY (psychologist_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict eE4DPx3bUKAT67n7YDlVYWqlv6KqMphtFjcv1VnNt0kfaERFkC6hkdjiOwL0Zw9

