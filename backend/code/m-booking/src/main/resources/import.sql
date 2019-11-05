INSERT INTO authority(id, name) VALUES (-1, "ROLE_CUSTOMER");
INSERT INTO authority(id, name) VALUES (-2, "ROLE_ADMIN");
INSERT INTO authority(id, name) VALUES (-3, "ROLE_SYS_ADMIN");

--password : admin
INSERT INTO users(user_type, id, email, password, firstname, lastname) VALUES ("ADMIN", -1, "sysadmin@example.com", "$2y$12$FWzpJ.Y3f.bIGXdq.HdfKePROV6hdJ/xHDd3cYagTySWoj.Lh8XMW", "Marko", "Markovic");
INSERT INTO users_authorities(user_id, authorities_id) VALUES (-1, -3);

--password : admin
INSERT INTO users(user_type, id, email, password, firstname, lastname) VALUES ("ADMIN", -2, "testadmin@example.com", "$2y$12$FWzpJ.Y3f.bIGXdq.HdfKePROV6hdJ/xHDd3cYagTySWoj.Lh8XMW", "Darko", "Darkovic");
INSERT INTO users_authorities(user_id, authorities_id) VALUES (-2, -2);

--gmail password: testtest!23
--password: user
INSERT INTO users(user_type, id, email, password, firstname, lastname, banned, email_confirmed) VALUES ("CUSTOMER", -3, "ktsnwt.customer@gmail.com", "$2y$12$n0dPqX3hXdSjQsOOzgtsXeZXE9tsBj9.vqokSbW.71agdUbysBf2m", "Petar", "Petrovic", 0, 1);
INSERT INTO users_authorities(user_id, authorities_id) VALUES (-3, -1);


-- Section queries
INSERT into section(id, name, section_columns, section_rows, type) values (1, "section1", 10, 10, 0);
INSERT into section(id, name, section_columns, section_rows, type) values (2, "section2", 5, 5, 1);

--Manifestation queries
INSERT into manifestation(id, reservations_available, description, manifestation_type, max_reservations, name, reservable_until) values (1, 1, "blablabla", 0, 5, "Manifestation 1", "2019-12-06");

-- Manifestation section queries
INSERT into manifestation_section(id, price, size, manifestation_id, section_id) values (1, 100, 50, 1, 1);

-- Manifestation days queries
INSERT into manifestation_day(id,date, manifestation_id) values(1,"2019-10-12T20:00:00", 1);
