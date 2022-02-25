create table livraria.books (id  bigserial not null, author varchar(255), image varchar(255), isbn varchar(255), resume varchar(255), title varchar(255), value numeric(19, 2), year int4, category_id int8, primary key (id));
create table livraria.categories (id  bigserial not null, description varchar(255), name varchar(255), primary key (id));
create table livraria.orders (id  bigserial not null, created_at timestamp, user_id int8, primary key (id));
create table livraria.orders_books (order_id int8 not null, books_id int8 not null, primary key (order_id, books_id));
create table livraria.users (id  bigserial not null, document varchar(32) not null, email varchar(255), name varchar(255), password varchar(255), primary key (id));
alter table livraria.users drop constraint if exists UK_kgnb8tmua5cirywqjj4m3id0l;
alter table livraria.users add constraint UK_kgnb8tmua5cirywqjj4m3id0l unique (document);
alter table livraria.books add constraint FKleqa3hhc0uhfvurq6mil47xk0 foreign key (category_id) references livraria.categories;
alter table livraria.orders add constraint FK32ql8ubntj5uh44ph9659tiih foreign key (user_id) references livraria.users;
alter table livraria.orders_books add constraint FKitc0se63ecpeviwish0dp0goh foreign key (books_id) references livraria.books;
alter table livraria.orders_books add constraint FKol7arli7ptfejk3kwuo2n2mx3 foreign key (order_id) references livraria.orders;
insert into livraria.categories(name) values ('Drama');
insert into livraria.categories(name) values ('Romance');
insert into livraria.categories(name) values ('Aventura');
insert into livraria.categories(name) values ('Romance');
insert into livraria.categories(name) values ('Ficção');
insert into books()