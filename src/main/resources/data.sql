-- Borrowers
INSERT INTO borrower (borrower_id, name, email)
VALUES ('e4585e69-321e-4f60-8e46-8b2adb650abe', 'Alice Johnson', 'alice@example.com');

INSERT INTO borrower (borrower_id, name, email)
VALUES ('98dd216a-c1a1-4b4a-8552-9bdd7b5bd595', 'Bob Smith', 'bob@example.com');

-- Books
INSERT INTO book (book_id, isbn, title, author)
VALUES ('afe38744-78e8-428d-97f5-d3b3854da8c2', '9780134685991', 'Effective Java', 'Joshua Bloch');

INSERT INTO book (book_id, isbn, title, author)
VALUES ('87646857-479b-4480-bb14-e2aabc490846', '9781617294945', 'Spring in Action', 'Craig Walls');

-- Another copy of the same book (multiple copies allowed)
INSERT INTO book (book_id, isbn, title, author)
VALUES ('4d55c5a1-eedc-403c-961e-a822b0bb9334', '9781617294945', 'Refactor Java Code', 'Martin Fowler');
