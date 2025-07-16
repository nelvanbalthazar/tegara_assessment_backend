INSERT INTO users (id, email, password, role, locked)
VALUES
  (1, 'admin@cvportal.com', '$2a$10$x6VVYtvHI5IuRZeaJV80L.qC2p52TJHF85QnCgWPCitqcJuO3vusS', 'ADMIN', false),
  (2, 'recruiter@cvportal.com', '$2a$10$x6VVYtvHI5IuRZeaJV80L.qC2p52TJHF85QnCgWPCitqcJuO3vusS', 'RECRUITER', false),
  (3, 'viewer@cvportal.com', '$2a$10$x6VVYtvHI5IuRZeaJV80L.qC2p52TJHF85QnCgWPCitqcJuO3vusS', 'VIEWER', false),
  (4, 'candidate@cvportal.com', '$2a$10$x6VVYtvHI5IuRZeaJV80L.qC2p52TJHF85QnCgWPCitqcJuO3vusS', 'CANDIDATE', false);
