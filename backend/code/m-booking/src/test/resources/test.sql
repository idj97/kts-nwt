--truncate table manifestation; --clear table data from previous tests (referential integrity!!!)

-- Location test data
INSERT INTO layout(id, name) VALUES (-1, "Test layout");
INSERT into location(id, address, name, layout_id) values (-1, "Test address", "Test location 1", -1);
INSERT into location(id, address, name, layout_id) values (-2, "Test address 2", "Test location 2", -1);

INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-1, "Test section 1", 1, 20, 50);
INSERT INTO section(id, name, is_seating, section_columns, section_rows) VALUES (-2, "Test section 2", 1, 20, 50);

-- Manifestation test data
INSERT into manifestation(id, reservations_available, description, manifestation_type, max_reservations, name, reservable_until, location_id) values (-1, 1, "test descr", 0, 5, "Test manifestation", "2020-12-12", -1);
INSERT into manifestation(id, reservations_available, description, manifestation_type, max_reservations, name, reservable_until, location_id) values (-3, 0, "test descr 3", 0, 0, "Test manifestation 3", null, -1);
INSERT into manifestation(id, reservations_available, description, manifestation_type, max_reservations, name, reservable_until, location_id) values (-2, 0, "test descr 2", 1, 0, "Test manifestation 2", null, -2);

INSERT into manifestation_day(id, date, manifestation_id) values (-1, "2020-12-15", -1);
INSERT into manifestation_day(id, date, manifestation_id) values (-2, "2020-12-16", -1);
INSERT into manifestation_day(id, date, manifestation_id) values (-3, "2020-12-17", -1);

INSERT into manifestation_day(id, date, manifestation_id) values (-4, "2020-06-15", -2);
INSERT into manifestation_day(id, date, manifestation_id) values (-5, "2020-06-16", -2);
INSERT into manifestation_day(id, date, manifestation_id) values (-6, "2020-06-17", -2);

INSERT into manifestation_day(id, date, manifestation_id) values (-7, "2020-01-15", -3);

