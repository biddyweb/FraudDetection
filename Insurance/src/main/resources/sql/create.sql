CREATE sequence hibernate_sequence START WITH 100;
CREATE TABLE insured
(
  id integer NOT NULL,
  name character varying(50),
  email character varying(100),
  phone_number character varying(10),
  nif character varying(10),
  insurance_number character varying(10),
  decision integer,
  CONSTRAINT insured_pkey PRIMARY KEY (id)
);

CREATE TABLE payment
(
  id integer NOT NULL,
  name character varying(100),
  email character varying(100),
  rules_outcome character varying(1024),
  currency character varying(40),
  insurance_number character varying(25),
  decision character varying(30),
  score integer,
  total_amount numeric(10,2),
  country_cc character varying(90),
  country_order character varying(90),
  invoice_date timestamp with time zone NOT NULL,
  CONSTRAINT payment_pkey PRIMARY KEY (id)
);


CREATE TABLE person
(
  id integer NOT NULL,
  name character varying(50),
  description character varying(100),
  imageurl character varying(500),
  CONSTRAINT person_pkey PRIMARY KEY (id)
);

CREATE TABLE black_list_accounts
(
  account_number character varying
);

CREATE TABLE black_list_nifs
(
  account_number character varying
);

CREATE TABLE white_list_accounts
(
  account_number character varying
);

CREATE TABLE white_list_nifs
(
  account_number character varying
);