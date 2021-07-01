CREATE SEQUENCE IF NOT EXISTS account_balance_sequence START 1 INCREMENT 1;

CREATE TABLE IF NOT EXISTS account_balance
(
    id                 BIGINT      NOT NULL PRIMARY KEY,
    account_id         VARCHAR(64) NOT NULL,
    currency           currency    NOT NULL,
    quantity           DECIMAL     NOT NULL,
    created_date       BIGINT      NOT NULL,
    last_modified_date BIGINT      NOT NULL,
    UNIQUE (account_id, currency)
);

CREATE SEQUENCE IF NOT EXISTS account_blocked_balance_sequence START 1 INCREMENT 1;

CREATE TABLE IF NOT EXISTS account_blocked_balance
(
    id                 BIGINT                NOT NULL PRIMARY KEY,
    account_balance_id BIGINT                NOT NULL,
    old_quantity       DECIMAL               NOT NULL,
    blocked_quantity   DECIMAL               NOT NULL,
    operation          balance_operation     NOT NULL,
    status             balance_change_status NOT NULL,
    source             balance_change_source NOT NULL,
    action             balance_change_action NOT NULL,
    created_date       BIGINT                NOT NULL,
    last_modified_date BIGINT                NOT NULL,
    FOREIGN KEY (account_balance_id) REFERENCES account_balance (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS account_blocked_balance_history
(
    id                 BIGINT                NOT NULL PRIMARY KEY,
    account_balance_id BIGINT                NOT NULL,
    old_quantity       DECIMAL               NOT NULL,
    blocked_quantity   DECIMAL               NOT NULL,
    operation          balance_operation     NOT NULL,
    status             balance_change_status NOT NULL,
    source             balance_change_source NOT NULL,
    action             balance_change_action NOT NULL,
    created_date       BIGINT                NOT NULL,
    last_modified_date BIGINT                NOT NULL,
    FOREIGN KEY (account_balance_id) REFERENCES account_balance (id) ON DELETE RESTRICT
);

