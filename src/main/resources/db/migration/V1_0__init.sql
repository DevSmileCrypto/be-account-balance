CREATE OR REPLACE FUNCTION now_epoch_timestamp() RETURNS bigint AS
$$
BEGIN
    RETURN (extract(epoch from now() at time zone 'utc') * 1000)::bigint;
END;
$$ LANGUAGE plpgsql;

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1 INCREMENT 1;

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'currency') THEN
            CREATE TYPE currency AS ENUM ('CBM', 'ASH', 'HBD', 'HIVE');
        END IF;
    END
$$
LANGUAGE plpgsql;

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'balance_operation') THEN
            CREATE TYPE balance_operation AS ENUM ('ADD', 'SUBTRACT');
        END IF;
    END
$$
LANGUAGE plpgsql;

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'balance_change_status') THEN
            CREATE TYPE balance_change_status AS ENUM ('IN_PROCESS', 'DONE', 'FAILED');
        END IF;
    END
$$
LANGUAGE plpgsql;

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'balance_change_source') THEN
            CREATE TYPE balance_change_source AS ENUM ('BLOCKCHAIN');
        END IF;
    END
$$
LANGUAGE plpgsql;

DO
$$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_type WHERE typname = 'balance_change_action') THEN
            CREATE TYPE balance_change_action AS ENUM ('WITHDRAW', 'DEPOSIT');
        END IF;
    END
$$
LANGUAGE plpgsql;