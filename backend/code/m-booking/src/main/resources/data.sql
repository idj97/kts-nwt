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

-- Location queries
INSERT into location(id, address, name, layout_id) values (-1, "Wherever", "Some stadium", -1);

--Manifestation queries
INSERT into manifestation(id, reservations_available, description, manifestation_type, max_reservations, name, reservable_until, location_id) values (-1, 1, "blablabla", 0, 5, "Manifestation 1", "2019-12-06", -1);

-- Manifestation section queries
INSERT into manifestation_section(id, price, size, manifestation_id, section_id) values (-1, 100, 50, -1, -1);
INSERT into manifestation_section(id, price, size, manifestation_id, section_id) values (-2, 100, 10, -1, -5);

-- Manifestation days queries
INSERT into manifestation_day(id,date, manifestation_id) values(-2,"2019-10-12T20:00:00", -1);
INSERT into manifestation_day(id,date, manifestation_id) values(-1,"2019-10-13T20:00:00", -1);


--RESERVATION QUERIES 

INSERT INTO `reservation` VALUES (1,'2019-12-16 21:45:58.897000','2019-12-19 21:45:58.897000',200,0,-3,-1),(2,'2019-12-16 21:46:04.776000','2019-12-19 21:46:04.776000',200,0,-3,-1),(3,'2019-12-16 21:46:15.168000','2019-12-19 21:46:15.168000',200,0,-3,-1);
INSERT INTO `reservation_details` VALUES (1,2,1,2,-1,-1,1),(2,1,1,2,-1,-1,1),(3,2,1,3,-1,-1,2),(4,1,1,3,-1,-1,2),(5,2,1,4,-1,-1,3),(6,1,1,4,-1,-1,3);
INSERT INTO `manifestation_section_reservations_details` VALUES (-1,1),(-1,2),(-1,3),(-1,4),(-1,5),(-1,6);
INSERT INTO `reservation_reservation_details` VALUES (1,1),(1,2),(2,3),(2,4),(3,5),(3,6);







