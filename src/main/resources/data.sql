-- Inserting example users
INSERT INTO person ( first_name, last_name, email, phone_number, birth_date, password, terms_of_use, join_date)
VALUES
('Adam', 'Szablewski', 'adamszablewski@example.com', '123456789', '2000-01-12', '$2a$10$5GZFpc2NeEjqaRj.VVMlwOZI9OTrsxSWOe9gNwrTpPAub.n2gICYO', 1, '2010-01-12'),
('Jane', 'Smith', 'jane.smith@example.com', '987654321', '1995-05-15', 'a1', 1, '2010-01-12'),
('Alice', 'Johnson', 'alice.johnson@example.com', '555555555', '1988-09-20', 'a1', 1, '2010-01-12'),
('Bob', 'Brown', 'bob.brown@example.com', '999999999', '1985-03-10', 'a1', 1, '2010-01-12'),
('Eve', 'Wilson', 'eve.wilson@example.com', '777777777', '1998-11-30', 'a1', 1, '2010-01-12');

INSERT INTO profile (id) VALUES (1), (2), (3), (4), (5);

-- Updating example users to associate them with profiles
UPDATE person SET profile_id = 1 WHERE id = 1;
UPDATE person SET profile_id = 2 WHERE id = 2;
UPDATE person SET profile_id = 3 WHERE id = 3;
UPDATE person SET profile_id = 4 WHERE id = 4;
UPDATE person SET profile_id = 5 WHERE id = 5;


-- Sample text posts
INSERT INTO post (user_id,person_id, is_public, post_type, creation_time, text, visible, multimedia_id)
VALUES
(1,1, true, 1, '2023-01-01 10:00:00', 'This is my first post!', true, NULL),
(2,2, true, 1, '2023-01-02 11:00:00', 'Just sharing my thoughts.', true, NULL),
(3,3, true, 1, '2023-01-03 12:00:00', 'Hello world!', true, NULL),
(4,4, true, 1, '2023-01-04 13:00:00', 'Feeling excited today!', true, NULL),
(5,5, true, 1, '2023-01-05 14:00:00', 'Enjoying the sunny weather.', true, NULL);


INSERT INTO comment (text, user_id, date_time)
VALUES
('Great post!', 2, '2023-01-01 10:30:00'),
('Interesting thoughts.', 3, '2023-01-02 11:30:00'),
('Nice!', 4, '2023-01-03 12:30:00'),
('I feel the same.', 5, '2023-01-04 13:30:00'),
('Glad you enjoy it!', 1, '2023-01-05 14:30:00'),
('First response to Comment 1.', 2, '2023-01-01 10:35:00'),
('Second response to Comment 1.', 3, '2023-01-01 10:40:00'),
('Third response to Comment 1.', 4, '2023-01-01 10:45:00');
INSERT INTO post_comments(comments_id, post_id)
    VALUES
    (1,1),
    (2,1),
    (3,1),
    (4,1);

INSERT INTO COMMENT_ANSWERS (answers_id, comment_id) VALUES
(6, 1),
(7, 1),
(8, 1);

INSERT INTO FRIEND_LIST(user_id)
    values
    (1),
    (2),
    (3),
    (4);

INSERT INTO FRIEND_LIST_FRIENDS(friend_list_id, profile_id)values

(1,2),
(1,3),
(2,1),
(3,1),
(3,4),
(4,3);

UPDATE profile SET friend_list_id = 1 WHERE id = 1;
UPDATE profile SET friend_list_id = 2 WHERE id = 2;
UPDATE profile SET friend_list_id = 3 WHERE id = 3;
UPDATE profile SET friend_list_id = 4 WHERE id = 4;

UPDATE profile SET user_id = 1 WHERE id = 1;
UPDATE profile SET user_id = 2 WHERE id = 2;
UPDATE profile SET user_id = 3 WHERE id = 3;
UPDATE profile SET user_id = 4 WHERE id = 4;

INSERT INTO FRIEND_REQUEST  (sender_id, receiver_id, date_time)
VALUES (4, 1, CURRENT_DATE);




