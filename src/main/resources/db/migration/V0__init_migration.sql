CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users
(
    id       uuid not null primary key   default gen_random_uuid(),
    username varchar(50),
    created  timestamp without time zone default now(),
    updated  timestamp without time zone default now()
);

CREATE TABLE IF NOT EXISTS accounts
(
    id      uuid not null primary key   default gen_random_uuid(),
    user_id uuid not null references users (id),
    balance numeric                     default 0 not null,
    created timestamp without time zone default now(),
    updated timestamp without time zone default now()
);

CREATE TABLE IF NOT EXISTS transactions
(
    id         uuid        not null primary key default gen_random_uuid(),
    account_id uuid        not null references accounts (id),
    amount     numeric                          default 0 not null,
    type       varchar(50) not null,
    created    timestamp without time zone      default now(),
    updated    timestamp without time zone      default now()
);