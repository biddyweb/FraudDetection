CREATE TABLE "black_list_accounts"
(
  "account_number" character varying
)
WITH (
  OIDS=FALSE
);

CREATE TABLE "black_list_nifs"
(
  "account_number" character varying
)
WITH (
  OIDS=FALSE
);

CREATE TABLE "white_list_nifs"
(
  "account_number" character varying
)
WITH (
  OIDS=FALSE
);

CREATE TABLE "white_list_accounts"
(
  "account_number" character varying
)
WITH (
  OIDS=FALSE
);

CREATE TABLE payment
(
  id integer NOT NULL,
  name character varying(50),
  email character varying(100),
  rules_outcome character varying(1024),
  currency character varying(25),
  insurance_number character varying(25),
  decision varying(50),
  score integer,
  total_amount numeric(10,2),
  country_cc character varying(25),
  country_order character varying(25),
  invoice_date timestamp with time zone NOT NULL,
  CONSTRAINT payment_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);