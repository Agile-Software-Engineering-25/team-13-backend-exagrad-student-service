-- Migration: Update student ID in exam_documents and pub_documents tables (PostgreSQL)
-- From: b7acb825-4e70-49e4-84a1-bf5dc7c8f509
-- To: d03aa006-6f0b-4939-9287-753798b6d403

UPDATE exam_documents
SET student_id = 'd03aa006-6f0b-4939-9287-753798b6d403'
WHERE student_id = 'b7acb825-4e70-49e4-84a1-bf5dc7c8f509';

UPDATE pub_documents
SET student_id = 'd03aa006-6f0b-4939-9287-753798b6d403'
WHERE student_id = 'b7acb825-4e70-49e4-84a1-bf5dc7c8f509';
