-- Load data for Services

INSERT INTO `service` (`created_at`, `description`, `title`)
VALUES
  (CURRENT_TIMESTAMP(), 'Sample service 1', 'Service 1'),
  (CURRENT_TIMESTAMP(), 'Sample service 2', 'Service 2'),
  (CURRENT_TIMESTAMP(), 'Sample service 3', 'Service 3'),
  (CURRENT_TIMESTAMP(), 'Sample service 4', 'Service 4'),
  (CURRENT_TIMESTAMP(), 'Sample service 5', 'Service 5'),
  (CURRENT_TIMESTAMP(), 'Sample service 6', 'Service 6');

-- Create default Manager user
-- $2a$10$WwUdOEZ6MlH5enFn17FrjutH.9SZLblVpuya.7e6lHYC9n5cpAGs6 = manager
INSERT INTO `user`
VALUES
  (1, NOW(), 'Manager User', '$2a$10$WwUdOEZ6MlH5enFn17FrjutH.9SZLblVpuya.7e6lHYC9n5cpAGs6', 2, 'manager');
