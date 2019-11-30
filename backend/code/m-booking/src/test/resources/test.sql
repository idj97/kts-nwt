--truncate table manifestation; --clear table data from previous tests (referential integrity!!!)

-- Location test data
INSERT INTO layout(id, name) VALUES (-1, "Test layout");
INSERT into location(id, address, name, layout_id) values (-1, "Test address", "Test location", -1);
INSERT into location(id, address, name, layout_id) values (-2, "Test address 2", "Test location 2", -1);

-- Manifestation test data
INSERT into manifestation(id, reservations_available, description, manifestation_type, max_reservations, name, reservable_until, location_id) values (-1, 1, "test descr", 0, 5, "Test manifestation", "2020-12-12", -1);

INSERT into manifestation_day(id, date, manifestation_id) values (-1, "2020-12-15", -1);
INSERT into manifestation_day(id, date, manifestation_id) values (-2, "2020-12-16", -1);
INSERT into manifestation_day(id, date, manifestation_id) values (-3, "2020-12-17", -1);

