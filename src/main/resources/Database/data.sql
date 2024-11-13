-- Insert roles
INSERT INTO role (roleId, name, description) VALUES
(1, 'User', 'Regular user role'),
(2, 'Admin', 'Administrator role');

-- Insert profiles
INSERT INTO profile (profileId, firstName, lastName, profilePic) VALUES
(1, 'Marwa', 'Jafail', 'upload-dir/pic2.png');

-- Insert users
INSERT INTO Users (uid, email, password, status, profile_id, role_id, is_enabled, reset_token, token_expiration_time, verification_code) VALUES
(1, 'marwa.a.jafail@gmail.com', '$2a$10$pqS8QfbIBFtNS9mHbY8KcOkQ/htsvH8FXw3EGH.dtZB7ClyQOXiIG', true, 1, 2, true, NULL, NULL, NULL);

-- Insert groups
INSERT INTO groups (groupId, name, uid) VALUES
(1, 'Friends', 1),
(2, 'Family', 1),
(3, 'Work', 1);

