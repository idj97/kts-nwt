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


-- Layouts and sections
INSERT INTO layout(id, name) VALUES (-1, "STADIUM");
INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-1, "NORTH", 1, 20, 50);
INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-2, "SOUTH", 1, 20, 50);
INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-3, "WEST", 1, 50, 50);
INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-4, "EAST", 1, 50, 50);
INSERT INTO section(id, name, is_seating) VALUES (-5, "PARTER", 0);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-1, -1);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-1, -2);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-1, -3);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-1, -4);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-1, -5);

INSERT INTO layout(id, name) VALUES (-2, "THEATER");
INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-6, "CLASS_1", 1, 30, 10);
INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-7, "CLASS_2", 1, 30, 10);
INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-8, "CLASS_3", 1, 30, 10);
INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-9, "CLASS_4", 1, 30, 10);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-2, -6);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-2, -7);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-2, -8);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-2, -9);

INSERT INTO layout(id, name) VALUES (-3, "OPEN_SPACE");
INSERT INTO section(id, name, is_seating) VALUES (-10, "AREA_1", 0);
INSERT INTO section(id, name, is_seating) VALUES (-11, "AREA_2", 0);
INSERT INTO section(id, name, is_seating) VALUES (-12, "AREA_3", 0);
INSERT INTO section(id, name, is_seating) VALUES (-13, "AREA_4", 0);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-3, -10);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-3, -11);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-3, -12);
INSERT INTO layout_sections(layout_id, sections_id) VALUES (-3, -13);

--Manifestation queries
INSERT into manifestation(id, reservations_available, description, manifestation_type, max_reservations, name, reservable_until) values (-1, 1, "blablabla", 0, 5, "Manifestation 1", "2019-12-06");

-- Manifestation section queries
INSERT into manifestation_section(id, price, size, manifestation_id, section_id) values (-1, 100, 50, -1, -1);

-- Manifestation days queries
INSERT into manifestation_day(id,date, manifestation_id) values(-2,"2019-10-12T20:00:00", -1);



--RESERVATION QUERIES 

/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,'2019-11-07 01:12:53.396000','2019-11-10 01:12:53.396000',200,0,-3),(2,'2019-11-07 01:13:04.088000','2019-11-10 01:13:04.088000',200,0,-3),(3,'2019-11-07 01:13:42.880000','2019-11-10 01:13:42.880000',200,0,-3),(4,'2019-11-07 01:13:55.806000','2019-11-10 01:13:55.806000',200,0,-3),(5,'2019-11-07 01:14:01.864000','2019-11-10 01:14:01.864000',200,0,-3),(6,'2019-11-07 01:14:06.756000','2019-11-10 01:14:06.756000',200,0,-3),(7,'2019-11-07 01:14:27.688000','2019-11-10 01:14:27.688000',200,0,-3),(8,'2019-11-07 01:14:34.858000','2019-11-10 01:14:34.858000',200,0,-3);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;

/*!40000 ALTER TABLE `reservation_details` DISABLE KEYS */;
INSERT INTO `reservation_details` VALUES (1,4,_binary '',1,-1,1),(2,2,_binary '',1,-1,1),(3,6,_binary '',1,-1,2),(4,5,_binary '',1,-1,2),(5,8,_binary '',1,-1,3),(6,7,_binary '',1,-1,3),(7,10,_binary '',1,-1,4),(8,9,_binary '',1,-1,4),(9,10,_binary '',5,-1,5),(10,9,_binary '',5,-1,5),(11,1,_binary '',5,-1,6),(12,2,_binary '',5,-1,6),(13,6,_binary '',5,-1,7),(14,5,_binary '',5,-1,7),(15,8,_binary '',5,-1,8),(16,7,_binary '',5,-1,8);
/*!40000 ALTER TABLE `reservation_details` ENABLE KEYS */;

/*!40000 ALTER TABLE `reservation_manifestation_days` DISABLE KEYS */;
INSERT INTO `reservation_manifestation_days` VALUES (1,-2),(2,-2),(3,-2),(4,-2),(5,-2),(6,-2),(7,-2),(8,-2);
/*!40000 ALTER TABLE `reservation_manifestation_days` ENABLE KEYS */;

/*!40000 ALTER TABLE `reservation_reservation_details` DISABLE KEYS */;
INSERT INTO `reservation_reservation_details` VALUES (1,1),(1,2),(2,3),(2,4),(3,5),(3,6),(4,7),(4,8),(5,9),(5,10),(6,11),(6,12),(7,13),(7,14),(8,15),(8,16);
/*!40000 ALTER TABLE `reservation_reservation_details` ENABLE KEYS */;

ALTER TABLE `manifestation_section_reservations_details` DISABLE KEYS;
INSERT INTO manifestation_section_reservations_details VALUES (-1,1),(-1,2),(-1,3),(-1,4),(-1,5),(-1,6),(-1,7),(-1,8),(-1,9),(-1,10),(-1,11),(-1,12),(-1,13),(-1,14),(-1,15),(-1,16);
ALTER TABLE `manifestation_section_reservations_details` ENABLE KEYS;












