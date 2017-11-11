CREATE TABLE accounts
(
  id               SERIAL PRIMARY KEY,
  created          TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
  name             VARCHAR NOT NULL,
  additional_info  VARCHAR NOT NULL,
  scoring          INTEGER NOT NULL
);

CREATE UNIQUE INDEX accounts_idx ON accounts (name);