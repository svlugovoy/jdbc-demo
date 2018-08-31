
-- The Shawshank Redemption
INSERT INTO actors (id, first_name, last_name, birthday, gender, instagram) VALUES (1, 'Tim', 'Robbins', '1958-10-16', 'm', 'instagram.com/tim');
INSERT INTO actors (id, first_name, last_name, birthday, gender, instagram) VALUES (2, 'Morgan', 'Freeman', '1937-06-01', 'm', null);
INSERT INTO directors (id, first_name, last_name, birthday, gender, instagram) VALUES (1, 'Frank', 'Darabont', '1959-01-28', 'm', null);
INSERT INTO movies (id, name, year_of_creation, genre, directors_id) VALUES (1, 'The Shawshank Redemption', 1994, 'Drama', 1);
INSERT INTO actors_movies (actors_id, movies_id) VALUES (1, 1);
INSERT INTO actors_movies (actors_id, movies_id) VALUES (2, 1);

-- Fight Club
INSERT INTO actors (id, first_name, last_name, birthday, gender, instagram) VALUES (3, 'Brad', 'Pitt', '1963-12-18', 'm', 'instagram.com/pittb');
INSERT INTO actors (id, first_name, last_name, birthday, gender, instagram) VALUES (4, 'Edward', 'Norton', '1969-08-18', 'm', 'instagram.com/ednort');
INSERT INTO actors (id, first_name, last_name, birthday, gender, instagram) VALUES (5, 'Meat', 'Loaf', '1947-09-27', 'm', null);
INSERT INTO directors (id, first_name, last_name, birthday, gender, instagram) VALUES (2, 'David', 'Fincher', '1962-08-28', 'm', 'instagram.com/dfit');
INSERT INTO movies (id, name, year_of_creation, genre, directors_id) VALUES (2, 'Fight Club', 1999, 'Drama', 2);
INSERT INTO actors_movies (actors_id, movies_id) VALUES (3, 2);
INSERT INTO actors_movies (actors_id, movies_id) VALUES (4, 2);
INSERT INTO actors_movies (actors_id, movies_id) VALUES (5, 2);

-- Kill Bill: Vol. 1
INSERT INTO actors (id, first_name, last_name, birthday, gender, instagram) VALUES (6, 'Uma', 'Thurman', '1970-04-29', 'f', 'instagram.com/umma');
INSERT INTO directors (id, first_name, last_name, birthday, gender, instagram) VALUES (3, 'Quentin', 'Tarantino', '1963-03-27', 'm', 'instagram.com/qwerty');
INSERT INTO movies (id, name, year_of_creation, genre, directors_id) VALUES (3, 'Kill Bill: Vol. 1', 2003, 'Action', 3);
INSERT INTO actors_movies (actors_id, movies_id) VALUES (6, 3);