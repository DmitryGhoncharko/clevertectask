create table public.product
(
    product_id           bigserial
        primary key
        unique,
    product_name         varchar(2000)    not null
        unique,
    product_price        double precision not null,
    product_is_promotion boolean          not null
);

alter table public.product
    owner to dangeonmaster;

create table public.discount_card
(
    discount_card_id             bigserial
        primary key
        unique,
    discount_card_discount_value double precision not null
);

alter table public.discount_card
    owner to dangeonmaster;
