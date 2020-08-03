CREATE OR REPLACE FUNCTION insert_timestamps()
RETURNS TRIGGER AS $$
BEGIN
    NEW.created_at = clock_timestamp();
    NEW.updated_at = clock_timestamp();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE OR REPLACE FUNCTION update_timestamps()
RETURNS TRIGGER AS $$
BEGIN
    NEW.created_at = OLD.created_at;
    NEW.updated_at = clock_timestamp();
    RETURN NEW;
END;
$$ language 'plpgsql';