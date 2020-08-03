CREATE TABLE accounts
(
  id               UUID PRIMARY KEY,
  created_at       TIMESTAMP NOT NULL DEFAULT clock_timestamp(),
  updated_at       TIMESTAMP NOT NULL DEFAULT clock_timestamp(),
  name             VARCHAR NOT NULL,
  scoring          INTEGER NOT NULL,
  version          INTEGER NOT NULL
);

CREATE TRIGGER insert_timestamp BEFORE INSERT ON accounts FOR EACH ROW EXECUTE PROCEDURE insert_timestamps();
CREATE TRIGGER update_timestamp BEFORE UPDATE ON accounts FOR EACH ROW EXECUTE PROCEDURE update_timestamps();